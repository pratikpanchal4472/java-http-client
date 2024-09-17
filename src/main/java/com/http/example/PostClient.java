package com.http.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class PostClient {
    private static final String POST_API = "https://jsonplaceholder.typicode.com/posts";
    private final HttpClient client;
    private final ObjectMapper mapper;

    public PostClient() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public List<Post> getPosts() throws RuntimeException {
        var request = HttpRequest.newBuilder().uri(URI.create(POST_API)).GET().build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), new TypeReference<List<Post>>() {});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Post getPost(int id) {
        var request = HttpRequest.newBuilder().uri(URI.create(POST_API + "/" + id)).GET().build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), Post.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
