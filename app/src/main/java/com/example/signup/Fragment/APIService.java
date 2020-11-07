package com.example.signup.Fragment;

import com.example.signup.Notifications.MyResponse;
import com.example.signup.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(

            {"Content-Type:application/json",
                    "Authorization:key=AAAA-UmdxHs:APA91bF1HN6Ik6RP8bMLS6v-qChL4Ee5DE9e5A2uUNSVTbmrYdd5W9D7fKP9-ZLNnINUW9aCiXWl8uRFpJ8fpNeMn4RXxWzUlhQHdvBgT6X95aCFaoY64uZlri824xeGGzy7QjOKAhzs"
            }

    )

    @POST("fcm/send")
        Call<MyResponse> sendNotification(@Body Sender body);

}
