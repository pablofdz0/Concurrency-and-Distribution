 import java.util.Scanner;

 
 public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Thread[] ths = new Thread[5];


        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            ths[i] = new Thread(new MyThread());
            ths[i].start();
        }

        for (Thread th : ths) {
            if(th != null) {
                try {
                    th.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        }

        System.out.println("Terminó");

    }

}

class MyThread implements Runnable {
    int mySum;
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            mySum += i;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Suma en el hilo: " + mySum);
    }
}