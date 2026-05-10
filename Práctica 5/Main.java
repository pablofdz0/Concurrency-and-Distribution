
import java.util.concurrent.Semaphore;

public class Main{
    public static void main(String[] args) {
        Thread[] empleados = new Thread[20];
        Caja caja = new Caja();
        Semaphore semaphore = new Semaphore(5);

        for (int i = 0; i < empleados.length; i++) {
            empleados[i] = new Thread(new Empleado(caja, semaphore));
            empleados[i].start();
        }

        for (Thread empleado : empleados) {
            try {
                empleado.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Todos los hilos han terminado.");
    }
}

class Caja{
    int ventasTotales = 20;
    Semaphore semaphore = new Semaphore(1);

    public  void ventas() {
        try {
            semaphore.acquire();
            if(ventasTotales > 0) {
                System.out.println("Soy el hilo " + Thread.currentThread().getName() + " y estoy vendiendo un producto.");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ventasTotales--;
                System.out.println("Soy el hilo " + Thread.currentThread().getName() + " y dejo la caja con " + ventasTotales + " ventas restantes.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            semaphore.release();
        }
       
    }

    public  int getVentasTotales() {
        return ventasTotales;
    }
}

class Empleado implements Runnable{
    Caja caja;
    Semaphore semaphore;
    int ventas;

    public Empleado(Caja caja, Semaphore semaphore) {
        this.caja = caja;
        this.semaphore = semaphore;
        ventas = 0;
    }

    @Override
    public void run() {
        System.out.println("Soy el hilo " + Thread.currentThread().getName() + " y estoy esperando para vender.");
        while (caja.getVentasTotales() > 0 && ventas < 3) {
            try {
                semaphore.acquire();
                caja.ventas();
                ventas++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
            
        }
        System.out.println("Soy el hilo " + Thread.currentThread().getName() + " y he realizado " + ventas + " ventas.");
    }
}
