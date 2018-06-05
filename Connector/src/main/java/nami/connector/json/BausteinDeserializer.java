package nami.connector.json;

import nami.connector.namitypes.enums.NamiBaustein;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

class BausteinDeserializer implements JsonDeserializer<NamiBaustein> {

    @Override
    public NamiBaustein deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return NamiBaustein.fromString(json.getAsString());
    }
}
