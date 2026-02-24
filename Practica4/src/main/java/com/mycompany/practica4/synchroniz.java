/*

package com.mycompany.practica4;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Estudante
 
public class Practica4 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Counter contador = new Counter();
        List<Thread> hilos = new ArrayList();
        
        System.out.println("Introduce numero de hilos: ");
        int m = sc.nextInt();
        
        for(int i = 0; i < m; i++){
            Thread hilo = new Thread(new MyTask(i, contador));
            hilos.add(hilo);
            hilo.start();
        }
        
        for(Thread t: hilos){
            try{
                t.join();
            }catch(InterruptedException e){
                System.err.println("Error");
            }
        }
        
        
        System.out.println("El resultado es: " + contador.getN());
        
    }
    
    
}


//HAY TRES FORMAS, CON SYNCHRONIZED SOBRE OBJ, SOBRE LA CLASE O SOBRE LOS METODOS

class MyTask implements Runnable{
    int id;
    Random rand = new Random();
    int time = rand.nextInt(101);
    Counter c;
    Object obj = new Object();  //Podemos usar el Obj, o sin él podemos hacerlo directamente sobre el contador
    
    public MyTask(int id, Counter c){
        this.id = id;
        this.c = c;
    }

    @Override
    public synchronized void run() {
        
        System.out.println("Hello world, I'm the java thread number " + id);
        try {
            Thread.sleep(time);
            synchronized (obj) { // Para hacerlo sobre el contador (válido) hacemos synchronized (c)
                c.increment();
            }
        } catch (InterruptedException ex) {
            System.getLogger(MyTask.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        System.out.println("Bye from thread number " + id);
        
    }
    
}


class Counter{
    int n;
     
    public Counter(){
        this.n = 0;
    }
    
    public int getN(){ //Tambien aqui el synchronized si se llama desde otra clase
        return this.n;
    }
    
    public void increment(){  // Para hacerlo sobre el método añadimos antes de void 'synchronized'
        this.n++;
    }
}

*/