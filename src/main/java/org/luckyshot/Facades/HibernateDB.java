package org.luckyshot.Facades;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;
import org.luckyshot.Models.Prova;

public class HibernateDB {
    private static HibernateDB instance;
    private SessionFactory sessionFactory;

    private HibernateDB() {
        this.sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                //Annotated classes
                //.addAnnotatedClass(User.class)
                .addAnnotatedClass(Prova.class)
                //Auto table generator
                .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, Action.SPEC_ACTION_DROP_AND_CREATE)
                //Build
                .buildSessionFactory();
    }

    public static HibernateDB getInstance() {
        if(instance == null) {
            instance = new HibernateDB();
        }
        return instance;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
