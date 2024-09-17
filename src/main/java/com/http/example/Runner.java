package com.http.example;

public class Runner {
    public static void main(String[] args) {
        var postClient = new PostClient();
        var posts = postClient.getPosts();
        System.out.println(posts);
    }
}
