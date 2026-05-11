import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServidorWeb {
    private static final int PUERTO = 5000;
    private static volatile boolean servidorCorriendo = true;
    private static ServerSocket servidorSocket;
    private static List<ManejadorCliente> clientesActivos = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        try {
            servidorSocket = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado en el puerto " + PUERTO);
            System.out.println("Escriba 'apagar' para cerrar el servidor.");

            // MEJORA 2: Hilo para monitorear el comando de apagado
            new Thread(() -> {
                Scanner sc = new Scanner(System.in);
                while (servidorCorriendo) {
                    if (sc.nextLine().equalsIgnoreCase("apagar")) {
                        apagarServidor();
                    }
                }
            }).start();

            // Bucle principal de aceptación de clientes
            while (servidorCorriendo) {
                try {
                    Socket socketCliente = servidorSocket.accept();
                    System.out.println("Nuevo cliente conectado: " + socketCliente.getInetAddress());
                    
                    // MEJORA 1: Manejo mediante hilos independientes
                    ManejadorCliente manejador = new ManejadorCliente(socketCliente);
                    clientesActivos.add(manejador);
                    new Thread(manejador).start();
                } catch (IOException e) {
                    if (servidorCorriendo) System.err.println("Error al aceptar cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el servidor: " + e.getMessage());
        }
    }

    private static void apagarServidor() {
        System.out.println("Iniciando apagado controlado...");
        servidorCorriendo = false;
        
        // Notificar y cerrar a todos los clientes conectados
        for (ManejadorCliente cliente : clientesActivos) {
            cliente.enviarMensaje("SERVIDOR_CIERRE: El servidor se va a cerrar.");
            cliente.cerrarConexion();
        }

        try {
            if (servidorSocket != null && !servidorSocket.isClosed()) {
                servidorSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el socket del servidor.");
        }
        System.out.println("Servidor finalizado.");
        System.exit(0);
    }

    // MEJORA 1 y 3: Lógica de comunicación con cada cliente
    static class ManejadorCliente implements Runnable {
        private Socket socket;
        private PrintWriter salida;
        private BufferedReader entrada;
        private Map<String, String> paginas;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
            this.paginas = new HashMap<>();
            paginas.put("1", "<html><body><h1>Index</h1><p>Bienvenido al simulador.</p></body></html>");
            paginas.put("2", "Noticias de última hora: Java es el lenguaje del mes.");
            paginas.put("3", "Gatos: Curiosidades sobre los felinos.");
            paginas.put("4", "Perros: Entendiendo el lenguaje canino.");
        }

        public void enviarMensaje(String msg) {
            if (salida != null) salida.println(msg);
        }

        public void cerrarConexion() {
            try {
                if (socket != null && !socket.isClosed()) socket.close();
                clientesActivos.remove(this);
            } catch (IOException e) { e.printStackTrace(); }
        }

        @Override
        public void run() {
            try {
                salida = new PrintWriter(socket.getOutputStream(), true);
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String peticion;
                while ((peticion = entrada.readLine()) != null) {
                    // MEJORA 3: Desconexión voluntaria del cliente
                    if (peticion.equalsIgnoreCase("SALIR")) {
                        System.out.println("Cliente " + socket.getInetAddress() + " ha cerrado sesión.");
                        break;
                    }

                    String respuesta = paginas.getOrDefault(peticion, "Error 404: Página no encontrada.");
                    salida.println(respuesta);
                }
            } catch (IOException e) {
                System.out.println("Conexión interrumpida con el cliente.");
            } finally {
                cerrarConexion();
            }
        }
    }
}