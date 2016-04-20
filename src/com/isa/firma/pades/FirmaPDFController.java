/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.firma.pades;

import com.isa.common.ActualCertInfo;
import com.isa.common.HandlerCert;
import com.isa.entities.Certificado;
import com.isa.entities.SignerInfo;
import com.isa.exception.AppletException;
import com.isa.plataform.KeyStoreValidator;
import com.isa.token.HandlerToken;
import com.isa.token.Token;
import com.isa.utiles.IdGenerator;
import com.isa.utiles.Utiles;
import com.isa.utiles.UtilesMsg;
import com.itextpdf.text.DocumentException;
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
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
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
    
    
    public ByteArrayOutputStream firmar(InputStream pdfbase64 ) throws AppletException{
        
        try {
            System.out.println("Firma Controller::firmar");
            
            SignerInfo signerInfo = ActualCertInfo.getInstance().getSingerInfo();
            HandlerCert handlerCert = HandlerCert.getInstance();
            Certificado certificado = handlerCert.getCertificadoPorAlias(signerInfo.getAlias());
            PrivateKey privateKey = handlerCert.getPrivateKeyPorAlias( signerInfo.getAlias() );
            
            PdfReader reader = new PdfReader( pdfbase64 );
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null, true);
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            ExternalSignature es = new PrivateKeySignature( privateKey, "SHA-256", certificado.getProviderName());
            ExternalDigest digest = new BouncyCastleDigest();
            
            if ( signerInfo.isApariencia() ){
                System.out.println("Insertando apriencia en documento...");

                appearance.setRenderingMode( PdfSignatureAppearance.RenderingMode.GRAPHIC );
                appearance.setSignatureGraphic( Image.getInstance( Utiles.convertirBase64StringToArrayBytes( signerInfo.getImagen() ) ) );
                int numeroPagFirma = signerInfo.getHoja() == -1 ? reader.getNumberOfPages() : signerInfo.getHoja();
                Rectangle pageSize = reader.getPageSize(numeroPagFirma);

                System.out.println("Hoja: " + signerInfo.getHoja());
                System.out.println("Ancho documento: " + pageSize.getWidth() );
                System.out.println("Alto documento: " + pageSize.getHeight() );

                float llx = Utiles.convertirPorcentajeUnits( signerInfo.getLlx(), pageSize.getWidth() );
                float lly = Utiles.convertirPorcentajeUnits( signerInfo.getLly(), pageSize.getHeight() );
                float urx = Utiles.convertirPorcentajeUnits( signerInfo.getUrx(), pageSize.getWidth() );
                float ury = Utiles.convertirPorcentajeUnits( signerInfo.getUry(), pageSize.getHeight() );

                System.out.println("Bottom documento: " + llx );
                System.out.println("Top documento: " + lly );
                System.out.println("Left documento: " + urx);
                System.out.println("Right documento: " + ury);                

                //float llx, float lly, float urx, float ury
                Rectangle rectangle = new Rectangle(llx, lly, urx, ury);
                appearance.setVisibleSignature(rectangle, numeroPagFirma, "Id: " + IdGenerator.generate());
            }
            appearance.setReason( signerInfo.getMotivo() );
            appearance.setLocation( signerInfo.getLocalidad() ); 
            MakeSignature.signDetached(appearance, digest, es, certificado.getChainCert(), null, null, null, 0, CryptoStandard.CMS);
            System.out.println("PDF Firmado correctamente.");
            
            return os;
            
        } 
        catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        } 
        catch (DocumentException ex) {
            ex.printStackTrace();
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        } 
        catch (KeyStoreException ex) {
            ex.printStackTrace();
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        }
        catch (UnrecoverableKeyException ex) {
            ex.printStackTrace();
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        }
        catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        }
        catch (Exception ex ){
            ex.printStackTrace();
            Logger.getLogger(FirmaPDFController.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_FIRMANDO_DOCUMENTO, null, ex.getCause());
        }
    }  
    
    public void validarFirmaOK( boolean validar, byte[] pdffirmado ){
       
            if (validar){
                PDFFirmaValidar validador = PDFFirmaValidar.getInstance();
                validador.validarFirma( pdffirmado );
            }
            
    }
    
}
