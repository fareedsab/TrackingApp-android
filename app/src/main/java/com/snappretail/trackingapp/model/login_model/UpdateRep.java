
package com.snappretail.trackingapp.model.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UpdateRep {

    @SerializedName("n")
    @Expose
    private Integer n;
    @SerializedName("nModified")
    @Expose
    private Integer nModified;
    @SerializedName("ok")
    @Expose
    private Integer ok;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getnModified() {
        return nModified;
    }

    public void setnModified(Integer nModified) {
        this.nModified = nModified;
    }

    public Integer getOk() {
        return ok;
    }

    public void setOk(Integer ok) {
        this.ok = ok;
    }

}
