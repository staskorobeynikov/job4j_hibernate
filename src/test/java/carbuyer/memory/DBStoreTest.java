package carbuyer.memory;

import carbuyer.models.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DBStoreTest {

    private final Store store = DBStore.getInstance();

    @Before
    public void init() {
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

        body.setType("hatchback");
        body.setColor("yellow");
        body.setCountDoor(5);

        engine.setVolume(1.5);
        engine.setPower(80);
        engine.setType("diesel");

        transmission.setGearBox("mechanic");
        transmission.setGearType("front");

        mark.setName("Renault");

        model.setName("Sandero II");

        car.setCreated(new Timestamp(1551398400000L));
        car.setMileAge(10000);

        advert.setImageName("");
        advert.setStatus(false);
        advert.setPrice(3250000);
        advert.setCreatedDate(new Timestamp(System.currentTimeMillis() - 500000));

        store.addNewAdvert(body, engine, transmission, mark, model, addUser, car, advert);
    }

    @Test
    public void whenFindAllAdvertsSizeListIsTwo() {
        List<Advert> list = store.findAll();

        assertThat(list.size(), is(2));
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
        User user = new User();
        user.setId(1);

        List<Advert> list = store.getAdvertsUser(user);

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

        assertThat(list.iterator().next().getOwner().getPhone(), is("+375295046003"));
        assertThat(list.get(0).getCar().getModel().getName(), is("Rio"));
    }

    @Test
    public void whenGetAdvertsWithSpecificMark() {
        Mark mark = new Mark();
        mark.setName("Renault");

        List<Advert> list = store.showWithSpecificMark(mark);

        assertThat(list.get(0).getCar().getCarBody().getColor(), is("yellow"));
        assertThat(list.get(0).getCar().getCarBody().getType(), is("hatchback"));
    }

    @Test
    public void whenUpdateStatusFirstAdvertThanStatusIsTrue() {
        Advert advert = new Advert();
        advert.setId(1);
        advert.setStatus(true);

        store.updateStatus(advert);

        List<Advert> list = store.findAll();

        assertThat(list.iterator().next().isStatus(), is(true));
        assertThat(list.iterator().next().getPrice(), is(250000));
        assertThat(list.get(0).getCar().getEngine().getType(), is("petrol"));
    }
}