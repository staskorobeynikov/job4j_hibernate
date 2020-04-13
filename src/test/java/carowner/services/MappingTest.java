package carowner.services;

import carowner.models.Car;
import carowner.models.Driver;
import carowner.models.Engine;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
@Ignore
public class MappingTest {

    @Test
    public void whenCreateNewCarWithOwnerAndEngine() {
        Mapping mapping = new Mapping();

        Car car = new Car();
        car.setCreated(new Timestamp(System.currentTimeMillis()));
        car.setModel("Opel");
        car.setMileAge(10000);

        Engine engine = new Engine();
        engine.setType("petrol");
        engine.setVolume(2.0);

        Driver driver = new Driver();
        driver.setName("Stas");
        driver.setPhone("+375295046003");

        Car result = mapping.create(car, engine, driver);
        boolean check = new ArrayList<>(result.getDrivers()).get(0).getPhone().equals(driver.getPhone());

        assertThat(result.getModel(), is(car.getModel()));
        assertThat(check, is(true));
    }

    @Test
    public void whenUpdateCarThenCarHasTwoOwnersInHistory() {
        Mapping mapping = new Mapping();

        Car car = new Car();
        car.setCreated(new Timestamp(System.currentTimeMillis()));
        car.setModel("Opel");
        car.setMileAge(10000);

        Engine engine = new Engine();
        engine.setType("petrol");
        engine.setVolume(2.0);

        Driver first = new Driver();
        first.setName("Stas");
        first.setPhone("+375295046003");
        Driver second = new Driver();
        second.setName("Petr");
        second.setPhone("+77777777777");

        Car createCar = mapping.create(car, engine, first);

        Car updateCar = mapping.edit(createCar.getId(), second);

        List<Driver> result = new ArrayList<>(updateCar.getDrivers());

        assertThat(result.size(), is(2));
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));
    }

    @Test
    public void whenCreateAndDeleteCarThenReturnNull() {
        Mapping mapping = new Mapping();

        Car car = new Car();
        car.setCreated(new Timestamp(System.currentTimeMillis()));
        car.setModel("Opel");
        car.setMileAge(10000);

        Engine engine = new Engine();
        engine.setType("petrol");
        engine.setVolume(2.0);

        Driver first = new Driver();
        first.setName("Stas");
        first.setPhone("+375295046003");
        Driver second = new Driver();
        second.setName("Petr");
        second.setPhone("+77777777777");

        Car createCar = mapping.create(car, engine, first);

        mapping.delete(createCar.getId());
        Car result = mapping.findById(createCar.getId());

        assertNull(result);
    }
}