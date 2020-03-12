package carowner.services;

import carowner.models.Car;
import carowner.models.Driver;
import carowner.models.Engine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Mapping {

    private static final Logger LOG = LogManager.getLogger(Mapping.class.getName());

    private final SessionFactory factory = new Configuration()
            .configure("Owner.cfg.xml")
            .buildSessionFactory();

    Car create(Car car, Engine engine, Driver owner) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(engine);
            car.setEngine(new Engine(engine.getId()));

            Set<Driver> drivers = new HashSet<>();
            drivers.add(owner);
            car.setDrivers(drivers);

            session.save(car);

            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return car;
    }

    Car edit(int id, Driver owner) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Car car = null;
        try {
            session.save(owner);
            car = session.get(Car.class, id);
            Set<Driver> set = car.getDrivers();
            set.add(owner);
            car.setDrivers(set);
            session.saveOrUpdate(car);
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return car;
    }

    Car delete(int id) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Car car = new Car();
        try {
            car = session.get(Car.class, id);
            session.delete(car);
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return car;
    }

    Car findById(int id) {
        Car car = null;
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
           car = session.get(Car.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return car;
    }
}
