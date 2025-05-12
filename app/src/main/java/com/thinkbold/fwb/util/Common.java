package com.thinkbold.fwb.util;

import com.thinkbold.fwb.Retrofit.FWBApI;
import com.thinkbold.fwb.Retrofit.RetrofitClient;

public class Common {
    public static final String BASE_URL = "https://thinkbold.africa/hooded/";
    //public static final String BASE_URL = "https://thinkbold.africa/fwb/";

    public static FWBApI getAPIs() {
        return RetrofitClient.getClient(BASE_URL).create(FWBApI.class);
    }
}
