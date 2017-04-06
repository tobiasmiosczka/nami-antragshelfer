package nami.connector.namitypes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import nami.connector.NamiConnector;
import nami.connector.NamiResponse;
import nami.connector.NamiURIBuilder;
import nami.connector.exception.NamiApiException;

import org.apache.http.client.methods.HttpGet;

import com.google.gson.reflect.TypeToken;

// TODO: Implementiere hascode (?)
public class NamiTaetigkeitAssignmentListElement implements Comparable<NamiTaetigkeitAssignmentListElement> {
    private static final int MAX_TAETIGKEITEN = 1000;
    
    public static class EntriesType {
        private String gruppierung;
        private String caeaGroup;
        private String untergliederung;
        private String anlagedatum;
        private String aktivVon;
        private String aktivBis;
        private String caeaGroupForGf;
        private String taetigkeit;
        private String rowCssClass;
        private String mitglied;
    }

    private String descriptor;
    private EntriesType entries;
    private String name;
    private String representedClass;
    private int id;

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NamiTaetigkeitAssignmentListElement)) {
            return false;
        }
        NamiTaetigkeitAssignmentListElement other = (NamiTaetigkeitAssignmentListElement) obj;
        return id == other.id;
    }

    @Override
    public int compareTo(NamiTaetigkeitAssignmentListElement o) {
        return this.id - o.id;
    }
    
    public static Collection<NamiTaetigkeitAssignmentListElement> getTaetigkeiten(NamiConnector con, int id) throws IOException, NamiApiException {
        NamiURIBuilder builder = con.getURIBuilder(NamiURIBuilder.URL_NAMI_TAETIGKEIT);
        builder.appendPath(Integer.toString(id));
        builder.appendPath("flist");
        builder.setParameter("limit", Integer.toString(MAX_TAETIGKEITEN));
        builder.setParameter("page", Integer.toString(0));
        builder.setParameter("start", Integer.toString(0));
        HttpGet httpGet = new HttpGet(builder.build());

        Type type = new TypeToken<NamiResponse<Collection<NamiTaetigkeitAssignmentListElement>>>() {
        }.getType();
        NamiResponse<Collection<NamiTaetigkeitAssignmentListElement>> resp = con.executeApiRequest(httpGet, type);
        
        if (resp.isSuccess()) {
            return resp.getData();
        } else {
            return null;
        }
    }
}
