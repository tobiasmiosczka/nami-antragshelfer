package nami.connector.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nami.connector.namitypes.enums.NamiGeschlecht;

import java.lang.reflect.Type;

class GeschlechtDeserializer implements JsonDeserializer<NamiGeschlecht> {

    @Override
    public NamiGeschlecht deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return NamiGeschlecht.fromString(json.getAsString());
    }
}
