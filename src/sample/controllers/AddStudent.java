package sample.controllers;

import com.sun.istack.internal.Nullable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class AddStudent {
    @FXML
    private TextField stC;
    @FXML
    private TextField stN;
    @FXML
    private Label msg;

    @Nullable
    @FXML
    public void add() {
        if((!stC.getText().isEmpty()&&!stC.getText().trim().isEmpty())&&
                (!stN.getText().isEmpty()&&!stN.getText().trim().isEmpty())) {
            try (Connection connection = sample.DBConnection.connection()) {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO students(name,class) VALUES(?,?)");
                ps.setString(1,stN.getText());
                ps.setInt(2,Integer.parseInt(stC.getText()));
                ps.execute();
                msg.setText(stN.getText() + " is added successfully");
            }catch (SQLException | NullPointerException e) {
                System.out.println(e.getMessage());
            }catch (NumberFormatException e) {
                msg.setText("Type a number in class field");
            }
        }else {
            msg.setText("Fields can't be empty");
        }
    }

    @FXML
    public void cancel() {
        Students.stage.close();
    }
}
