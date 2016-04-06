package com.isa.utiles;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.isa.exception.AppletException;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;

/**
 *
 * @author JMiraballes
 * 
 * Clase contiene metodos estaticos utilitarios, que puden ser
 * accedidos de forma sencilla desde cualquier punto.
 * 
 */
public class Utiles {

    public static final SimpleDateFormat DATE_FORMAT_MIN = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat DATE_NAME_FORMAT_FULL = new SimpleDateFormat("yyyy-MM-dd HHmmss");
    
    public static final String SMARTWRAPPER_PROPERTIES = "smartwrapper.properties";
    
    public static String PKCS11_EXCEPTION_CKR_PIN_LOCKED = "CKR_PIN_LOCKED";
    public static String PKCS11_EXCEPTION_CKR_PIN_INCORRECT = "CKR_PIN_INCORRECT";
    public static String PKCS11_EXCEPTION_CKR_PIN_LEN_RANGE = "CKR_PIN_LEN_RANGE";
    public static String PKCS11_EXCEPTION_CKR_TOKEN_NOT_RECOGNIZED = "CKR_TOKEN_NOT_RECOGNIZED";
    
    public static String TRUE_VALUE = "true";
    public static String PARAM_TIPO_FIRMA = "tipoFirma";
    public static String VALUE_TIPO_FIRMA_PKCS7 = "pkcs7";
    public static String VALUE_TIPO_FIRMA_XADES_ENVELOPING = "xades";
    
    public static boolean isNullOrEmpty(String value){
        return (value == null || value.isEmpty());
    }    
       
    /**
     * Función para obtener el nombre identificado por CN= 
     * @return String
     * @param nombre
     */
    public static String getCN(String nombre){
        String[] arreglo;
        arreglo = nombre.split(",");
        for ( int i = 0; i < arreglo.length; i++ ){
            if(arreglo[i].startsWith(" CN=")||arreglo[i].startsWith("CN=")){
                if(arreglo[i].startsWith(" CN="))
                    return arreglo[i].replace(" CN=", "");
                else
                    return arreglo[i].replace("CN=", "");
            }
        }
        return "";
    }

    
    
    /**
     * Función para obtener el nombre identificado por O= 
     * @return String
     * @param nombre
     */
    public static String getO(String nombre){
        String[] arreglo;
        arreglo = nombre.split(",");
        for ( int i = 0; i < arreglo.length; i++ ){
            if(arreglo[i].startsWith(" O=")||arreglo[i].startsWith("O=")){
                if(arreglo[i].startsWith(" O="))
                    return arreglo[i].replace(" O=", "");
                else
                    return arreglo[i].replace("O=", "");
            }
        }
        return "";
    }    
    
    
    /**
     * Función para obtener el nombre identificado por OU= 
     * @return String
     * @param nombre
     */
    public static String getOU(String nombre){
        String[] arreglo;
        arreglo = nombre.split(",");
        for ( int i = 0; i < arreglo.length; i++ ){
            if(arreglo[i].startsWith(" OU=")||arreglo[i].startsWith("OU=")){
                if(arreglo[i].startsWith(" OU="))
                    return arreglo[i].replace(" OU=", "");
                else
                    return arreglo[i].replace("OU=", "");
            }
        }
        return "";
    }     
    
    
    public static String getCI( String nombre ){
        String[] arr = nombre.split(",");
        for ( int i = 0; i < arr.length; i++ ){
            if( arr[i].startsWith(" SERIALNUMBER=") || arr[i].startsWith("SERIALNUMBER=")){
                
                String serialnumber = arr[i].split("=")[1];
                String cedula = "";
                if (serialnumber.startsWith("CI ")){
                    cedula = serialnumber.replace("CI ", "");
                }
                else if (serialnumber.startsWith("CI")){
                    cedula = serialnumber.replace("CI", "");
                }
                return cedula;
            }
        }
        return "";
    }
    
    
    public static String getCIE( String nombre ){
        String[] arr = nombre.split(",");
        for ( int i = 0; i < arr.length; i++ ){
            if( arr[i].startsWith(" SERIALNUMBER=") || arr[i].startsWith("SERIALNUMBER=")){
                
                String serialnumber = arr[i].split("=")[1];
                String cedula = "";
                if (serialnumber.startsWith("CIE ")){
                    cedula = serialnumber.replace("CIE ", "");
                }
                else if (serialnumber.startsWith("CIE")){
                    cedula = serialnumber.replace("CIE", "");
                }
                return cedula;
            }
        }
        return "";
    }    
    
    public static String getPSP( String nombre ){
        String[] arr = nombre.split(",");
        for ( int i = 0; i < arr.length; i++ ){
            if( arr[i].startsWith(" SERIALNUMBER=") || arr[i].startsWith("SERIALNUMBER=")){
                
                String serialnumber = arr[i].split("=")[1];
                String cedula = "";
                if (serialnumber.startsWith("PSP ")){
                    cedula = serialnumber.replace("PSP ", "");
                }
                else if (serialnumber.startsWith("PSP")){
                    cedula = serialnumber.replace("PSP", "");
                }
                return cedula;
            }
        }
        return "";
    }    
    
    public static String getDocIDSerialNumber( String dn ){
        
        String cieUserCert = Utiles.getCIE( dn );
        String ciUserCert = Utiles.getCI( dn );
        String pspUserCert = Utiles.getPSP( dn );
        
        if (!Utiles.isNullOrEmpty(cieUserCert)){
            return cieUserCert;
        }        
        else if (!Utiles.isNullOrEmpty(ciUserCert)){
            return ciUserCert;
        }
        else if (!Utiles.isNullOrEmpty(pspUserCert)){
            return pspUserCert;
        }
        return null;
    }
    
    /**
     * Método que retorna un nombre distintivo de un usuario
     * en trustex, a partir de los valores pasados por parámetro.
     * El parámetro cn es obligatorio, pero o y oU son parámetros
     * opcionales.
     * 
     * @param cn
     * @param o
     * @param oU
     * @return 
     */
    public static String getDN (String cn, String o, String oU){   
        String dn = "CN=" + cn;
        
        if (!isNullOrEmpty(oU)){
            dn += ",OU="+oU;
        }
        return dn;
    }
    
    public static String[] splitByCaracter( String value, String caracter ){
        return value.split( caracter );
    }
    
    public static String setKeyValue(String param, String value){
        return ( param + "=" + value +  "\n")  ;
    }
    
    /**
     * 
     * @param rutaDestino
     * @throws java.io.IOException
     */
    public static void workarroundSmartWrapperProp( String rutaDestino ) throws AppletException{
        File fileDestino = new File( rutaDestino + SMARTWRAPPER_PROPERTIES);
        String linkDownload = UtilesResources.getProperty( "appletConfig.swHelper" ) + 
                UtilesResources.getProperty( "appletConfig.pathSWHelper" ) + 
                    "/" + SMARTWRAPPER_PROPERTIES;
        
        System.out.println("RUTA DESTINO: " + fileDestino);
        downloadFile(linkDownload, rutaDestino);   
    }
    

    /**
    * Método encargado de crear una carpeta. La misma
    * se crea bajo la ruta absoluta pasada por parámetro.
     * @param ruta
     * @throws java.io.IOException
    **/
    public static void crearCarpeta( String ruta ) throws IOException{
        File file = new File( ruta );
        if (!file.exists()) {
            if (!file.mkdir()) {    
                throw new IOException("El directorio no existe.");
            }
        }
    } 
    
    
    public static void downloadFile(String linkDescarga, String rutaDestino) throws AppletException{
        
        BufferedWriter bw = null;
        try {
            URL urlFile = new URL( linkDescarga );
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = new BufferedInputStream( urlFile.openStream() );
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != ( n = in.read(buf))){
                out.write(buf, 0, n);
            }   out.close();
            in.close();
            
            File file = new File(rutaDestino);
            file.setWritable(true);
            file.setReadable(true);
            bw = new BufferedWriter(new FileWriter(file, true));
            bw.write( out.toString() );
            bw.close();
        } 
        catch (IOException ex) {
            Logger.getLogger(Utiles.class.getName()).log(Level.SEVERE, null, ex); 
            throw new AppletException("Error Utiles::downloadFile", null, ex.getCause());
        } 
        finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Utiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
    * Método que copia un archivo desde una carpeta a otra.
    * Se pasa por parámetro el archivo fuente que se va
    * copiar y el archivo destino al cual se le copiarán 
    * los datos.
    *
     * @param source
     * @param dest
     * @throws java.io.IOException 
     */
    public static void copiarArchivo(File source, File dest) throws IOException {
        InputStream input = null;
	OutputStream output = null;
	try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
	}
        catch( Exception ex ){
            //throw new IOException("El directorio no existe.");
        }
        finally {    
            input.close();
            output.close();
	}
    }
   
    
    public static byte[] convertirBase64StringToArrayBytes( String imgBase64 ){
        return Base64.decodeBase64(imgBase64);
    } 
    
    
    public static String convertBase64ToString (byte[] data){
        String p="";
        for (int i = 0; i< data.length; i++){
            p = p + (char) data[i];
        } 
        return p;
    }
    

    /**
     * Converts a byte array to a X509Certificate instance.
     * @param bytes the byte array
     * @return a X509Certificate instance
     * @throws CertificateException if the conversion fails
     */
    public static X509Certificate fromByteArrayToX509Certificate(byte[] bytes) throws CertificateException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        InputStream in = new ByteArrayInputStream(bytes);
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
        return cert;
    }    
    
    
    public static String convertArrayByteToString(byte[] data){
        if (data != null){
            String s = new String(data);
            return s;
        }
        return "";
    }
    
    public static double convertTimeMillisToSeconds(long millisSecond){
        return millisSecond / 1000.0;
    }
    
    
    public static File crearArchivosFromBytes(byte[] documento, String fileName) {
        System.out.println("Creando archivo: " + fileName);
        File archivo = new File(fileName);
        try {
                FileOutputStream fos = new FileOutputStream(archivo);
                fos.write(documento);
                fos.close();
        } 
        catch (Exception x) {
                x.printStackTrace();
                if (archivo != null)
                        archivo.delete();
                return null;
        }
        return archivo;
    }
    
    public static String printDocument (Document doc ){
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
            
        } 
        catch (TransformerConfigurationException ex) {
            return "";
        } 
        catch (TransformerException ex) {
            return "";
        }
    }
    
    public static String encodeFileToBase64Binary(String fileName) throws IOException {

            File file = new File(fileName);
            byte[] bytes = loadFile(file);
            String encodedString = Base64.encodeBase64String(bytes);

            return encodedString;
    }

    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }
    
    
    public static PdfSignatureAppearance.RenderingMode getModoApariencia() throws AppletException{
        int modo = Integer.valueOf( UtilesResources.getProperty(UtilesResources.PROP_MODO_FIRMA));
        PdfSignatureAppearance.RenderingMode m = null;
        switch(modo){
            case 1: m = PdfSignatureAppearance.RenderingMode.GRAPHIC;
                    break;
            case 2: m = PdfSignatureAppearance.RenderingMode.DESCRIPTION;
                    break;
            case 3: m = PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION;
                    break;
            default: m = PdfSignatureAppearance.RenderingMode.DESCRIPTION;
                    break;
        }
        
        return m;
    }
    
    
    public static float convertirMmToPDFUnits( int value ){
        
        double pulgadas = 0d;
        double pdfunits = 0d;
        double centimetros = 0d;
        
        centimetros = convertirMMtoCM(value);
        pulgadas = convertirCmToPulgadas(centimetros); 
        
        pdfunits =  pulgadas * 72;
        
        return (float) pdfunits ;
    }
    
    public static double convertirMMtoCM ( int value ){
        return (value / 10);
    }
    
    public static double convertirCmToPulgadas( double cm ){
        return cm * 0.39370;
    }
    
}
