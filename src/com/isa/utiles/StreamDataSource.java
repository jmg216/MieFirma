/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isa.utiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

/**
 *
 * @author JMiraballes
 */
public class StreamDataSource implements DataSource{
    
    private OutputStream outputStream;
    private InputStream inputStream;

    public StreamDataSource(InputStream inputStream, OutputStream outputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public String getContentType() {
        return "*/*";
    }

    @Override
    public String getName() {
        return "OutputStreamDataSource";
    }
    
}
