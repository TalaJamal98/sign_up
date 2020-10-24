package com.example.signup.Model;

public class User {
String id;
String first_name;
String second_name;
String birthdate;
String prof_picture;
String aboutMe;
String city;

    public User(String id, String first_name, String second_name, String birthdate, String prof_picture, String aboutMe, String city) {
        this.id = id;
        this.first_name = first_name;
        this.second_name = second_name;
        this.birthdate = birthdate;
        this.prof_picture = prof_picture;
        this.aboutMe = aboutMe;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getProf_picture() {
        return prof_picture;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getCity() {
        return city;
    }
}
