/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.main;

import com.google.gson.Gson;
import com.isa.common.ActualCertInfo;
import com.isa.common.FrontCommon;
import com.isa.common.GsonHelper;
import com.isa.common.HandlerCert;
import com.isa.common.ICommon;
import com.isa.entities.Certificado;
import com.isa.entities.Codigo;
import com.isa.entities.ParamInfo;
import com.isa.entities.SignerInfo;
import com.isa.entities.WrapperCert;
import com.isa.exception.AppletException;
import com.isa.firma.FirmaPDFController;
import com.isa.firma.PDFFirma;
import com.isa.plataform.KeyStoreValidator;
import com.isa.security.ISCertSecurityManager;
import com.isa.token.HandlerToken;
import com.isa.token.Token;
import com.isa.utiles.Utiles;
import com.isa.utiles.UtilesMsg;
import com.isa.utiles.UtilesResources;
import com.isa.utiles.UtilesWS;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import netscape.javascript.JSObject;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import uy.isa.docman.schemas.GetDocumentResponse;
import uy.isa.docman.schemas.UploadDocumentResponse;

/**
 *
 * @author JMiraballes
 */
public class Main extends javax.swing.JApplet implements ICommon {

    /**
     * Initializes the applet Main
     */
    @Override
    public void init() {
       
            SecurityManager sm = new ISCertSecurityManager();
            System.setSecurityManager( sm ); 
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
            initComponents();
            cargarParametros();
            UtilesResources.setRutaProperties(ParamInfo.getInstance().getParam_ruta());
            KeyStoreValidator.setInitStoreValidator( ParamInfo.getInstance().getParam_keystores() );
            ActualCertInfo.getInstance().inicializar();
            setFrontPanelSize();
            sincronizarKeystores();
    }
    
    private void cargarParametros(){
        ParamInfo pinfo = ParamInfo.getInstance();
        
        pinfo.setParam_keystores(getParameter("KEYSTORES"));
        pinfo.setParam_name(getParameter("PKCS11_PARAM_NAME"));
        pinfo.setParam_lib(getParameter("PKCS11_PARAM_LIBRARY"));
        pinfo.setParam_showinfo(getParameter("PKCS11_PARAM_SHOWINFO"));
        pinfo.setParam_modulos(getParameter("PKCS11_MODULOS"));
        pinfo.setParam_lib_win(getParameter("PKCS11_LIB_WIN"));
        pinfo.setParam_lib_unx(getParameter("PKCS11_LIB_UNX"));
        pinfo.setParam_ruta(getParameter("RUTA_PROPERTIES"));
    } 
    
    /**
     * Precondición de que esté configurado la firma con token.
     */
    private void sincronizarTokens(){
        
        System.out.println("Main::setSmartCardListener");
            
            Thread thcheckconnected = new Thread(){
                @Override
                public void run(){  
                    boolean tokenConnected = false;
                    
                    while ( !tokenConnected ){
                        tokenConnected = checkTokenConectado(  );
                    }
                }
            };
            thcheckconnected.start();
    }
    
    public boolean checkTokenConectado( ){
        
            HandlerToken handler = HandlerToken.getInstance();
            handler.cargarTokens();
            boolean tokenPlugged = handler.isTokenActivo();
            if (!tokenPlugged){
                try {
                    System.out.println("Buscando token.");
                    Thread.sleep(2000);
                } 
                catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //llamar error a error que no se pudo verificar token conectado
                }
            }
            else{
                tokenConectado();
            }
            return tokenPlugged;
    }
    
    
    private void sincronizarKeystores(){
        try{
            if (KeyStoreValidator.isKeystoreWindows()){
                sincronizarWindows();
            }
            if (KeyStoreValidator.isKeystoreToken()){
                sincronizarTokens();
            }
        }
        catch (AppletException e){
            e.printStackTrace();
            errorApplet(e.getMsj());            
        }
    }
    
    public String obtenerCertificadoPorAlias( String alias ){
        System.out.println("Main::obtenerCertificadoPorAlias: " + alias);
        Gson gsonHelper = GsonHelper.getInstance().getGson();
        
        ArrayList<Certificado> certs = HandlerCert.getInstance().getCertificados();
        if ( !certs.isEmpty() ){
            for (Certificado c : certs){
                if ( c.getAlias().equals(alias ) ){
                    return gsonHelper.toJson(c);
                }
            }
        }
        return gsonHelper.toJson("");
    }
    

    public String obtenerCertificados(){
        Gson  gsonHelper = null;
        ArrayList<Certificado> certs = new ArrayList();
        try{
            if (HandlerToken.getInstance().isTokenActivo()){

                HandlerToken handlerToken = HandlerToken.getInstance();         
                Token token = handlerToken.getTokenActivo();
                token.cargarCertificados();
            }
            certs = HandlerCert.getInstance().getCertificados();
            
            gsonHelper = GsonHelper.getInstance().getGson();
            return gsonHelper.toJson(certs);
        }
        catch( AppletException e){
            e.printStackTrace();
            errorApplet(e.getMsj());
            gsonHelper = GsonHelper.getInstance().getGson();
            return gsonHelper.toJson(certs);
        }
    }    
    
    
    public String isTokenActivo() { 
        return Boolean.toString( HandlerToken.getInstance().isTokenActivo() );
    }
    
    public String authSCard( String password ){
        System.out.println("Main::authSCard");
 
        WrapperCert wcert = new WrapperCert();
        
        if (Utiles.isNullOrEmpty(password)){
            wcert.setCodigo(Codigo.ERROR);
            wcert.setMsj("El pin es obligatorio.");
            return GsonHelper.getInstance().getGson().toJson(wcert, WrapperCert.class);
        }
        
        try {
            HandlerToken handlerToken = HandlerToken.getInstance();         
            Token token = handlerToken.getTokenActivo();
            
            token.login( password );
            wcert.setCodigo(Codigo.OK);
            wcert.setMsj("Se ha autenticado correctamente.");            
            
            return GsonHelper.getInstance().getGson().toJson(wcert, WrapperCert.class);
            
        } 
        catch (IOException ex) {
            
            if (ex.getCause() instanceof LoginException){
                LoginException log = (LoginException) ex.getCause();
                PKCS11Exception pkcs = (PKCS11Exception) log.getCause();
                if ( Utiles.PKCS11_EXCEPTION_CKR_PIN_INCORRECT.equals(pkcs.getMessage())){
                    wcert.setCodigo(Codigo.ERROR);
                    wcert.setMsj(UtilesMsg.ERROR_PIN_INCORRECTO);
                }
                else if (Utiles.PKCS11_EXCEPTION_CKR_PIN_LEN_RANGE.equals(pkcs.getMessage())){
                    wcert.setCodigo(Codigo.ERROR);
                    wcert.setMsj(UtilesMsg.ERROR_PIN_INCORRECTO);
                }
                else if (Utiles.PKCS11_EXCEPTION_CKR_PIN_LOCKED.equals(pkcs.getMessage())){
                    wcert.setCodigo(Codigo.ERROR);
                    wcert.setMsj(UtilesMsg.ERROR_TOKEN_BLOQUEADO);
                }
                else if (Utiles.PKCS11_EXCEPTION_CKR_TOKEN_NOT_RECOGNIZED.equals(pkcs.getMessage())){
                    wcert.setCodigo(Codigo.ERROR);
                    wcert.setMsj(UtilesMsg.ERROR_TOKEN_SIN_IDENTIFICAR);                    
                }
                else{
                    wcert.setCodigo(Codigo.ERROR);
                    wcert.setMsj(UtilesMsg.ERROR_AUTH);                   
                }
            }
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            wcert.setCodigo(Codigo.ERROR);
            wcert.setMsj(UtilesMsg.ERROR_AUTH);             
        } 
        catch (CertificateException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            wcert.setCodigo(Codigo.ERROR);
            wcert.setMsj(UtilesMsg.ERROR_AUTH);  
        } 
        catch (KeyStoreException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            wcert.setCodigo(Codigo.ERROR);
            wcert.setMsj("No se pudo acceder al keystore.");              
        } 
        catch (LoginException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error autenticando token: LoginException");
            ex.printStackTrace();
        }
         return GsonHelper.getInstance().getGson().toJson(wcert, WrapperCert.class);
    }    
    
    
    public void sincronizarWindows() throws AppletException{
        
        try {
            Certificate c;
            //Obtengo el almacén de windows
            KeyStore keystore = KeyStore.getInstance("Windows-MY");
            keystore.load(null, null);
            //El almacen de windows posee un BUG con respecto a los alias de los
            //certificados ya que dichos alias pueden no ser únicos por lo que los modifico
            //para obtener unicidad en los alias.
            _fixAliases(keystore);
            Enumeration enumer = keystore.aliases();
            ArrayList<Certificado> elementos = new ArrayList();

            //Recorro todos los certificados del almace´n de windows para obtener los
            //certificados del usuario logueado.
            for (; enumer.hasMoreElements(); ) {
                
                Certificado cer = new Certificado();
                
                String alias = (String) enumer.nextElement();
                c = (Certificate) keystore.getCertificate(alias);
                X509Certificate x509cert = (X509Certificate) c;
                
                Principal nombre = x509cert.getSubjectDN();
                Principal emisor = x509cert.getIssuerDN();
                String issuerDn = emisor.getName();
                String subjectDn = nombre.getName();
                BigInteger nroserie = x509cert.getSerialNumber();

                cer.setAlias(alias);
                //emitido para
                cer.setNroSerie( nroserie.toString(16) );
                cer.setNombre( Utiles.getCN( subjectDn ) );
                cer.setoSubject( Utiles.getO( subjectDn ) );
                cer.setoUSubject( Utiles.getOU( subjectDn ) );
                
                        
                //emitidoPor
                cer.setEmisor( Utiles.getCN( issuerDn ) );
                cer.setoEmisor( Utiles.getO( issuerDn ) );
                cer.setoUEmisor( Utiles.getOU( issuerDn) );      
                
                //periodo validez
                cer.setFechaDesde( Utiles.DATE_FORMAT_MIN.format(x509cert.getNotBefore()) );
                cer.setFechaHasta(Utiles.DATE_FORMAT_MIN.format(x509cert.getNotAfter()) );
                //certificado
                cer.setChainCert( keystore.getCertificateChain(alias) );
                cer.setCertX509( x509cert );
                cer.setProviderName(keystore.getProvider().getName());
                        
                boolean valido = true;
                try{
                    x509cert.checkValidity();
                } 
                catch (CertificateExpiredException exe) {
                    valido = false;
                }
                catch ( CertificateNotYetValidException exe){
                    valido = false;
                }
                
                Date fechaActual = new Date();               
                if (valido && fechaActual.after(x509cert.getNotBefore()) && fechaActual.before(x509cert.getNotAfter())){
                    System.out.println("Certificado agregado: " + x509cert.getIssuerDN().getName());
                    elementos.add(cer);
                }
            }
            HandlerCert.getInstance().setCertificados(elementos);
            HandlerCert.getInstance().addKeystore(keystore);
        } 
        catch (KeyStoreException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_CARGANDO_CERTIFICADOS , null, ex.getCause());
        } 
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_CARGANDO_CERTIFICADOS , null, ex.getCause());
            
        } 
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_CARGANDO_CERTIFICADOS , null, ex.getCause());
            
        } 
        catch (CertificateException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_CARGANDO_CERTIFICADOS , null, ex.getCause());
        }
    }
       
    
    //Función para devolver la unicidad de alias de los certificados del almacén de windows
    private static void _fixAliases(KeyStore keyStore) {
        Field field;
        KeyStoreSpi keyStoreVeritable;
        try {
            field = keyStore.getClass().getDeclaredField("keyStoreSpi");
            field.setAccessible(true);
            keyStoreVeritable = (KeyStoreSpi)field.get(keyStore);

            if("sun.security.mscapi.KeyStore$MY".equals(keyStoreVeritable.getClass().getName())) {
                Collection entries;
                String alias, hashCode;
                X509Certificate[] certificates;

                field = keyStoreVeritable.getClass().getEnclosingClass().getDeclaredField("entries");
                field.setAccessible(true);
                entries = (Collection)field.get(keyStoreVeritable);

                for(Object entry : entries) {
                    field = entry.getClass().getDeclaredField("certChain");
                    field.setAccessible(true);
                    certificates = (X509Certificate[])field.get(entry);
                    hashCode = Integer.toString(certificates[0].hashCode());

                    field = entry.getClass().getDeclaredField("alias");
                    field.setAccessible(true);
                    alias = (String)field.get(entry);

                    if(!alias.equals(hashCode)) {
                            field.set(entry, alias.concat("_").concat(hashCode));
                    } // if
                } // for
            } // if
        }
        catch(Exception e) {
            
        } // catch
    } // _fixAliases    

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    
    public void firmar(String jsonData, String successCallback, String errorCallback){
        System.out.println("Main::firmar");
        
        Gson gson = GsonHelper.getInstance().getGson();
        SignerInfo signerInfo = gson.fromJson(jsonData, SignerInfo.class);
        ActualCertInfo.getInstance().inicializar();
        ActualCertInfo.getInstance().setSingerInfo(signerInfo);
        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    String documento = ActualCertInfo.getInstance().getSingerInfo().getDoc();
                    System.out.println("Previo a conectar con el servcio de documento");
                    GetDocumentResponse docToSign = UtilesWS.getInstanceWS().getDocumento( documento );
                    System.out.println("Documento obtenido: " + docToSign.getDocument() != null ? docToSign.getDocument().getName() :  "NULL");
                    
                    InputStream is = new ByteArrayInputStream( docToSign.getDocument().getDocument() );
                    FirmaPDFController firmapdfcontroller = FirmaPDFController.getInstance();
                    ByteArrayOutputStream pdfOS = firmapdfcontroller.firmar( is );
                    if (KeyStoreValidator.isKeystoreToken()){
                        HandlerToken.getInstance().getTokenActivo().logout();
                    }
                    //Documento firmado;
                    docToSign.getDocument().setDocument( pdfOS.toByteArray() );
                    UploadDocumentResponse uploadeddocument = UtilesWS.getInstanceWS().uploadDocumento( docToSign.getDocument() ); 
                    
                    System.out.println("Documento guardado: " + uploadeddocument.getId());
                    firmaExitosa( successCallback, uploadeddocument.getId(), UtilesMsg.DOC_FIRMADO_OK);
                }
                catch (AppletException ex) {
                    Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
                    firmaError( errorCallback, ex.getMsj() );
                } catch (LoginException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }
    
    @Override
    public void setFrontPanelSize() {
        setSize(FrontCommon.SIZE_WIDTH, FrontCommon.SIZE_HEIGHT);
    }  
    
    public void firmaError(String errorCallback, String msg){
        System.out.println("Main::init::firmaError: " + errorCallback);
        JSObject win = (JSObject) JSObject.getWindow(this);
        win.call(errorCallback, new String[]{  msg });        
    }
    
    public void firmaExitosa(String successCallback, String id,  String msj ){  
        System.out.println("Main::init::firmaExitosa: " + successCallback);
        JSObject win = (JSObject) JSObject.getWindow(this);
        win.call(successCallback, new String[]{  id, msj } );  
    }  
    
    public void tokenConectado(){
        System.out.println("Main::init::tokenConectado");
        JSObject win = (JSObject) JSObject.getWindow(this);
        win.call("callbackTokenConectado", new String[]{  "El token o tarjeta se encuentra conectado." } ); 
    }
    
    public void errorApplet( String msj ){
        System.out.println("Main::init::errorApplet");
        JSObject win = (JSObject) JSObject.getWindow(this);
        win.call("errorApplet", new String[]{  msj } ); 
    }    
    
    
    
    
}
