/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.entities;

/**
 *
 * @author JMiraballes
 */
public class ParamInfo {
    
    private String param_keystores;
    private String param_name;
    private String param_lib;
    private String param_showinfo;
    private String param_modulos;
    private String param_lib_win;
    private String param_lib_unx;
    private String param_ruta;
    
    private static ParamInfo instance;
    
    public static ParamInfo getInstance(){
        
        if (instance == null){
            instance = new ParamInfo();
        }
        return instance;
    }
   
    private ParamInfo(){    
    }

    public String getParam_ruta() {
        return param_ruta;
    }

    public void setParam_ruta(String param_ruta) {
        this.param_ruta = param_ruta;
    }   
    
    public String getParam_keystores() {
        return param_keystores;
    }

    public void setParam_keystores(String param_keystores) {
        this.param_keystores = param_keystores;
    }

    public String getParam_name() {
        return param_name;
    }

    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    public String getParam_lib() {
        return param_lib;
    }

    public void setParam_lib(String param_lib) {
        this.param_lib = param_lib;
    }

    public String getParam_showinfo() {
        return param_showinfo;
    }

    public void setParam_showinfo(String param_showinfo) {
        this.param_showinfo = param_showinfo;
    }

    public String getParam_modulos() {
        return param_modulos;
    }

    public void setParam_modulos(String param_modulos) {
        this.param_modulos = param_modulos;
    }

    public String getParam_lib_win() {
        return param_lib_win;
    }

    public void setParam_lib_win(String param_lib_win) {
        this.param_lib_win = param_lib_win;
    }

    public String getParam_lib_unx() {
        return param_lib_unx;
    }

    public void setParam_lib_unx(String param_lib_unx) {
        this.param_lib_unx = param_lib_unx;
    }
    
    
    
    
}
