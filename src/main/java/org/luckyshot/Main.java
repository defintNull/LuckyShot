package org.luckyshot;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.luckyshot.Facades.HibernateDB;
import org.luckyshot.Models.Prova;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Facade facade = Facade.getInstance();
        //facade.menu();

        HibernateDB hibernatedb = HibernateDB.getInstance();
        Prova prova = new Prova("prova", 456);

        Session session = hibernatedb.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();

            session.persist(prova);

            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        session = hibernatedb.getSessionFactory().openSession();
        Prova prova2 = session.find(Prova.class, 1);
        session.close();

        System.out.println(prova2.getString());
        Scanner scanner = new Scanner(System.in);
        int s = scanner.nextInt();
    }
}