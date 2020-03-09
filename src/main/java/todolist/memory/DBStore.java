package todolist.memory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import todolist.models.Item;

import java.util.ArrayList;
import java.util.List;

public class DBStore implements Store {

    private static final DBStore INSTANCE = new DBStore();

    private static final Logger LOG = LogManager.getLogger(DBStore.class.getName());

    private final SessionFactory factory = new Configuration()
            .configure("Items.cfg.xml")
            .buildSessionFactory();

    public static DBStore getInstance() {
        return INSTANCE;
    }

    @Override
    public void addItem(Item item) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void updateItem(Item item) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery("UPDATE todolist.models.Item SET done = :done1 where id = :id");
            query.setParameter("done1", item.isDone());
            query.setParameter("id", item.getId());
            query.executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Item> findAll() {
        List<Item> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM todolist.models.Item ORDER BY id").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Item> showFilterItems() {
        List<Item> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM todolist.models.Item WHERE done = false ORDER BY id").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
}
