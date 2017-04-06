package nami.connector;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Entspricht dem NaMi-API-Wrapper-Objekt. In diesem sind die Antworten auf
 * API-Anfragen eingeschlossen.
 * 
 * @author Fabian Lipp
 * 
 * @param <ResponseT>
 */
public class NamiApiResponse<ResponseT> {
    // Die folgenden Variablen stammen aus NaMi. Keinesfalls umbenennen.
    //private String apiSessionName;
    //private String apiSessionToken;
    //private int minorNumber;
    //private int majorNumber;
    private int statusCode;
    private String statusMessage;
    //private String servicePrefix;
    //private String methodCall;
    private ResponseT response;

    /**
     * Liefert den Statuscode, den NaMi zurückgibt.
     * 
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Liefert die Status-Nachricht, die NaMi zurückgibt.
     * 
     * @return the statusMessage
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Liefert die im Wrapper-Objekt enthaltene Antwort.
     * 
     * @return the response
     */
    public ResponseT getResponse() {
        return response;
    }

    /**
     * Ergänzt einen Typ um NamiApiResponse.
     * 
     * @param responseT
     *            einzuschließender Typ
     * @return Typ NamiApiResponse<responseT>
     */
    public static Type getType(final Type responseT) {
        return new ParameterizedType() {

            @Override
            public Type getRawType() {
                return NamiApiResponse.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] {responseT};
            }
        };
    }
}
