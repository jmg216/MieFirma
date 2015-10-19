/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.common;

import com.isa.entities.Certificado;
import com.isa.utiles.Utiles;
import java.security.KeyStore;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

/**
 *
 * @author JMiraballes
 */
public class HandlerCert {
    
    
    
    private static HandlerCert handlerCert;
    
    private HandlerCert(){
        this.certs = new ArrayList<>();
    }
    
    public static HandlerCert getInstance(){
        if (handlerCert == null){
            handlerCert = new HandlerCert();
        }
        return handlerCert;
    }
    
    private ArrayList<Certificado> certs;
    private KeyStore keystore;
    

    
    
    public ArrayList<Certificado> getCertificados() {
        return HandlerCert.getInstance().getCerts();
    }
    
    public void setCertificados( ArrayList<Certificado> certs ) {
        HandlerCert.getInstance().setCerts(certs);
    }    
    
    public void addCertificado( Certificado certs ){
        HandlerCert.getInstance().getCertificados().add(certs);
    }

    public void addKeystore(KeyStore keystore){
        HandlerCert.getInstance().setKeystore(keystore);
    }
    
    public KeyStore getKeystoreCert(){
        return HandlerCert.getInstance().getKeystore();
    }
    
    private ArrayList<Certificado> getCerts() {
        return certs;
    }

    private void setCerts(ArrayList<Certificado> certs) {
        this.certs = certs;
    }
    
    private KeyStore getKeystore() {
        return keystore;
    }

    private void setKeystore(KeyStore keystore) {
        this.keystore = keystore;
    }
}
