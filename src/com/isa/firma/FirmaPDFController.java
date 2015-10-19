/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.firma;

import com.isa.common.ActualCertInfo;
import com.isa.exception.AppletException;
import com.isa.utiles.IdGenerator;
import com.isa.utiles.Utiles;
import com.isa.utiles.UtilesMsg;
import com.isa.utiles.UtilesResources;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JMiraballes
 */
public class FirmaPDFController {
    
    private static FirmaPDFController firmaPDFController;
    
    public FirmaPDFController(){}
    
    public static FirmaPDFController getInstance(){
        if (firmaPDFController == null){
            firmaPDFController = new FirmaPDFController();
        }
        return firmaPDFController;
    } 
    
    
    public PDFFirma generarFirmaApariencia( KeyStore keystore, String alias ) throws AppletException{
        
        try {
            System.out.println("Firma Controller::generarApariencia");
            
            
            HashMap aliasHash = ActualCertInfo.getInstance().getAliasHash();
            int certIndex = ActualCertInfo.getInstance().getCertIndex();
            
            PDFFirma infofirma = new PDFFirma();
            infofirma.setPk( (PrivateKey) keystore.getKey(alias, null) );
            infofirma.setChainCert( keystore.getCertificateChain(alias) );
            infofirma.setProvidername( keystore.getProvider().getName() );
            //Definiendo apariencia de firma.
            infofirma.setFirmante(ActualCertInfo.getInstance().getFirmante());
            infofirma.setTextoFirma(alias);
            infofirma.setApariencia(UtilesResources.getProperty(UtilesResources.PROP_APARIENCIA).equals(UtilesResources.TRUE_VALUE));
            
            System.out.println("Generando apariencia");
            System.out.println("AliasHash: " + aliasHash );
            System.out.println("CertIndex: " + certIndex );
            System.out.println("Alias: " + alias);
            System.out.println("Apariencia Properties: " + UtilesResources.getProperty(UtilesResources.PROP_APARIENCIA));
            System.out.println("Apariencia: " + infofirma.isApariencia());
            System.out.println("Hoja: "  + UtilesResources.getProperty(UtilesResources.PROP_PAG_FIRMA));
            System.out.println("Ruta Imagen: " + UtilesResources.getProperty(UtilesResources.PROP_URL_IMAGEN) );
            System.out.println("Largo: " + UtilesResources.getProperty(UtilesResources.PROP_LARGO_FIRMA));
            System.out.println("Ancho: " + UtilesResources.getProperty(UtilesResources.PROP_ANCHO_FIRMA));            
            
            if (infofirma.isApariencia()){
                System.out.println("Definiendo propiedades de apariencia");
                
                infofirma.setHoja( Integer.valueOf( UtilesResources.getProperty(UtilesResources.PROP_PAG_FIRMA)) );
                infofirma.setAncho( Integer.valueOf(UtilesResources.getProperty(UtilesResources.PROP_ANCHO_FIRMA)) );
                infofirma.setLargo( Integer.valueOf(UtilesResources.getProperty(UtilesResources.PROP_LARGO_FIRMA)) );
                infofirma.setRutaImagen( UtilesResources.getProperty(UtilesResources.PROP_URL_IMAGEN) );
            }
            return infofirma;
        } 
        catch (KeyStoreException ex) {
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(null, null, ex.getCause());
        } 
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(null, null, ex.getCause());
        } 
        catch (UnrecoverableKeyException ex) {
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(null, null, ex.getCause());
        }
    } 
    
    
    
    public ByteArrayOutputStream firmar(PDFFirma infoFirma, InputStream pdfbase64 ) throws AppletException{
        
        try {
            System.out.println("Firma Controller::firmar");
            
            PdfReader reader = new PdfReader( pdfbase64 );
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null, true);
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            System.out.println("Pre definir apariencia...");
            if (infoFirma.isApariencia()){
                System.out.println("Insertando apriencia en documento...");
                appearance.setSignatureGraphic( Image.getInstance(new URL( infoFirma.getRutaImagen() )) );
                appearance.setRenderingMode(Utiles.getModoApariencia());           
                
                int numeroPagFirma = infoFirma.getHoja() == -1 ? reader.getNumberOfPages() : infoFirma.getHoja();
                int cantidadFirmaActuales = reader.getAcroFields().getSignatureNames().size();
                int[] coords = infoFirma.calcularCorrdenadasFirma( cantidadFirmaActuales,  infoFirma.getAncho(), infoFirma.getLargo() );
                
                //llx, lly, urx, ury
                String textofirma = "Firmado digitalmente por: " + infoFirma.getFirmante() + 
                                    "\nFecha: " + Utiles.DATE_FORMAT_MIN.format(new Date());
                
                appearance.setLayer2Text( textofirma );
                appearance.setReason("Prueba de Recepción");
                appearance.setLocation("Montevideo - Uruguay");
                
                int tamanioLetra = Integer.valueOf( UtilesResources.getProperty("appletConfig.TamanioLetra") );
                appearance.setLayer2Font(new Font(FontFamily.HELVETICA, tamanioLetra, Font.NORMAL, new BaseColor(0, 0, 0)));
                appearance.setVisibleSignature(new Rectangle(coords[0], coords[1], coords[2], coords[3]), numeroPagFirma, "Id: " + IdGenerator.generate());
            }
            
            ExternalSignature es = new PrivateKeySignature(infoFirma.getPk(), "SHA-256", infoFirma.getProvidername());
            ExternalDigest digest = new BouncyCastleDigest();
            MakeSignature.signDetached(appearance, digest, es, infoFirma.getChainCert(), null, null, null, 0, CryptoStandard.CMS);
           
            System.out.println("PDF Firmado correctamente.");
            
            return os;
            
        } 
        catch (IOException ex) {
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        } 
        catch (DocumentException ex) {
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        } 
        catch (KeyStoreException ex) {
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        }
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        }
        catch (UnrecoverableKeyException ex) {
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        }
        catch (GeneralSecurityException ex) {
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        }
    }    
    
    public void validarFirmaOK( boolean validar, byte[] pdffirmado ){
        
//        try{
            if (validar){
                PDFFirmaValidar validador = PDFFirmaValidar.getInstance();
                validador.validarFirma( pdffirmado );
            }
            
//        }
//        catch ( AppletException e ){
//            throw new AppletException(null, null, e);    
//        }
    }
    
}
