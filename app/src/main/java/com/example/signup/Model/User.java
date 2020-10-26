package com.example.signup.Model;

public class User {
String id;
String firstname;
String secondname;
String birthdate;
String profilepic;
String aboutme;
    String email;
    String password;
    String gender;
    String username;


    public User(){}
    public User(String id, String firstname, String secondname, String birthdate, String profilepic, String aboutMe, String city, String email, String password, String gender) {
        this.id = id;
        this.firstname = firstname;
        this.secondname = secondname;
        this.birthdate = birthdate;
        this.profilepic = profilepic;
        this.aboutme = aboutMe;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.username = username;

    }

    public String getAbout_me() {
        return aboutme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return firstname;
    }

    public String getSecond_name() {
        return secondname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getProfile_pic() {
        return profilepic;
    }

    public String getAboutMe() {
        return aboutme;
    }



    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.firstname = first_name;
    }

    public void setSecond_name(String second_name) {
        this.secondname = second_name;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setProfile_pic(String profile_pic) {
        this.profilepic = profile_pic;
    }

    public void setAbout_me(String about_me) {
        this.aboutme = about_me;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
