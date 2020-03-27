package carbuyer.memory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.lang.reflect.Proxy;

import static org.mockito.Mockito.mock;

public class ConnectionRollback {

    public static SessionFactory create(SessionFactory factory) {
        Session session = factory.openSession();
        session.beginTransaction();
        return (SessionFactory) Proxy.newProxyInstance(
                ConnectionRollback.class.getClassLoader(),
                new Class[] {
                        SessionFactory.class
                },
                (proxy, method, args) -> {
                    Object result = null;
                    if ("openSession".equals(method.getName())) {
                        result = create(session);
                    } else if ("close".equals(method.getName())) {
                        session.getTransaction().rollback();
                        session.close();
                    } else {
                        result = method.invoke(factory, args);
                    }
                    return result;
                }
        );
    }

    public static Session create(Session session) {
        return (Session) Proxy.newProxyInstance(
                ConnectionRollback.class.getClassLoader(),
                new Class[] {
                        Session.class
                },
                (proxy, method, args) -> {
                    Object result;
                    if ("beginTransaction".equals(method.getName())) {
                        result = mock(Transaction.class);
                    } else if ("close".equals(method.getName())) {
                        result = null;
                    } else if ("clear".equals(method.getName())) {
                        session.getTransaction().rollback();
                        result = null;
                    } else {
                        result = method.invoke(session, args);
                    }
                    return result;
                }
        );
    }
}
