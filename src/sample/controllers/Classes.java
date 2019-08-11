package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
            id = classes_table.getSelectionModel().getSelectedItem().getId();
            name = classes_table.getSelectionModel().getSelectedItem().getName();
            num = classes_table.getSelectionModel().getSelectedItem().getNum();
            Edit edit = new Edit(id,name,num);
            Stage stage = new Stage();
            stage.setScene(new Scene(edit));
            stage.initOwner(Controller.students_stage);
            stage.show();
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

    class Edit extends AnchorPane {

        public Edit(Integer id,String name,Long num) {
            drawScreen(id,name,num);
        }

        private void drawScreen(Integer id,String name,Long num) {
            Label lName = new Label("Name");
            Label lCapacity = new Label("Capacity");
            Label msg = new Label();

            TextField tName = new TextField(name);
            TextField tCapacity = new TextField(num+"");

            Button add = new Button("Edit");
            Button cancel = new Button("Cancel");
            add.setOnAction(event -> {
                if((!tCapacity.getText().isEmpty()&&!tCapacity.getText().trim().isEmpty())&&
                        (!tName.getText().isEmpty()&&!tName.getText().trim().isEmpty())) {
                    try (Connection connection = sample.CDBConnection.connect()) {
                        PreparedStatement ps = connection.prepareStatement("UPDATE classes SET name=?,num=? WHERE id = ?");
                        ps.setString(1, tName.getText());
                        ps.setInt(2,Integer.parseInt(tCapacity.getText()));
                        ps.setInt(3,id);
                        ps.execute();
                        msg.setTextFill(Color.GREEN);
                        msg.setText(name + " is edited to " + tName.getText());
                    }catch (SQLException | NullPointerException e) {
                        System.out.println(e.getMessage());
                    }catch (NumberFormatException e) {
                        msg.setTextFill(Color.RED);
                        msg.setText("Type a number in class field");
                    }
                }else {
                    msg.setTextFill(Color.RED);
                    msg.setText("Fields can't be empty");
                }
            });

            HBox hName = new HBox(lName,tName);
            HBox hCapacity = new HBox(lCapacity,tCapacity);
            HBox hControle = new HBox(add,cancel);
            VBox vBox = new VBox(hName,hCapacity,msg,hControle);
            hName.setAlignment(Pos.CENTER);
            hCapacity.setAlignment(Pos.CENTER);
            hControle.setAlignment(Pos.CENTER);

            lName.setFont(Font.font("System bold",20.0));
            lCapacity.setFont(Font.font("System bold",20.0));
            tName.setFont(Font.font("System bold",20.0));
            tCapacity.setFont(Font.font("System bold",20.0));
            add.setFont(Font.font("System bold",20.0));
            cancel.setFont(Font.font("System bold",20.0));
            msg.setFont(Font.font("System bold",20.0));

            hName.setSpacing(25.0);
            hCapacity.setSpacing(5.0);
            hControle.setSpacing(30.0);

            AnchorPane.setLeftAnchor(vBox,5.0);
            AnchorPane.setRightAnchor(vBox,5.0);
            AnchorPane.setTopAnchor(vBox,5.0);
            AnchorPane.setBottomAnchor(vBox,5.0);
            vBox.setSpacing(10.0);
            vBox.setAlignment(Pos.CENTER);
            getChildren().add(vBox);
            setPrefWidth(637.0);
            getStylesheets().add(this.getClass().getResource("/sample/style/style.css").toExternalForm());
        }
    }
}

