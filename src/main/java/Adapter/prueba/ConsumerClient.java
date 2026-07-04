package Adapter.prueba;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConsumerClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("SUBSCRIBE noticias");
        System.out.println("Suscrito al tópico noticias.");

        String line;
        while ((line = in.readLine()) != null) {
            System.out.println("Mensaje recibido: " + line);
        }
    }
}
