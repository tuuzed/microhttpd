# MicroHTTPd
### 一个纯Java实现的轻量极的可嵌入的Http服务器。

#### 简单使用
```Java
// Example.java
public class Example {
    public static void main(String[] args) {
        MicroHTTPd server = new MicroHTTPdBuilder()
                .setBindPort(5000)
                .setDebug(true)
                .build();
        server.register(staticfile, new StaticFileHandler(staticfile, "D:\\"));
        server.register("^/index$", new IndexHttpHandler());
        try {
            server.startup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


// IndexHttpHandler.java
public class IndexHttpHandler extends HttpHandler {

    @Override
    public void doGet(Request request, Response response) throws IOException {
        // 注意这里不能调用父类的doGet(Request request, Response response)方法
        response.setContentType("text/plain");
        response.addHeader("Date", new Date().toString());
        response.write("hello get\n" + request.toString());
    }

    @Override
    public void doPost(Request request, Response response) throws IOException {
        // 注意这里不能调用父类的doPost(Request request, Response response)方法
        response.setContentType("text/plain");
        response.addHeader("Date", new Date().toString());
        response.write("hello post\n" + request.toString());
    }
}

```