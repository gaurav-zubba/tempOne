package com.leanix.rssfeed.beans;

public class ChunkInformation
{
    private String lastDataObject;

    private String maxDataObject;

    private String firstDataObject;

    public String getLastDataObject ()
    {
        return lastDataObject;
    }

    public void setLastDataObject (String lastDataObject)
    {
        this.lastDataObject = lastDataObject;
    }

    public String getMaxDataObject ()
    {
        return maxDataObject;
    }

    public void setMaxDataObject (String maxDataObject)
    {
        this.maxDataObject = maxDataObject;
    }

    public String getFirstDataObject ()
    {
        return firstDataObject;
    }

    public void setFirstDataObject (String firstDataObject)
    {
        this.firstDataObject = firstDataObject;
    }

    @Override
    public String toString()
    {
        return "Class [lastDataObject = "+lastDataObject+", maxDataObject = "+maxDataObject+", firstDataObject = "+firstDataObject+"]";
    }
}