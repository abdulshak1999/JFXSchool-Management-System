package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Classes {
    @FXML
    TableView<ClassData> classes_table;
    @FXML
    private TableColumn<ClassData,Integer> Id;
    @FXML
    private TableColumn<ClassData,String> classN;
    @FXML
    private TableColumn<ClassData,Integer> capacity;
    @FXML
    private TextField searchB;
    static Stage stage;
    static Stage edit_stage;

    private ObservableList<ClassData> data;
    static int id;
    String s_name;
    long c_capacity;
    private String name;
    private long num;

    public Classes() {
        s_name = name;
        c_capacity = num;
    }

    @FXML
    public void initialize() throws SQLException {
        reload();
    }

    @FXML
    public void reload() throws SQLException{
        Connection con = CDBConnection.connect();
        data = FXCollections.observableArrayList();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM classes");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            data.add(new ClassData(rs.getInt("id"),rs.getString("name"),rs.getInt("num")));
        }
        Id.setCellValueFactory(new PropertyValueFactory<ClassData,Integer>("id"));
        classN.setCellValueFactory(new PropertyValueFactory<ClassData,String>("name"));
        capacity.setCellValueFactory(new PropertyValueFactory<ClassData,Integer>("num"));
        classes_table.setItems(null);
        classes_table.setItems(data);
        ps.close();
        rs.close();
        con.close();
    }

    @FXML
    public void addClass() throws Exception{
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/fxml/add_class.fxml"));
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
        if(classes_table.getSelectionModel().getSelectedItem() != null) {
            Connection con = CDBConnection.connect();
            PreparedStatement ps = con.prepareStatement("delete from classes where id = ?");
            ps.setInt(1, classes_table.getSelectionModel().getSelectedItem().getId());
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
        if(classes_table.getSelectionModel().getSelectedItem() != null) {
            loadEditStage();
            id = classes_table.getSelectionModel().getSelectedItem().getId();
            name = classes_table.getSelectionModel().getSelectedItem().getName();
            num = classes_table.getSelectionModel().getSelectedItem().getNum();
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
        loader.setLocation(getClass().getResource("/sample/fxml/edit_class.fxml"));
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
