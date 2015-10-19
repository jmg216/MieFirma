/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.utiles;

import com.isa.exception.AppletException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import py.com.miefirma.docman.definitions.Docman;
import py.com.miefirma.docman.definitions.DocmanService;
import py.com.miefirma.docman.schemas.DocumentType;
import py.com.miefirma.docman.schemas.GetDocumentRequest;
import py.com.miefirma.docman.schemas.GetDocumentRequestType;
import py.com.miefirma.docman.schemas.GetDocumentResponse;
import py.com.miefirma.docman.schemas.GetDocumentResponseType;
import py.com.miefirma.docman.schemas.UploadDocumentRequest;
import py.com.miefirma.docman.schemas.UploadDocumentRequestType;
import py.com.miefirma.docman.schemas.UploadDocumentResponse;

/**
 *
 * @author JMiraballes
 */
public class UtilesWS {
    
    private static Docman port = null;

    public static int CODIGO_RESPUESTA_ERROR = -1; 
    public static int CODIGO_RESPUESTA_OK = 1;
    
    public static Docman getInstancePortWS() throws AppletException{
        if (port == null){
            URL wsdllocation = null;
            try {
                wsdllocation = new URL(UtilesResources.getProperty(UtilesResources.PROP_WS_ENDPOINT));
                DocmanService serviceRes = new DocmanService( wsdllocation );
                port = serviceRes.getDocmanPort();
            } 
            catch (MalformedURLException ex) {
                Logger.getLogger(UtilesWS.class.getName()).log(Level.SEVERE, null, ex);
                throw new AppletException(UtilesMsg.ERROR_URL + UtilesResources.getProperty(UtilesResources.PROP_WS_ENDPOINT), null, ex.getCause());
            }
        }
        return port;
    }
    
    
    public static DocumentType getDocumento( String documento ) throws AppletException{
        GetDocumentRequest docrequest = new GetDocumentRequest();
        GetDocumentRequestType docrequesttype = new GetDocumentRequestType();
        docrequesttype.setId(documento);
        
        docrequest.setGetDocumentReq(docrequesttype);
        GetDocumentResponse docresponse = getInstancePortWS().getDocument( docrequest );
        
        return docresponse.getGetDocumentRes().getGetDocumentRes();
    }
    
    public static String subirDocumento( DocumentType documentType ) throws AppletException{
        
        UploadDocumentRequest uprequest = new UploadDocumentRequest();
        UploadDocumentRequestType uprequesttype = new UploadDocumentRequestType();
        uprequesttype.setUploadDocumentReq(documentType);
        
        uprequest.setUploadDocumentReq(uprequesttype);
        UploadDocumentResponse upresponse = getInstancePortWS().uploadDocument(uprequest);
        
        return upresponse.getUploadDocumentRes().getId();
    }
    
}
