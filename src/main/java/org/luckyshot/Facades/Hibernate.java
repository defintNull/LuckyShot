package org.luckyshot.Facades;

public class Hibernate {
    private static Hibernate instance;
    private SessionFacto sessionFactory;

    private Hibernate() {
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

    public static Hibernate getInstance() {
        if(instance == null) {
            instance = new Hibernate();
        }
        return instance;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
