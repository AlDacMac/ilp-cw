# HEATMAP
Code to generate a heatmap of predicted pollution values over the drone confinement area, as specified by the coursework document.

## makefile
I have attatched a makefile to make building and running the code easier. 
- "make package" will clean first run mvn clean inside the maven project, deleting everything in the target directory, and then mvn package to create the shaded jarfile heatmap-0.01-SNAPSHOT.jar.
- "make run" will run the jarfile with the predictions.txt file in the root as input, outputting heatmap.geojson to the root directory