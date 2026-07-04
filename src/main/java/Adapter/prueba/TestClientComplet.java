package Adapter.prueba;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


@RequiredArgsConstructor
public class TestClientComplet {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientId;
    private ExecutorService executor;

    public TestClientComplet(String clientId, String host, int port) throws IOException {
        this.clientId = clientId;
        this.socket = new Socket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.executor = Executors.newSingleThreadExecutor();
    }
    public void iniciar() {
        System.out.println("=== Cliente: " + clientId + " ===");
        System.out.println("Conectado a localhost:5000");
        System.out.println("\nOpciones:");
        System.out.println("  SUBSCRIBE <topic>");
        System.out.println("  PUBLISH <topic> <mensaje>");
        System.out.println("  EXIT");
        System.out.println("================\n");

        executor.submit(this::escucharRespuestas);
        enviarComandos();
    }

    private void escucharRespuestas() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("→ [RESPUESTA] " + line);
            }
        } catch (IOException e) {
            System.out.println("Conexión cerrada por el servidor");
        }
    }

    private void enviarComandos() {
        try (Scanner scanner = new Scanner(System.in)) {
            String comando;
            while (true) {
                System.out.print(clientId + "> ");
                comando = scanner.nextLine().trim();

                if (comando.equalsIgnoreCase("EXIT")) {
                    System.out.println("Desconectando...");
                    break;
                }

                if (comando.isEmpty()) continue;

                out.println(comando);
                System.out.println("← [ENVIADO] " + comando);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            cerrar();
        }
    }

    private void cerrar() {
        try {
            executor.shutdown();
            socket.close();
            System.out.println("Desconectado.");
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String clientId = args.length > 0 ? args[0] : "cliente_" + System.currentTimeMillis();
        try {
            TestClientComplet client = new TestClientComplet(clientId, "localhost", 5000);
            client.iniciar();
        } catch (IOException e) {
            System.out.println("No se pudo conectar al servidor. ¿Está corriendo?");
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}


