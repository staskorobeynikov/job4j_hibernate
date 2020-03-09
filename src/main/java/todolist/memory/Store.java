package todolist.memory;

import todolist.models.Item;

import java.util.List;

public interface Store {

    void addItem(Item item);

    void updateItem(Item item);

    List<Item> showFilterItems();

    List<Item> findAll();
}
