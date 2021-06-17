package App;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class RequeteTest {

    @Test
    @DisplayName("URL nom/precision")
    void getURL() {
        assertEquals("https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae", App.Requete.getURL("Delphinidae",3));
    }

    @Test
    @DisplayName("URL nom/precision/dates début&fin")
    void getURLDate() {
        assertEquals("https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae&startdate=2012-12-12&enddate=2021-01-01",
                App.Requete.getURLDate("Delphinidae",3,"2012-12-12","2021-01-01"));
    }

    @Test
    @DisplayName("URL nom/geohash")
    void getURLZone() {
        assertAll(() -> assertEquals("https://api.obis.org/v3/occurrence?geometry=spd", App.Requete.getURLZone("","spd")),
                () -> assertEquals("https://api.obis.org/v3/occurrence?scientificname=Delphinidae&geometry=spd", App.Requete.getURLZone("Delphinidae","spd")));
    }

    @Test
    @DisplayName("URL verbose")
    void getURLNom() {
        assertEquals("https://api.obis.org/v3/taxon/complete/verbose/de", App.Requete.getURLNom("de"));
    }

    @Test
    @DisplayName("Lire le JSON local")
    void getStartJSon(){
        JSONObject actual = App.Requete.getStartJSon("src/main/java/Selachii.json");
        assertEquals(163261, actual.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getInt("n"));
    }

    @Test
    @DisplayName("Lire un JSONObject a partir d'un URL")
    void readJsonFromUrl() {
        JSONObject actual = App.Requete.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/3?scientificname=Selachii");
        assertEquals(163261, actual.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getInt("n"));
    }

    @Test
    @DisplayName("Lire un JSONArray a partir d'un URL")
    void readJsonFromUrlListeNom() {
        JSONArray actual = App.Requete.readJsonFromUrlListeNom("https://api.obis.org/v3/taxon/complete/verbose/de");
        assertEquals("Deinococcus-Thermus", actual.getJSONObject(0).getString("scientificName"));
    }

    @Test
    void creerRechercheNom() {

    }

    @Test
    void creerRechercheDate() {
    }

    @Test
    void creerRechercheZone() {
    }

    @Test
    void listeNom() {
    }
}