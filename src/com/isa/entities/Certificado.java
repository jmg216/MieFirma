/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.entities;

import java.math.BigInteger;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 *
 * @author JMiraballes
 */
public class Certificado {
    
    
    private String alias;
    private String nombre;
    private String emisor;
    private String fechaDesde;
    private String fechaHasta;
    private String oSubject;
    private String oUSubject;
    private String oEmisor;
    private String oUEmisor;
    private String nroSerie;
    private transient String providerName;
    private transient X509Certificate certX509;
    private transient Certificate[] chainCert;

    public Certificado(){}
    
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
    
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }    

    public X509Certificate getCertX509() {
        return certX509;
    }

    public void setCertX509(X509Certificate certX509) {
        this.certX509 = certX509;
    }

    public Certificate[] getChainCert() {
        return chainCert;
    }

    public void setChainCert(Certificate[] chainCert) {
        this.chainCert = chainCert;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    public String getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public String getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getoSubject() {
        return oSubject;
    }

    public void setoSubject(String oSubject) {
        this.oSubject = oSubject;
    }

    public String getoUSubject() {
        return oUSubject;
    }

    public void setoUSubject(String oUSubject) {
        this.oUSubject = oUSubject;
    }

    public String getoEmisor() {
        return oEmisor;
    }

    public void setoEmisor(String oEmisor) {
        this.oEmisor = oEmisor;
    }

    public String getoUEmisor() {
        return oUEmisor;
    }

    public void setoUEmisor(String oUEmisor) {
        this.oUEmisor = oUEmisor;
    }
    

    
    
}
