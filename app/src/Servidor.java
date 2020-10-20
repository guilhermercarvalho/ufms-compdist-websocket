import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    private static ArrayList<ServerThread> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws Exception {
        // Valida argumentos
        validateArgs(args);

        final int PORT = args.length > 1 ? Integer.parseInt(args[1]) : 8080;
        ServerSocket socket = new ServerSocket(PORT);

        try {
            while (true) {
                System.out.println("Server IP " + socket.getInetAddress().getHostAddress() + " on port " + PORT);
                Socket client = socket.accept();

                ServerThread clientThread = new ServerThread(client);
                clients.add(clientThread);
                pool.execute(clientThread);

            }
        } catch (Exception ioe) {
            System.err.println(ioe.getMessage());
            System.exit(-1);
        } finally {
            socket.close();
            System.out.println("Server Stopped");
        }
    }

    static void validateArgs(String[] args) {
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