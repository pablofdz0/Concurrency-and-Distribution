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

        System.out.println("dame un numero");
        
        int m = sc.nextInt();

        ths[m].interrupt();


        for (int i = 0; i < n; i++) {
            try {
                ths[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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