package Adapter.web;
import Application.ports.inputs.BrokerService;
import Domain.Mensaje;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.net.Socket;

@RequiredArgsConstructor
public class BrokerController implements Runnable {
    private final Socket socket;
    private final BrokerService brokerService;
    private final String clientId;
    private final PrintWriter out;
    private final BufferedReader in;


    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Comando recibido: " + line);
                String[] parts = line.split(" ", 3);
                String command = parts[0];

                switch (command.toUpperCase()) {
                    case "SUBSCRIBE":
                        String topic = parts[1];
                        brokerService.suscribir(clientId, topic);
                        out.println("Suscrito al tópico: " + topic);
                        break;

                    case "PUBLISH":
                        String pubTopic = parts[1];
                        String message = parts[2];
                        brokerService.publicar(new Mensaje(pubTopic, message));
                        out.println("Mensaje publicado en: " + pubTopic);
                        break;

                    default:
                        out.println("Comando no reconocido.");
                }
            }
        } catch (IOException e) {
            System.out.println("Cliente desconectado: " + clientId);
        }
    }
}
