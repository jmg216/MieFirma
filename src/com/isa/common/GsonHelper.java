/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.common;

import com.google.gson.Gson;

/**
 *
 * @author JMiraballes
 */
public class GsonHelper {
    private Gson gson;
    private static GsonHelper gsonHelper;
    
    private GsonHelper(){
        gson = new Gson();
    }

    public static GsonHelper getInstance(){
        if ( gsonHelper == null ){
            gsonHelper = new GsonHelper();
        }
        return gsonHelper;
    }
    
    /*
    public RequestPdfWS fromJsonToRequestPdfWS( String json, String cedula ){
        RequestPdfWS request = gson.fromJson(json, RequestPdfWS.class);
        request.setCedula(cedula);
        return request;
    }*/
    
    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
    
    
}
