package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AirportRepository {
    private HashMap<String, Airport> airportDb = new HashMap();
    private HashMap<Integer, Flight> flightDb = new HashMap();
    private HashMap<Integer, Passenger> passengerDb = new HashMap();

    private HashMap<Integer, List<Passenger>> passengersOnSameFlight = new HashMap<>();//integrating passengers List with flightId, flightPassenger
    private HashMap<Integer,List<Flight>> bookedFlightByPassengers = new HashMap<>();//integrating flights with passengerId, Incase one passenger has book many flights

    private List<Airport> airports = new ArrayList();
    private List<Flight> flights = new ArrayList();
    private List<Passenger> passengers = new ArrayList();

    public void addAirport(Airport airport){
        String airportName = airport.getAirportName();
        airportDb.put(airportName, airport);
    }
    public List<Airport> getAirports(){
        for(Airport airport : airportDb.values()){
            airports.add(airport);
        }
        return airports;
    }
    public HashMap<String, Airport> getAirportDb(){
        return airportDb;
    }
    public HashMap<Integer, Flight> getFlightDb(){
        return flightDb;
    }
    public HashMap<Integer, Passenger> getPassengerDb(){
        return passengerDb;
    }
    public List<Flight> getFlights(){
        for(Flight flight : flightDb.values()){
            flights.add(flight);
        }
        return flights;
    }

    public List<Passenger> getPassengers(){
        for(Passenger passenger : passengerDb.values()){
            passengers.add(passenger);
        }
        return passengers;
    }
    public Passenger getPassenger(int passengerId){
        Passenger toReturn = null;
        for(Passenger passenger : passengerDb.values()){
            if(passengerId == passenger.getPassengerId()){
                toReturn =  passenger;
                break;
            }
        }
        return toReturn;
    }
    public Flight getFlight(int flightId){
        Flight toReturn = null;
        for(Flight flight : flightDb.values()){
            if(flightId == flight.getFlightId()){
                toReturn =  flight;
                break;
            }
        }
        return toReturn;
    }
    public HashMap<Integer, List<Passenger>> getPassengersOnSameFlight(){//get passengers from a flight
        return passengersOnSameFlight;
    }
    public void setPassengersOnSameFlight(HashMap<Integer, List<Passenger>> passengersOnSameFlight){//set passengers in a flight
        this.passengersOnSameFlight = passengersOnSameFlight;
    }
    public void setBookedFlightsByPassengers(HashMap<Integer, List<Flight>> bookedFlightByPassengers){//set passengers in a flight
        this.bookedFlightByPassengers = bookedFlightByPassengers;
    }
    public HashMap<Integer, List<Flight>> getBookedFlightByPassengers(){
        return bookedFlightByPassengers;
    }
}
