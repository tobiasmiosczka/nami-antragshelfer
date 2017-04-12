package nami.connector.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nami.connector.Stufe;

import java.lang.reflect.Type;

class StufeDeserializer implements JsonDeserializer<Stufe> {

    @Override
    public Stufe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Stufe.fromString(json.getAsString());
    }
}
