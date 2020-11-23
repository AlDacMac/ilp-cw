package uk.ac.ed.inf.aqmaps;


import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;

public class JsonListDataServer<KeyT, OutputT, ListElementT extends JsonListElement<KeyT , OutputT>> extends DataServer<String, OutputT>{
    KeyT key;

    public JsonListDataServer(String url, KeyT key){
        super(url, BodyHandlers.ofString());
        this.key = key;
    }

    @Override
    public OutputT process(String recievedData){
        //Use Gson to convert recievedData to an Arraylist of ListElementT
        Type listType = new TypeToken<ArrayList<ListElementT>>() {}.getType();
        ArrayList<ListElementT> elementList = new Gson().fromJson(recievedData, listType);
        //Search the arrayList for an element that matches our desired key
        for(ListElementT element : elementList){
            if(element.matchesKey(key)){
                return element.toOutput();
            }
        }
        //TODO If such an element cannot be found, throw an error
        return null;
    }
    
}