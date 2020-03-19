package carbuyer.memory;

import carbuyer.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBStore implements Store {

    private static final DBStore INSTANCE = new DBStore();

    private static final Logger LOG = LogManager.getLogger(DBStore.class.getName());

    private final SessionFactory factory = new Configuration()
            .configure("CarBuyer.cfg.xml")
            .buildSessionFactory();

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

            session.getTransaction().commit();
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
    public List<Advert> findAll() {
        List<Advert> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM carbuyer.models.Advert").list();
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
    public List<User> getUsers() {
        List<User> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM carbuyer.models.User").list();
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
    public List<Advert> getAdvertsUser(User user) {
        List<Advert> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM carbuyer.models.Advert WHERE owner_id = :id")
                    .setParameter("id", user.getId())
                    .list();
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
    public List<Advert> showLastDay() {
        List<Advert> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM carbuyer.models.Advert "
                    + "WHERE extract(day from created_date) > extract(day from current_date) - 1").list();
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
    public List<Advert> showWithPhoto() {
        List<Advert> result = new ArrayList<>();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = session.createQuery("FROM carbuyer.models.Advert WHERE image_name != ''").list();
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
    public void updateStatus(Advert advert) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery("UPDATE carbuyer.models.Advert SET status = :status1 WHERE id = :id");
            query.setParameter("status1", advert.isStatus());
            query.setParameter("id", advert.getId());
            query.executeUpdate();
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
            car.setCarBody(new CarBody(carBody.getId()));

            session.save(engine);
            car.setEngine(new Engine(engine.getId()));

            session.save(transmission);
            car.setTransmission(new Transmission(transmission.getId()));

            session.save(mark);
            car.setMark(new Mark(mark.getId()));

            session.save(model);
            car.setModel(new Model(model.getId()));

            session.save(car);

            Car car1 = session.get(Car.class, car.getId());

            owner = session.load(User.class, user.getId());

            advert.setCar(car1);
            advert.setOwner(owner);
            session.save(advert);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            transaction.rollback();
        } finally {
            session.close();
        }
    }
}
