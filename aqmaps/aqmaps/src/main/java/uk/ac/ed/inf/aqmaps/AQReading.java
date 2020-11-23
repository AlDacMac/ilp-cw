package uk.ac.ed.inf.aqmaps;

import com.google.gson.JsonObject;

/**
 * Class for air quality readings. 
 */
public class AQReading extends SensorReading{
    private static RangeMap<String> colourMapping;
    private static RangeMap<String> symbolMapping;

    public static void setColourMapping(RangeMap<String> mapping){
        colourMapping = mapping;
    }

    public static void setSymbolMapping(RangeMap<String> mapping){
        symbolMapping = mapping;
    }

    /**
     * {@inheritDoc}
     * @return A JsonObject specifying gray rgb-string and marker-colour
     */
    public static JsonObject nullGeoJsonProperties(){
        var json = new JsonObject();
        json.addProperty("rgb-string", "#aaaaaa");
        json.addProperty("marker-colour", "#aaaaaa");
        return json;
    }

    //TODO Write logic for preventing operations relating to pollution when the reading is not good
    private boolean goodReading;
    private int pollution;
    private double battery;

    public AQReading(boolean goodReading, int pollution, double battery){
        this.goodReading = goodReading;
        this.pollution = pollution;
        this.battery = battery;
    }

    /**
     * Constructor for AQReading taking in raw battery and reading values. This che
     * @param battery
     * @param reading
     */
    public AQReading(double battery, String reading){
        
    }

    public boolean getGoodReading(){
        return this.goodReading;
    }

    public int getPollution(){
        return this.pollution;
    }

    public double getBattery(){
        return this.battery;
    }
    
    public String getColourHex(){
        if(!this.goodReading){
            return("#000000");
        } else{
            return colourMapping.get(this.pollution);
        }
    }

    public String getSymbol(){
        if(!this.goodReading){
            return("cross");
        } else{
            return symbolMapping.get(this.pollution);
        }
    }

    @Override
    public JsonObject toGeoJsonProperties(){
        var json = new JsonObject();
        String colour = this.getColourHex();
        String symbol = this.getSymbol();
        json.addProperty("rgb-string", colour);
        json.addProperty("marker-colour", colour);
        json.addProperty("marker-symbol", symbol);
        return json;
    }
}