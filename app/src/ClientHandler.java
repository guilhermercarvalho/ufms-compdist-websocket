import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private OutputStream out;

    ClientHandler(Socket c) throws IOException {
        client = c;
        InputStreamReader inst = new InputStreamReader(client.getInputStream());
        in = new BufferedReader(inst);
        out = client.getOutputStream();
        client.setSoTimeout((2 * 1000));
    }

    public void run() {
        System.out.println("Client " + client.toString());

        try {
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

            String accessLog = String.format("method %s, path %s, version %s, host %s, headers %s", method, path,
                    version, host, headers.toString());
            System.out.println(accessLog);

            int indexConnection = headers.indexOf("connection: keep-alive");
            if (indexConnection == -1)
                client.setKeepAlive(false);
            else
                client.setKeepAlive(true);

            Path filePath = getFilePath(path);
            File fileName = new File(filePath.toString());

            if (fileName.getParent().substring(6).equals("/cgi-bin")) {
                ByteArrayOutputStream content = new ByteArrayOutputStream();

                String fileExec = fileName.getPath().split("\\?")[0];
                String queryString = fileName.getPath().split("\\?")[1];

                ProcessBuilder pb = new ProcessBuilder(fileExec);

                Map<String, String> env = pb.environment();
                env.put("QUERY_STRING", queryString);

                Process proc = pb.start();

                InputStream is = proc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String lineq;
                while ((lineq = br.readLine()) != null) {
                    content.write(lineq.getBytes());
                }

                br.close();
                isr.close();
                is.close();
                sendResponse("200 Document Follows", "text/html", content.toByteArray());
                content.close();

            } else if (Files.isDirectory(filePath)) {
                String[] names = fileName.list();
                ByteArrayOutputStream content = new ByteArrayOutputStream();
                content.write(
                        ("<html xmlns=\"http://www.w3.org/1999/xhtml\"xml:lang=\"en\" lang=\"en\">\r\n" + "<head>\r\n"
                                + "<title>Java Webserver</title>\r\n" + "</head>\r\n" + "<body>\r\n").getBytes());

                content.write(
                        ("<td><a href=\"" + fileName.getParent().substring(6) + "/\">voltar</a></td>\n").getBytes());

                for (int i = 0; i < names.length; i++) {
                    String newLine = String.format("<td><a href=\"%s\">%s</a></td>\n",
                            filePath.toString().substring(6) + "/" + names[i], names[i]);
                    content.write(newLine.getBytes());
                }
                content.write("<body>\r\n".getBytes());

                sendResponse("200 Document Follows", "text/html", content.toByteArray());
                content.close();

            } else if (Files.exists(filePath)) {
                String contentType = wichContentType(filePath);
                sendResponse("200 Document Follows", contentType, Files.readAllBytes(filePath));
            } else {
                byte[] notFoundContet = "<h1> File Not Found</h1>".getBytes();
                sendResponse("Could not find the specified URL", "text/plain", notFoundContet);
            }
        } catch (Exception e) {
            System.err.print(e.getStackTrace());
        } finally {
            try {
                in.close();
                out.close();
                client.close();
            } catch (Exception e) {
                System.err.print(e.getStackTrace().toString());
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
