package nami.connector.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nami.connector.namitypes.enums.NamiStufe;

import java.lang.reflect.Type;

class StufeDeserializer implements JsonDeserializer<NamiStufe> {

    @Override
    public NamiStufe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return NamiStufe.fromString(json.getAsString());
    }
}
