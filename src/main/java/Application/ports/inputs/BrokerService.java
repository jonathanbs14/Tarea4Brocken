package Application.ports.inputs;

import Domain.Mensaje;

public interface BrokerService {
    void suscribir(String clienteId, String topic);
    void publicar(Mensaje mensaje);
}
