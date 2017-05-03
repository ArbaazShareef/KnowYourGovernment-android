package com.arbaaz.knowyourgovernment;

import java.io.Serializable;

/**
 * Created by Arbaaz on 08-04-2017.
 */

public class Official implements Serializable{
    private String Name ;
    private String Title;
    private String Party ;
    private String Address ;
    private String Phno ;
    private String Email ;
    private String Website ;
    private String YoutubeUrl ;
    private String GooglePlusURL;
    private String FacebookURL;
    private String PhotoURL;

    public String getNormAddress() {
        return NormAddress;
    }

    public void setNormAddress(String normAddress) {
        NormAddress = normAddress;
    }

    private String NormAddress;

    public String getA1() {
        return A1;
    }

    public void setA1(String a1) {
        A1 = a1;
    }

    public String getA2() {
        return A2;
    }

    public void setA2(String a2) {
        A2 = a2;
    }

    public String getA3() {
        return A3;
    }

    public void setA3(String a3) {
        A3 = a3;
    }

    private String A1,A2,A3;

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public String getTwitter() {
        return Twitter;
    }

    public void setTwitter(String twitter) {
        Twitter = twitter;
    }

    private String Twitter ;
    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhno() {
        return Phno;
    }

    public void setPhno(String phno) {
        Phno = phno;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getYoutubeUrl() {
        return YoutubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        YoutubeUrl = youtubeUrl;
    }

    public String getGooglePlusURL() {
        return GooglePlusURL;
    }

    public void setGooglePlusURL(String googlePlusURL) {
        GooglePlusURL = googlePlusURL;
    }

    public String getFacebookURL() {
        return FacebookURL;
    }

    public void setFacebookURL(String facebookURL) {
        FacebookURL = facebookURL;
    }

    public Official() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getParty() {
        return Party;
    }

    public void setParty(String party) {
        Party = party;
    }
}
