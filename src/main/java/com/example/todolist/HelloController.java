package com.example.todolist;

import com.example.todolist.db.MySQLConnection;
import com.example.todolist.db.dao.TaskDao;
import com.example.todolist.models.Task;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;

public class HelloController implements Initializable {
    @FXML
    private Button btnAdd,btnReset;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtDesc;
    @FXML
    private TableView<Task> tblTasks;
    ObservableList<Task> taskList = FXCollections.observableArrayList();
    Boolean editMode = false;
    Task taskSelect = null;
    Random ran = new Random();
    //Connection conn = MySQLConnection.getConnection();
    TaskDao taskDao = new TaskDao();
    @FXML
    protected void onAddButtonClick() {
        //showMessage("Funcionaaaaaaaaaaaaaa");
        if(txtName.getText().isEmpty()){
            showMessage("Ingresa un titulo a la tarea");
            txtName.requestFocus();
        }else if(txtDesc.getText().isEmpty()) {
                showMessage("Ingresa una descripción");
                txtDesc.requestFocus();
            }else{
            if(editMode){
                taskList.get(taskList.indexOf(taskSelect)).setName(txtName.getText());
                taskList.get(taskList.indexOf(taskSelect)).setDescription(txtDesc.getText());
                btnAdd.setText("Add");
                editMode = false;
            }else{
                Task newTask = new Task();
                newTask.setId(ran.nextInt(1000));
                newTask.setName(txtName.getText());
                newTask.setDescription(txtDesc.getText());
                newTask.setStatus(false);
                taskList.add(newTask);
            }
            tblTasks.refresh();
            limpiar();
        }
    }
    public void onResetButtonClick() {
        limpiar();
    }
    private void limpiar() {
        txtName.setText("");
        txtDesc.setText("");
        txtName.requestFocus();
    }

    private void showMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("YEEEEEEEEIIIII");
        alert.setContentText(message);
        alert.show();
    }
    public void customResize(TableView<?> view) {

        AtomicLong width = new AtomicLong();
        view.getColumns().forEach(col -> {
            width.addAndGet((long) col.getWidth());
        });
        double tableWidth = view.getWidth();

        if (tableWidth > width.get()) {
            view.getColumns().forEach(col -> {
                col.setPrefWidth(col.getWidth()+((tableWidth-width.get())/view.getColumns().size()));
            });
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableInfo();

    }

    private void initTableInfo() {
        for(int i=0; i<5; i++){
            //taskList.add(new Task(i,"Task "+i,"Description "+i));

        }
        taskList = FXCollections.observableArrayList( taskDao.findAll() );
        tblTasks.setItems(taskList);
        tblTasks.setItems(taskList);
        tblTasks.setColumnResizePolicy((param) -> true );
        Platform.runLater(() -> customResize(tblTasks));
        tblTasks.setOnMouseClicked((mouseEvent)->{
            if(mouseEvent.getClickCount() == 2){
                taskSelect = tblTasks.getSelectionModel().getSelectedItem();
                txtName.setText(taskSelect.getName());
                txtDesc.setText(taskSelect.getDescription());
                btnAdd.setText("Guardar");
                editMode = true;
            }
        });

    }


}