/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.firma.pades;

/**
 *
 * @author JMiraballes
 */
public class PDFFirmaValidar {
    private static PDFFirmaValidar validarFirma;
    
    public PDFFirmaValidar(){}
    
    public static PDFFirmaValidar getInstance(){
        if (validarFirma == null){
            validarFirma = new PDFFirmaValidar();
        }
        return validarFirma;
    }
    
    public boolean validarFirma( byte[] pdfFirmado){
        return true;
    }
}
