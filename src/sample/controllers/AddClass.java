package sample.controllers;

import com.sun.istack.internal.Nullable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddClass {
    @FXML
    private TextField capacity;
    @FXML
    private TextField name;
    @FXML
    private Label msg;

    @Nullable
    @FXML
    public void add() {
        if((!capacity.getText().isEmpty()&&!capacity.getText().trim().isEmpty())&&
                (!name.getText().isEmpty()&&!name.getText().trim().isEmpty())) {
            if(capacity.getText().matches("\\d*")) {
                try (Connection connection = sample.CDBConnection.connect()) {
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO classes(name,num) VALUES(?,?)");
                    ps.setString(1, name.getText());
                    ps.setInt(2, Integer.parseInt(capacity.getText()));
                    ps.execute();
                    msg.setText(name.getText() + " is added successfully");
                } catch (SQLException | NullPointerException e) {
                    System.out.println(e.getMessage());
                }
            }else {
                msg.setText("You must type a number on last field");
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
