# MicroHTTPd
### 一个纯Java实现的轻量极的可嵌入的Http服务器。

#### 简单使用
```Java
// Simple.java
public class Simple {
    public static void main(String[] args) {
        MicroHTTPd server = new MicroHTTPd.Builder()
                .setPort(5000)
                .useFileView("^/static/.*", "C:\\")
                .debug(true, true)
                .build();
        server.register(new IndexView());
        server.register(new UploadView());
        try {
            server.startup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



// IndexView.java
@Route("^/$")
public class IndexView extends MethodView {

    @Override
    public void doGet(Request req, Response resp) throws IOException {
        resp.renderText("hello\n" + req.toString());
    }

    @Override
    public void doPost(Request req, Response resp) throws IOException {
        resp.renderText("hello\n" + req.toString());
    }
}

```