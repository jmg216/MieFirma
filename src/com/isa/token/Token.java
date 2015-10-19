package com.isa.token;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.isa.common.ActualCertInfo;
import com.isa.entities.Certificado;
import com.isa.entities.Codigo;
import com.isa.entities.WrapperCert;
import com.isa.exception.AppletException;
import com.isa.utiles.Utiles;
import com.isa.utiles.UtilesMsg;
import com.isa.utiles.UtilesResources;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
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
    
    
    public Token(String module, String library) throws AppletException  {
        
        this.module = module;
        this.library = library;
        this.showInfo = true;
        this.listaCerts = new ArrayList();
            
        try {    
            String pkcs11config = Utiles.setKeyValue(UtilesResources.getProperty("appletConfig.paramName"), module);
            pkcs11config += Utiles.setKeyValue(UtilesResources.getProperty("appletConfig.paramLibrary"), library);
            pkcs11config += Utiles.setKeyValue(UtilesResources.getProperty("appletConfig.paramShowInfo"), showInfo.toString());
            
            System.out.println("Config pkcs11: " + pkcs11config);
            
            ByteArrayInputStream confStream = new ByteArrayInputStream(pkcs11config.getBytes());
            Provider prov = new sun.security.pkcs11.SunPKCS11( confStream );
            Security.addProvider( prov );
            keystore = KeyStore.getInstance("PKCS11");
            activo = true;
            
        }
        catch(ProviderException ex){
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
            activo = false;            
            throw new AppletException(UtilesMsg.ERROR_ACCEDIENDO_PROVEEDOR, null, ex.getCause());
        }
        catch(KeyStoreException ex){
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
            activo = false;    
            throw new AppletException(UtilesMsg.ERROR_ACCEDIENDO_TOKEN, null, ex.getCause());
        }   
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
    
    public void obtenerCertificados() throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
        
        if (isLogued()){
            Enumeration aliases = keystore.aliases();
            while (aliases.hasMoreElements()) {
                Object alias = aliases.nextElement();
                X509Certificate cert0 = (X509Certificate) keystore.getCertificate(alias.toString());
                System.out.println("Certificado: " + Utiles.getCN(cert0.getSubjectDN().getName()));
                System.out.println("Emisor: " + Utiles.getCN(cert0.getIssuerDN().getName()) );
                System.out.println("Fecha Validez : " + Utiles.DATE_FORMAT_MIN.format(cert0.getNotAfter()));
                listaCerts.add( cert0 );
            }
        }
    }
    
    public WrapperCert obtenerCertCI(){
        WrapperCert wcert = new WrapperCert();
        if (isLogued()){
            try {
                String nombre = "";
                String emisor = "";
                String fechaValidez = "";
                String cedula = "";
                
                Enumeration aliases = keystore.aliases();

                while (aliases.hasMoreElements()) {
                    Object alias = aliases.nextElement();
                    
                    X509Certificate cert0 = (X509Certificate) keystore.getCertificate(alias.toString());
                    
                    //cedula = Utiles.getDNI(cert0.getSubjectDN().getName());
                    nombre = Utiles.getCN(cert0.getSubjectDN().getName());
                    emisor = Utiles.getCN(cert0.getIssuerDN().getName());
                    fechaValidez = Utiles.DATE_FORMAT_MIN.format(cert0.getNotAfter());
                    
                    System.out.println("Cédula: " + cedula);
                    System.out.println("Nombre: " + nombre);
                    System.out.println("Emisor: " + emisor );
                    System.out.println("Fecha Validez : " + fechaValidez );
                    
                    HashMap aliasHash = new HashMap();
                    aliasHash.put(aliasHash.size(), alias);
                    
                    ActualCertInfo.getInstance().setAliasHash(aliasHash);
                    ActualCertInfo.getInstance().setCertIndex(0);
                    
                    listaCerts.add( cert0 );

                    Certificado certificado = new Certificado(cedula, nombre, emisor, fechaValidez);
                    
                    wcert.setCertificado( certificado );
                    wcert.setMsj( UtilesMsg.CERTIFICADO_OBTENIDO_OK );
                    wcert.setCodigo( Codigo.OK );
                    return wcert;
                }
                
                wcert.setMsj(UtilesMsg.ERROR_CERT_VACIO);
                wcert.setCodigo(Codigo.ERROR);
                return wcert;
            } 
            catch (KeyStoreException ex) {

                wcert.setMsj( UtilesMsg.ERROR_AUTH );
                wcert.setCodigo( Codigo.ERROR );
                Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
                return wcert;
            }
        }
        else{
            wcert.setMsj( UtilesMsg.ERROR_AUTH );
            wcert.setCodigo( Codigo.ERROR );
            return wcert;
        }
    }
    
    public boolean isLogued(){
        return logued;
    }
    
    public void login( String password ) throws IOException, NoSuchAlgorithmException, CertificateException{
        this.password = password;
        KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection( password.toCharArray() );
        keystore.load(null , pp.getPassword() );
        logued = true;
    }
    
    public void logout() throws LoginException{
        ((SunPKCS11) keystore.getProvider() ).logout();
        keystore.getProvider().clear();     
        password = null;        
        logued = false; 
        System.out.println("Token::logout");
    }
}
