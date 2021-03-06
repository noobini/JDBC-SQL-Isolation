/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package non_repeatable_read;

import connection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class T1 extends Thread{
    
    private String name;
    private int isolationLevel;
    
    // semaforo sincronizar  T1
    private Semaphore semT1;
    
    // semaforo para sincronizar com T2
    private Semaphore semT2;
    
    
    
    public T1(int isolationLevel,Semaphore sem)
    {
        this.isolationLevel=isolationLevel;
        this.name="Transaction 1";
        this.semT2 = sem;
    }
    
    @Override
    public void run()
    {
        
        try {

            // criar a conexão
            Connection conn = DBConnection.getSQLConnection();

            conn.setTransactionIsolation(isolationLevel);

            conn.setAutoCommit(false);
            
            // lê X
            String sql = "select Number from consistencia where Id=1";
            ResultSet result = conn.createStatement().executeQuery(sql);
            int number=-1;
            if (result.next())
                number = result.getInt(1);
            
            System.out.println("Thread " + name + ": leu " + (number==-1?"erro":number));
            
            //up semaforo
            semT2.release();
            
            //bloqueia a espera do input do utilizador
            System.out.println("Thread " + name + ": bloqueada a espera de input para fazer a segunda leitura");
            Scanner sc = new Scanner(System.in);
            sc.nextLine();
            
            // lê X
            String sql2 = "select Number from consistencia where Id=1";
            ResultSet result2 = conn.createStatement().executeQuery(sql);
            number=-1;
            if (result2.next())
                number = result2.getInt(1);
            
            System.out.println("Thread " + name + ": leu " + (number==-1?"erro":number));
            
            conn.commit();
            System.out.println("Thread " + name + ": Commit");
            
            
            conn.close();
        } catch (Exception ex) {
            
            Logger.getLogger(dirty_read.T1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
