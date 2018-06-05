package nami.connector.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import nami.connector.namitypes.enums.NamiMitgliedstyp;

import java.lang.reflect.Type;

class MitgliedstypDeserializer implements JsonDeserializer<NamiMitgliedstyp> {

    @Override
    public NamiMitgliedstyp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return NamiMitgliedstyp.fromString(json.getAsString());
    }
}
