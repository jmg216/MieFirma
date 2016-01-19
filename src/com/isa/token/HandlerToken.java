package com.isa.token;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.isa.entities.ParamInfo;
import com.isa.exception.AppletException;
import com.isa.plataform.OSValidator;
import com.isa.utiles.Utiles;
import java.security.KeyStoreException;
import java.security.ProviderException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

/**
 *
 * @author JMiraballes
 */
public class HandlerToken {
    
    private static HandlerToken instance;
    
    private ArrayList<Token> tokens;
    private String[] modulos;
    private String[] libs;
    
    public static HandlerToken getInstance(){
        if (instance == null){
            instance = new HandlerToken();
        }
        return instance;
    }
    
    /**
     * Carga todos los tokens configurados en la pc del usuario. 
     */
    private HandlerToken(){
        System.out.println("HandlerToken::Constructor");
        tokens = new ArrayList();

        String libraries = "";
        if (OSValidator.isWindows()) {
            libraries = ParamInfo.getInstance().getParam_lib_win();
        }        
        if (OSValidator.isUnix()){
            libraries = ParamInfo.getInstance().getParam_lib_unx();
        }
        String modulosstr = ParamInfo.getInstance().getParam_modulos();

        modulos = Utiles.splitByCaracter(modulosstr, ",");
        libs = Utiles.splitByCaracter(libraries, ",");
    } 
    
    public void cargarTokens(){
        System.out.println("HandlerToken::cargarTokens");
        
        tokens.clear();
        for (int i = 0; i < modulos.length; i++){
            Token token = new Token(modulos[i], libs[i]);
            tokens.add(token);
        }   
    }
    

    public ArrayList<Token> getTokens() {
        if (tokens == null){
            tokens = new ArrayList();
        }
        return tokens;
    }

    public void setTokens(ArrayList<Token> lista) {
        this.tokens = lista;
    }
    
    /**
     * Obtiene el token activo de la lista de tokens configurados
     * en la maquina local.
     * 
     * @return 
     */
    public Token getTokenActivo(){
        try {
            Iterator<Token> it = getTokens().iterator();
            Token token = null;

            while (it.hasNext()){
                Token t = it.next();
                t.reLoadConfig();
                if (t.isActivo()){
                    token = t;
                    break;
                }
            }
            return token;
        } catch (ProviderException ex) {
            Logger.getLogger(HandlerToken.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }            
    }
    
    /**
     * Método que retorna true si existe un token activo en el sistema, o sea
     * que se encuentra conectado a la pc.
     * @return 
     */
    public boolean isTokenActivo(){
        try {
            boolean isActivo = false;
            System.out.println("HandlerToken::isTokenActivo");

            Iterator<Token> it = getTokens().iterator();

            while (it.hasNext()){
                Token t = it.next();
                t.reLoadConfig();
                if (t.isActivo()){
                    isActivo = true;
                    break;
                }
            }
            return isActivo;
        } 
        catch (ProviderException ex) {
            Logger.getLogger(HandlerToken.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return false;
        }       
    }
    
    public void desactivarAllTokens() throws LoginException{
        Iterator<Token> it = getTokens().iterator();
        
        while (it.hasNext()){
            Token t = it.next();
            t.setActivo(false);
            if (t.isLogued()){
                t.logout();
            }
        }
    }

    public String[] getModulos() {
        return modulos;
    }

    public void setModulos(String[] modulos) {
        this.modulos = modulos;
    }

    public String[] getLibs() {
        return libs;
    }

    public void setLibs(String[] libs) {
        this.libs = libs;
    }
    
    
}
