import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private static OutputStream out;

    ClientHandler(Socket c) throws IOException {
        client = c;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = client.getOutputStream();
    }

    public void run() {
        // System.out.println("Client " + client.toString());

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
                // System.out.println(accessLog);

                // File file = new File(path);
                Path filePath = getFilePath(path);
                File file = new File(filePath.toString());

                System.out.println("===================================================");
                System.out.println((file.getParent().substring(6).equals("\\cgi-bin")));
                System.out.println("===================================================");

                if (file.getParent().substring(6).equals("\\cgi-bin")) {
                    // String commandLine;
                    // BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println(file.getPath());

                    String queryString = file.getPath().split("?")[1]; // pega o valor da query depois do ?  SE COLOCA ? NO QUERY ELE NAO ENTRA AQUI

                    System.out.println("===================================================");
                    System.out.println(queryString);
                    System.out.println("===================================================");

                    // System.out.print("jsh> ");
                    // commandLine = console.readLine();

                    // if (commandLine.equals(""))
                    // continue;
                    // if (commandLine.equals("exit"))
                    // break;

                    // ProcessBuilder pb = new ProcessBuilder(commandLine);
                    // Map<String, String> env = pb.environment();
                    // env.put("QUERY_STRING", queryString);

                    // Process proc = pb.start();
                    // // obtain the input stream
                    // InputStream is = proc.getInputStream();
                    // InputStreamReader isr = new InputStreamReader(is);
                    // BufferedReader br = new BufferedReader(isr);
                    // // read what is returned by the command
                    // line = "";
                    // while ((line = br.readLine()) != null)
                    //     System.out.println(line);

                    // br.close();
                }

                else if (Files.isDirectory(filePath)) {

                    String[] names = file.list();
                    ByteArrayOutputStream content = new ByteArrayOutputStream();
                    // List<by> content = new ArrayList<>();

                    // out.write("HTTP/1.0 200 Document Follows\r\n".getBytes());
                    // out.write("Content-Type: text/html\r\n\r\n".getBytes());
                    // out.write(
                    // "<html xmlns=\"http://www.w3.org/1999/xhtml\"xml:lang=\"en\"
                    // lang=\"en\">\r\n".getBytes());
                    // out.write("<head>\r\n".getBytes());
                    // out.write("<title>Linux/kernel/ - Linux Cross Reference - Free
                    // Electrons</title>\r\n".getBytes());
                    // out.write("</head>\r\n".getBytes());
                    // out.write("<body>\r\n".getBytes());

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
                client.close();
            }
        } catch (

        Exception e) {
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
