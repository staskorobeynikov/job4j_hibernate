package carbuyer.logic;

import carbuyer.memory.DBStore;
import carbuyer.memory.Store;
import carbuyer.models.*;

import java.util.List;

public class ValidateService implements Validate {

    private static final Validate INSTANCE = new ValidateService();

    private final Store store = DBStore.getInstance();

    public ValidateService() {
    }

    public static Validate getInstance() {
        return INSTANCE;
    }

    @Override
    public User addUser(User user, Account account) {
        return store.addUser(user, account);
    }

    @Override
    public List<Advert> findAll() {
        return store.findAll();
    }

    @Override
    public Car getCar(int id) {
        Car result = new Car();
        for (Car car : store.getCars()) {
            if (car.getId() == id) {
                result = car;
                break;
            }
        }
        return result;
    }

    @Override
    public User isCredential(Account account) {
        User result = null;
        Account check;
        for (User user : store.getUsers()) {
            check = user.getAccount();
            if (check.getLogin().equals(account.getLogin())
                    && check.getPassword().equals(account.getPassword())) {
                result = user;
                break;
            }
        }
        return result;
    }

    @Override
    public List<Advert> getAdvertsUser(User user) {
        return store.getAdvertsUser(user);
    }

    @Override
    public List<Advert> showLastDay() {
        return store.showLastDay();
    }

    @Override
    public List<Advert> showWithPhoto() {
        return store.showWithPhoto();
    }

    @Override
    public List<Advert> showWithSpecificMark(Mark mark) {
        return store.showWithSpecificMark(mark);
    }

    @Override
    public void updateStatus(Advert advert) {
        store.updateStatus(advert);
    }

    @Override
    public void addNewAdvert(CarBody carBody, Engine engine, Transmission transmission,
                             Mark mark, Model model, User user, Car car, Advert advert) {
        store.addNewAdvert(carBody, engine, transmission, mark, model, user, car, advert);
    }
}
