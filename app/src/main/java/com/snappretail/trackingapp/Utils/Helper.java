package com.snappretail.trackingapp.Utils;

import com.snappretail.trackingapp.Interface.MyWebService;

public class Helper {
    private static MyWebService myWebService;
    public static MyWebService myWebService() {
        if (myWebService == null) {
            myWebService = OkhhtpInterception.getRetrofit().create(MyWebService.class);

        }
        return myWebService;
    }

    public static MyWebService myWebService2() {
        if (myWebService == null) {
            myWebService = OkhhtpInterception.getRetrofit().create(MyWebService.class);

        }
        return myWebService;
    }
}
