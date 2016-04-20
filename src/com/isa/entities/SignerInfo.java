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
    
    private static final String FIRMA_XML = "XML";
    private static final String FIRMA_PDF = "PDF";
//    
    private static final String XML_ENVED = "ENVED";
    private static final String XML_ENVING = "ENVING";
    private String doc;
    private String perfil_firma;
    private String tipo_xml;
    private String alias;
    private String rol;
    private String motivo;
    private String localidad;
    private boolean apariencia;
    private Integer hoja;
    private Integer llx;
    private Integer lly;
    private Integer urx;
    private Integer ury;
    private String imagen;
    private String params;

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getPerfil_firma() {
        return perfil_firma;
    }

    public void setPerfil_firma(String perfil_firma) {
        this.perfil_firma = perfil_firma;
    }

    public String getTipo_xml() {
        return tipo_xml;
    }

    public void setTipo_xml(String tipo_xml) {
        this.tipo_xml = tipo_xml;
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

    public Integer getHoja() {
        return hoja;
    }

    public void setHoja(Integer hoja) {
        this.hoja = hoja;
    }

    public Integer getLlx() {
        return llx;
    }

    public void setLlx(Integer llx) {
        this.llx = llx;
    }

    public Integer getLly() {
        return lly;
    }

    public void setLly(Integer lly) {
        this.lly = lly;
    }

    public Integer getUrx() {
        return urx;
    }

    public void setUrx(Integer urx) {
        this.urx = urx;
    }

    public Integer getUry() {
        return ury;
    }

    public void setUry(Integer ury) {
        this.ury = ury;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
    
    public boolean isFirmaXML(){
        return this.perfil_firma.equals(FIRMA_XML);
    }
    
    public boolean isXMLEnveloping(){
        return this.tipo_xml.equals(XML_ENVING);
    }
    
    public boolean isXMLEnveloped(){
        return this.tipo_xml.equals(XML_ENVED);
    }
    
    public boolean isFirmaPDF(){
        return this.perfil_firma.equals(FIRMA_PDF);
    }
}
