package uk.ac.ed.inf.aqmaps;

import com.google.gson.JsonObject;

/**
 * Abstract class for sensor readings.
 */
public abstract class SensorReading{

    /**
     * Return properties for the sensor's geojson if a reading was not taken
     * @return An empty JsonObject
     */
    public static JsonObject nullGeoJsonProperties(){
        return new JsonObject();
    }
    /**
     * Method for outputting properties for the sensor's geojson representation
     * @return A JsobObject specifying properties for a GeoJson object corresponding to the sensor that this reading came from
     */
    public abstract JsonObject toGeoJsonProperties();
}