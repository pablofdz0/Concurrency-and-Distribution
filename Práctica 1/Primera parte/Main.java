import java.util.Scanner;

public class Main { // Clase con Mayúscula
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("¿Cuántos hilos quieres lanzar? (máx 10): ");
        int n = scanner.nextInt();
        
        // Limitamos n para no salirnos del array
        if (n > 10) n = 10;

        Thread[] threads = new Thread[10];

        // Creación e inicio
        for (int i = 0; i < n; i++) {
            Thread thread2 = new Thread(new myRunnable());
            threads[i] = thread2;
            thread2.start();
        }

        // Espera (Join)
        for (Thread thread : threads) {
            // IMPORTANTE: Solo hacemos join si el espacio del array no es null
            if (thread != null) { 
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Todos los hilos han terminado.");
        scanner.close(); // Buena práctica cerrar el scanner
    }
}

class myRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Hola desde el runnable - Hilo ID: " + Thread.currentThread().getId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
