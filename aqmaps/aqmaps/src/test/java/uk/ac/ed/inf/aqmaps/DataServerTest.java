package uk.ac.ed.inf.aqmaps;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DataServerTest{
    Connectable<String, AQReading> testServer = new JsonListDataServer<String, AQReading, AQListElement>(
        "http://localhost:80/maps/2020/01/01/air-quality-data.json", "love.behind.orchestra");
    AQReading testReading = testServer.connect();

    @Test
    public void baseTest(){
        assertTrue(true);
    }
}