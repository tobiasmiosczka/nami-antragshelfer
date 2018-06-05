package nami.connector.json;

import nami.connector.namitypes.enums.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Date;

public class JsonHelp {
    private static final Gson gson = new GsonBuilder()

            .registerTypeAdapter(Date.class, new DateDeserializer())

            .registerTypeAdapter(Beitragsart.class, new BeitragsartDeserializer())
            .registerTypeAdapter(Geschlecht.class, new GeschlechtDeserializer())
            .registerTypeAdapter(MitgliedStatus.class, new MitgliedStatusDeserializer())
            .registerTypeAdapter(Mitgliedstyp.class, new MitgliedstypDeserializer())
            .registerTypeAdapter(Stufe.class, new StufeDeserializer())
            .registerTypeAdapter(Baustein.class, new BausteinDeserializer())
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
