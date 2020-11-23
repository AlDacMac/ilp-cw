package uk.ac.ed.inf.aqmaps;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;

/**
 * Abstract extension of Connectable, where the object being connected to is a data server at a 
 * a specific address. Data is requested using the Java 11 HttpClient, and as such RecievedT
 * is HttpResponse<HttpBodyT>, where HttpBodyT is a generic type that we will are expected to 
 * provide a bodyhandler for.
 */
public abstract class DataServer<RecievedT, OutputT> extends Connectable<RecievedT, OutputT>{

    private String url;
    private HttpClient client;
    private HttpResponse.BodyHandler<RecievedT> bodyHandler;

    public DataServer(String url, HttpResponse.BodyHandler<RecievedT> bodyHandler){
        this.client = HttpClient.newHttpClient();
        this.url = url;
        this.bodyHandler = bodyHandler;
    }

    /**
     * Requests data from the server via the HttpClient, and extracts the body from the HttpResponse
     * using this.bodyHandler.
     */
    @Override 
    public RecievedT requestData(){
        var request = HttpRequest.newBuilder().uri(URI.create(this.url)).build();
        try{
            HttpResponse<RecievedT> response = client.send(request, bodyHandler);
            if(response.statusCode() != 200){
                //TODO throw an exception, not sure what type, but something that can be caught
            } else{
                RecievedT data = response.body();
                return data;
            }
        } catch(InterruptedException| IOException e){
            System.out.println("Fatal error: couldn't connect to server");
            System.exit(1);
        }
        //Should be unreachable
        return null;
    }
}