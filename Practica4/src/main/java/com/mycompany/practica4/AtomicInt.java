/*
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 

package com.mycompany.practica4;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

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


class MyTask implements Runnable{
    int id;
    Random rand = new Random();
    int time = rand.nextInt(101);
    
    Counter c;
    Object obj = new Object();
    
    public MyTask(int id, Counter c){
        this.id = id;
        this.c = c;
    }

    @Override
    public void run() {
        
        System.out.println("Hello world, I'm the java thread number " + id);
        try {
            Thread.sleep(time);
            c.increment();
            
        } catch (InterruptedException ex) {
            System.getLogger(MyTask.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        System.out.println("Bye from thread number " + id);
        
    }
    
}


class Counter{
    AtomicInteger n; //Usamos AtomicInteger de forma que los hilos no pueden acceder de forma directa a la variable
    
    public Counter(){
        this.n = new AtomicInteger(0);
    }

    public int getN(){
        return n.get(); //Usamos los metodos de AtomicInteger
    }
    
    public void increment(){
        n.addAndGet(1); //Usamos metodos de AtomicInteger
    }
}
*/