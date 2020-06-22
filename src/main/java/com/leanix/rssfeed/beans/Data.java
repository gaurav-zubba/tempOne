package com.leanix.rssfeed.beans;


public class Data
{
    private Provider[] provider;

    private Documents[] documents;

    private String name;

    public Provider[] getProvider ()
    {
        return provider;
    }

    public void setProvider (Provider[] provider)
    {
        this.provider = provider;
    }

    public Documents[] getDocuments ()
    {
        return documents;
    }

    public void setDocuments (Documents[] documents)
    {
        this.documents = documents;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [provider = "+provider+", documents = "+documents+", name = "+name+"]";
    }
}


