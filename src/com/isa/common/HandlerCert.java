/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.common;

import com.isa.entities.Certificado;
import com.isa.exception.AppletException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    

    public Certificado getCertificadoPorAlias( String alias ){
        if (!certs.isEmpty()){
            for (Certificado cer : certs){
                if (cer.getAlias().equals(alias) ){
                    return cer;
                }
            }
        }
        return null;
    }
    
    public PrivateKey getPrivateKeyPorAlias( String alias ) throws AppletException{
        
        try {
            return (PrivateKey) keystore.getKey(alias, null);
        } 
        catch (KeyStoreException ex) {
            Logger.getLogger(HandlerCert.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException("Ha ocurrido un error al acceder al almacén de certificados.", null, ex.getCause());    
        } 
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HandlerCert.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException("Ha ocurrido un error al acceder al almacén de certificados.", null, ex.getCause());    
        } 
        catch (UnrecoverableKeyException ex) {
            Logger.getLogger(HandlerCert.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException("No se ha podido acceder a la clave privada el certificado.", null, ex.getCause());    
        }
    }
    
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
