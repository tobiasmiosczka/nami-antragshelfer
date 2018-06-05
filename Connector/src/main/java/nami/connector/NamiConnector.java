package nami.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.reflect.TypeToken;
import nami.connector.credentials.NamiCredentials;
import nami.connector.exception.NamiApiException;
import nami.connector.exception.NamiLoginException;

import nami.connector.json.JsonHelp;
import nami.connector.namitypes.NamiBeitragszahlung;
import nami.connector.namitypes.NamiEnum;
import nami.connector.namitypes.NamiGruppierung;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSchulung;
import nami.connector.namitypes.NamiSchulungenMap;
import nami.connector.namitypes.NamiSearchedValues;
import nami.connector.namitypes.NamiTaetigkeitAssignment;
import nami.connector.namitypes.enums.NamiEbene;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.JDomSerializer;
import org.htmlcleaner.TagNode;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

/**
 * Baut die Verbindung zum NaMi-Server auf und schickt die API-Anfragen an ihn.
 * 
 * @author Fabian Lipp
 */ 
public class NamiConnector {
    // Adresse des Nami-Servers und Zugangsdaten
    private final NamiServer server;
    private final NamiCredentials credentials;

    private final CloseableHttpClient httpclient = HttpClientBuilder.create().build();
    private static final Logger log = Logger.getLogger(NamiConnector.class.getName());

    private static final int INITIAL_LIMIT = 1000; // Maximale Anzahl der gefundenen Datensätze, wenn kein Limit vorgegeben wird.
    private static final int MAX_TAETIGKEITEN = 1000;

    /**
     * Speichert, ob der Login am NaMi-Server bereits erfolgreich durchgeführt
     * wurde.
     */
    private boolean isAuthenticated = false;

    /**
     * Erzeugt eine NaMi-Verbindung. Beim Erzeugen erfolgt noch <i>kein</i>
     * Login und es wird kein Kontakt zum Server aufgenommen.
     * 
     * @param server
     *            Adresse des Servers
     * @param credentials
     *            Zugangsdaten
     */
    public NamiConnector(NamiServer server, NamiCredentials credentials) {
        this.server = server;
        this.credentials = credentials;
    }

    /**
     * Führt den Login am NaMi-Server durch.
     * 
     * @throws IOException
     *             IOException
     * @throws NamiLoginException
     *             Probleme beim NaMi-Login, z. B. falsche Zugangsdaten
     */
    public void namiLogin() throws IOException, NamiLoginException {
        // skip if already logged in
        if (isAuthenticated) {
            return;
        }

        HttpPost httpPost = new HttpPost(NamiURIBuilder.getLoginURIBuilder(server).build());
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("username", credentials.getApiUser()));
        nvps.add(new BasicNameValuePair("password", credentials.getApiPass()));
        nvps.add(new BasicNameValuePair("redirectTo", "app.jsp"));

        if (server.useApiAccess()) { //API-Login
            nvps.add(new BasicNameValuePair("Login", "API"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            HttpResponse response = execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                // need to follow one redirect
                Header locationHeader = response.getFirstHeader("Location");
                EntityUtils.consume(response.getEntity());
                if (locationHeader != null) {
                    String redirectUrl = locationHeader.getValue();
                    HttpGet httpGet = new HttpGet(redirectUrl);
                    response = execute(httpGet);
                    statusCode = response.getStatusLine().getStatusCode();
                    log.info("Got redirect to: " + redirectUrl);
                    if (statusCode == HttpStatus.SC_OK) {
                        EntityUtils.consume(response.getEntity());
                        isAuthenticated = true;
                        log.info("Authenticated to NaMi-Server with API.");
                    }
                }
            } else {
                //login failed
                Type type = new TypeToken<NamiResponse<Object>>(){}.getType();
                NamiResponse<Object> namiResponse = JsonHelp.fromJson(new InputStreamReader(response.getEntity().getContent()), type);
                String e = namiResponse.getStatusMessage();
                throw new NamiLoginException(e);
            }
        } else { //Non-API-Login
            nvps.add(new BasicNameValuePair("Login", "Anmelden"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            HttpResponse response = execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                // need to follow one redirect
                Header locationHeader = response.getFirstHeader("Location");
                EntityUtils.consume(response.getEntity());
                if (locationHeader != null) {
                    String redirectUrl = locationHeader.getValue();
                    HttpGet httpGet = new HttpGet(redirectUrl);
                    response = execute(httpGet);
                    statusCode = response.getStatusLine().getStatusCode();
                    log.info("Got redirect to: " + redirectUrl);
                    if (statusCode == HttpStatus.SC_OK) {
                        // login successful
                        EntityUtils.consume(response.getEntity());
                        isAuthenticated = true;
                        log.info("Authenticated to NaMi-Server without API.");
                    }
                }
            } else {
                // login not successful
                isAuthenticated = false;
                String error = "";
                try {
                    Document doc = getCleanedDom(response.getEntity().getContent());
                    XPathFactory xpathFac = XPathFactory.instance();

                    // XPath describes content of description field
                    String xpathStr = "/html/body//p[1]/text()";
                    XPathExpression<Text> xpath = xpathFac.compile(xpathStr, Filters.textOnly());
                    Text xpathResult = xpath.evaluateFirst(doc);
                    if (xpathResult != null) {
                        error = StringEscapeUtils.unescapeHtml4(xpathResult.getText());
                    }
                } catch (Exception e) {
                    throw new NamiLoginException(e);
                }
                throw new NamiLoginException(error);
            }
        }
    }

    /**
     * Liefert den DOM-Baum zu einem übergebenen HTML-Dokument. Dabei wird
     * HtmlCleaner miteingebunden, d.h. es können auch HTML-Dokumente
     * verarbeitet werden, die kein valides XML sind.
     * 
     * @param is
     *            Stream, aus dem das HTML-Dokument gelesen wird
     * @return der entsprechende DOM-Baum
     * @throws IOException
     *             IOException
     */
    private Document getCleanedDom(InputStream is) throws IOException {
        CleanerProperties prop = new CleanerProperties();
        prop.setTransSpecialEntitiesToNCR(true);
        HtmlCleaner cleaner = new HtmlCleaner(prop);
        TagNode tagNode = cleaner.clean(is);
        tagNode.removeAttribute("xmlns");
        JDomSerializer serializer = new JDomSerializer(prop);
        return serializer.createJDom(tagNode);
    }

    private HttpResponse execute(HttpUriRequest request) throws IOException {
        log.fine("Sending request to NaMi-Server: " + request.getURI());
        return httpclient.execute(request);
    }

    /**
     * Überprüft, ob die Anfrage erfolgreich war (StatusCode 200) und den
     * richtigen ContentType beinhaltet. Falls dies nicht der Fall ist, werden
     * entsprechende Exceptions geworfen.
     * 
     * @param response
     *            Antwort auf die HTTP-Anfrage
     * @param responseEntity
     *            entsprechende Entity
     * @param expectedContentType
     *            Content-Type, den die Antwort haben soll
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             Fehler bei der Nami-Anfrage
     */
    private void checkResponse(HttpResponse response, HttpEntity responseEntity, String expectedContentType) throws IOException, NamiApiException {
        // Teste, ob der Statuscode der Antwort 200 (OK) ist
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            // check if response is redirect to an error page
            Header locationHeader = response.getFirstHeader("Location");
            if (locationHeader != null) {
                String redirectTarget = locationHeader.getValue();
                // Extract error message from URL in location
                log.warning("Got redirect to: " + redirectTarget);
                // get query part (after '?')
                String redirectQuery = redirectTarget.substring(redirectTarget.indexOf('?') + 1);
                if (redirectTarget.contains("error.jsp")) {
                    String message = URLDecoder.decode(redirectQuery, "UTF-8");
                    message = message.split("=", 2)[1];
                    throw new NamiApiException(message);
                }
            } else {
                // extract description from JBoss error page
                String error = "";
                try {
                    Document doc = getCleanedDom(response.getEntity().getContent());
                    XPathFactory xpathFac = XPathFactory.instance();

                    // XPath describes content of description field
                    String xpathStr = "/html/body/p[3]/u/text()";
                    XPathExpression<Text> xpath = xpathFac.compile(xpathStr, Filters.textOnly());
                    Text xpathResult = xpath.evaluateFirst(doc);
                    if (xpathResult != null) {
                        error = StringEscapeUtils.unescapeHtml4(xpathResult.getText());
                    }
                } catch (Exception e) {
                    throw new NamiApiException(e);
                }
                throw new NamiApiException(error);
            }
            throw new NamiApiException("Statuscode of response is not 200 OK.");
        }

        // Teste, ob die Antwort den richtigen Content-Type liefert
        Header contentTypeHdr = responseEntity.getContentType();
        if (contentTypeHdr == null) {
            throw new NamiApiException("Response has no Content-Type.");
        } else {
            String contentType = contentTypeHdr.getValue();
            if (!contentType.equals(expectedContentType) && !contentType.contains(expectedContentType + ";")) {
                throw new NamiApiException("Content-Type of response is " + contentType + "; expected " + expectedContentType + ".");
            }
        }
    }

    /**
     * Schickt eine Anfrage an den NaMi-Server und wandelt das gelieferte JSON
     * in ein passendes Objekt um.
     * 
     * @param request
     *            Anfrage, die an den Server geschickt wird
     * @param typeOfT
     *            Typ der Rückgabe. Hier wird nur der Typ der eigentlichen
     *            Antwort (ohne den durch die API hinzugefügten Wrapper)
     *            übergeben
     * @param <T>
     *            Typ des zurückgegebenen Objekts
     * @return das gelieferte Objekt
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             wenn die Anfrage fehlschlägt. Das kann unter anderem folgende
     *             Gründe haben:
     *             <ul>
     *             <li>Der Statuscode der Antwort ist nicht 200 (OK)</li>
     *             <li>Der Content-Type der Antwort ist nicht application/json</li>
     *             <li>Im API-Wrapper-Objekt wird ein Status-Code ungleich 0
     *             geliefert</li>
     *             </ul>
     */
    public <T> T executeApiRequest(HttpUriRequest request, final Type typeOfT) throws IOException, NamiApiException {
        log.info("HTTP Call: " + request.getURI().toString());
        if (!isAuthenticated) {
            throw new NamiApiException("Did not login before API Request.");
        }
        HttpResponse response = execute(request);
        HttpEntity responseEntity = response.getEntity();
        checkResponse(response, responseEntity, "application/json");
        return JsonHelp.fromJson(new InputStreamReader(responseEntity.getContent()), typeOfT);
    }

    /**
     * <p>
     * Schickt eine Anfrage an den NaMi-Server, wobei als Antwort kein
     * JSON-Objekt, sondern eine HTML-Seite erwartet wird.
     * </p>
     * 
     * <p>
     * In dieser HTML-Seite wird dann das erste <tt>&lt;script&gt;</tt>-Tag
     * gesucht, das kein src-Attribut enthält (dessen Quelltext also direkt in
     * der HTML-Seite steht). Auf diesen JavaScript-Code, wird dann ein
     * regulärer Ausdruck angewendet, um das gesuchte Objekt (in JSON-Format) zu
     * finden.
     * </p>
     * 
     * @param request
     *            Anfrage, die an den Server geschickt wird
     * @param objectRegex
     *            regulärer Ausdruck, mit dem das Objekt im JavaScript-Code
     *            gefunden wird. Das Objekt wird im Ausdruck als Gruppe markiert
     *            (mit runden Klammern). Die erste gefundene Gruppe
     *            (matcher.group(1)) wird als JSON-Code des gesuchten Objekts
     *            interpretiert.
     * @param typeOfT
     *            Typ der Rückgabe. Hier wird nur der Typ der eigentlichen
     *            Antwort (ohne den durch die API hinzugefügten Wrapper)
     *            übergeben
     * @param <T>
     *            Typ des zurückgegebenen Objekts
     * @return das gelieferte Objekt
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             wenn die Anfrage fehlschlägt. Das kann unter anderem folgende
     *             Gründe haben:
     *             <ul>
     *             <li>Der Statuscode der Antwort ist nicht 200 (OK)</li>
     *             <li>Der Content-Type der Antwort ist nicht application/json</li>
     *             <li>Im API-Wrapper-Objekt wird ein Status-Code ungleich 0
     *             geliefert</li>
     *             </ul>
     */
    public <T> T executeHtmlRequest(HttpUriRequest request,
            Pattern objectRegex, final Type typeOfT) throws IOException,
            NamiApiException {
        if (!isAuthenticated) {
            throw new NamiApiException("Did not login before API Request.");
        }

        // Sende Request an Server
        HttpResponse response = execute(request);
        HttpEntity responseEntity = response.getEntity();

        checkResponse(response, responseEntity, "text/html");

        // Decodiere geliefertes HTML
        String jsSource = "";
        Document doc = getCleanedDom(response.getEntity().getContent());

        XPathFactory xpathFac = XPathFactory.instance();
        XPathExpression<Element> xpath = xpathFac.compile("/html/head/script", Filters.element());
        List<Element> scriptTags = xpath.evaluate(doc);

        for (Element scriptTag : scriptTags) {
            if (scriptTag.getAttribute("src") == null) {
                // there is no src attribute -> look at contents
                jsSource = scriptTag.getText();
            }
        }

        if (jsSource == null || jsSource.isEmpty()) {
            throw new NamiApiException("No Javascript Code found in response.");
        }
        Matcher match = objectRegex.matcher(jsSource);
        if (!match.find()) {
            throw new NamiApiException("Regex not found in Javascript Code.");
        }

        // get matched string
        String json = match.group(1);
        return JsonHelp.fromJson(json, typeOfT);
    }

    /**
     * Liefert einen URIBuilder für den NaMi-Server dieser Connection.
     * 
     * @param path
     *            Pfad, der aufgerufen wird
     * @return erzeugter URIBuilder
     */
    public NamiURIBuilder getURIBuilder(String path) {
        return getURIBuilder(path, true);
    }

    /**
     * Liefert einen URIBuilder für den NaMi-Server dieser Connection.
     * 
     * @param path
     *            Pfad, der aufgerufen wird
     * @param restUrl
     *            gibt an, ob vor dem übergebenen Pfad noch die Pfadkomponente
     *            "rest/" und ggf. die API-Version eingefügt werden soll
     * @return erzeugter URIBuilder
     */
    public NamiURIBuilder getURIBuilder(String path, boolean restUrl) {
        return new NamiURIBuilder(server, path, restUrl);
    }

    /**
     * Liefert einen Teil der Mitglieder, die der Suchanfrage entsprechen.
     *
     * @param limit
     *            maximale Anzahl an gelieferten Ergebnissen
     * @param page
     *            Seite
     * @param start
     *            Index des ersten zurückgegeben Datensatzes in der gesamten
     *            Ergebnismenge
     * @return gefundene Mitglieder //TODO: stimmt momentan nicht exakt wegen
     *         NamiRepsonse
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    // TODO: Warum NamiResponse nötig
    // -> gebe stattdessen direkt die Collection zurück oder null, wenn kein
    // success
    public NamiResponse<Collection<NamiMitglied>> getSearchResult(NamiSearchedValues searchedValues, int limit, int page, int start) throws IOException, NamiApiException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_NAMI_SEARCH);
        builder.setParameter("limit", Integer.toString(limit));
        builder.setParameter("page", Integer.toString(page));
        builder.setParameter("start", Integer.toString(start));
        builder.setParameter("searchedValues", JsonHelp.toJson(searchedValues));
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<Collection<NamiMitglied>>>() {}.getType();
        return executeApiRequest(httpGet, type);
    }

    // TODO: Teste was passiert, wenn es keine Treffer gibt bzw. die Suchanfrage ungültig ist
    /**
     * Liefert alle Mitglieder, die der Suchanfrage entsprechen.
     *
     * @param searchedValues
     * Suchparamenter
     *
     * @return gefundene Mitglieder
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    public Collection<NamiMitglied> getAllResults(NamiSearchedValues searchedValues) throws IOException, NamiApiException {
        NamiResponse<Collection<NamiMitglied>> resp = getSearchResult(searchedValues, INITIAL_LIMIT, 1, 0);
        if (resp.getTotalEntries() > INITIAL_LIMIT) {
            resp = getSearchResult(searchedValues, resp.getTotalEntries(), 1, 0);
        }
        return resp.getData();
    }

    /**
     * Holt den Datensatz eines Mitglieds aus NaMi.
     *
     * @param id
     *            ID des Mitglieds
     * @return Mitgliedsdatensatz
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    public NamiMitglied getMitgliedById(int id) throws IOException, NamiApiException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_NAMI_MITGLIEDER);
        builder.appendPath("0");
        builder.appendPath(Integer.toString(id));
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<NamiMitglied>>() {}.getType();
        NamiResponse<NamiMitglied> resp = executeApiRequest(httpGet, type);
        return (resp.isSuccess() ? resp.getData() : null);
    }

    public NamiSchulungenMap getSchulungen(NamiMitglied namiMitglied) throws IOException, NamiApiException {
        return getSchulungen(namiMitglied.getId());
    }

    public NamiSchulungenMap getSchulungen(int mitgliedsID) throws IOException, NamiApiException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_SCHULUNGEN);
        builder.appendPath(Integer.toString(mitgliedsID));
        builder.appendPath("/flist");
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<Collection<NamiSchulung>>>() {}.getType();
        NamiResponse<Collection<NamiSchulung>> resp = executeApiRequest(httpGet, type);
        return new NamiSchulungenMap(resp.getData());
    }

    /**
     * Liest alle verfügbaren Tätigkeiten aus NaMi aus.
     *
     * @return Liste der verfügbaren Tätigkeiten
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     * @throws IOException
     *             IOException
     */
    public List<NamiEnum> getTaetigkeiten() throws NamiApiException, IOException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_TAETIGKEITEN);
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<List<NamiEnum>>>() {}.getType();
        NamiResponse<List<NamiEnum>> resp = executeApiRequest(httpGet, type);
        return resp.getData();
    }

    /**
     * Liest alle verfügbaren Untergliederungen (Stufe/Abteilung) aus NaMi aus.
     *
     * @return Liste der verfügbaren Untergliederungen
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     * @throws IOException
     *             IOException
     */
    public List<NamiEnum> getUntergliederungen() throws NamiApiException, IOException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_UNTERGLIEDERUNGEN);
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<List<NamiEnum>>>() {}.getType();
        NamiResponse<List<NamiEnum>> resp = executeApiRequest(httpGet, type);
        return resp.getData();
    }

    /**
     * Liefert die Mitglieder, die einer bestimmten Gruppierung angehören
     * (entweder als Stammgruppierung oder sie üben dort eine Tätigkeit aus).
     *
     * @param gruppierungsnummer
     *            Nummer der Gruppierung, in der gesucht werden soll
     * @return gefundene Mitglieder
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     * @throws IOException
     *             IOException
     */
    public Collection<NamiMitglied> getMitgliederFromGruppierung(int gruppierungsnummer) throws NamiApiException, IOException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_NAMI_MITGLIEDER);
        builder.appendPath(Integer.toString(gruppierungsnummer));
        builder.appendPath("flist");
        builder.setParameter("limit", "5000");
        builder.setParameter("page", "1");
        builder.setParameter("start", "0");
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<Collection<NamiMitglied>>>() {}.getType();
        NamiResponse<Collection<NamiMitglied>> resp = executeApiRequest(httpGet, type);
        if (resp.isSuccess()) {
            return resp.getData();
        } else {
            throw new NamiApiException("Could not get member list from Nami: " + resp.getStatusMessage());
        }
    }

    public Collection<NamiTaetigkeitAssignment> getTaetigkeiten(int id) throws IOException, NamiApiException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_NAMI_TAETIGKEIT);
        builder.appendPath(Integer.toString(id));
        builder.appendPath("flist");
        builder.setParameter("limit", Integer.toString(MAX_TAETIGKEITEN));
        builder.setParameter("page", Integer.toString(0));
        builder.setParameter("start", Integer.toString(0));
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<Collection<NamiTaetigkeitAssignment>>>() {}.getType();
        NamiResponse<Collection<NamiTaetigkeitAssignment>> resp = executeApiRequest(httpGet, type);
        if (resp.isSuccess()) {
            return resp.getData();
        } else {
            return null;
        }
    }

    public Collection<NamiGruppierung> getChildGruppierungen(int rootGruppierung) throws IOException, NamiApiException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_GRUPPIERUNGEN);
        builder.appendPath(Integer.toString(rootGruppierung));
        builder.addParameter("node", Integer.toString(rootGruppierung));
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<Collection<NamiGruppierung>>>() {}.getType();
        NamiResponse<Collection<NamiGruppierung>> resp = executeApiRequest(httpGet, type);
        Collection<NamiGruppierung> allChildren = resp.getData();
        Collection<NamiGruppierung> activeChildren = new LinkedList<>();
        for (NamiGruppierung child : allChildren) {
            activeChildren.add(child);
            // Kinder brauchen nur abgefragt werden, wenn es sich nicht um
            // einen Stamm handelt (denn Stämme haben keine Kinder)
            if (child.getEbene() == NamiEbene.STAMM) {
                child.setChildren(new LinkedList<>());
            } else {
                child.setChildren(getChildGruppierungen(child.getId()));
            }
        }
        return activeChildren;
    }

    /**
     * Liest den kompletten Gruppierungsbaum aus, auf den der Benutzer Zugriff
     * hat.
     *
     * @return Root-Gruppierung (in dieser sind die Kinder gespeichert)
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    public NamiGruppierung getRootGruppierung() throws IOException, NamiApiException {
        NamiGruppierung rootGrp = getRootGruppierungWithoutChildren();
        rootGrp.setChildren(getChildGruppierungen(rootGrp.getId()));
        return rootGrp;
    }

    public Collection<NamiGruppierung> getGruppierungenFromUser() throws IOException, NamiApiException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_GRUPPIERUNGEN);
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<Collection<NamiGruppierung>>>() {}.getType();
        NamiResponse<Collection<NamiGruppierung>> resp = executeApiRequest(httpGet, type);
        return resp.getData();
    }

    /**
     * Liest den Gruppierungsbaum ausgehend von einer vorgegebenen Wurzel aus.
     *

     * @param gruppierungsnummer
     *            Gruppierungsnummer der Gruppierung, die die Wurzel des Baumes
     *            bilden soll
     * @return vorgegebene Wurzel-Gruppierung (in dieser sind die Kinder
     *         gespeichert)
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    public NamiGruppierung getGruppierung(int gruppierungsnummer) throws IOException, NamiApiException {
        NamiGruppierung rootGrp = getRootGruppierung();
        // nicht sehr effizient, da trotzdem der gesamte Baum aus NaMi geladen
        // wird
        // auf Diözesanebene sollte das aber kein Problem sein, da die Anzahl
        // der Bezirke doch sehr begrenzt ist
        NamiGruppierung found = rootGrp.findGruppierung(gruppierungsnummer);
        if (found == null) {
            throw new NamiApiException("Gruppierung not found: " + gruppierungsnummer);
        } else {
            return found;
        }
    }

    /**
     * Liest die Root-Gruppierung aus NaMi aus. Es wird nicht der gesamte
     * Gruppierungsbaum gelesen, d.h. in der Root-Gruppierung wird anstelle der
     * Liste der Kinder nur <code>null</code> gespeichert.
     *
     * @return Root-Gruppierung ohne Kinder
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    private NamiGruppierung getRootGruppierungWithoutChildren() throws IOException, NamiApiException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_GRUPPIERUNGEN);
        builder.appendPath("root");
        builder.addParameter("node", "root");
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<Collection<NamiGruppierung>>>() {}.getType();
        NamiResponse<Collection<NamiGruppierung>> resp = executeApiRequest(httpGet, type);
        if (!resp.isSuccess()) {
            throw new NamiApiException("Could not get root Gruppierung");
        }
        NamiGruppierung rootGrp = resp.getData().iterator().next();
        rootGrp.setChildren(null);
        return rootGrp;
    }

    /**
     * Liefert die Liste der Beitragszahlungen eines Mitglieds.
     *
     * @param mitgliedId
     *            ID des Mitglieds
     * @return in NaMi erfasste Beitragszahlungen des Mitglieds
     * @throws NamiApiException
     *             Fehler bei der Anfrage an NaMi
     * @throws IOException
     *             IOException
     */
    // TODO: was passiert, wenn Mitglied nicht vorhanden?
    public Collection<NamiBeitragszahlung> getBeitragszahlungen(int mitgliedId) throws NamiApiException, IOException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_BEITRAGSZAHLUNGEN, false);
        builder.setParameter("id", Integer.toString(mitgliedId));
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<Collection<NamiBeitragszahlung>>() {}.getType();

        // Load Regular Expression
        //^\s*var zahlungenStore = Ext.create\('Ext.data.Store',\{data:\{items:(.*)\},fields:\["id","rechnungsNummer","rechnungsPosition","buchungsText","beitragsSatz","value","beitragBis","status","beitragsKonto"\],proxy:\{type: 'memory',reader: \{type: 'json',root: 'items'\}\}\}\);$
        String regex = "^\\s*var zahlungenStore = Ext.create\\('Ext.data.Store',\\{data:\\{items:(.*)\\},fields:\\[\"id\",\"rechnungsNummer\",\"rechnungsPosition\",\"buchungsText\",\"beitragsSatz\",\"value\",\"beitragBis\",\"status\",\"beitragsKonto\"\\],proxy:\\{type: 'memory',reader: \\{type: 'json',root: 'items'\\}\\}\\}\\);$";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        return executeHtmlRequest(httpGet, pattern, type);
    }

    /**
     * Holt eine Tätigkeitszuordnung aus NaMi.
     *
     * @param personId
     *            ID des Mitglieds, dessen Tätigkeit abgefragt werden soll
     * @param taetigkeitId
     *            ID der Tätigkeitszuordnung
     * @return Tätigkeits-Datensatz
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    public NamiTaetigkeitAssignment getTaetigkeit(int personId, int taetigkeitId) throws IOException, NamiApiException {
        NamiURIBuilder builder = getURIBuilder(NamiURIBuilder.URL_NAMI_TAETIGKEIT);
        builder.appendPath(Integer.toString(personId));
        builder.appendPath(Integer.toString(taetigkeitId));
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<NamiTaetigkeitAssignment>>() {}.getType();
        NamiResponse<NamiTaetigkeitAssignment> resp = executeApiRequest(httpGet, type);
        if (resp.isSuccess()) {
            return resp.getData();
        } else {
            return null;
        }
    }
}
