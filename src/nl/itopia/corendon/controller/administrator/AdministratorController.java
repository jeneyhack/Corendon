package nl.itopia.corendon.controller.administrator;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import nl.itopia.corendon.controller.administrator.CreateUserController;
import nl.itopia.corendon.data.Employee;
import nl.itopia.corendon.data.table.TableUser;
//import nl.itopia.corendon.data.Luggage;
import nl.itopia.corendon.model.DatabaseManager;
import nl.itopia.corendon.model.EmployeeModel;
//import nl.itopia.corendon.model.LuggageModel;
import nl.itopia.corendon.mvc.Controller;

/**
 *
 * @author Erik
 */
public class AdministratorController extends Controller {
    
    //private AdministratorView view;
    private Employee employee;
    private DatabaseManager dbManager;
    
    public final ObservableList<TableUser> data;
    
    public final List<Employee> employeeList;
    @FXML private TableView userTable;
    
    @FXML private TableColumn <Employee,String>userIDtable;
    @FXML private TableColumn <Employee,String>nameTable;
    @FXML private TableColumn <Employee,String>usernameTable;
    
    @FXML private Button adduserButton;
    @FXML private Button deleteuserButton;
    private String deleteUserId;
    
    public AdministratorController() {
        
        registerFXML("gui/Overview_administrator.fxml");
        
        adduserButton.setOnAction(this::createNewEmployee);
        
        userIDtable.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        nameTable.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        usernameTable.setCellValueFactory(new PropertyValueFactory<Employee, String>("telephoneNumber"));
        
        employeeList = EmployeeModel.getDefault().getEmployees();
        
//        for(int i = 0; i < employeeList.size(); i++) {
//            Employee a = employeeList.get(i);
//            Log.display(a.firstName, a.lastName, a.username);
//        }
        
        data = FXCollections.observableArrayList();
        
        for(Employee employee : employeeList) {
            
            if( !employee.account_status.equals("deleted") ) {
                 String name = employee.firstName + " " + employee.lastName;
                TableUser user = new TableUser(employee.firstName, employee.lastName, employee.username, Integer.toString(employee.id));
                data.add(user);
            }
        }
        
//        data = FXCollections.observableArrayList(new TableUser("Wies", "Kueter", "06-12345678"),
//                new TableUser("Robin", "de Jong", "06-12345678"),
//                new TableUser("Jeroen", "Groot", "06-12345678"),
//                new TableUser("Stefan", "de Groot", "06-12345678"),
//                new TableUser("Erik", "Schouten", "06-12345678"),
//                new TableUser("Igor", "Willems", "06-12345678")
//        );
        
        userTable.setItems(data);
        
        
        /**
         * Delete user
         */
        userTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            
            // Get the object for the selected user in the table
            TableUser user = (TableUser) userTable.getSelectionModel().getSelectedItem();
            this.deleteUserId = user.getUserID();
            
            // Trigger click on button and run delete method
            deleteuserButton.setOnAction(this::deleteEmployee);
        });
        
    }
    
    public void createNewEmployee(ActionEvent event) {
        addController(new CreateUserController());
    }
    
    /**
     * Handle delete action through database
     * @param event 
     */
    public void deleteEmployee(ActionEvent event) {
        EmployeeModel employeemodel = EmployeeModel.getDefault();
        employeemodel.deleteEmployee(this.deleteUserId);
    }
    
    void initializeTable() {
        
        //Create columns and set their datatype
        
        //MAAK LIST VAN LUGGAGE OBJECTS EN VUL DE KOLOMMEN
        //LuggageModel luggageModel = LuggageModel.getDefault(); 
        //List<Employee> employeeList = EmployeeModel.getAllEmployee();
        //luggageInfo.getItems().setAll(luggageList);
    }
}
