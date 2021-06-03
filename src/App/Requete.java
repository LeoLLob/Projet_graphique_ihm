package App;

import GeoHash.GeoHash;
import org.json.*;
import sun.misc.Version;
import sun.net.www.http.HttpClient;


import java.io.*;
import java.time.Duration;


public class Requete {

    private static String readAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1){
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject getStartJSon(String path){
        try (Reader reader = new FileReader("path")){
            BufferedReader rd = new BufferedReader(reader);
            String jsonText = readAll(rd);
            JSONObject jsonRoot = new JSONObject(jsonText);
            return jsonRoot;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getURL(String nomScientifique, int precision){
       String url = "https://api.obis.org/v3/occurrence/grid/" + precision +"?scientificname=" + nomScientifique;
       return url;
    }

    public String getURLDate(String nomScientifique, int precision, String dateDebut, String dateFin){
        String url = "https://api.obis.org/v3/occurrence/grid/" + precision + "?scientificname=" + nomScientifique +
                "&startdate=" + dateDebut + "&enddate=" + dateFin;
        return url;
    }

    public String getURLZone(String nomScientifique, GeoHash geohash){
        if(nomScientifique.isEmpty()) {
            String url = "https://api.obis.org/v3/occurrence?geometry=" +geohash;
            return url;
        }else{
            String url = "https://api.obis.org/v3/occurrence?scientificname=" + nomScientifique + "&geometry=" +geohash;
            return url;
        }
    }

    public String getURLNom(String chaine){

        String url = "https://api.obis.org/v3/taxon/complete/verbose/"  + chaine;
        return url;

    }

    public static JSONObject readJsonFromUrl(String url) {
        String json = "";
        HttpClient client = HttpClient.newBuilder()
                .version(Version.HTTP_1_1)
                .fllowRedirects(Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            json = client.sendAsync(request, BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10,TimeUnit.SECONDS)
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject(json);
    }
}

}
