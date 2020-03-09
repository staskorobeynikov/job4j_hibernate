package todolist.logic;

import todolist.memory.DBStore;
import todolist.memory.Store;
import todolist.models.Item;

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
    public void addItem(Item item) {
        store.addItem(item);
    }

    @Override
    public void updateItem(Item item) {
        store.updateItem(item);
    }

    @Override
    public List<Item> findAll() {
        return store.findAll();
    }

    @Override
    public List<Item> showFilterItems() {
        return store.showFilterItems();
    }
}
