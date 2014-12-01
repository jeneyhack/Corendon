package nl.itopia.corendon.controller.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import nl.itopia.corendon.data.*;
import nl.itopia.corendon.model.*;
import nl.itopia.corendon.mvc.Controller;
import nl.itopia.corendon.utils.DateUtil;
import nl.itopia.corendon.utils.Log;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * © 2014, Biodiscus.net Robin
 */
public class EditLuggageController extends Controller {
    @FXML private TextField labelInputfield, fileInputfield, brandInputfield, heightInputfield, weightInputfield,
            notesInputfield, widthInputfield, depthInputfield;

    @FXML private ChoiceBox foundonAirportdropdown, colorDropdown;
    @FXML private ScrollPane imageScrollpane;
    @FXML private Button cancelButton, editButton;

    private VBox imageScrollContent;

    private LuggageModel luggageModel;
    private AirportModel airportModel;
    private ColorModel colorModel;
    private ImageModel imageModel;

    private Luggage currentLuggage;

    public EditLuggageController(int luggageID) {
        this(LuggageModel.getDefault().getLuggage(luggageID));
    }

    public EditLuggageController(Luggage luggage) {
        registerFXML("gui/edit_luggage.fxml");
        currentLuggage = luggage;

        luggageModel = LuggageModel.getDefault();
        airportModel = AirportModel.getDefault();
        colorModel = ColorModel.getDefault();
        imageModel = ImageModel.getDefault();

        cancelButton.setOnAction(this::cancelHandler);

        labelInputfield.setText(luggage.label);
//        brandInputfield.setText(luggage.brand.getName());
        notesInputfield.setText(luggage.notes);

        String[] dimensions = luggage.getDimensions();
        widthInputfield.setText(dimensions[0]);
        heightInputfield.setText(dimensions[1]);
        depthInputfield.setText(dimensions[2]);

        weightInputfield.setText(luggage.weight);

        // Set the imageScrollpane content
        imageScrollContent = new VBox();
        imageScrollpane.setContent(imageScrollContent);


        // Set the Airports in the foundonAirportdropdown
        List<Airport> airports = airportModel.getAirports();
        int currentAirportPlace = 1; // This will hold the place in the choisebox
        for(int i = 0; i < airports.size(); i ++) {
            Airport airport = airports.get(i);
            ChooseItem c = airportModel.airportToChoose(airport);
            foundonAirportdropdown.getItems().add(c);

            if(airport.getID() == luggage.airport.getID()) {
                currentAirportPlace = i;
            }
        }
        foundonAirportdropdown.getSelectionModel().select(currentAirportPlace);

        // Set the Colors in the colorDropdown
        List<Color> colors = colorModel.getColors();
        int currentColorPlace = 1; // This will hold the place in the choisebox
        for(int i = 0; i < colors.size(); i ++) {
            Color color = colors.get(i);
            ChooseItem c = colorModel.colorToChoose(color);
            colorDropdown.getItems().add(c);

            if(color.getID() == luggage.color.getID()) {
                currentColorPlace = i;
            }
        }
        colorDropdown.getSelectionModel().select(currentColorPlace);

        // Get the photos
        List<Picture> pictures = imageModel.getPicturesFromLuggage(luggage.getID());
        for(Picture pic : pictures) {
            double width = imageScrollpane.getPrefWidth() - 50;
            Image image = new Image(pic.getPath(), width, 0, true, true);
            imageScrollContent.getChildren().add(new ImageView(image));
        }

        cancelButton.setOnAction(this::cancelHandler);
        editButton.setOnAction(this::editHandler);
    }

    private void editHandler(ActionEvent e) {
        ChooseItem airport = (ChooseItem)foundonAirportdropdown.getValue();
        ChooseItem color = (ChooseItem)colorDropdown.getValue();

        Luggage luggage = new Luggage(currentLuggage.getID());
        luggage.color = ColorModel.getDefault().getColor(color.getKey());
        luggage.status = StatusModel.getDefault().getStatus(1);

        // This won't work if no user is logged in!
        luggage.employee = EmployeeModel.getDefault().currentEmployee;
//        luggage.employee = EmployeeModel.getDefault().getEmployee(0);

        luggage.customer = CustomerModel.getDefault().getCustomer(2);
        luggage.airport = airportModel.getAirport(airport.getKey());

        String[] dimensions = {
                widthInputfield.getText(),
                heightInputfield.getText(),
                depthInputfield.getText(),
                "cm"
        };

        luggage.setDimensions(dimensions);


        luggage.label = labelInputfield.getText();
        luggage.notes = notesInputfield.getText();
        luggage.weight = weightInputfield.getText();
        luggage.brand = BrandModel.getDefault().getBrand(1);

        long currentTimeStamp = DateUtil.getCurrentTimeStamp();

        luggage.foundDate = currentTimeStamp;
        luggage.createDate = currentTimeStamp;
        luggage.returnDate = 0;

        currentLuggage = luggage;
        luggageModel.editLuggage(luggage);


//        Pane parent = (Pane)getView().getParent();
//        addController(new DetailLuggageController(luggage), parent);
        removeController(this);
    }

    private void cancelHandler(ActionEvent e) {
//        Pane parent = (Pane)getView().getParent();
//        addController(new DetailLuggageController(currentLuggage), parent);
        removeController(this);
    }

    @Override
    protected Object destroyReturn() {
        return currentLuggage;
    }
}
