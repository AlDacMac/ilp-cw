package uk.ac.ed.inf.aqmaps;

public abstract class JsonListElement<KeyT, OutputT>{

    public abstract OutputT toOutput();

    public abstract Boolean matchesKey(KeyT key);
}