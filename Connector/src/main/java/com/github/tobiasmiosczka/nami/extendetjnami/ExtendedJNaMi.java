package com.github.tobiasmiosczka.nami.extendetjnami;

import com.github.tobiasmiosczka.nami.extendetjnami.namitypes.Gruppierung;
import com.github.tobiasmiosczka.nami.extendetjnami.namitypes.Schulung;
import com.github.tobiasmiosczka.nami.extendetjnami.namitypes.SchulungenMap;
import com.google.gson.reflect.TypeToken;
import nami.connector.NamiConnector;
import nami.connector.NamiResponse;
import nami.connector.NamiURIBuilder;
import nami.connector.exception.NamiApiException;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by Tobias on 25.09.2016.
 * Class with static methods to extend the functionality of JNami
 */
public class ExtendedJNaMi {

    private static final String URL_GRUPPIERUNGEN = "/nami/gruppierungen/filtered-for-navigation/gruppierung/node/";
    private static final String URL_SCHULUNGEN = "/nami/mitglied-ausbildung/filtered-for-navigation/mitglied/mitglied/";

    public static Collection<Gruppierung> getGruppierungen(NamiConnector con) throws IOException, NamiApiException {
        NamiURIBuilder builder = con.getURIBuilder(URL_GRUPPIERUNGEN);
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<Collection<Gruppierung>>>() {
        }.getType();
        NamiResponse<Collection<Gruppierung>> resp = con.executeApiRequest(httpGet, type);
        return resp.getData();
    }

    public static SchulungenMap getSchulungen(NamiConnector con, int mitgliedsID) throws IOException, NamiApiException {
        NamiURIBuilder builder = con.getURIBuilder(URL_SCHULUNGEN);
        builder.appendPath(Integer.toString(mitgliedsID));
        builder.appendPath("/flist");
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<Collection<Schulung>>>() {
        }.getType();
        NamiResponse<Collection<Schulung>> resp = con.executeApiRequest(httpGet, type);

        return new SchulungenMap(resp.getData());
    }

}
