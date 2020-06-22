package com.leanix.rssfeed.core;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;


@Entity
@Table(name = "master_rss_links")
public class MasterRssEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Id
    @Column(name = "csid", nullable = false)
    private String csid;

    @Column(name = "rsslink")
    private String rsslink;

    @Column(name = "Provider")
    private String provider;

    public MasterRssEntity() {
        this.rsslink = "";
        this.provider = "";
    }

    public MasterRssEntity(String csid, String rsslink, String provider) {
        this.csid = csid;
        this.rsslink = rsslink;
        this.provider = provider;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCsid() {
        return csid;
    }

    public void setCsid(String csid) {
        this.csid = csid;
    }

    public String getRsslink() {
        return rsslink;
    }

    public void setRsslink(String rsslink) {
        this.rsslink = rsslink;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
