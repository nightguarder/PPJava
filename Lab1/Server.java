package Lab1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.function.Predicate;
import java.nio.file.Path;
import com.sun.net.httpserver.*;

/**
 * Hello
 */
public class Server {
    private final InetSocketAddress address = new InetSocketAddress(8080);
    private final Path path = Path.of("/Users/cyrils/Developer/Java/PPJava");
    public static void main (String [] args){
        Server client = new Server();
        HttpServer server = client.createWith401Handler();
        server.start();
        System.out.println("Server is running on http:" + server.getAddress());
    }
    private HttpServer createBasic() {
        return SimpleFileServer.createFileServer(address, path, SimpleFileServer.OutputLevel.VERBOSE);
    }
    
    
    private HttpServer createWith401Handler(){
        Predicate<Request> findAllowedPath = r -> r.getRequestURI()
        .getPath()
        .equals("/test/allowed");

      HttpHandler allowedResponse = HttpHandlers.of(200, Headers.of("Allow", "GET"), "Welcome");
      HttpHandler deniedResponse = HttpHandlers.of(401, Headers.of("Deny", "GET"), "Denied");

      HttpHandler handler = HttpHandlers.handleOrElse(findAllowedPath, allowedResponse, deniedResponse);

      HttpServer server = SimpleFileServer.createFileServer(address, path, SimpleFileServer.OutputLevel.VERBOSE);
      server.createContext("/test", handler);
      return server;
    }
}
