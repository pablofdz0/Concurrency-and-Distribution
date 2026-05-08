import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;




public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter();
        Thread[] threads = new Thread[200];

        for (int i = 0; i < 200; i++) {
            Thread thread = new Thread(new MyThread(counter));
            threads[i] = thread;
            thread.start();
        }


        for(Thread thread : threads){
            if(thread != null){
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }s

        System.out.println("Contador final: " + counter.getCount());
    }
}


class MyThread implements Runnable{
    Counter counter;
    Random random;

    public MyThread(Counter counter){
        this.counter = counter;
        this.random = new Random();
    }

    @Override
    public void run(){
        try{
            System.out.println("Hilo " + Thread.currentThread().getName() + " iniciado.");
            Thread.sleep(random.nextInt(100));


            //Tambien podemos usar el lock para evitar condiciones de carrera
            // synchronized (counter) { // añadiendo synchronized para evitar condiciones de carrera (1ª forma)
                counter.increment();
            // }

            System.out.println("Hilo " + Thread.currentThread().getName() + " ha terminado");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Counter{
    public int count; // Utilizando AtomicInteger para evitar condición de carrera (hay que cambiar los metodos)
    Lock lock = new ReentrantLock(); // Utilizando Lock para evitar condición de carrera (hay que cambiar los metodos)

    public Counter(){
        count = 0;
        
    }

    public void increment(){ //añadiendo synchronized para evitar condiciones de carrera (2ª forma)
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
        
    }

    public int getCount(){ //añadiendo synchronized para evitar condiciones de carrera (2ª forma)
        return count;
    }

}