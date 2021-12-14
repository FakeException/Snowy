package me.darkboy.utils;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class UserService {

    private final OkHttpClient client = new OkHttpClient();

    public Response createAccount(UserDetails user) throws IOException {

        FormBody postBody = new FormBody.Builder()
                .addEncoded("name", user.getUsername())
                .addEncoded("email", user.getEmail())
                .addEncoded("password", user.getPassword())
                .build();

        Request request = new Request.Builder()
                .post(postBody)
                .url("http://localhost:8080/accounts/register")
                .build();

        return client.newCall(request).execute();
    }

    public Response login(UserDetails user) throws IOException {

        FormBody postBody = new FormBody.Builder()
                .addEncoded("email", user.getEmail())
                .addEncoded("password", user.getPassword())
                .build();

        Request request = new Request.Builder()
                .post(postBody)
                .url("http://localhost:8080/accounts/login")
                .build();

        return client.newCall(request).execute();
    }
}
