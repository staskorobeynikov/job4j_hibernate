package carbuyer.memory;

import carbuyer.models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DBStoreTest {

    private SessionFactory factory;

    private Session session;

    private Store store;

    @Before
    public void init() {
        factory = ConnectionRollback.create(HibernateFactory.FACTORY);
        session = factory.openSession();
        store = new DBStore(factory);

        CarBody body = new CarBody();
        body.setType("sedan");
        body.setColor("black");
        body.setCountDoor(5);

        Engine engine = new Engine();
        engine.setVolume(1.6);
        engine.setType("petrol");
        engine.setPower(85);

        Transmission transmission = new Transmission();
        transmission.setGearBox("automatic");
        transmission.setGearType("rear");

        Mark mark = new Mark();
        mark.setName("Kia");

        Model model = new Model();
        model.setName("Rio");

        Car car = new Car();
        car.setCreated(new Timestamp(1425168000000L));
        car.setMileAge(50000);

        User user = new User();
        user.setName("Stas");
        user.setPhone("+375295046003");
        user.setAddress("Slutsk");

        Account account = new Account();
        account.setLogin("root");
        account.setPassword("root");

        Advert advert = new Advert();
        advert.setImageName("image1.png");
        advert.setStatus(false);
        advert.setPrice(250000);
        advert.setCreatedDate(new Timestamp(System.currentTimeMillis() - 500000));

        User addUser = store.addUser(user, account);
        store.addNewAdvert(body, engine, transmission, mark, model, addUser, car, advert);

        CarBody body1 = new CarBody();
        body1.setType("hatchback");
        body1.setColor("yellow");
        body1.setCountDoor(5);

        Engine engine1 = new Engine();
        engine1.setVolume(1.5);
        engine1.setPower(80);
        engine1.setType("diesel");

        Transmission transmission1 = new Transmission();
        transmission1.setGearBox("mechanic");
        transmission1.setGearType("front");

        Mark mark1 = new Mark();
        mark1.setName("Renault");

        Model model1 = new Model();
        model1.setName("Sandero II");

        Car car1 = new Car();
        car1.setCreated(new Timestamp(1551398400000L));
        car1.setMileAge(10000);

        Advert advert1 = new Advert();
        advert1.setImageName("");
        advert1.setStatus(false);
        advert1.setPrice(3250000);
        advert1.setCreatedDate(new Timestamp(System.currentTimeMillis() - 500000));

        store.addNewAdvert(body1, engine1, transmission1, mark1, model1, addUser, car1, advert1);
    }

    @After
    public void destroy() {
        session.clear();
        factory.close();
    }

    @Test
    public void whenFindAllAdvertsSizeListIsTwo() {
        List<Advert> list = store.findAll();
        assertThat(list.size(), is(2));
        assertThat(list.get(1).getCar().getMark().getName(), is("Renault"));
        assertThat(list.iterator().next().getPrice(), is(250000));
    }

    @Test
    public void whenGetCarsThanFirstCarNameIsKiaSecondCarEngineIsDiesel() {
        List<Car> list = store.getCars();

        assertThat(list.iterator().next().getMark().getName(), is("Kia"));
        assertThat(list.get(1).getEngine().getType(), is("diesel"));
    }

    @Test
    public void whenGetUsersThanNameAndPhoneIsCorrect() {
        List<User> list = store.getUsers();

        assertThat(list.iterator().next().getName(), is("Stas"));
        assertThat(list.get(0).getPhone(), is("+375295046003"));
    }

    @Test
    public void whenGetAllAdvertsForUserThanListSizeIsTwoImageAndModelCarIsCorrect() {
        int id = store.findAll().get(1).getOwner().getId();
        User user = new User();
        user.setId(id);

        List<Advert> list = store.getAdvertsUser(user);

        assertThat(list.size(), is(2));
        assertThat(list.iterator().next().getImageName(), is("image1.png"));
        assertThat(list.get(1).getCar().getModel().getName(), is("Sandero II"));
    }

    @Test
    public void whenGetAdvertsLastDay() {
        List<Advert> list = store.showLastDay();

        assertThat(list.iterator().next().getCar().getCarBody().getType(), is("sedan"));
        assertThat(list.get(1).getCar().getMileAge(), is(10000));
        assertThat(list.get(1).getCar().getTransmission().getGearBox(), is("mechanic"));
    }

    @Test
    public void whenGetAdvertsWithPhoto() {
        List<Advert> list = store.showWithPhoto();

        assertThat(list.size(), is(1));
        assertThat(list.iterator().next().getOwner().getPhone(), is("+375295046003"));
        assertThat(list.get(0).getCar().getModel().getName(), is("Rio"));
    }

    @Test
    public void whenGetAdvertsWithSpecificMark() {
        Mark mark = new Mark();
        mark.setName("Renault");

        List<Advert> list = store.showWithSpecificMark(mark);

        assertThat(list.size(), is(1));
        assertThat(list.get(0).getCar().getCarBody().getColor(), is("yellow"));
        assertThat(list.get(0).getCar().getCarBody().getType(), is("hatchback"));
    }

    @Test
    public void whenUpdateStatusFirstAdvertThanStatusIsTrue() {
        int id = store.findAll().get(0).getId();
        Advert advert = new Advert();
        advert.setId(id);
        advert.setStatus(true);

        store.updateStatus(advert);

        List<Advert> list = store.findAll();

        assertThat(list.size(), is(2));

    }
}