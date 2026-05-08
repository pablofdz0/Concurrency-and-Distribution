import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


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
        }

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
    public AtomicInteger count;

    public Counter(){
        count = new AtomicInteger(0);
    }

    public void increment(){ //añadiendo synchronized para evitar condiciones de carrera (2ª forma)
        count.incrementAndGet();
    }

    public int getCount(){ //añadiendo synchronized para evitar condiciones de carrera (2ª forma)
        return count.get();
    }

}