package main;


import java.sql.Connection;
import java.util.concurrent.Semaphore;
import dirty_read.T2;
import dirty_read.T1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */
public class Dirty_Read_Demo {

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        
        
        // semaforo partilhado pelas threads para sincronização inicial
        Semaphore semT1 = new Semaphore(0);
        // semaforo partilhado pelas threads para sincronização inicial
        Semaphore semT2 = new Semaphore(0);
        
        
        //transação T2
        T2 t2 = new T2(Connection.TRANSACTION_SERIALIZABLE,semT2);
        
        //transação T1
        T1 t1 = new T1(Connection.TRANSACTION_READ_UNCOMMITTED, semT2);
        
        //inicio das transações em condiçao de corrida
        t1.start();
        t2.start();
   
    }
    
}
