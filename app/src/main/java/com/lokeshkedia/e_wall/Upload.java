package com.lokeshkedia.e_wall;

public class Upload {
    String tags;
    String Img;

    public Upload() {

    }

    public Upload(String Img, String tags) {
        this.tags = tags;
        this.Img = Img;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String Img) {
        this.Img = Img;
    }
}
