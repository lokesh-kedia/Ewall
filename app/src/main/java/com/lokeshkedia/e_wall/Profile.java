package com.lokeshkedia.e_wall;

public class Profile {
    String Email;
    String Img;
    String Name;

    public Profile() {

    }

    public Profile(String email, String img, String name) {
        Email = email;
        Img = img;
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
