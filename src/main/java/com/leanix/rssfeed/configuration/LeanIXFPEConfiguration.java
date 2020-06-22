package com.leanix.rssfeed.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

public class LeanIXFPEConfiguration extends Configuration {

    @NotEmpty
    private String apiToken;

    @NotEmpty
    private String workspaceId;

    @NotNull
    @Valid
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty
    public String getApiToken() {
        return apiToken;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty
    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    @JsonProperty
    public String getWorkspaceId() {
        return workspaceId;
    }

    @JsonProperty
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }
}
