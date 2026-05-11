import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorWeb {
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        Map<String, String> paginas = new HashMap<>();
        paginas.put("1", "<html><body><h1>Index</h1><p>Bienvenido al simulador.</p></body></html>");
        paginas.put("2", "Noticias: Java es el lenguaje más usado en la práctica.");
        paginas.put("3", "Gatos: Curiosidades sobre los felinos.");
        paginas.put("4", "Perros: Entendiendo el lenguaje canino.");
        
        try{ServerSocket servidor = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado en el puerto " + PUERTO);
            System.out.println("Esperando a un cliente...");

            while(true){
                try(Socket cliente = servidor.accept()){
                    PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                    System.out.println("Cliente conectado: " + cliente.getInetAddress());

                    String mensaje;

                    while((mensaje = entrada.readLine()) != null){
                        if(mensaje.equalsIgnoreCase("SALIR")){
                            System.out.println("El cliente ha solicitado cerrar la conexión.");
                            break;
                        }

                        String contenido = paginas.getOrDefault(mensaje, "Página no encontrada.");
                        salida.println(contenido);
                    }

                    System.out.println("Cliente desconectado: " + cliente.getInetAddress());
                }catch(IOException e){
                    System.err.println("Error al manejar la conexión del cliente: " + e.getMessage());
                }
            }



        }catch(IOException e){
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }




    }

    
}