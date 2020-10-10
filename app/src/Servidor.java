import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Servidor {

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(3332);
        System.out.println("Porta 3332 aberta!");
        
        Socket cliente = servidor.accept();
        System.out.println("Nova conexão com o cliente " + 
            cliente.getInetAddress().getHostAddress()
        );

        Scanner s = new Scanner(cliente.getInputStream());
        while(s.hasNextLine()) {
            System.out.println(s.nextLine());
        }

        s.close();
        servidor.close();
        cliente.close();
    }
}