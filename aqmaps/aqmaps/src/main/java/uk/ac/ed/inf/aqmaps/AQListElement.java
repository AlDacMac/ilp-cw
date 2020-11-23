package uk.ac.ed.inf.aqmaps;

public class AQListElement extends JsonListElement<String, AQReading>{
    public String location;
    public double battery;
    public String reading;

    @Override
    public AQReading toOutput(){
        return new AQReading(this.battery, this.reading);
    }

    @Override
    public Boolean matchesKey(String key){
        return(this.location.equals(key));
    }
}