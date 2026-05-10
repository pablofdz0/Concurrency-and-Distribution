import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        int n = 4;

        Semaphore carril = new Semaphore(1, true);
        Semaphore[] testigos = new Semaphore[n];

        Thread[] hilos = new Thread[n];

        for (int i = 0; i < n; i++) {
            if (i == 0) {
                testigos[i] = new Semaphore(1, true); // el primero puede empezar
            } else {
                testigos[i] = new Semaphore(0, true); // los demás esperan
            }
        }

        for (int i = 0; i < n; i++) {
            hilos[i] = new Thread(new Corredor(i, carril, testigos));
            hilos[i].start();
        }

        for (int i = 0; i < n; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("La carrera ha terminado");
    }
}

class Corredor implements Runnable {
    private int numero;
    private Semaphore carril;
    private Semaphore[] testigos;

    public Corredor(int numero, Semaphore carril, Semaphore[] testigos) {
        this.numero = numero;
        this.carril = carril;
        this.testigos = testigos;
    }

    @Override
    public void run() {
        try {
            testigos[numero].acquire();

            carril.acquire();

            System.out.println("Corredor " + numero + " empieza a correr");

            int tiempo = 5000 + (int) (Math.random() * 5001);
            Thread.sleep(tiempo);

            System.out.println("Corredor " + numero + " termina de correr");

            carril.release();

            if (numero < testigos.length - 1) {
                testigos[numero + 1].release();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}