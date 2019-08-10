package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditClass {

    @FXML
    private TextField capacity;
    @FXML
    private TextField name;
    @FXML
    private Label msg;
    private Classes c = new Classes();

    @FXML
    public void initialize() {
        name.setText(c.s_name);
        capacity.setText(c.c_capacity+"");
    }
    @FXML
    public void edit() throws Exception {
        if((!capacity.getText().isEmpty()&&!capacity.getText().trim().isEmpty())&&
                (!name.getText().isEmpty()&&!name.getText().trim().isEmpty())) {
            try (Connection connection = sample.CDBConnection.connect()) {
                PreparedStatement ps = connection.prepareStatement("UPDATE classes SET name=?,num=? WHERE id = ?");
                ps.setString(1, name.getText());
                ps.setInt(2,Integer.parseInt(capacity.getText()));
                ps.setInt(3,Classes.id);
                ps.execute();
                msg.setText(c.s_name + " is edited to " + name.getText());
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
        Students.edit_stage.close();
    }
}
