package com.leanix.rssfeed.beans;

public class ResponseBean
{
    private String connectorType;

    private String lxVersion;

    private String connectorId;

    private String connectorVersion;

    private String description;

    private ChunkInformation chunkInformation;

    private String lxWorkspace;

    private String processingDirection;

    private String processingMode;

    private Content[] content;

    public String getConnectorType ()
    {
        return connectorType;
    }

    public void setConnectorType (String connectorType)
    {
        this.connectorType = connectorType;
    }

    public String getLxVersion ()
    {
        return lxVersion;
    }

    public void setLxVersion (String lxVersion)
    {
        this.lxVersion = lxVersion;
    }

    public String getConnectorId ()
    {
        return connectorId;
    }

    public void setConnectorId (String connectorId)
    {
        this.connectorId = connectorId;
    }

    public String getConnectorVersion ()
    {
        return connectorVersion;
    }

    public void setConnectorVersion (String connectorVersion)
    {
        this.connectorVersion = connectorVersion;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public ChunkInformation getChunkInformation ()
    {
        return chunkInformation;
    }

    public void setChunkInformation (ChunkInformation chunkInformation)
    {
        this.chunkInformation = chunkInformation;
    }

    public String getLxWorkspace ()
{
    return lxWorkspace;
}

    public void setLxWorkspace (String lxWorkspace)
    {
        this.lxWorkspace = lxWorkspace;
    }

    public String getProcessingDirection ()
    {
        return processingDirection;
    }

    public void setProcessingDirection (String processingDirection)
    {
        this.processingDirection = processingDirection;
    }

    public String getProcessingMode ()
    {
        return processingMode;
    }

    public void setProcessingMode (String processingMode)
    {
        this.processingMode = processingMode;
    }

    public Content[] getContent ()
    {
        return content;
    }

    public void setContent (Content[] content)
    {
        this.content = content;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [connectorType = "+connectorType+", lxVersion = "+lxVersion+", connectorId = "+connectorId+", connectorVersion = "+connectorVersion+", description = "+description+", chunkInformation = "+chunkInformation+", lxWorkspace = "+lxWorkspace+", processingDirection = "+processingDirection+", processingMode = "+processingMode+", content = "+content+"]";
    }
}