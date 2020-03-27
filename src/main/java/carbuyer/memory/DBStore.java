package carbuyer.memory;

import carbuyer.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class DBStore implements Store {

    private static final DBStore INSTANCE = new DBStore(HibernateFactory.getFactory());

    private static final Logger LOG = LogManager.getLogger(DBStore.class.getName());

    private final SessionFactory factory;

    public DBStore(SessionFactory factory) {
        this.factory = factory;
    }

    public static DBStore getInstance() {
        return INSTANCE;
    }

    @Override
    public User addUser(User user, Account account) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(account);

            user.setAccount(account);
            session.save(user);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public List<Car> getCars() {
        List<Car> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM carbuyer.models.Car").list();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Advert> findAll() {
        List<Advert> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM carbuyer.models.Advert").list();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<User> getUsers() {
        List<User> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM carbuyer.models.User").list();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Advert> getAdvertsUser(User user) {
        List<Advert> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM carbuyer.models.Advert WHERE owner_id = :id")
                    .setParameter("id", user.getId())
                    .list();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Advert> showLastDay() {
        List<Advert> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM carbuyer.models.Advert "
                    + "WHERE extract(day from created_date) > extract(day from current_date) - 1").list();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Advert> showWithPhoto() {
        List<Advert> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM carbuyer.models.Advert WHERE image_name != ''").list();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Advert> showWithSpecificMark(Mark mark) {
        List<Advert> result = new ArrayList<>();
        int findId = 0;
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Mark> list = session.createQuery("FROM carbuyer.models.Mark").list();
            for (Mark mark1 : list) {
                if (mark1.getName().equals(mark.getName())) {
                    findId = mark1.getId();
                    break;
                }
            }
            if (findId != 0) {
                Query query = session.createQuery("FROM carbuyer.models.Advert as adv "
                        + "join fetch adv.car WHERE mark_id = :id");
                query.setParameter("id", findId);
                result = query.list();
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public void updateStatus(Advert advert) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery("UPDATE carbuyer.models.Advert SET status = :done WHERE id = :id");
            query.setParameter("done", advert.isStatus());
            query.setParameter("id", advert.getId());
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void addNewAdvert(CarBody carBody, Engine engine, Transmission transmission,
                             Mark mark, Model model, User user, Car car, Advert advert) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        User owner;
        try {
            session.save(carBody);
            car.setCarBody(carBody);

            session.save(engine);
            car.setEngine(engine);

            session.save(transmission);
            car.setTransmission(transmission);

            session.save(mark);
            car.setMark(mark);

            session.save(model);
            car.setModel(model);

            session.save(car);

            Car car1 = session.get(Car.class, car.getId());

            owner = session.load(User.class, user.getId());

            advert.setCar(car1);

            advert.setOwner(owner);

            session.save(advert);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}
