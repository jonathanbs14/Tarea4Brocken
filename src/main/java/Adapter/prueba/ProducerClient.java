package Adapter.prueba;

import java.io.*;
import java.net.*;

public class ProducerClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println("PUBLISH noticias Hola desde el productor!");
        System.out.println("Mensaje enviado al broker.");

        socket.close();
    }
}
