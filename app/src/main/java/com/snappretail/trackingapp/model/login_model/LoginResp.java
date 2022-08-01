
package com.snappretail.trackingapp.model.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LoginResp {

    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("representativeID")
    @Expose
    private Integer representativeID;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getRepresentativeID() {
        return representativeID;
    }

    public void setRepresentativeID(Integer representativeID) {
        this.representativeID = representativeID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
