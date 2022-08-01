
package com.snappretail.trackingapp.model.relocation_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepLocationModel {

    @SerializedName("state")
    @Expose
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
