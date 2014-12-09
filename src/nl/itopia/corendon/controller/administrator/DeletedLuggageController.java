/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.itopia.corendon.controller.administrator;

import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import nl.itopia.corendon.controller.HelpFunctionController;
import nl.itopia.corendon.controller.LoginController;
import nl.itopia.corendon.data.LogAction;
import nl.itopia.corendon.data.Luggage;
import nl.itopia.corendon.data.table.TableLog;
import nl.itopia.corendon.data.table.TableLuggage;
import nl.itopia.corendon.model.LogModel;
import nl.itopia.corendon.model.LuggageModel;
import nl.itopia.corendon.mvc.Controller;
import nl.itopia.corendon.utils.DateUtil;
/**
 *
 * @author Erik
 */
public class DeletedLuggageController extends Controller {

    @FXML private TableView luggageInfo;
    
    @FXML private Button revertLuggageButton, helpButton, logoutButton, deleteLuggageButton, overviewbutton, logfilesbutton;
    @FXML private TableView logInfo;

    public ObservableList<TableLuggage> tableData;
    public List<Luggage> luggageList;

    @FXML private TableColumn <Luggage,String>ID;
    @FXML private TableColumn <Luggage,String>Brand;
    @FXML private TableColumn <Luggage,String>Dimensions;
    @FXML private TableColumn <Luggage,String>Color;
    @FXML private TableColumn <Luggage,String>Airport;
    @FXML private TableColumn <Luggage,String>Status;
    @FXML private TableColumn <Luggage,String>Notes; 
    
    private LuggageModel luggageModel;
    
    private ImageView spinningIcon;
    private StackPane iconPane;

    
    public DeletedLuggageController() {
        registerFXML("gui/deleted_luggage_admin.fxml");

        luggageModel = LuggageModel.getDefault();

        // Show a spinning icon to indicate to the user that we are getting the tableData
        Image image = new Image("img/loader.gif", 24, 16.5, true, false);
        spinningIcon = new ImageView(image);
        
        logoutButton.setOnAction(this::logoutHandler);
        helpButton.setOnAction(this::helpHandler);
        overviewbutton.setOnAction(this::overviewHandler);
        logfilesbutton.setOnAction(this::logHandler);
        view.fxmlPane.setOnKeyReleased(this::f1HelpFunction);


        // Create columns and set their datatype for building the Luggage Table
        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        Brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        Dimensions.setCellValueFactory(new PropertyValueFactory<>("dimensions"));
        Color.setCellValueFactory(new PropertyValueFactory<>("color"));
        Airport.setCellValueFactory(new PropertyValueFactory<>("airport"));
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        Notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        
        Thread dataThread = new Thread(()-> receiveData());
        dataThread.start();

    }
    
    private void receiveData() {
        luggageList = luggageModel.getAllDeletedLuggage();
        tableData = FXCollections.observableArrayList();

        for(Luggage luggage : luggageList) {
            TableLuggage luggageTable = new TableLuggage(
                    luggage.getID(),
                    luggage.dimensions,
                    luggage.notes,
                    luggage.airport.getName(),
                    luggage.brand.getName(),
                    luggage.color.getHex(),
                    luggage.status.getName()
            );

            tableData.add(luggageTable);
        }

        Platform.runLater(() -> {
            luggageInfo.setItems(tableData);
            view.fxmlPane.getChildren().remove(iconPane);
        });
    }
    
    private void overviewHandler(ActionEvent e) {
        changeController(new AdministratorController());
    }

    // We will call this function in a new thread, so the user can still click buttons

    private void logHandler(ActionEvent e) {
        changeController(new LogController());
    }
    
    
    private void helpHandler(ActionEvent e) {
        addController(new HelpFunctionController());
        //opens help function
    }

    
    private void f1HelpFunction(KeyEvent e) {
        if(e.getCode() == KeyCode.F1 && e.getEventType() == KeyEvent.KEY_RELEASED) {
            addController(new HelpFunctionController());
            //opens helpfunction with the f1 key
        }
    }    
    
    
    private void logoutHandler(ActionEvent e) {
        changeController(new LoginController());
    }
    
   

}
