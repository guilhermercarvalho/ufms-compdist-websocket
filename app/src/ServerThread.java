import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class ServerThread implements Runnable {

    private Socket client;
    private BufferedReader in;
    private static OutputStream out;

    ServerThread(Socket c) throws IOException {
        client = c;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = client.getOutputStream();
    }

    public void run() {
        System.out.println("Client " + client.toString());

        try {
            while (true) {

                StringBuilder reqBuilder = new StringBuilder();
                String line;

                while (!(line = in.readLine()).isBlank()) {
                    reqBuilder.append(line + "\r\n");
                }

                String req = reqBuilder.toString();

                String[] reqsLines = req.split("\r\n");
                String[] reqLine = reqsLines[0].split(" ");

                String method = reqLine[0];
                String path = reqLine[1];
                String version = reqLine[2];
                String host = reqsLines[1].split(" ")[1];

                List<String> headers = new ArrayList<>();

                for (int h = 2; h < reqsLines.length; h++) {
                    String header = reqsLines[h];
                    headers.add(header);
                }

                String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
                        client.toString(), method, path, version, host, headers.toString());
                System.out.println(accessLog);

                Path filePath = getFilePath(path);

                if (Files.exists(filePath)) {
                    String contentType = wichContentType(filePath);
                    sendResponse("200 OK", contentType, Files.readAllBytes(filePath));
                } else {
                    byte[] notFoundContet = "<h1>Not found</h1>".getBytes();
                    sendResponse("404 Not Found", "text/html", notFoundContet);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        } finally {
            try {
                in.close();
                out.close();
                client.close();
            } catch (Exception e) {
                System.err.println(e.getStackTrace());
            }
        }

    }

    private void sendResponse(String status, String contentType, byte[] content) throws IOException {

        out.write(("HTTP/1.1 " + status + "\r\n").getBytes());
        out.write("Server: FACOM-CD-2020/1.0\r\n".getBytes());
        out.write(("Content-type: " + contentType + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.write(content);
        out.write("\r\n\r\n".getBytes());
        out.flush();
    }

    private String wichContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }

    private Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "/index.html";
        }

        return Paths.get("../www", path);
    }
}
