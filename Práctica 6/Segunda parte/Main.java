import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        ConcurrentLinkedQueue<Articulo> almacen = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedDeque<Articulo> pedidos = new ConcurrentLinkedDeque<>();

        HiloProduccion produccion = new HiloProduccion(almacen, pedidos);
        HiloLogistica logistica = new HiloLogistica(almacen, pedidos);

        produccion.start();
        logistica.start();
    }
}

class Articulo {
    private int id;
    private String tipo;
    private double peso;

    public Articulo(int id, String tipo, double peso) {
        this.id = id;
        this.tipo = tipo;
        this.peso = peso;
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "Articulo{id=" + id + ", tipo='" + tipo + "', peso=" + peso + "}";
    }
}

class HiloProduccion extends Thread {
    private ConcurrentLinkedQueue<Articulo> almacen;
    private ConcurrentLinkedDeque<Articulo> pedidos;
    private AtomicInteger contador = new AtomicInteger(1);

    public HiloProduccion(
            ConcurrentLinkedQueue<Articulo> almacen,
            ConcurrentLinkedDeque<Articulo> pedidos
    ) {
        this.almacen = almacen;
        this.pedidos = pedidos;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Articulo articuloPedido = pedidos.peek();

                String tipo;

                if (articuloPedido != null) {
                    tipo = articuloPedido.getTipo();
                    System.out.println("Producción consulta pedidos. Prioriza tipo: " + tipo);
                } else {
                    tipo = "General";
                    System.out.println("Producción no ve pedidos pendientes. Produce artículo general.");
                }

                Articulo nuevo = new Articulo(
                        contador.getAndIncrement(),
                        tipo,
                        Math.round((1 + Math.random() * 9) * 100.0) / 100.0
                );

                almacen.add(nuevo);

                System.out.println("Producción fabrica y añade al almacén: " + nuevo);

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                System.out.println("Producción interrumpida.");
                break;
            }
        }
    }
}

class HiloLogistica extends Thread {
    private ConcurrentLinkedQueue<Articulo> almacen;
    private ConcurrentLinkedDeque<Articulo> pedidos;
    private AtomicInteger contadorPedidos = new AtomicInteger(1);

    public HiloLogistica(
            ConcurrentLinkedQueue<Articulo> almacen,
            ConcurrentLinkedDeque<Articulo> pedidos
    ) {
        this.almacen = almacen;
        this.pedidos = pedidos;
    }

    @Override
    public void run() {
        while (true) {
            try {
                generarPedido();

                Articulo pedido = pedidos.peek();

                if (pedido != null) {
                    Articulo articuloAlmacen = buscarArticuloEnAlmacen(pedido.getTipo());

                    if (articuloAlmacen != null) {
                        pedidos.pop();

                        System.out.println(
                                "Logística prepara pedido: " + pedido +
                                " usando artículo del almacén: " + articuloAlmacen
                        );
                    } else {
                        System.out.println(
                                "Logística no encuentra artículo tipo " +
                                pedido.getTipo() +
                                " en el almacén. Pedido pendiente."
                        );
                    }
                }

                Thread.sleep(1500);

            } catch (InterruptedException e) {
                System.out.println("Logística interrumpida.");
                break;
            }
        }
    }

    private void generarPedido() {
        String[] tipos = {"General", "A", "B", "C"};

        String tipo = tipos[(int) (Math.random() * tipos.length)];

        Articulo pedido = new Articulo(
                contadorPedidos.getAndIncrement(),
                tipo,
                0
        );

        pedidos.push(pedido);

        System.out.println("Nuevo pedido recibido: " + pedido);
    }

    private Articulo buscarArticuloEnAlmacen(String tipo) {
        for (Articulo articulo : almacen) {
            if (articulo.getTipo().equals(tipo)) {
                almacen.remove(articulo);
                return articulo;
            }
        }

        return null;
    }
}