package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Edit {

    @FXML
    private TextField stC;
    @FXML
    private TextField stN;
    @FXML
    private Label msg;

    @FXML
    public void edit() throws Exception {
        if((!stC.getText().isEmpty()&&!stC.getText().trim().isEmpty())&&
                (!stN.getText().isEmpty()&&!stN.getText().trim().isEmpty())) {
            try (Connection connection = sample.DBConnection.connection()) {
                PreparedStatement ps = connection.prepareStatement("UPDATE students SET name=?,class=? WHERE id = ?");
                ps.setString(1,stN.getText());
                ps.setInt(2,Integer.parseInt(stC.getText()));
                ps.setInt(3,Students.id);
                ps.execute();
                msg.setText(Students.s_name + " is edited to " + stN.getText());
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
