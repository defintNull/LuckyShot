package org.luckyshot;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.luckyshot.Facades.Facade;
import org.luckyshot.Facades.HibernateDB;
import org.luckyshot.Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Facade facade = Facade.getInstance();
        facade.menu();

//        HibernateDB hibernatedb = HibernateDB.getInstance();
//        User user;
//
//        Session session = hibernatedb.getSessionFactory().openSession();
//        Transaction transaction = null;
//        try {
//            transaction = session.getTransaction();
//            transaction.begin();
//
//            user = session.find(User.class, 1);
////
//            if (user != null) {
//                System.out.println("Utente trovato: " + user.getUsername());
//            } else {
//                System.out.println("Nessun utente trovato con ID: " + 1);
//            }
//
//            //session.persist(user);
//
//            transaction.commit();
//        } catch (Exception e) {
//            if(transaction != null) {
//                transaction.rollback();
//            }
//            System.out.println("ERROREEEEEE");
//            e.printStackTrace();
//        } finally {
//            session.close();
//        }

//        Scanner scanner = new Scanner(System.in);
//        int s = scanner.nextInt();
    }
}