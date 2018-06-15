package org.codekidd.jabbachatapp;

/**
 * Created by codekid on 12/06/2018.
 */

public class Users {

//    we want to retrieve just three things here the username, status and display image

    public String name;
//    make sure the "name" matches the object "key name" in the database
    public String image;
    public String status;
    public String thumb_image;

//    empty constructor
    public Users(){

    } //very important app may crash without this constructor



    //Constructors
    public Users(String name, String image, String status) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_image = thumb_image;
    }
    //now set getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }



}
