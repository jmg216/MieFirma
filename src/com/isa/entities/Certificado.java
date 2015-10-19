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
public class Certificado {
    
    private String alias;
    private String nombre;
    private String emisor;
    private String fechaValidez;

    public Certificado(){}
    
    public Certificado(String nombre, String emisor, String fechaValidez, String alias) {
        this.alias = alias;
        this.nombre = nombre;
        this.emisor = emisor;
        this.fechaValidez = fechaValidez;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getFechaValidez() {
        return fechaValidez;
    }

    public void setFechaValidez(String fechaValidez) {
        this.fechaValidez = fechaValidez;
    }
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }    
    
    
}
