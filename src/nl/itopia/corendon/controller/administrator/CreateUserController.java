package nl.itopia.corendon.controller.administrator;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import nl.itopia.corendon.data.Airport;
import nl.itopia.corendon.data.ChooseItem;
import nl.itopia.corendon.data.Employee;
import nl.itopia.corendon.data.Role;
import nl.itopia.corendon.model.AirportModel;
import nl.itopia.corendon.model.DateModel;
import nl.itopia.corendon.model.EmployeeModel;
import nl.itopia.corendon.model.RoleModel;
import nl.itopia.corendon.mvc.Controller;
import nl.itopia.corendon.utils.Hashing;
import nl.itopia.corendon.utils.Log;

/**
 * @author wieskueter.com
 */
public class CreateUserController extends Controller {
        
    private TextField usernameInputfield, firstnameInputfield, lastnameInputfield, passwordInputfield, repeatpasswordInputfield, 
            contactdetailsInputfield, notesInputfield;
    private ChoiceBox<ChooseItem> roleDropdownmenu, airportDropdownmenu;
    
    private Button addButton, cancelButton;
    //private ChoiceBox<ChooseItem> roleDropdownmenu = new ChoiceBox<>();

    private final List<Role> roleList;
    private final List<Airport> airportList;
    private AdministratorController AdministratorController;

    // Return the created employee upon removing the controller
    private Employee createdEmployee;

    public CreateUserController() {

        // Set view
        registerFXML("gui/add_user.fxml");

        // Populate dropdownmenu with role values
        roleList = RoleModel.getDefault().getRoles();

        for (Role role : roleList) {
            roleDropdownmenu.getItems().add(new ChooseItem(role.getID(), role.getName()));
        }

        airportList = AirportModel.getDefault().getAirports();
        for (Airport airport : airportList) {
            airportDropdownmenu.getItems().add(new ChooseItem(airport.getID(), airport.getName()));
        }

        roleDropdownmenu.getSelectionModel().select(0);
        airportDropdownmenu.getSelectionModel().select(0);

        // Trigger button to create new employee
        addButton.setOnAction(this::createNewEmployee);
        cancelButton.setOnAction(this::cancelHandler);
    }

    public void createNewEmployee(ActionEvent event) {
        EmployeeModel employeemodel = EmployeeModel.getDefault();
        
        String userName = usernameInputfield.getText();
        String firstName = firstnameInputfield.getText();
        String lastName = lastnameInputfield.getText();
        String password = passwordInputfield.getText();
        String repeatPassword = repeatpasswordInputfield.getText();
        int userRole = roleDropdownmenu.getValue().getKey();
        //String employeeSince = employeeSinceField.getText();
        String contactDetails = contactdetailsInputfield.getText();
        String notes = notesInputfield.getText();

        //System.out.println("Selected role: "+ userRole);

        String salt = Hashing.generateSaltString();

        // Check if username already exists
        //employeemodel.usernameExists(String username);

        // Check if passwords match
        if (!password.equals(repeatPassword)) {
            Log.display("Passwords doesn't match");
        } else {
            System.out.println(password + " is equal to " + repeatPassword);

            /**
             * Check if username already exists
             * Generate random password
             */
            // TODO: Set the correct employeeID
            ChooseItem airport = airportDropdownmenu.getValue();
            Employee employee = new Employee(-1);
            employee.username = userName;
            employee.firstName = firstName;
            employee.lastName = lastName;
            employee.notes = notes;
            employee.contactDetails = contactDetails;
            employee.role = new Role(userRole, "none"); // The role will only be inserted in the database, so no reason to get the correct role from the database
            employee.password = Hashing.sha256(password + salt);
            employee.salt = salt;
            employee.createDate = DateModel.getDefault().getCurrentTimeStamp();
            employee.airport = AirportModel.getDefault().getAirport(airport.getKey());

            // employeemodel.createEmployee(userName, password, userRole);
            createdEmployee = employee;
            
            //employee.airport = employeemodel.currentEmployee.airport; // TODO: We also need an airport field
            //employeemodel.createEmployee(userName, password, userRole);
            employeemodel.createEmployee(employee);
            removeController(this);
        }
    }

    @Override
    protected Object destroyReturn() {
        return createdEmployee;
    }

    private void cancelHandler(ActionEvent e) {
        removeController(this);
    }
}
