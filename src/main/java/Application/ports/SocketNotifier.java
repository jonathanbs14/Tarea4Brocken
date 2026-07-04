package Application.ports;

import Application.ports.output.ClientNotifier;
import Domain.Mensaje;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocketNotifier implements ClientNotifier {

    private final Map<String, PrintWriter> clientes;

    @Override
    public void notificar(String clienteId, Mensaje mensaje) {
        PrintWriter out = clientes.get(clienteId);
        if (out != null) {
            out.println("[" + mensaje.topic() + "] " + mensaje.contenido());
        }
    }
}
