/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frame.JavaBeans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Jesus Ruiz Oliva
 */
public class Frame implements Serializable{
    private String type;
    private String LD;
    private String LH;
    private String LN;
    private String LT;
    private String RD;
    private BigDecimal ID;

    public BigDecimal getID() {
        return ID;
    }

    public void setID(BigDecimal ID) {
        this.ID = ID;
    }
    
    public Frame(){
        
    }

    public String getLD() {
        return LD;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLD(String LD) {
        this.LD = LD;
    }

    public String getLH() {
        return LH;
    }

    public void setLH(String LH) {
        this.LH = LH;
    }

    public String getLN() {
        return LN;
    }

    public void setLN(String LN) {
        this.LN = LN;
    }

    public String getLT() {
        return LT;
    }

    public void setLT(String LT) {
        this.LT = LT;
    }

    public String getRD() {
        return RD;
    }

    public void setRD(String RD) {
        this.RD = RD;
    }
    
}

