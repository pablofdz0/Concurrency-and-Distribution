import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteWeb {
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        try(Socket socket = new Socket(HOST, PUERTO); //Intentamos conectar al servidor
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);  // Para enviar datos al servidor
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Para recibir datos del servidor
            Scanner scanner = new Scanner(System.in)){  // Para leer la entrada del usuario desde la consola
            System.out.println("Conectado al servidor en " + HOST + ":" + PUERTO);
            String opcion = "";

            while(!opcion.equalsIgnoreCase("SALIR")){
                System.out.println("\n--- Menú de Páginas ---");
                System.out.println("1. index.html | 2. noticias.html | 3. gatos.html | 4. perros.html");
                System.out.println("Escriba 'SALIR' para finalizar.");
                System.out.print("Seleccione una opción: ");

                opcion = scanner.nextLine();
                salida.println(opcion); // Enviamos la opción seleccionada al servidor
                
                if(!opcion.equalsIgnoreCase("SALIR")){ // Si el usuario no ha decidido salir, esperamos la respuesta del servidor
                    String respuesta = entrada.readLine();
                    System.out.println("\n--- Respuesta del Servidor ---");
                    System.out.println(respuesta);
                }
            }
    
       }catch(IOException e){
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }

}