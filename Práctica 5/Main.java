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