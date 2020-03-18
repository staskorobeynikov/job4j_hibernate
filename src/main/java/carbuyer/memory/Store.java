package carbuyer.memory;

import carbuyer.models.*;

import java.util.List;

public interface Store {

    User addUser(User user, Account account);

    List<Car> getCars();

    List<Advert> findAll();

    List<User> getUsers();

    List<Advert> getAdvertsUser(User user);

    List<Advert> showLastDay();

    List<Advert> showWithPhoto();

    List<Advert> showWithSpecificMark(Mark mark);

    void updateStatus(Advert advert);

    void addNewAdvert(CarBody carBody, Engine engine, Transmission transmission,
                      Mark mark, Model model, User user, Car car, Advert advert);
}
