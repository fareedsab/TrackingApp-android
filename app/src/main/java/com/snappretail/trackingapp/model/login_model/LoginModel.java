
package com.snappretail.trackingapp.model.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LoginModel {

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("login_resp")
    @Expose
    private LoginResp loginResp;
    @SerializedName("update_rep")
    @Expose
    private UpdateRep updateRep;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LoginResp getLoginResp() {
        return loginResp;
    }

    public void setLoginResp(LoginResp loginResp) {
        this.loginResp = loginResp;
    }

    public UpdateRep getUpdateRep() {
        return updateRep;
    }

    public void setUpdateRep(UpdateRep updateRep) {
        this.updateRep = updateRep;
    }

}
