package me.darkboy.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import okhttp3.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class UserService {

    private final OkHttpClient client;

    public UserService() {
        client = new OkHttpClient();
    }

    public String createAccount(UserDetails user) throws IOException {

        FormBody postBody = new FormBody.Builder()
                .addEncoded("name", user.getUsername())
                .addEncoded("email", user.getEmail())
                .addEncoded("password", user.getPassword())
                .build();

        Request request = new Request.Builder()
                .post(postBody)
                .url("http://localhost:8080/accounts/register")
                .build();

        return Objects.requireNonNull(client.newCall(request).execute().body()).string();
    }

    public String login(UserDetails user) throws IOException {

        FormBody postBody = new FormBody.Builder()
                .addEncoded("email", user.getEmail())
                .addEncoded("password", user.getPassword())
                .build();

        Request request = new Request.Builder()
                .post(postBody)
                .url("http://localhost:8080/accounts/login")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        String body = Objects.requireNonNull(response.body()).string();

        Path file = Paths.get("session.txt");
        byte[] buf = body.getBytes();
        Files.write(file, buf);

        return body;
    }

    public boolean tokenExist(String token) throws IOException {

        FormBody postBody = new FormBody.Builder()
                .addEncoded("token", token)
                .build();

        Request request = new Request.Builder()
                .post(postBody)
                .url("http://localhost:8080/accounts/checkToken")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        String body = Objects.requireNonNull(response.body()).string();

        return !body.contains("exist");
    }

    public List<String> fetchUsers() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url("http://localhost:8080/accounts/fetchUsers")
                .build();

        Call call = client.newCall(request);
        ResponseBody response = call.execute().body();

        if (response != null) {

            return objectMapper.readValue(response.string(), TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
        }

        return null;
    }

    public boolean isSessionAlive() throws IOException {
        File session = new File("session.txt");

        if (session.length() != 0) {
            return tokenExist(getSession());
        }

        return false;
    }

    public String getSession() throws IOException {
        File session = new File("session.txt");
        BufferedReader br = new BufferedReader(new FileReader(session));
        String line = br.readLine();
        br.close();
        return line;
    }
}
