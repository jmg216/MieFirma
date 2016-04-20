package com.isa.token;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.isa.common.HandlerCert;
import com.isa.entities.Certificado;
import com.isa.entities.ParamInfo;
import com.isa.exception.AppletException;
import com.isa.utiles.Utiles;
import com.isa.utiles.UtilesResources;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import sun.security.pkcs11.SunPKCS11;

/**
 *
 * @author JMiraballes
 */
public class Token {
    
    private String module;
    private String library;
    private Boolean showInfo;
    private String password;
    private boolean activo;
    private KeyStore keystore;
    private boolean logued;
    private String pkcs11config;
    private ArrayList<X509Certificate> listaCerts;
    
    public Token(){
        this.activo = false;
    }

    public Token(String module, String library, String password) {
        this.module = module;
        this.library = library;
        this.showInfo = true;
        this.password = password;
        this.listaCerts = new ArrayList();
    }
    
    
    public Token(String module, String library)  {
        
        this.module = module;
        this.library = library;
        this.showInfo = true;
        this.listaCerts = new ArrayList();
            
        try {    
            cargarConfiguracionInit();
        }
        catch(ProviderException ex){
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
            activo = false;
        }  
        catch(Exception ex){
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
            activo = false;    
        }  
    } 
    
    private void cargarConfiguracionInit() throws  ProviderException {

        System.out.println("Token::cargarConfiguracionInit");
        
        pkcs11config = Utiles.setKeyValue(ParamInfo.getInstance().getParam_name(), module);
        pkcs11config += Utiles.setKeyValue(ParamInfo.getInstance().getParam_lib(), library);
        pkcs11config += Utiles.setKeyValue(ParamInfo.getInstance().getParam_showinfo(), showInfo.toString());
        
        System.out.println("Config pkcs11: " + pkcs11config);            
        ByteArrayInputStream confStream = new ByteArrayInputStream(pkcs11config.getBytes());
        Provider prov = new sun.security.pkcs11.SunPKCS11( confStream );
        activo = true;
    }   
    
    public void reLoadConfig() throws ProviderException{
        cargarConfiguracionInit();
    }
    
    private void instanciarKeyStore() throws KeyStoreException {
        System.out.println("Token::instanciarKeyStore");
        ByteArrayInputStream confStream = new ByteArrayInputStream(pkcs11config.getBytes());
        Provider prov = new sun.security.pkcs11.SunPKCS11( confStream );
        Security.addProvider( prov );
        keystore = KeyStore.getInstance("PKCS11");
    }    
    
    //Operaciones
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public boolean isShowInfo() {
        return showInfo;
    }

    public void setShowInfo(boolean showInfo) {
        this.showInfo = showInfo;
    }

    public Boolean getShowInfo() {
        return showInfo;
    }

    public void setShowInfo(Boolean showInfo) {
        this.showInfo = showInfo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }    

    public KeyStore getKeystore() {
        return keystore;
    }

    public void setKeystore(KeyStore keystore) {
        this.keystore = keystore;
    }    

    public ArrayList<X509Certificate> getListaCerts() {
        return listaCerts;
    }

    public void setListaCerts(ArrayList<X509Certificate> listaCerts) {
        this.listaCerts = listaCerts;
    }
    
    public void cargarCertificados() throws AppletException{
        
        try {
            List<Certificado> listaCertificado = new ArrayList();

            Enumeration aliases = keystore.aliases();

            while (aliases.hasMoreElements()) {
                Object alias = aliases.nextElement();

                X509Certificate cert0 = (X509Certificate) keystore.getCertificate(alias.toString());
                
                Certificate[] chainCert = keystore.getCertificateChain(alias.toString());
                String providerName = keystore.getProvider().getName();
                
                HashMap aliasHash = new HashMap();
                aliasHash.put(aliasHash.size(), alias);
                
                Date fechaActual = new Date();
                if ( fechaActual.after(cert0.getNotBefore()) && fechaActual.before(cert0.getNotAfter()) ) {
                    listaCerts.add( cert0 );
                    
                    Certificado certificado = new Certificado();
                    certificado.setNroSerie(cert0.getSerialNumber().toString(16));
                    certificado.setAlias(alias.toString());
                    certificado.setNombre( Utiles.getCN(cert0.getSubjectDN().getName()) );
                    certificado.setEmisor( Utiles.getCN(cert0.getIssuerDN().getName()) );
                    certificado.setFechaDesde( Utiles.DATE_FORMAT_MIN.format(cert0.getNotBefore()));
                    certificado.setFechaHasta( Utiles.DATE_FORMAT_MIN.format(cert0.getNotAfter()));
                    certificado.setoSubject( Utiles.getO(cert0.getSubjectDN().getName()) );
                    certificado.setoUSubject( Utiles.getOU(cert0.getSubjectDN().getName()) );
                    certificado.setoEmisor( Utiles.getO(cert0.getIssuerDN().getName()) );
                    certificado.setoUEmisor( Utiles.getOU(cert0.getIssuerDN().getName()) );
                    certificado.setChainCert(chainCert);
                    certificado.setProviderName(providerName);
                    
                    listaCertificado.add( certificado );
                }
            }
            HandlerCert.getInstance().addKeystore(keystore);
            HandlerCert.getInstance().getCertificados().clear();
            HandlerCert.getInstance().getCertificados().addAll(listaCertificado);
        } 
        catch (KeyStoreException ex) {
               String msj = "Error accediendo al almacén de certificados.";
               throw new AppletException(msj, null, ex.getCause());
        }
    }
    
    public boolean isLogued(){
        return logued;
    }
    
    public void login( String password ) throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, LoginException{
        if (isLogued()){
            logout();
        }
        instanciarKeyStore();
        this.password = password;
        KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection( password.toCharArray() );
        keystore.load(null , pp.getPassword() );
        logued = true;
        System.out.println("Token::login ok");
    }
    
    public void logout() throws LoginException{
        ((SunPKCS11) keystore.getProvider() ).logout();    
        password = null;        
        logued = false; 
        System.out.println("Token::logout ok");
    }
}
