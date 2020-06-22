package com.leanix.rssfeed.beans;

public class Content
{
    private Data data;

    private String id;

    private String type;

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", id = "+id+", type = "+type+"]";
    }
}