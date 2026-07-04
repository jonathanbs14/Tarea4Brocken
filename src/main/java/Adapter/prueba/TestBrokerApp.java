package Adapter.prueba;

import Application.ports.BrokerUserCase;
import Application.ports.SocketNotifier;
import Domain.Mensaje;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.*;

public class TestBrokerApp {
    private static final int PORT = 5000;

    public static void main(String[] args) throws IOException {
        Map<String, PrintWriter> clientes = new ConcurrentHashMap<>();
        SocketNotifier notifier = new SocketNotifier(clientes);
        BrokerUserCase broker = new BrokerUserCase(notifier);

        ServerSocket server = new ServerSocket(PORT);
        System.out.println("TestBrokerApp escuchando en puerto " + PORT);

        ExecutorService exec = Executors.newCachedThreadPool();
        while (true) {
            Socket s = server.accept();
            exec.submit(new ClientHandler(s, broker, clientes));
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;
        private final BrokerUserCase broker;
        private final Map<String, PrintWriter> clientes;
        private final String clientId;
        private final BufferedReader in;
        private final PrintWriter out;

        ClientHandler(Socket socket, BrokerUserCase broker, Map<String, PrintWriter> clientes) throws IOException {
            this.socket = socket;
            this.broker = broker;
            this.clientes = clientes;
            this.clientId = "client_" + socket.getPort();
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientes.put(clientId, out);
            out.println("CONNECTED " + clientId);
            System.out.println("Conexión: " + clientId);
        }

        @Override
        public void run() {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("[" + clientId + "] -> " + line);
                    String[] parts = line.split(" ", 3);
                    String cmd = parts[0].toUpperCase();

                    if ("SUBSCRIBE".equals(cmd) && parts.length >= 2) {
                        String topic = parts[1];
                        broker.suscribir(clientId, topic);
                        out.println("Suscrito a " + topic);
                    } else if ("PUBLISH".equals(cmd) && parts.length >= 3) {
                        String topic = parts[1];
                        String msg = parts[2];
                        broker.publicar(new Mensaje(topic, msg));
                        out.println("Publicado en " + topic);
                    } else {
                        out.println("COMANDO_INVALIDO");
                    }
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado: " + clientId);
            } finally {
                clientes.remove(clientId);
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }
}
