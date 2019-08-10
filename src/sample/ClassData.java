package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClassData {

    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty num;

    public ClassData(Integer id,String name,Integer num) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.num = new SimpleIntegerProperty(num);
    }

    public Integer getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public Integer getNum() {
        return num.get();
    }
}
