package Adapter.prueba;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
public class BrokerServer {
    private static final int PORT = 5000;
    private static Map<String, List<PrintWriter>> subscribers = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Broker iniciado en puerto " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Mensaje recibido: " + line);
                    String[] parts = line.split(" ", 3);
                    String command = parts[0];

                    if ("SUBSCRIBE".equalsIgnoreCase(command)) {
                        String topic = parts[1];
                        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(out);
                        System.out.println("Cliente suscrito a " + topic);
                    } else if ("PUBLISH".equalsIgnoreCase(command)) {
                        String topic = parts[1];
                        String message = parts[2];
                        List<PrintWriter> subs = subscribers.getOrDefault(topic, new ArrayList<>());
                        for (PrintWriter pw : subs) {
                            pw.println("[" + topic + "] " + message);
                        }
                        System.out.println("Mensaje distribuido a " + subs.size() + " suscriptores");
                    }
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado");
            }
        }
    }
}
