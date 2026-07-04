package Application.ports.output;

import Domain.Mensaje;

public interface ClientNotifier {
    void notificar(String clienteId, Mensaje mensaje);
}
