package com.isa.utiles;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.isa.exception.AppletException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JMiraballes
 * 
 * Clase que encapsula el mecanismo de acceso a un archivo de properties que
 * se encuentra en la la web donde se embebe el applet.
 * 
 */
public class UtilesResources {
    
    public static final String TRUE_VALUE = "true";
    public static final String PROP_KEYSTORE = "appletConfig.keystores";
    public static final String PROP_SWHELPER = "appletConfig.swHelper";
    public static final String PROP_PATH_SWHELPER = "appletConfig.pathSWHelper";
    public static final String PROP_PARAM_NAME = "appletConfig.paramName";
    public static final String PROP_PARAM_LIB = "appletConfig.paramLibrary";
    public static final String PROP_PARAM_SHOWINFO = "appletConfig.paramShowInfo";
    public static final String PROP_MODULOS = "appletConfig.Modulos";
    public static final String PROP_LIB_WIN = "appletConfig.LibrariesWin";
    public static final String PROP_REPO_TODOS = "appletConfig.RepositoriosTodos";
    public static final String PROP_REPO_TRUSTEDX = "appletConfig.RepositoriosTrustedX";
    public static final String PROP_REPO_WIN = "appletConfig.RepositoriosWindows";
    public static final String PROP_REPO_IGDOC = "appletConfig.RepositoriosIGDOC";
    public static final String PROP_REPO_TOKEN = "appletConfig.RepositoriosToken";
    public static final String PROP_TIPO_FIRMA_PKCS7 = "appletConfig.TipoFirmaPKCS7";
    public static final String PROP_TIPO_FIRMA_XAdES_ENVELOING =  "appletConfig.TipoFirmaXAdESEnveloping";
    public static final String PROP_VALIDAR_TRUSTEDX = "appletConfig.validarDesdeTrustedX";
    public static final String PROP_NODO_FIRMA = "appletConfig.NodoFirma";
    
    public static final String PROP_URL_IMAGEN = "appletConfig.URLImagen";
    public static final String PROP_APARIENCIA = "appletConfig.Apariencia";
    public static final String PROP_PAG_FIRMA = "appletConfig.PaginaFirma";
    public static final String PROP_ANCHO_FIRMA = "appletConfig.TamanioRecuadroAncho";
    public static final String PROP_LARGO_FIRMA = "appletConfig.TamanioRecuadroLargo";
    public static final String PROP_ESPACIO_FIRMAS = "appletConfig.EspacioHorizontalEntreFirmas";
    
    public static final String PROP_INIT_IZQ_X = "appletConfig.CoordsInitAbajoIzqX";
    public static final String PROP_INIT_IZQ_Y = "appletConfig.CoordsInitAbajoIzqY";
    public static final String PROP_MODO_FIRMA = "appletConfig.ModoFirma";
    
    public static final String PROP_WS_ENDPOINT = "appletConfig.WSEndpoint";
    public static final String PROP_WS_AUTH = "appletConfig.WSAutenticacion";
    public static final String PROP_WS_USER = "appletConfig.WSUser";    
    public static final String PROP_WS_PASSWD = "appletConfig.WSPasswd";    
    
    public static final String PROP_WS_ENDPOINT_VALIDACION = "appletConfig.WSEndpointValidacion";
    
    private static UtilesResources instance;
    private static String rutaProperties;
    private Properties appProperties = null;    
    
    private UtilesResources() throws AppletException {
        try{
            appProperties = new Properties();            
            appProperties.load(( new URL( rutaProperties )).openStream());
        }
        catch(IOException ex){
            Logger.getLogger(UtilesResources.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppletException(UtilesMsg.ERROR_ACCEDIENDO_ARCHIVO, null, ex.getCause());           
        }
    }
    
    private static UtilesResources getInstance() throws AppletException {
        if (instance == null){
            instance = new UtilesResources();
        }
        return instance;
    }
     
    public static String getProperty(String key) throws AppletException {
        return getInstance().getProperties().getProperty( key ).trim();   
    }
    
    private Properties getProperties(){
        return this.appProperties;
    }
    
    public static String getRutaProperties(){
        return rutaProperties;
    }
    
    public static void setRutaProperties( String ruta ){
        rutaProperties = ruta;
    }    
    
    
}
