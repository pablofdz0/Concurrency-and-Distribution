import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Semaphore tesoro = new Semaphore(1, true);
        Thread[] hilos = new Thread[9];

        String[] equipos = {"a", "b", "c"};

        for (int i = 0; i < equipos.length; i++) {
            CyclicBarrier barreraEquipo = new CyclicBarrier(3);

            for (int j = 0; j < 3; j++) {
                hilos[i * 3 + j] = new Thread(
                    new Explorador(equipos[i], j + 1, barreraEquipo, tesoro)
                );
                hilos[i * 3 + j].start();
            }
        }

        for (int i = 0; i < hilos.length; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Fin de la competición");
    }
}

class Explorador implements Runnable {
    String equipo;
    int numero;
    CyclicBarrier barrera;
    Semaphore tesoro;

    private static boolean tesoroReclamado = false;

    public Explorador(String equipo, int numero, CyclicBarrier barrera, Semaphore tesoro) {
        this.equipo = equipo;
        this.numero = numero;
        this.barrera = barrera;
        this.tesoro = tesoro;
    }

    public void run() {
        for (int punto = 1; punto <= 3; punto++) {
            try {
                System.out.println(
                    "Explorador " + numero +
                    " del equipo " + equipo +
                    " empieza a correr hacia el punto " + punto
                );

                int tiempo = 2000 + (int)(Math.random() * 3001);
                Thread.sleep(tiempo);

                System.out.println(
                    "Explorador " + numero +
                    " del equipo " + equipo +
                    " ha llegado al punto " + punto
                );

                barrera.await();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (numero == 1) {
            reclamarTesoro();
}
    }

    public void reclamarTesoro() {
        try {
            tesoro.acquire();

            if (!tesoroReclamado) {
                tesoroReclamado = true;
                System.out.println("El equipo " + equipo + " ha reclamado el tesoro!");
            } else {
                System.out.println("El equipo " + equipo + " llegó tarde. El tesoro ya fue reclamado.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            tesoro.release();
        }
    }
}