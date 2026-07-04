Broker de Mensajes – Cliente/Servidor (Arquitectura Hexagonal)

1. Descripción del proyecto
Este proyecto implementa una aplicación distribuida que simula el funcionamiento básico de un broker de mensajes bajo una arquitectura Cliente–Servidor.
El sistema permite que múltiples clientes (productores y consumidores) se conecten a un servidor central, envíen mensajes y los reciban según el canal, tópico o cola al que estén suscritos.
El objetivo es comprender cómo funciona internamente un broker, aplicando conceptos de redes, concurrencia, colas de mensajes y publicación/suscripción, sin utilizar plataformas externas como RabbitMQ, Kafka o ActiveMQ.

2. Arquitectura propuesta
Se utiliza arquitectura hexagonal (Ports & Adapters) para separar la lógica de negocio del broker de los detalles técnicos:

- Dominio (Core):
  Entidades: Mensaje, Cliente, Suscripción.
  Casos de uso: PublicarMensaje, SuscribirCliente, DistribuirMensajes.

- Puertos (Interfaces):
  Inbound Ports: BrokerService (interfaz para suscribir y publicar).
  Outbound Ports: ClientNotifier (interfaz para enviar mensajes a clientes).

- Adaptadores (Adapters):
  Inbound Adapter: Controlador de sockets (BrokerController) que recibe comandos de clientes.
  Outbound Adapter: Notificador por sockets (SocketNotifier) que reenvía mensajes a consumidores.

Este diseño permite reemplazar fácilmente los adaptadores (ej. usar HTTP o WebSockets en lugar de sockets) sin modificar la lógica del dominio.

3. Tecnologías utilizadas
- Lenguaje: Java 25
- Comunicación: Sockets TCP
- IDE recomendado: IntelliJ IDEA
- Arquitectura: Hexagonal (Ports & Adapters)
- Ejecución: Local o en contenedores Docker

4. Instrucciones de instalación
1. Clonar el repositorio o copiar los archivos fuente:
   git clone https://github.com/usuario/broker-mensajes.git
2. Abrir el proyecto en IntelliJ IDEA.
3. Compilar las clases:
   javac *.java

5. Instrucciones de ejecución
1. Iniciar el servidor broker:
   java BrokerServer
   El servidor quedará escuchando en el puerto 5000.

2. Ejecutar consumidores:
   java ConsumerClient
   Cada consumidor se suscribe a un tópico (ej. noticias).

3. Ejecutar productores:
   java ProducerClient
   El productor publica mensajes en un tópico (ej. noticias).

6. Ejemplos de uso
1. Inicia el BrokerServer.
2. Conecta dos consumidores y suscríbelos:
   SUBSCRIBE noticias
   SUBSCRIBE alertas
3. Conecta un productor y publica:
   PUBLISH noticias Hola desde el productor!
4. El broker recibe el mensaje y lo distribuye a los consumidores suscritos a noticias.
5. Los consumidores muestran en consola:
   Mensaje recibido: [noticias] Hola desde el productor!
