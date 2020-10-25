import java.io.*;
import java.lang.ProcessBuilder.Redirect;
import java.lang.reflect.Array;
import java.net.*;
import java.nio.file.*;
import java.util.*;

import javax.print.attribute.standard.MediaSize.ISO;

public class TesteRoger implements Runnable {

    private Socket client;
    private BufferedReader in;
    private static OutputStream out;

    TesteRoger(Socket c) throws IOException {
        client = c;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = client.getOutputStream();
        c.setSoTimeout((10 * 1000));
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
                    String header = reqsLines[h].toLowerCase();
                    headers.add(header);
                }

                String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
                        client.toString(), method, path, version, host, headers.toString());
                System.out.println(accessLog);

                int indexConnection = headers.indexOf("connection: keep-alive");
                if (indexConnection == -1)
                    client.setKeepAlive(false);
                else
                    client.setKeepAlive(true);

                System.out.println("is alive: " + client.getKeepAlive());
                Path filePath = getFilePath(path);
                File file = new File(filePath.toString());

                if (file.getParent().substring(6).equals("/cgi-bin")) {

                    String fileExec = file.getPath().split("\\?")[0]; // pega o valor da query depois do ? SE COLOCA ?
                    String queryString = file.getPath().split("\\?")[1]; // pega o valor da query depois do ? SE COLOCA
                                                                         // ?
                                                                         // NO QUERY ELE NAO ENTRA AQUI

                    ByteArrayOutputStream content = new ByteArrayOutputStream();
                    
                    ProcessBuilder pb = new ProcessBuilder(fileExec);
                    
                    Map<String, String> env = pb.environment();
                    env.put("QUERY_STRING", queryString);
                    // pb.redirectOutput(content.);

                    Process proc = pb.start();

                    InputStream is = proc.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    
                    String lineq;
                    while ((lineq = br.readLine()) != null) {
                        content.write(lineq.getBytes());
                    }

                    br.close();
                    sendResponse("200 Document Follows", "text/html", content.toByteArray());
                }
                else if (Files.isDirectory(filePath)) {

                    String[] names = file.list();
                    ByteArrayOutputStream content = new ByteArrayOutputStream();

                    content.write(
                            ("<td><a href=\"" + file.getParent().substring(6) + "/\">voltar</a></td>\n").getBytes());
                    for (int i = 0; i < names.length; i++) {
                        String newLine = String.format("<td><a href=\"%s\">%s</a></td>\n",
                                filePath.toString().substring(6) + "/" + names[i], names[i]);
                        content.write(newLine.getBytes());
                    }
                    sendResponse("200 Document Follows", "text/html", content.toByteArray());

                } else if (Files.exists(filePath)) {

                    String contentType = wichContentType(filePath);
                    sendResponse("200 Document Follows", contentType, Files.readAllBytes(filePath));

                } else {
                    byte[] notFoundContet = "<h1>Not found</h1>".getBytes();
                    sendResponse("404 Not Found", "text/html", notFoundContet);
                }
            }
        } catch (

        Exception e) {
            System.err.println(e.getStackTrace());
        } finally {
            try {
                in.close();
                out.close();
                if (!(client.getKeepAlive()))
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

    // private void sendCGIResponse(String status, String contentType, byte[]
    // content) throws IOException { nadave tá tarde deu sono
    // Date date = new Date();
    // SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    // out.write(("HTTP/1.1 " + status + "\r\n").getBytes());
    // out.write(("Date: " + formatter.format(date) + "\r\n").getBytes());
    // out.write("Server: FACOM-CD-2020/1.0\r\n".getBytes());
    // out.write(("Content-type: " + contentType + "\r\n").getBytes());
    // out.write("\r\n".getBytes());
    // out.write(content);
    // out.write("\r\n\r\n".getBytes());
    // out.flush();
    // }

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
