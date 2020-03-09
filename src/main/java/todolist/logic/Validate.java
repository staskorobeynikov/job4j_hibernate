package todolist.logic;

import todolist.models.Item;

import java.util.List;

public interface Validate {

    void addItem(Item item);

    void updateItem(Item item);

    List<Item> showFilterItems();

    List<Item> findAll();
}
