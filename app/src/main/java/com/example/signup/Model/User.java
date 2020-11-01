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
    String city;
    String phone;


    public User(){}
    public User(String id, String firstname, String secondname, String birthdate, String profilepic, String aboutme, String city, String email, String password, String gender,String phone) {
        this.id = id;
        this.firstname = firstname;
        this.secondname = secondname;
        this.birthdate = birthdate;
        this.profilepic = profilepic;
        this.aboutme = aboutme;
        this.email = email;
        this.city = city;
        this.password = password;
        this.gender = gender;
        this.username = username;
        this.phone = username;


    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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


    public String getBirthdate() {
        return birthdate;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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


    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
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
