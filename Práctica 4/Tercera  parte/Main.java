import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter();
        Lock lock = new ReentrantLock();

        Thread hilo1 = new Thread(new MyThread(counter, lock));
        Thread hilo2 = new Thread(new MyThread(counter, lock));

        hilo1.start();
        hilo2.start();


       
        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            

        System.out.println("Contador final: " + counter.getCount());
    }
}

class MyThread implements Runnable{
    Counter counter;
    Lock lock;

    public MyThread(Counter counter, Lock lock){
        this.counter = counter;
        this.lock = lock;
    }

    @Override
    public void run(){
        System.out.println("Hilo " + Thread.currentThread().getName() + " iniciado.");
        
        for(int i = 0; i < 1000; i++){
            try{
                
                lock.lock();
                try {
                    
                    counter.increment(); 
                    System.out.println("Incrementeado a: " + counter.getCount());
                    Thread.sleep(100);
                    counter.decrement();
                    System.out.println("Decrementado a: " + counter.getCount());
                } finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Hilo " + Thread.currentThread().getName() + " ha terminado");
    }
}

class Counter{
    private int count;

    public Counter(){
        this.count = 0;
    }

    public void increment(){
        count++;
    }

    public void decrement(){
        count--;
    }

    public synchronized int getCount(){
        return count;
    }
}