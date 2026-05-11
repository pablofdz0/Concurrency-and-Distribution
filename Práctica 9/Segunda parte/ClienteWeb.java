import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteWeb {
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PUERTO);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Conectado al servidor en " + HOST + ":" + PUERTO);

            // Hilo secundario: Escucha mensajes asíncronos del servidor (Mejora 2)
            Thread escucharServidor = new Thread(() -> {
                try {
                    String respuesta;
                    while ((respuesta = entrada.readLine()) != null) {
                        if (respuesta.startsWith("SERVIDOR_CIERRE:")) {
                            System.out.println("\n--- AVISO ---");
                            System.out.println(respuesta);
                            System.out.println("Cerrando aplicación...");
                            System.exit(0);
                        }
                        System.out.println("\n--- RESPUESTA DEL SERVIDOR ---");
                        System.out.println(respuesta);
                        System.out.println("------------------------------\n");
                    }
                } catch (IOException e) {
                    System.out.println("\nConexión con el servidor terminada.");
                }
            });
            escucharServidor.setDaemon(true); 
            escucharServidor.start();

            // Hilo principal: Interacción con el usuario
            String opcion = "";
            while (!opcion.equalsIgnoreCase("SALIR")) {
                mostrarMenu();
                opcion = sc.nextLine();

                if (!opcion.isEmpty()) {
                    salida.println(opcion);
                }
                
                // Pequeña espera para no solapar el menú con la respuesta del hilo de escucha
                Thread.sleep(300);
            }

        } catch (Exception e) {
            System.err.println("No se pudo conectar con el servidor: " + e.getMessage());
        }
    }

    private static void mostrarMenu() {
        System.out.println("Menú de Páginas Disponibles:");
        System.out.println("1. index.html | 2. ver-noticias.html | 3. gatos.html | 4. perros.html");
        System.out.println("Escriba 'SALIR' para desconectarse.");
        System.out.print("Selección > ");
    }
}