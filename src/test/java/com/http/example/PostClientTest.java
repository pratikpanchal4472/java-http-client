package com.http.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostClientTest {

    PostClient postClient = new PostClient();

    @Test
    void getPosts() {
        var posts = postClient.getPosts();
        assertEquals(100, posts.size());
    }

    @Test
    void getPost() {
        var post = postClient.getPost(1);
        assertEquals(1, post.id());
        assertEquals(1, post.userId());
    }
}