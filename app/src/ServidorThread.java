import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.lang.Thread;

public class ServidorThread implements Runnable {

    private Socket sAccepted;

    public ServidorThread(Socket aceito) {
        this.sAccepted = aceito;
    }

    public static void main(String[] args) throws Exception {
        // Valida argumentos
        validateArgs(args);

        final int PORT = args.length > 1 ? Integer.parseInt(args[1]) : 8080;

        try (ServerSocket socketEscuta = new ServerSocket(PORT)) {

            System.out.println("Server IP " + socketEscuta.getInetAddress().getHostAddress() + " on port " + PORT);

            while (true) {
                ServidorThread client = new ServidorThread(socketEscuta.accept());
                Thread thread = new Thread(client);

                thread.start();
                // handleClient(client);
            }
        } catch (

        Exception e) {
            System.err.println(e);
        }

    }

    @Override
    public void run() {

        try {
            while (true) {

                BufferedReader in = new BufferedReader(new InputStreamReader(sAccepted.getInputStream()));
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
                        sAccepted.toString(), method, path, version, host, headers.toString());
                System.out.println(accessLog);

                Path filePath = getFilePath(path);

                if (Files.exists(filePath)) {
                    String contentType = wichContentType(filePath);
                    sendResponse(sAccepted, "200 OK", contentType, Files.readAllBytes(filePath));
                } else {
                    byte[] notFoundContet = "<h1>Not found</h1>".getBytes();
                    sendResponse(sAccepted, "404 Not Found", "text/html", notFoundContet);
                }

            }

        } catch (IOException e) {

        }
    }

    private static void sendResponse(Socket client, String status, String contentType, byte[] content)
            throws IOException {
        OutputStream clientOut = client.getOutputStream();

        clientOut.write(("HTTP/1.1 " + status + "\r\n").getBytes());
        clientOut.write("Server: FACOM-CD-2020/1.0\r\n".getBytes());
        clientOut.write(("Content-type: " + contentType + "\r\n").getBytes());
        clientOut.write("\r\n".getBytes());
        clientOut.write(content);
        clientOut.write("\r\n\r\n".getBytes());
        clientOut.flush();

        client.close();
    }

    private static String wichContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }

    private static Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "/index.html";
        }

        return Paths.get("../www", path);
    }

    private static void validateArgs(String[] args) {
        String avaliable = "-t";

        try {
            if (args.length > 0 && args.length < 3) {

                if (!args[0].equals(avaliable))
                    throw new IllegalArgumentException();

                if (args.length > 1)
                    Integer.parseInt(args[1]);

            } else {
                throw new IllegalArgumentException();
            }
        } catch (NumberFormatException e) {
            System.err.println("Argumento" + args[1] + " deve ser um inteiro.");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Agumentos inválido!\n" + "Args validos: " + avaliable);
            System.exit(1);
        }
    }
}