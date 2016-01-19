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
public class SignerInfo {
    
    private String doc;
    private String alias;
    private String rol;
    private String motivo;
    private String localidad;
    private boolean apariencia;
    private int hoja;
    private int llx;
    private int lly;
    private int urx;
    private int ury;
    private String imagen;

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public boolean isApariencia() {
        return apariencia;
    }

    public void setApariencia(boolean apariencia) {
        this.apariencia = apariencia;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getHoja() {
        return hoja;
    }

    public void setHoja(int hoja) {
        this.hoja = hoja;
    }

    public int getLlx() {
        return llx;
    }

    public void setLlx(int llx) {
        this.llx = llx;
    }

    public int getLly() {
        return lly;
    }

    public void setLly(int lly) {
        this.lly = lly;
    }

    public int getUrx() {
        return urx;
    }

    public void setUrx(int urx) {
        this.urx = urx;
    }

    public int getUry() {
        return ury;
    }

    public void setUry(int ury) {
        this.ury = ury;
    }
    
    
    
    
}
