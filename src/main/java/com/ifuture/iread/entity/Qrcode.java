package com.ifuture.iread.entity;

import javax.persistence.Transient;

/**
 * Created by maofn on 2017/3/29.
 */
public class Qrcode {

    private String bianhao;

    private String base64;

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
