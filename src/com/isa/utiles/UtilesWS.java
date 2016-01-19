/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.utiles;

import com.isa.exception.AppletException;
import uy.isa.docman.client.DocmanClient;
import uy.isa.docman.schemas.DocumentType;
import uy.isa.docman.schemas.GetDocumentRequest;
import uy.isa.docman.schemas.GetDocumentResponse;
import uy.isa.docman.schemas.ObjectFactory;
import uy.isa.docman.schemas.UploadDocumentRequest;
import uy.isa.docman.schemas.UploadDocumentResponse;

/**
 *
 * @author JMiraballes
 */
public class UtilesWS {
    
    private static DocmanClient docmanClient = null;
    
    private static UtilesWS instance;

    public static int CODIGO_RESPUESTA_ERROR = -1; 
    public static int CODIGO_RESPUESTA_OK = 1;
    
    public UtilesWS() throws AppletException {
        
        String endpoint = UtilesResources.getProperty(UtilesResources.PROP_WS_ENDPOINT);
        docmanClient = new DocmanClient();
        docmanClient.setDefaultUri( endpoint );
        docmanClient.setMarshallerAndBound();
    }
    
    public static UtilesWS getInstanceWS() throws AppletException{
        if (instance == null){
            instance = new UtilesWS();
        }
        return instance;
    }
    
    
    public  GetDocumentResponse getDocumento( String documento ){
        
        GetDocumentRequest getDocRequest = docmanClient.CreateGetDocumentRequest( documento );
        return (GetDocumentResponse) docmanClient.GetDocument(getDocRequest);
    }
    
    public  UploadDocumentResponse uploadDocumento( DocumentType document ) throws AppletException{
        UploadDocumentRequest uploadDocRequest = new UploadDocumentRequest();
        uploadDocRequest.setDocument(document);
        return (UploadDocumentResponse) docmanClient.UploadDocument( uploadDocRequest );
    }
    
}
