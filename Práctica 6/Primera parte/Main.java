public class Main {
    public static void main(String[] args) {
        Barra barra = new Barra(40);

        Thread chefThread = new Thread(new Chef(barra));
        Thread camareroThread = new Thread(new Camarero(barra));

        chefThread.start();
        camareroThread.start();
    }
}

class Barra {
    private int platosBarra = 0;
    private int platosTotales;
    private final int CAPACIDAD = 5;

    public Barra(int platosTotales) {
        this.platosTotales = platosTotales;
    }

    public synchronized boolean ponerPlato() {
        while (platosBarra == CAPACIDAD && platosTotales > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (platosTotales == 0) {
            notifyAll();
            return false;
        }

        platosBarra++;
        platosTotales--;

        notifyAll();
        return true;
    }

    public synchronized boolean quitarPlato() {
        while (platosBarra == 0 && platosTotales > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (platosBarra == 0 && platosTotales == 0) {
            notifyAll();
            return false;
        }

        platosBarra--;

        notifyAll();
        return true;
    }

    public synchronized int getPlatosBarra() {
        return platosBarra;
    }

    public synchronized int getPlatosTotales() {
        return platosTotales;
    }
}


class Chef implements Runnable {
    private Barra barra;

    public Chef(Barra barra) {
        this.barra = barra;
    }

    @Override
    public void run() {
        while (barra.ponerPlato()) {
            System.out.println(
                "Chef ha puesto un plato. En barra: "
                + barra.getPlatosBarra()
                + ". Faltan por cocinar: "
                + barra.getPlatosTotales()
            );
        }

        System.out.println("Chef terminó su jornada");
    }
}

class Camarero implements Runnable {
    private Barra barra;

    public Camarero(Barra barra) {
        this.barra = barra;
    }

    @Override
    public void run() {
        while (barra.quitarPlato()) {
            System.out.println(
                "Camarero ha quitado un plato. En barra: "
                + barra.getPlatosBarra()
            );
        }

        System.out.println("Camarero terminó su jornada");
    }
}