package App;

import GeoHash.GeoHash;

public class RechercheZone {
    private GeoHash geoHash;
    private String scientificName;
    private String order;
    private String superclass;
    private String recordedBy;
    private String species;

    public RechercheZone(GeoHash geoHash, String scientificName, String order, String superclass, String recordedBy, String species){
        this.geoHash = geoHash;
        this.scientificName = scientificName;
        this.order = order;
        this.superclass = superclass;
        this.recordedBy = recordedBy;
        this.species = species;
    }
}
