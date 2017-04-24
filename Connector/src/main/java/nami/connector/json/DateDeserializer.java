package nami.connector.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tobias on 24.04.2017.
 */
public class DateDeserializer implements JsonDeserializer<Date>{

    SimpleDateFormat sdfNaMi = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json.getAsString() == null || json.getAsString().equals("")) {
            return null;
        }

        //TODO: exception handling
        try {
            return sdfNaMi.parse(json.getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
