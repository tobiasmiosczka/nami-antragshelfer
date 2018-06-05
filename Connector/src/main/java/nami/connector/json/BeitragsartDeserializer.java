package nami.connector.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nami.connector.namitypes.enums.NamiBeitragsart;

import java.lang.reflect.Type;

class BeitragsartDeserializer implements JsonDeserializer<NamiBeitragsart> {

    @Override
    public NamiBeitragsart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return NamiBeitragsart.fromString(json.getAsString());
    }
}
