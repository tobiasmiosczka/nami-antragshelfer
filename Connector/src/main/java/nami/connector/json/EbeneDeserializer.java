package nami.connector.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nami.connector.namitypes.enums.NamiEbene;

import java.lang.reflect.Type;

public class EbeneDeserializer implements JsonDeserializer<NamiEbene> {

    @Override
    public NamiEbene deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return NamiEbene.fromString(json.getAsString());
    }
}
