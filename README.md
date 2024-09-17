In **Java 11**, the new **HTTP Client API** was introduced as part of the `java.net.http` package, providing a modern, efficient, and feature-rich alternative to the older `HttpURLConnection`. This API supports both HTTP/1.1 and HTTP/2, allowing developers to create non-blocking, asynchronous HTTP requests with ease. 

### 1. **Overview of Java 11 HTTP Client**
The Java 11 HTTP Client API simplifies making HTTP requests and handling responses, replacing the older `HttpURLConnection` with more flexible, powerful features.

### 2. **Key Features of Java 11 HTTP Client**
- **HTTP/2 Support**: The client supports HTTP/2 out of the box, providing better performance and efficiency, such as multiplexing and header compression.
- **Asynchronous Requests**: It supports both synchronous and asynchronous requests via `CompletableFuture`.
- **WebSocket Support**: Built-in support for WebSockets for handling real-time, full-duplex communication.
- **Timeouts**: Easy configuration of connection and request timeouts.
- **Redirection**: Supports automatic handling of HTTP redirections.
- **Pipelining and Multi-Request Handling**: Efficient sending of multiple requests over the same connection.
- **Immutable**: Once created, an instance of the `HttpClient` is immutable, ensuring thread-safety.

### 3. **Basic Components of the HTTP Client API**
The HTTP Client API consists of three main components:
- **`HttpClient`**: The main client object to send requests and receive responses.
- **`HttpRequest`**: Represents an HTTP request, including headers, URI, and request method (GET, POST, etc.).
- **`HttpResponse`**: Represents the response returned from the server, containing status code, body, and headers.

### 4. **Creating an HTTP Client**
The `HttpClient` object is used to send requests and is created using a builder pattern.

Example:
```java
HttpClient client = HttpClient.newHttpClient();
```
Or for more customization:
```java
HttpClient client = HttpClient.newBuilder()
                             .version(HttpClient.Version.HTTP_2)
                             .followRedirects(HttpClient.Redirect.ALWAYS)
                             .build();
```

### 5. **Creating and Sending a Request**
The `HttpRequest` object represents the HTTP request, which is created using its builder.

Example of a GET request:
```java
HttpRequest request = HttpRequest.newBuilder()
                                 .uri(URI.create("https://example.com"))
                                 .build();
```

Example of a POST request:
```java
HttpRequest request = HttpRequest.newBuilder()
                                 .uri(URI.create("https://example.com"))
                                 .POST(HttpRequest.BodyPublishers.ofString("Request body"))
                                 .header("Content-Type", "application/json")
                                 .build();
```

### 6. **Handling the Response**
The `HttpClient` sends the request, and the response is represented by the `HttpResponse` object.

#### Synchronous Request (Blocking):
```java
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
System.out.println(response.body());
```

#### Asynchronous Request (Non-blocking):
```java
client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
      .thenApply(HttpResponse::body)
      .thenAccept(System.out::println);
```

### 7. **Handling Different Body Types**
The `HttpResponse.BodyHandler` can be used to handle different types of response bodies:
- **String**: To receive the response as a string.
    ```java
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    ```
- **File**: To write the response to a file.
    ```java
    HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get("response.txt")));
    ```
- **Byte Array**: To receive the response as a byte array.
    ```java
    HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
    ```

### 8. **Asynchronous Requests with `CompletableFuture`**
The HTTP Client API allows for fully non-blocking operations using `CompletableFuture`. For instance, sending a request asynchronously:
```java
HttpRequest request = HttpRequest.newBuilder()
                                 .uri(URI.create("https://example.com"))
                                 .build();

client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
      .thenApply(HttpResponse::body)
      .thenAccept(System.out::println)
      .join();
```
The use of `CompletableFuture` allows you to handle responses when they are ready, making it suitable for applications that require high concurrency.

### 9. **HTTP/2 and Asynchronous Request Benefits**
- **Multiplexing**: HTTP/2 allows multiple requests over a single connection, improving performance.
- **Header Compression**: Compresses headers to reduce the size of transmitted data.
- **Server Push**: The server can push additional resources to the client without the client explicitly requesting them.

### 10. **Configuring Timeouts**
You can set timeouts for both the connection and the entire request/response lifecycle.

Example:
```java
HttpClient client = HttpClient.newBuilder()
                             .connectTimeout(Duration.ofSeconds(10))  // Connection timeout
                             .build();

HttpRequest request = HttpRequest.newBuilder()
                                 .uri(URI.create("https://example.com"))
                                 .timeout(Duration.ofMinutes(2))  // Request/Response timeout
                                 .build();
```

### 11. **Handling HTTP Redirections**
By default, the client does not follow redirects automatically. However, you can configure the client to follow redirects using the `followRedirects` method.

Example:
```java
HttpClient client = HttpClient.newBuilder()
                             .followRedirects(HttpClient.Redirect.NORMAL)
                             .build();
```

### 12. **WebSocket Support**
The HTTP Client API also supports WebSockets for bi-directional communication. Here’s a basic example:

```java
HttpClient client = HttpClient.newHttpClient();
WebSocket.Builder webSocketBuilder = client.newWebSocketBuilder();
URI uri = URI.create("wss://echo.websocket.org");
WebSocket webSocket = webSocketBuilder.buildAsync(uri, new WebSocket.Listener() {
    @Override
    public void onOpen(WebSocket webSocket) {
        System.out.println("WebSocket opened");
        webSocket.sendText("Hello WebSocket", true);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println("Received: " + data);
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        System.out.println("Error: " + error.getMessage());
    }
}).join();
```

### 13. **Example: Full GET Request and Response**
Here’s a full example of a synchronous GET request with the Java 11 HTTP Client:
```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
                                 .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                                 .build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

System.out.println("Status Code: " + response.statusCode());
System.out.println("Headers: " + response.headers());
System.out.println("Body: " + response.body());
```

### 14. **Example: Full POST Request**
Here’s a full example of a POST request:
```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
                                 .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                                 .header("Content-Type", "application/json")
                                 .POST(HttpRequest.BodyPublishers.ofString("{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }"))
                                 .build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

System.out.println("Status Code: " + response.statusCode());
System.out.println("Body: " + response.body());
```

### Summary of Java 11 HTTP Client
- **Modern HTTP Client**: A feature-rich, modern API that replaces `HttpURLConnection`.
- **Supports Synchronous and Asynchronous Requests**: Use blocking or non-blocking calls as needed.
- **HTTP/2 and WebSocket Support**: Built-in support for modern web standards.
- **Simple to Use**: Easy-to-use API for making HTTP requests and handling responses, including flexible body handlers for different types of data.

This new client makes it easier to perform network communication, especially in applications requiring advanced features like HTTP/2 and non-blocking requests.