package nami.connector.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nami.connector.namitypes.enums.NamiMitgliedStatus;

import java.lang.reflect.Type;

class MitgliedStatusDeserializer implements JsonDeserializer<NamiMitgliedStatus> {

    @Override
    public NamiMitgliedStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return NamiMitgliedStatus.fromString(json.getAsString());
    }
}
