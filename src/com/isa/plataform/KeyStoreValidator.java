package com.isa.plataform;

import com.isa.exception.AppletException;
import com.isa.utiles.Utiles;
import com.isa.utiles.UtilesResources;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Clase que se encarga de encapsular el comportamiento
 * que indica cual keystore se esta utilizando.
 * 
 * @author JMiraballes
 */
public class KeyStoreValidator {
    
        public static final String KEYSTORE_TOKEN = "PKCS11";
        public static final String KEYSTORE_WINDOWS = "WINDOWS";
        
	private static String KEYSTORE;
        
        public static void setInitStoreValidator() throws AppletException{
            KEYSTORE = UtilesResources.getProperty(UtilesResources.PROP_KEYSTORE);
            System.out.println("KEYSTORE: "  + KEYSTORE);
        }
        
	public static boolean isKeystoreToken() { 
            String [] keys = KEYSTORE.split(",");
            for (String key : keys) {
                if (KEYSTORE_TOKEN.equals(key)) {
                    return true;
                }
            }
            return false;
	}
 
	public static boolean isKeystoreWindows() {
            String [] keys = KEYSTORE.split(",");
            for (String key : keys) {
                if (KEYSTORE_WINDOWS.equals(key)) {
                    return true;
                }
            }
            return false;
	}   
        
        public static void limpiarKeystore(){
            KEYSTORE = "";
        }
}
