package nami.connector.namitypes.enums;

public enum NamiBaustein {
    MLK("Abgeschlossene Modulausbildung"),
    WBK("Woodbadge-Kurs oder Woodbadge-Kurs II"),
    BAUSTEIN_1A("Baustein 1a"),
    BAUSTEIN_1B("Baustein 1b"),
    BAUSTEIN_1C("Baustein 1c"),
    BAUSTEIN_2A("Baustein 2a"),
    BAUSTEIN_2B("Baustein 2b"),
    BAUSTEIN_2C("Baustein 2c"),
    BAUSTEIN_2D("Baustein 2d"),
    BAUSTEIN_2E("Baustein 2e"),
    BAUSTEIN_3A("Baustein 3a"),
    BAUSTEIN_3B("Baustein 3b"),
    BAUSTEIN_3C("Baustein 3c"),
    BAUSTEIN_3D("Baustein 3d"),
    BAUSTEIN_3E("Baustein 3e"),
    BAUSTEIN_3F("Baustein 3f"),
    SCHRITT1("Schritt 1"),
    SCHRITT2("Schritt 2");

    final String name;

    NamiBaustein(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static NamiBaustein fromString(String baustein) {
        for (NamiBaustein b : NamiBaustein.values()) {
            if (b.getName().equals(baustein)) {
                return b;
            }
        }
        return null;
    }
}