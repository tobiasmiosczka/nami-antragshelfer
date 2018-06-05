package nami.connector.namitypes;

/**
 * Beschreibt die Bankverbindung eines Mitglieds.
 */
public class NamiKontoverbindung {
    private Integer id;
    private Integer mitgliedsNummer;

    private String kontoinhaber;
    private String institut;
    private String kontonummer;
    private String bankleitzahl;

    private String iban;
    private String bic;

    private Integer zahlungsKonditionId;
    private String zahlungsKondition;

    public String getKontoinhaber() {
        return kontoinhaber;
    }

    public String getInstitut() {
        return institut;
    }

    public String getKontonummer() {
        return kontonummer;
    }

    public String getBankleitzahl() {
        return bankleitzahl;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }
}