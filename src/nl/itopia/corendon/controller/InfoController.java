package nl.itopia.corendon.controller;

import nl.itopia.corendon.mvc.Controller;

/**
 *
 * @author wieskueter.com
 */
public class InfoController extends Controller {
    
    public InfoController()
    {
        registerFXML("gui/info_screen.fxml");
        System.out.println("This is the info controller");
    }
}
