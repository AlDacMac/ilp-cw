package uk.ac.ed.inf.aqmaps;

import java.awt.geom.Point2D;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.google.gson.JsonObject;

public class Sensor<StorageType, ReadingType extends SensorReading>{
    private SensorLocation location;
    //TODO see if there is a way that I can make the Connectable StorageType-agnostic
    private Connectable<StorageType, ReadingType> sensorConnection;
    private ReadingType reading;
    private boolean visited;
    
    /**
     * Constructor for Sensor class.
     * @param location A location
     */
    public Sensor(SensorLocation location, Connectable<StorageType, ReadingType> sensorConnection){
        this.location = location;
        this.sensorConnection = sensorConnection;
        this.visited = false;
    }

    public void takeReading(){
        this.reading = this.sensorConnection.connect();
    }

    public Feature toGeoJson(RangeMap<String> colourMapping, RangeMap<String> symbolMapping){
        JsonObject properties = ReadingType.nullGeoJsonProperties();
        if(visited){
            properties = this.reading.toGeoJsonProperties();
        }
        Feature sensor = this.location.toFeature(properties);
        return sensor;
    }
}