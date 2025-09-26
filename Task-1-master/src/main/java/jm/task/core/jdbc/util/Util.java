package jm.task.core.jdbc.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.Properties;

public final class Util {

    private static final Properties PROPERTIES = new Properties();
    private static SessionFactory sessionFactory;

    private Util() {
    }

    static {
        loadProperties("hibernate.properties", PROPERTIES);
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties(String filename, Properties prop) {
        try (var inputStream =
                     Util.class.getClassLoader().getResourceAsStream(filename)) {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.setProperties(PROPERTIES);
            configuration.addAnnotatedClass(jm.task.core.jdbc.model.User.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

    public static void testConnection() {
        try (Session session = getSessionFactory().openSession()) {
            System.out.println("успех");
        } catch (Exception e) {
            System.err.println("лох" + e.getMessage());
        }
    }
}
