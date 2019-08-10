package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.DBConnection;
import sample.Main;
import sample.StudentData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class Students {
    @FXML
    private TableView<StudentData> stuT;
    @FXML
    private TableColumn<StudentData,Integer> Id;
    @FXML
    private TableColumn<StudentData,String> name;
    @FXML
    private TableColumn<StudentData,Integer> classN;
    @FXML
    private TextField searchB;
    static Stage stage;
    static Stage edit_stage;

    private ObservableList<StudentData> data;
    static int id;
    static  String s_name;

    @FXML
    public void initialize() throws SQLException {
        reload();
    }

    @FXML
    public void reload() throws SQLException{
        Connection con = DBConnection.connection();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM students");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            data.add(new StudentData(rs.getInt("id"),rs.getString("name"),rs.getInt("class")));
        }
        Id.setCellValueFactory(new PropertyValueFactory<StudentData,Integer>("StudentId"));
        name.setCellValueFactory(new PropertyValueFactory<StudentData,String>("StudentName"));
        classN.setCellValueFactory(new PropertyValueFactory<StudentData,Integer>("StudentClass"));
        stuT.setItems(null);
        stuT.setItems(data);
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    public void addStudent() throws Exception{
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/fxml/add_student.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(Controller.students_stage);
        stage.show();
    }

    @FXML
    public void delete() throws Exception{
        if(stuT.getSelectionModel().getSelectedItem() != null) {
            Connection con = DBConnection.connection();
            PreparedStatement ps = con.prepareStatement("delete from students where id = ?");
            ps.setInt(1, stuT.getSelectionModel().getSelectedItem().getStudentId());
            ps.execute();
            reload();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete");
            alert.setContentText("Select an item first");
            alert.setWidth(alert.getContentText().length());
            alert.initOwner(stage);
            alert.setHeaderText("");
            alert.show();
        }
    }

    @FXML
    public void edit() throws Exception {
        if(stuT.getSelectionModel().getSelectedItem() != null) {
            loadEditStage();
            id = stuT.getSelectionModel().getSelectedItem().getStudentId();
            s_name = stuT.getSelectionModel().getSelectedItem().getStudentName();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edit");
            alert.setContentText("Select an item first");
            alert.setWidth(alert.getContentText().length());
            alert.initOwner(edit_stage);
            alert.setHeaderText("");
            alert.show();
        }
    }

    private void loadEditStage() throws Exception{
        edit_stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/fxml/edit.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        edit_stage.setScene(scene);
        edit_stage.setResizable(false);
        edit_stage.initOwner(Controller.students_stage);
        edit_stage.show();
    }

    @FXML
    public void back() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/fxml/home.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        Main.stage.setScene(scene);
    }

    String key = "";
    @FXML
    public void search(KeyEvent event) {
        key += event.getText();
    }
}
