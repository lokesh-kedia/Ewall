package com.lokeshkedia.e_wall;

import java.io.Serializable;

public class Wall  implements Serializable {
    String original;
    String webformat;

    public Wall(String original, String webformat) {
        this.original = original;
        this.webformat = webformat;
    }

    public Wall() {

    }

    public String getoriginal() {
        return original;
    }

    public String getWebformat() {
        return webformat;
    }

    public void setWebformat(String webformat) {
        this.webformat = webformat;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
