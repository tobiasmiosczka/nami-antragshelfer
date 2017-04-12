package nami.connector.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nami.connector.Beitragsart;

import java.lang.reflect.Type;

class BeitragsartDeserializer implements JsonDeserializer<Beitragsart> {

    @Override
    public Beitragsart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Beitragsart.fromString(json.getAsString());
    }
}
