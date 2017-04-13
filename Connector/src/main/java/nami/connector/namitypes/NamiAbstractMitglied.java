package nami.connector.namitypes;

import nami.connector.namitypes.enums.Geschlecht;
import nami.connector.namitypes.enums.MitgliedStatus;
import nami.connector.namitypes.enums.Mitgliedstyp;

public abstract class NamiAbstractMitglied {
    public abstract int getId();

    public abstract String getVorname();

    public abstract String getNachname();

    public abstract String getEmail();

    public abstract int getMitgliedsnummer();

    public abstract MitgliedStatus getStatus();

    public abstract Mitgliedstyp getMitgliedstyp();

    public abstract Geschlecht getGeschlecht();

    public abstract int getGruppierungId();

    public abstract String getGruppierung();

    public abstract int getVersion();

    @Override
    public final String toString() {
        return String.format("%s %s", getVorname(), getNachname());
    }
}
