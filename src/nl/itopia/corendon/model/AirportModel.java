package nl.itopia.corendon.model;

import nl.itopia.corendon.data.Airport;
import nl.itopia.corendon.data.ChooseItem;
import nl.itopia.corendon.utils.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * © 2014, Biodiscus.net Robin
 */
public class AirportModel {
    private static final AirportModel _default = new AirportModel();
    private final DatabaseManager dbmanager = DatabaseManager.getDefault();

    private AirportModel() {}

    public Airport getAirport(int id) {
        Airport airport = null;

        try {
            ResultSet result = dbmanager.doQuery("SELECT * FROM airport WHERE id = "+ id);
            if(result.next()) {
                int code = result.getInt("code");
                String name = result.getString("name");
                airport = new Airport(id, code, name);
            }
        } catch (SQLException e) {
            Log.display("SQLEXCEPTION", e.getErrorCode(), e.getSQLState(), e.getMessage());
        }

        return airport;
    }

    public List<Airport> getAirports() {
        List<Airport> airports = new ArrayList<>();
        try {
            String sql = "SELECT * FROM airport";
            ResultSet result = dbmanager.doQuery(sql);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                int code = result.getInt("code");
                Airport luggage = new Airport(id, code, name);
                airports.add(luggage);
            }
        } catch (SQLException e) {
            Log.display("SQLEXCEPTION", e.getErrorCode(), e.getSQLState(), e.getMessage());
        }

        return airports;
    }

    public ChooseItem airportToChoose(Airport airport) {
        return new ChooseItem(airport.getID(), airport.getName());
    }

    public static AirportModel getDefault() {
        return _default;
    }
    
    public Airport getAirportByEmployeeId(int EmployeeId) {
        /* airport query */
        String airportQuery = "SELECT airport.id, airport.name, airport.code FROM employee\n" +
                                "INNER JOIN airport ON airport.id = employee.airports_id\n" +
                                "WHERE employee.id = " + EmployeeId;
        
        /* create role object so we can manipulate it later on */
        Airport airport = null;
        
        try {
            /* try to run the sql query */
            ResultSet result = dbmanager.doQuery(airportQuery);
            if(result.next()) {
                /* ok, there is a record. Get the field values */
                int airportId = result.getInt("id");
                String airportName = result.getString("name");
                int airportCode = result.getInt("code");
                
                /* make a new airport object and send the field values to the constructor */
                airport = new Airport(airportId,airportCode,airportName);
                
            }
        } catch (SQLException e) {
           Log.display("SQLEXCEPTION", e.getErrorCode(), e.getSQLState(), e.getMessage());
        }
        
        /* return the full airport */
        return airport;
    }


}
