package Application.ports;

import Application.ports.inputs.BrokerService;
import Application.ports.output.ClientNotifier;
import Domain.Mensaje;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BrokerUserCase implements BrokerService {

    private final Map<String, List<String>> suscripciones = new HashMap<>();
    private final ClientNotifier notifier;

    @Override
    public void suscribir(String clienteId, String topic) {
        suscripciones.computeIfAbsent(topic, k -> new ArrayList<>()).add(clienteId);
    }

    @Override
    public void publicar(Mensaje mensaje) {
        List<String> clientes = suscripciones.getOrDefault(mensaje.topic(), new ArrayList<>());
        for (String clienteId : clientes) {
            notifier.notificar(clienteId, mensaje);
        }
    }
}
