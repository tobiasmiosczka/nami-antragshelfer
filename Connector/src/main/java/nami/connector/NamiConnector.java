package nami.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nami.connector.credentials.NamiCredentials;
import nami.connector.exception.NamiApiException;
import nami.connector.exception.NamiLoginException;

import nami.connector.json.JsonHelp;
import org.apache.commons.lang3.StringEscapeUtils;
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

        if (server.useApiAccess()) {
            nvps.add(new BasicNameValuePair("Login", "API"));
            nvps.add(new BasicNameValuePair("redirectTo", "./pages/loggedin.jsp"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            HttpResponse response = execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            Type type = NamiApiResponse.getType(Object.class);
            NamiApiResponse<Object> resp = JsonHelp.fromJson(new InputStreamReader(responseEntity.getContent()), type);

            if (resp.getStatusCode() == 0) {
                isAuthenticated = true;
                log.info("Authenticated to NaMi-Server using API.");
                // SessionToken wird automatisch als Cookie im HttpClient
                // gespeichert
            } else {
                // Fehler beim Verbinden (3000 z.B. bei falschem Passwort)
                isAuthenticated = false;
                throw new NamiLoginException(resp);
            }
        } else {
            nvps.add(new BasicNameValuePair("redirectTo", "app.jsp"));
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
                    System.out.println(statusCode);
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

        // Sende Request an Server
        HttpResponse response = execute(request);
        HttpEntity responseEntity = response.getEntity();

        checkResponse(response, responseEntity, "application/json");

        // Decodiere geliefertes JSON
        Reader respReader = new InputStreamReader(responseEntity.getContent());
        if (server.useApiAccess()) {
            Type type = NamiApiResponse.getType(typeOfT);
            NamiApiResponse<T> resp = JsonHelp.fromJson(respReader, type);

            if (resp.getStatusCode() != 0) {
                throw new NamiApiException(resp);
            }
            return resp.getResponse();
        } else {

            return JsonHelp.fromJson(respReader, typeOfT);
        }
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
}
