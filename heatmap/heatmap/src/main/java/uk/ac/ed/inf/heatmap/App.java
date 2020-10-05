package uk.ac.ed.inf.heatmap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import com.mapbox.geojson.*;

public class App 
{
    /**
     * Returns pollution level predictions in 2d list format, given a path to a txt file 
     * containing those predictions
     * 
     * This method expects the predictions file to be written in the same format as in
     *  the coursework doc (on page 13). Given this assumption, the returned int[][] is a 
     *  rectangle with the [i][j] element corresponding to the jth number in the ith row 
     *  of the txt file.
     * @param filePath path to the prediction file (here "./predictions.txt") in String form. 
     * @return A 2d list of ints representing pollution levels
     */
    private static int[][] parsePredictions(String filePath){
        /** 
         * predictions ArrayList is used to store values, once all of them are parsed 
         *  it can be converted into an array. This is done to get around immutable array lengths.
        */
        var predictions = new ArrayList<int[]>();  
        try{
            var predFile = new File(filePath);
            var scanner = new Scanner(predFile);
            /** Iterates over the lines of of the file */
            while(scanner.hasNextLine()){
                /** 
                 * Split the line by  ", " characters, and iterate through the list of Strings,
                 *  converting them into ints and thowing an error if they cannot be converted.
                */
                var predStrings = scanner.nextLine().split(", ");
                /** preds is an array used to store converted predictions */
                var preds = new int[predStrings.length];
                for(int i = 0; i < predStrings.length; i++){
                    try { 
                        preds[i] = Integer.parseInt(predStrings[i]);
                    } catch (NumberFormatException e){
                        System.out.println("Error parsing file - ensure all values are integers");
                        e.printStackTrace();
                    }
                }
                predictions.add(preds);
            }
            scanner.close();
        } catch(FileNotFoundException e) {
            System.out.println("Error finding file - check your input path");
            e.printStackTrace();
        }
        /** Convert our arraylist to an array and return it */
        var predArrays = predictions.toArray(new int[predictions.size()][]);
        return predArrays;
    }

    /**
     * Returns a FeatureCollection representing a heatmap of predicted pollution levels, 
     *  when given a 2d list containing those predictions.
     * 
     * The bound values are used to calculate the dimensions of each individual grid square
     * 
     * predictions[0][0] is expected to contain the predicted values for the square at the 
     *  northeast corner, and as we iterate we move south and east
     * @param predictions   A 2d list of predicted pollution value
     * @param nBound    The northmost boundary of the grid
     * @param eBound    The eastmost boundary of the grid
     * @param sBound    The southmost boundary of the grid
     * @param wBound    The westmost boundary of the grid
     * @param hexMapping    A RangeMap from int (representing pollution level) to hex color string
     * @return
     */
    private static FeatureCollection drawheatMap(int[][] predictions, double nBound, double eBound, 
    double sBound, double wBound, RangeMap<String> hexMapping){
        double latDimension = predictions.length; /** Number of grids squares from north to south */
        double lngDimension = predictions[0].length; /** Number of grids squares from west to east */

        /** Checks that all the nested int[]s are the same length, ensuring that grid is rectangular*/
        for(var predArr: predictions){
            if(predArr.length != lngDimension){
                throw(new IllegalArgumentException("Predicton grid must be rectangular"));
            }
        }

        /** 
         * We increment through the prediction grid, as well as through lat/long values, creating 
         * our grid squares using geojson and placng them in the gridSquares arrayList.
        */
        var gridSquares = new ArrayList<Feature>();
        double latIncrement = (nBound - sBound) / latDimension;
        double lngIncrement = (wBound - eBound) / latDimension;
        /** Iterate with respect to latitude */
        for(double i = 0, nLat = nBound, sLat = nLat - latIncrement; i < latDimension; 
        i++, nLat -= latIncrement, sLat -= latIncrement){
            /** Iterate with respect to longitude */
            for(double j = 0, wLng = wBound, eLng = wLng - lngIncrement; j < lngDimension; 
            j++, wLng -= lngIncrement, eLng -= lngIncrement){
                /** 
                 * Creates a list of lng/lat points, representing the bounds of a gridsquare
                 *  (Note that the first point is repeated, closing the square)
                */
                var coordinates = Arrays.asList(Arrays.asList(
                    Point.fromLngLat(wLng, nLat),
                    Point.fromLngLat(eLng, nLat),
                    Point.fromLngLat(eLng, sLat),
                    Point.fromLngLat(wLng, sLat),
                    Point.fromLngLat(wLng, nLat)
                ));
                /** Use the calculated properies of the gridsquare to create it as a geojson feature */
                var geometry = Polygon.fromLngLats(coordinates);
                var gridSquare = Feature.fromGeometry(geometry);
                gridSquare.addStringProperty("fill", hexMapping.get(predictions[(int)i][(int)j]));
                gridSquare.addNumberProperty("fill-opacity", 0.75);
                gridSquares.add(gridSquare);
            }
        }
        return(FeatureCollection.fromFeatures(gridSquares));
    }

    /**
     * Given the path to pollution prediction file as input, returns a heatmap covering the drone
     *  confinement area specified by the coursework
     * @param args  args[0] is our prediction file path
     */
    public static void main( String[] args )
    {
        var predictionPath = args[0];
        /** Set up the RangeMap from pollution ranges to hex colour strings */
        var hexMapping = new RangeMap<String>();
        hexMapping.add(96, 128, "#c0ff00");
        hexMapping.add(32, 64, "#40ff00");
        hexMapping.add(0, 32, "#00ff00");
        hexMapping.add(64, 96, "#80ff00");
        hexMapping.add(160, 192, "#ff8000");
        hexMapping.add(128, 160, "#ffc000");
        hexMapping.add(192, 224, "#ff4000");
        hexMapping.add(224, 256, "#ff0000");
        
        /** Create the geojson object using our other methods */
        var predictions = parsePredictions(predictionPath);
        FeatureCollection heatmap = drawheatMap(predictions, 55.946233, -3.184319, 55.942617, -3.192473, hexMapping);

        /** Write the geojson strings to "./heatmap.geojson" */
        var outputFile = new File("./heatmap.geojson");
        try{
            outputFile.createNewFile();
        } catch(IOException e){
            e.printStackTrace();
        }
        try{
            var writer = new FileWriter("./heatmap.geojson");
            writer.write(heatmap.toJson());
            writer.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
