package com.isa.firma.xades;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.isa.exception.AppletException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author JMiraballes
 */
public class NodoFirma {
    
    private Document document;
    
    public NodoFirma(String data) throws AppletException {
        
        try {
            InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            document = dbf.newDocumentBuilder().parse(is); 
        } 
        catch (ParserConfigurationException ex) {
            Logger.getLogger(NodoFirma.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException("Error parseando xml", null, ex.getCause());
            
        } catch (SAXException ex) {
            Logger.getLogger(NodoFirma.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException("Error al parseando xml", null, ex.getCause());
            
        } catch (IOException ex) {
            Logger.getLogger(NodoFirma.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException("Error de escritura al parsear xml", null, ex.getCause());
        }
    }
    
    public void setDocument(Document document){
        this.document = document;
    }
    
    public Document getDocument(){
        return document;
    }
    
}
