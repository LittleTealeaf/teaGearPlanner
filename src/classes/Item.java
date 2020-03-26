package classes;

import java.util.ArrayList;
import java.util.List;

public class Item extends Source {

    public List<Effect> effects = new ArrayList<>();
    public List<String> itemSets = new ArrayList<>();


    public Item() {
        super();
    }

    public Item(String name) {
        super(name);
    }


}
