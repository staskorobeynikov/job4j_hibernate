package carbuyer.memory;

import carbuyer.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DBStoreTest {

    private Store store = DBStore.getInstance();

    @Before
    public void init() {
        CarBody body = new CarBody("sedan", "black", 5);
        Engine engine = new Engine(1.6, 85, "petrol");
        Transmission transmission = new Transmission("automatic", "rear");
        Mark mark = new Mark("Kia");
        Model model = new Model("Rio");
        Car car = new Car(50000, new Timestamp(1425168000000L));
        User user = new User("Stas", "+375295046003", "Slutsk");
        Account account = new Account("root", "root");

        Advert advert = new Advert(
                250000,
                false,
                new Timestamp(System.currentTimeMillis() - 500000),
                "image1.png"
        );

        User addUser = store.addUser(user, account);
        store.addNewAdvert(body, engine, transmission, mark, model, addUser, car, advert);

        CarBody carBody = new CarBody("hatchback", "yellow", 5);
        Engine eSecond = new Engine(1.5, 80, "diesel");
        Transmission tSecond = new Transmission("mechanic", "front");
        Mark mSecond = new Mark("Renault");
        Model modSecond = new Model("Sandero II");
        Car cSecond = new Car(10000, new Timestamp(1551398400000L));


        Advert aSecond = new Advert(
                3250000,
                false,
                new Timestamp(System.currentTimeMillis() - 500000),
                ""
        );

        store.addNewAdvert(carBody, eSecond, tSecond, mSecond, modSecond, addUser, cSecond, aSecond);
    }

    @After
    public void destroy() {
        store.deleteAll();
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