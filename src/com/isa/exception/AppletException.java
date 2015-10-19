/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.exception;

/**
 *
 * @author JMiraballes
 */
public class AppletException extends Exception {
    private String msj;
    private String stacktrace;    

    public AppletException(String stacktrace, String message) {
        super(message);
        this.msj = message;
        this.stacktrace = stacktrace;
    }

    public AppletException(String msj, String stacktrace, String message, Throwable cause) {
        super(message, cause);
        this.msj = msj;
        this.stacktrace = stacktrace;
    }

    public AppletException(String msj, String stacktrace, Throwable cause) {
        super(cause);
        this.msj = msj;
        this.stacktrace = stacktrace;
    }

    public AppletException(String msj, String stacktrace, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msj = msj;
        this.stacktrace = stacktrace;
    }

    
    
    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }
    
    
}
