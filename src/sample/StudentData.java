package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentData {
    private final IntegerProperty StudentId;
    private final StringProperty StudentName;
    private final IntegerProperty StudentClass;

    public StudentData(Integer StudentId,String StudentName,Integer StudentClass) {
        this.StudentId = new SimpleIntegerProperty(StudentId);
        this.StudentName = new SimpleStringProperty(StudentName);
        this.StudentClass = new SimpleIntegerProperty(StudentClass);
    }
    public Integer getStudentId(){
        return StudentId.get();
    }
    public String getStudentName(){
        return StudentName.get();
    }
    public Integer getStudentClass(){
        return StudentClass.get();
    }
}
