package uk.ac.ed.inf.aqmaps;

/**
 * Abstract class for things that can be connected to. Data is recieved from the connectable as
 * type RecievedT, and are processed into something of type OutputT.
 */
public abstract class Connectable<RecievedT, OutputT>{

    /**
     * Connect to the Connectable, process the data and return it
     * @return An object of type 
     */
    public OutputT connect(){
        RecievedT data = this.requestData();
        OutputT processedData = this.process(data);
        return processedData;
    }

    /**
     * Request data from the Connectable
     * @return  Data of type RecievedT
     */
    public abstract RecievedT requestData();

    /**
     * Process data of type RecievedT into the desired OutputT
     * @param data  Data of type RecievedT
     * @return  Data of type OutputT
     */
    public abstract OutputT process(RecievedT data);
}