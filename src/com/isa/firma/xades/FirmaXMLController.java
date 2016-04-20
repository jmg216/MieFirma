/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.firma.xades;

import com.isa.common.ActualCertInfo;
import com.isa.common.HandlerCert;
import com.isa.entities.Certificado;
import com.isa.entities.ParamInfo;
import com.isa.entities.SignerInfo;
import com.isa.exception.AppletException;
import com.isa.plataform.KeyStoreValidator;
import com.isa.plataform.OSValidator;
import com.isa.utiles.Utiles;
import java.security.AccessControlException;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import xades4j.XAdES4jException;
import xades4j.algorithms.EnvelopedSignatureTransform;
import xades4j.production.DataObjectReference;
import xades4j.production.EnvelopedXmlObject;
import xades4j.production.SignedDataObjects;
import xades4j.production.XadesBesSigningProfile;
import xades4j.production.XadesSignatureResult;
import xades4j.production.XadesSigner;
import xades4j.production.XadesSigningProfile;
import xades4j.properties.DataObjectDesc;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.impl.DirectKeyingDataProvider;
import xades4j.providers.impl.PKCS11KeyStoreKeyingDataProvider;

/**
 *
 * @author JMiraballes
 */
public class FirmaXMLController {    
    
    private static FirmaXMLController firmaXMLController;
    
    public FirmaXMLController(){}
    
    public static FirmaXMLController getInstance(){
        if (firmaXMLController == null){
            firmaXMLController = new FirmaXMLController();
        }
        return firmaXMLController;
    }
    
    public String firmarXades( String dataToSign ) throws AppletException {
        System.out.println("Firma XML: " + dataToSign);
        try{
            SignerInfo signerInfo = ActualCertInfo.getInstance().getSingerInfo();
            String alias = signerInfo.getAlias();
            ArrayList<Certificado> listaCertificado = Utiles.obtenerCertificados();
            KeyingDataProvider kp = null;
            
            if (KeyStoreValidator.isKeystoreWindows()){
                PrivateKey pk = HandlerCert.getInstance().getPrivateKeyPorAlias( alias );
                Certificado c = HandlerCert.getInstance().getCertificadoPorAlias(alias);
                kp = new DirectKeyingDataProvider(c.getCertX509(), pk);
            }
            else{
                int certindex = -1;
                String passwd = "";
                for (Certificado cert : listaCertificado ){
                    certindex++;
                    if (alias.equals(cert.getAlias())){
                        break;
                    }
                }                
                String modulo = "";
                String lib = "";
                if (OSValidator.isWindows()) {
                    lib = ParamInfo.getInstance().getParam_lib_win();
                }        
                if (OSValidator.isUnix()){
                    lib = ParamInfo.getInstance().getParam_lib_unx();
                }
                modulo = ParamInfo.getInstance().getParam_modulos();                
                kp = new PKCS11KeyStoreKeyingDataProvider(
                                            lib + 
                                            "\nshowInfo=true", 
                                            modulo,
                                            new CertificateSelector( certindex ),
                                            new DirectPasswordProvider( passwd ),
                                            null,
                                            true );
                System.out.println("Se instancia PKCS11.");               
            }
            
            XadesSigningProfile p = new XadesBesSigningProfile( kp );
            XadesSigner signer = p.newSigner();
            AlgorithmsProvider algorithmProvider = new AlgorithmsProvider();
            p.withAlgorithmsProviderEx( algorithmProvider );
       
            System.out.println("Se crea nodo a firmar.");
            NodoFirma nodoFirma = new NodoFirma( dataToSign );
            Document doc = nodoFirma.getDocument();
            
            //data object reference.
            System.out.println("******* Previo a firmar ***************");
            XadesSignatureResult result = null;
            System.out.println("Tipo xml: " + signerInfo.getTipo_xml());
            System.out.println("Doc a firmar: : " + dataToSign);
            if (signerInfo.isXMLEnveloping()){
                //enveloped bancard.
                DataObjectDesc obj = new EnvelopedXmlObject(doc.getDocumentElement(), "text/plain", "http://www.w3.org/2000/09/xmldsig#");
                SignedDataObjects dataObjs = new SignedDataObjects(obj);                
                result = signer.sign(dataObjs, doc);
            }
            if (signerInfo.isXMLEnveloped()){
                DataObjectDesc obj1 = new DataObjectReference("").withTransform(new EnvelopedSignatureTransform());
                 result = signer.sign(new SignedDataObjects(obj1), doc.getDocumentElement());                
            }
            System.out.println("******** Firmado **************");
            String xmlSignature = "EMPTY";
            if (result != null) 
                xmlSignature = Utiles.printDocument(result.getSignature().getDocument());
            
            return xmlSignature;
        }
        catch(AccessControlException e){
            e.printStackTrace();
            throw new AppletException(e.getMessage(), null, e.getCause());
        }        
        catch(XAdES4jException e ){
            e.printStackTrace();
            throw new AppletException(e.getMessage(), null, e.getCause());
        } 
        catch (KeyStoreException e) {
            Logger.getLogger(FirmaXMLController.class.getName()).log(Level.SEVERE, null, e);
            throw new AppletException(e.getMessage(), null, e.getCause());
        }  
        catch (Exception e){
            Logger.getLogger(FirmaXMLController.class.getName()).log(Level.SEVERE, null, e);
            throw new AppletException(e.getMessage(), null, e.getCause());
        } 
    }
}
