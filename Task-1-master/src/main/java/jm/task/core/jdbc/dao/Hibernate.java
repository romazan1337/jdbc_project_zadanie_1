package jm.task.core.jdbc.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;


public class Hibernate {

    private final SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(Hibernate.class);

    public Hibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void consume(Consumer<Session> consumer) {
        this.funct(session -> {
            consumer.accept(session);
            return null;
        });
    }

    public <R> R funct(Function<Session, R> function) {

        try (Session session = sessionFactory.openSession();) {
            Transaction transaction = session.beginTransaction();

            try {
                R result = function.apply(session);
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                    logger.warn("Транзакция откатана", e);
                }
                logger.error("Ошибка при выполнении операции", e);
                throw new RuntimeException("Ошибка" + e);
            }
        }
    }
    //    public void command(Consumer<Session> function) {
//        Session session = null;
//        Transaction transaction = null;
//
//        try {
//            session = SessionManager.getSessionFactory().openSession();
//            transaction = session.beginTransaction();
//            function.accept(session);
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//                //logger.info("Транзакция откатана", e);
//            }
//            //logger.error("Ошибка при выполнении операции", e);
//            throw new RuntimeException("Ошибка" + e);
//        } finally {
//            if (session != null) {
//                session.close();
//                //logger.debug("Сессия завершена");
//            }
//
//        }
//    }
}