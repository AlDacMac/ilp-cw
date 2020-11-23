package uk.ac.ed.inf.aqmaps;

import java.awt.geom.Point2D;
import com.mapbox.geojson.Feature;
import com.google.gson.JsonObject;

/**
 * Class for representing the physical location of Sensors.
 */
public class SensorLocation extends Point2D.Double{

    /**
     * Constructor for AQLocation class. 
     * @param lat The location's latitude.
     * @param lng The location's longitude.
     */
    public SensorLocation(double lat, double lng){
        super(lng, lat);
    }

    public double getLat(){
        return this.getY();
    }

    public double getLng(){
        return this.getX();
    }

    public Feature toFeature(){
        com.mapbox.geojson.Point position = com.mapbox.geojson.Point.fromLngLat(this.getLng(), this.getLat());
        Feature location = Feature.fromGeometry(position);
        return location;
    }

    public Feature toFeature(JsonObject properties){
        com.mapbox.geojson.Point position = com.mapbox.geojson.Point.fromLngLat(this.getLng(), this.getLat());
        Feature location = Feature.fromGeometry(position, properties);
        return location;
    }

}