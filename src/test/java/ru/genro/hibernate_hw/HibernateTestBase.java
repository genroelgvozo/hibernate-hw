package ru.genro.hibernate_hw;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.junit.After;
import ru.genro.hibernate_hw.hibernateBase.HibernateConfigFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class HibernateTestBase {

    protected static final SessionFactory sessionFactory = getSessionFactory();

    private static SessionFactory getSessionFactory() {
        return hibernateTestConfig().buildSessionFactory();
    }

    private static Configuration hibernateTestConfig() {
        return HibernateConfigFactory.prod()
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:test");
    }

    @After
    public void hibernateTestBaseTearDown() throws Exception {
        cleanTables();
    }

    private static void cleanTables() {

        Set<String> tablesNames = sessionFactory.getAllClassMetadata().values().stream()
                .map(classMetadata -> ((AbstractEntityPersister) classMetadata).getTableName())
                .collect(Collectors.toSet());

        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            // too primitive: will not work if tables have FKs
            tablesNames.stream()
                    .forEach(tableName -> session.createSQLQuery("DELETE FROM " + tableName).executeUpdate());
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
