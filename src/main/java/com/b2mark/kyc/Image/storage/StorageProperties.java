package com.b2mark.kyc.image.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";
    private String jeusUid = "7552A.jpg";

    public String getLocation() {
        return location;
    }

    public String getJeusUid()
    {
        return jeusUid;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setJeusUid(String jeusUid)
    {
        this.jeusUid = jeusUid;
    }

}
