package App;

import GeoHash.GeoHash;
import org.json.*;


import javafx.geometry.Point2D;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Requete {
    private ArrayList<RechercheNom> listeRechercheNom;
    private ArrayList<RechercheDate> listeRechercheDate;
    private ArrayList<RechercheZone> listeRechercheZone;

    public Requete(){
        this.listeRechercheNom = new ArrayList<>();
        this.listeRechercheDate = new ArrayList<>();
        this.listeRechercheZone = new ArrayList<>();
    }

    private static String readAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1){
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject getStartJSon(String path){
        try (Reader reader = new FileReader(path)){
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
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject(json);
    }

    public void creerRechercheNom(String scientificName, int precision, JSONObject JsonRoot){
        JSONArray resultatRecherche = JsonRoot.getJSONArray("features");
        for(Object object : resultatRecherche ) {
            JSONObject recherche = (JSONObject) object;
            ArrayList<Point2D> coordonnees = new ArrayList<>();
            JSONArray fauxJsonCoords = recherche.getJSONObject("geometry").getJSONArray("coordinates");
            for(Object fauxObjectPoint2D : fauxJsonCoords){
                JSONArray ArrayPoint2D = (JSONArray) fauxObjectPoint2D;
                for (Object objectPoint2D : ArrayPoint2D){
                    JSONArray jsonPoint2D = (JSONArray) objectPoint2D;
                    double x = jsonPoint2D.getDouble(0);
                    double y = jsonPoint2D.getDouble(1);

                    Point2D point2D = new Point2D(x,y);
                    coordonnees.add(point2D);
                }
            }


            Object objectOccurence = recherche.getJSONObject("properties").getInt("n");
            int occurence = (int) objectOccurence;

            RechercheNom rechercheNom = new RechercheNom(scientificName, coordonnees, occurence, precision);
            listeRechercheNom.add(rechercheNom);


        }
    }

    public  static void main(String[] args){

        Requete requete = new Requete();
        JSONObject requin = requete.getStartJSon("src/Selachii.json");
        requete.creerRechercheNom("Selachii", 3, requin);
        for(RechercheNom rechercheNom : requete.listeRechercheNom){
            System.out.println(rechercheNom.getCoord());
        }
    }

}


