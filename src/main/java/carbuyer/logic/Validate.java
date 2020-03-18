package carbuyer.logic;

import carbuyer.models.*;

import java.util.List;

public interface Validate {

    User addUser(User user, Account account);

    List<Advert> findAll();

    Car getCar(int id);

    User isCredential(Account account);

    List<Advert> getAdvertsUser(User user);

    List<Advert> showLastDay();

    List<Advert> showWithPhoto();

    List<Advert> showWithSpecificMark(Mark mark);

    void updateStatus(Advert advert);

    void addNewAdvert(CarBody carBody, Engine engine, Transmission transmission,
                      Mark mark, Model model, User user, Car car, Advert advert);
}
