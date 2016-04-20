/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.firma.pades;

import com.isa.exception.AppletException;
import com.isa.utiles.Utiles;
import com.isa.utiles.UtilesResources;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 *
 * @author JMiraballes
 */
public class PDFFirma {

    private PrivateKey pk;
    private Certificate[] chainCert;
    private String providername;
    
    //Apariencia de firma.
    private String firmante;
    private String rutaImagen;
    private boolean apariencia;
    private String textoFirma;
    private int hoja;
    //private String posicionVertical;
    private int largo;
    private int ancho;  

    public String getTextoFirma() {
        return textoFirma;
    }

    public void setTextoFirma(String textoFirma) {
        this.textoFirma = textoFirma;
    }
    
    public PrivateKey getPk() {
        return pk;
    }

    public void setPk(PrivateKey pk) {
        this.pk = pk;
    }

    public Certificate[] getChainCert() {
        return chainCert;
    }

    public void setChainCert(Certificate[] chainCert) {
        this.chainCert = chainCert;
    }

    public String getProvidername() {
        return providername;
    }

    public void setProvidername(String providername) {
        this.providername = providername;
    }

    public String getFirmante() {
        return firmante;
    }

    public void setFirmante(String firmante) {
        this.firmante = firmante;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public boolean isApariencia() {
        return apariencia;
    }

    public void setApariencia(boolean apariencia) {
        this.apariencia = apariencia;
    }

    public int getHoja() {
        return hoja;
    }

    public void setHoja(int hoja) {
        this.hoja = hoja;
    }

    public int getLargo() {
        return largo;
    }

    public void setLargo(int largo) {
        this.largo = largo;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }
    
    public int[] calcularCorrdenadasFirma(  int cantidadFirmaActuales,  int ancho, int largo  ) throws AppletException{
        //[0] llx, [1] lly, [3] urx, [4] ury
        int[] coord = new int[4];
        
        int espacioFirma = Integer.valueOf( UtilesResources.getProperty(UtilesResources.PROP_ESPACIO_FIRMAS));
        
        int llx = Integer.valueOf(UtilesResources.getProperty(UtilesResources.PROP_INIT_IZQ_X));
        int lly = Integer.valueOf(UtilesResources.getProperty(UtilesResources.PROP_INIT_IZQ_Y));
        int urx = (llx + largo);
        int ury = (lly + ancho);
        
        if (cantidadFirmaActuales >= 1){
            llx = (llx + (espacioFirma * cantidadFirmaActuales));
            urx = (urx + (espacioFirma * cantidadFirmaActuales));
        }
        
        coord[0] = llx;
        coord[1] = lly;
        coord[2] = urx;
        coord[3] = ury;
        
        return coord;
    }    
}
