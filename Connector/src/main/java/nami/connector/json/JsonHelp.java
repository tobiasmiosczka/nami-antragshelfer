package nami.connector.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nami.connector.namitypes.enums.NamiBaustein;
import nami.connector.namitypes.enums.NamiBeitragsart;
import nami.connector.namitypes.enums.NamiEbene;
import nami.connector.namitypes.enums.NamiGeschlecht;
import nami.connector.namitypes.enums.NamiMitgliedStatus;
import nami.connector.namitypes.enums.NamiMitgliedstyp;
import nami.connector.namitypes.enums.NamiStufe;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Date;

public class JsonHelp {
    private static final Gson gson = new GsonBuilder()

            .registerTypeAdapter(Date.class, new DateDeserializer())

            .registerTypeAdapter(NamiEbene.class, new EbeneDeserializer())
            .registerTypeAdapter(NamiBeitragsart.class, new BeitragsartDeserializer())
            .registerTypeAdapter(NamiGeschlecht.class, new GeschlechtDeserializer())
            .registerTypeAdapter(NamiMitgliedStatus.class, new MitgliedStatusDeserializer())
            .registerTypeAdapter(NamiMitgliedstyp.class, new MitgliedstypDeserializer())
            .registerTypeAdapter(NamiStufe.class, new StufeDeserializer())
            .registerTypeAdapter(NamiBaustein.class, new BausteinDeserializer())
            .create();

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromJson(Reader json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }
}
