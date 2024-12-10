package org.luckyshot;

import org.hibernate.Session;
import org.luckyshot.Facades.Facade;
import org.luckyshot.Facades.HibernateService;
import org.luckyshot.Facades.LoginFacade;
import org.luckyshot.Models.User;

public class Main {
    public static void main(String[] args) {

        // Login
        LoginFacade loginFacade = LoginFacade.getInstance();
        loginFacade.start();

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