package uk.ac.ed.inf.aqmaps;

import java.awt.geom.Point2D;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

public class WordsLocation extends SensorLocation{
    private String words;

    public WordsLocation(double lat, double lng, String words){
        super(lat, lng);
        this.words = words;
    }
    
    @Override
    public Feature toFeature(){
        Feature location = super.toFeature();
        location.addStringProperty("location", words);
        return location;
    }
}