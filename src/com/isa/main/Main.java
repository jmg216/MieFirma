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
import com.isa.exception.AppletException;
import com.isa.firma.FirmaPDFController;
import com.isa.firma.PDFFirma;
import com.isa.plataform.KeyStoreValidator;
import com.isa.security.ISCertSecurityManager;
import com.isa.token.HandlerToken;
import com.isa.utiles.StreamDataSource;
import com.isa.utiles.Utiles;
import com.isa.utiles.UtilesMsg;
import com.isa.utiles.UtilesResources;
import com.isa.utiles.UtilesWS;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import netscape.javascript.JSObject;
import py.com.miefirma.docman.schemas.DocumentType;

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
        try{
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
            setFrontPanelSize();        
            UtilesResources.setRutaProperties(getParameter("ruta"));
            KeyStoreValidator.setInitStoreValidator();
            sincronizarKeystores();
        }
        catch( AppletException ex ){
            ex.printStackTrace();
        }        
    }
    
    public void sincronizarKeystores(){
        try{
            if (KeyStoreValidator.isKeystoreWindows()){
                sincronizarWindows();
            }
            if (KeyStoreValidator.isKeystoreToken()){
                sincronizarTokens();
            }
        }
        catch (AppletException e){
            
        }
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


                cer.setAlias(issuerDn);
                cer.setEmisor( Utiles.getCN(issuerDn) );
                cer.setNombre( subjectDn );
                cer.setFechaValidez(Utiles.DATE_FORMAT_MIN.format(x509cert.getNotAfter()));                
                
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
                if (valido){
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
    
    public void sincronizarTokens() throws AppletException {
        try{
            System.out.println("Main::sincronizarTokens");
            HandlerToken handler = new HandlerToken();
            System.out.println("Main::sincronizarTokens FIN");
        }
        catch( AppletException e ){
            //ingreso token inactivo           
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
                            field.set(entry, alias.concat(" - ").concat(hashCode));
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

    
    public String obtenerCertificados(){
        ArrayList<Certificado> certs = HandlerCert.getInstance().getCertificados();
        Gson  gsonHelper = GsonHelper.getInstance().getGson();
        return gsonHelper.toJson(certs);
    }
    
    public void firmar(String documento, String aliascert, String successCallback, String errorCallback){
        ActualCertInfo.getInstance().inicializar();
        ActualCertInfo.getInstance().setDocumento(documento);
        ActualCertInfo.getInstance().setAliasCert(aliascert);
        
        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    String documento = ActualCertInfo.getInstance().getDocumento();
                    DocumentType docToSign = UtilesWS.getDocumento( documento );
                    
                    KeyStore keystore = HandlerCert.getInstance().getKeystoreCert();
                    String alias = ActualCertInfo.getInstance().getAliasCert();
                    
                    FirmaPDFController firmapdfcontroller = FirmaPDFController.getInstance();
                    PDFFirma infoFirma = firmapdfcontroller.generarFirmaApariencia( keystore, alias );
                    
                    InputStream is = new ByteArrayInputStream(docToSign.getDocument());
                    ByteArrayOutputStream pdfOS = firmapdfcontroller.firmar(infoFirma, is);                 
                    
                    //Documento firmado;
                    docToSign.setDocument(pdfOS.toByteArray());
                    String id = UtilesWS.subirDocumento(docToSign); 
                    firmaExitosa( successCallback, UtilesMsg.DOC_FIRMADO_OK );
                }
                catch (AppletException ex) {
                    Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
                    firmaError( successCallback, ex.getMsj() );
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
        JSObject win = (JSObject) JSObject.getWindow(this);
        win.call(errorCallback, new String[]{  msg });        
    }
    
    public void firmaExitosa(String successCallback,  String msj ){  
        JSObject win = (JSObject) JSObject.getWindow(this);
        win.call(successCallback, new String[]{  msj } );  
    }  
    
    
    
}
