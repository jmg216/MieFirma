/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.firma.xades;

import com.isa.common.ActualCertInfo;
import com.isa.exception.AppletException;
import com.isa.main.Main;
import com.isa.utiles.UtilesResources;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author JMiraballes
 */
public class TestXAdES {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

//        try {
            // TODO code application logic here
//            UtilesResources.setRutaProperties("http://localhost:9000/ISCert/resources/aduana/applet.properties");
//            ActualCertInfo.getInstance().setCertIndex(2);
//            ActualCertInfo.getInstance().setPassword("1111");
//            FirmaXMLController controller = new FirmaXMLController();
//            String dataToSign = readFile("C:\\Users\\JMiraballes\\Documents\\Proyectos\\Aduana\\xmlSinFirma\\xmlFirmado-12-02-2016-144039.xml");
//            String xmlFirmado = controller.firmarXades(dataToSign);
            
            //Output stream de xml firmado
            //outputStream(xmlFirmado);
        //{"doc": "43214321421321412","perfil_firma": "ENVING","tipo_xml": "XML","alias": "-1","rol": "Rol 1","motivo": "Motivo 1","localidad": "Lugar 1","apariencia": "false","hoja": "-1","llx": "0","lly": "0","urx": "0","ury": "0","imagen" : ""}

            
            String uno = "{\"doc\": \"43214321421321412\",\"perfil_firma\": \"ENVING\",\"tipo_xml\": \"XML\",\"alias\": \"-1\",\"rol\": \"Rol 1\",\"motivo\": \"Motivo 1\",\"localidad\": \"Lugar 1\",\"apariencia\": \"false\",\"hoja\": \"-1\",\"llx\": \"0\",\"lly\": \"0\",\"urx\": \"0\",\"ury\": \"0\",\"imagen\" : \"\"}";
            String dos = "algo";
            String tres = "algo";
            
            Main.testGson(uno, dos, tres);
            
//        } catch (FileNotFoundException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(TestXAdES.class.getName()).log(Level.SEVERE, null, ex);
//            
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(TestXAdES.class.getName()).log(Level.SEVERE, null, ex);
//            
//        } catch (AppletException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(TestXAdES.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    
    private static void outputStream(String xmlFirmado){
    
        try {
            String nombre = "xmlFirmado-" + new SimpleDateFormat("dd-MM-yyyy-HHmmss").format(new Date());
            String ruta = "C:\\Users\\JMiraballes\\Documents\\Proyectos\\Aduana\\\\xmlfirmado\\";
            FileWriter fw = new FileWriter( ruta + nombre + ".xml" );
            fw.write(xmlFirmado);
            fw.close();
            
            
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(TestXAdES.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(TestXAdES.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
    }
    
    private static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }    
    
}
