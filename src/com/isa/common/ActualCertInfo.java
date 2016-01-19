/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.common;

import com.isa.entities.SignerInfo;

/**
 *
 * @author JMiraballes
 */
public class ActualCertInfo {
   
    private SignerInfo singerInfo;

    
    
    public SignerInfo getSingerInfo() {
        return singerInfo;
    }

    public void setSingerInfo(SignerInfo singerInfo) {
        this.singerInfo = singerInfo;
    }
    
    private static ActualCertInfo actualCertInfo;
    
    private ActualCertInfo(){
        inicializar();
    }
    
    public static ActualCertInfo getInstance(){
        if ( actualCertInfo == null ){
            actualCertInfo = new ActualCertInfo();
        }
        return actualCertInfo;
    }    
    
    public final void inicializar(){  
        singerInfo = null;
    }
  
    
    
    
}
