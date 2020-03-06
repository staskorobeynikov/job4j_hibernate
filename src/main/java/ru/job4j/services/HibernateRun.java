package ru.job4j.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.job4j.models.User;

import java.sql.Timestamp;
import java.util.List;

public class HibernateRun {

    private final SessionFactory factory = new Configuration()
            .configure()
            .buildSessionFactory();

    void createUser(User user) {
        Session session = factory.openSession();
        session.beginTransaction();

        session.save(user);

        session.getTransaction().commit();
        session.close();
    }

    User findUser(User user) {
        Session session = factory.openSession();
        session.beginTransaction();

        User result = session.get(User.class, user.getId());

        session.getTransaction().commit();
        session.close();
        return result;
    }

    void updateUser(User user) {
        Session session = factory.openSession();
        session.beginTransaction();

        session.update(user);

        session.getTransaction().commit();
        session.close();
    }

    void deleteUser(User user) {
        Session session = factory.openSession();
        session.beginTransaction();

        session.delete(user);

        session.getTransaction().commit();
        session.close();
    }

    void finish() {
        factory.close();
    }

    List<User> findAllUser() {
        SessionFactory factory = new Configuration()
                .configure()
                .buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();

        List<User> result = session.createQuery("from ru.job4j.models.User").list();

        session.getTransaction().commit();
        session.close();
        return result;
    }

    public static void main(String[] args) {
        HibernateRun hibernateRun = new HibernateRun();
        User user = new User();
        user.setId(1);
        user.setName("Stas");
        user.setExpired(new Timestamp(System.currentTimeMillis()));
        hibernateRun.createUser(user);

        System.out.println(hibernateRun.findUser(user));

        User user1 = new User();
        user1.setId(1);
        user1.setName("Petr");
        user1.setExpired(new Timestamp(System.currentTimeMillis()));
        hibernateRun.updateUser(user1);

        System.out.println(hibernateRun.findUser(user));

        User user2 = new User();
        user2.setId(1);
        hibernateRun.deleteUser(user2);

        System.out.println(hibernateRun.findUser(user));

        hibernateRun.createUser(new User(1, "Stas", new Timestamp(System.currentTimeMillis())));
        hibernateRun.createUser(new User(2, "Petr", new Timestamp(System.currentTimeMillis())));
        hibernateRun.createUser(new User(3, "Andrei", new Timestamp(System.currentTimeMillis())));
        for (User find : hibernateRun.findAllUser()) {
            System.out.println(find);
        }

        hibernateRun.finish();
    }
}
