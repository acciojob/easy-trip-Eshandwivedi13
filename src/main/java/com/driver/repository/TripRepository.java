package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripRepository {
    private Map<String, Airport> airportMap = new HashMap<>();
    private Map<Integer, Flight> flightMap = new HashMap<>();

    private Map<Integer, Passenger> passengerMap = new HashMap<>();

    private Map<Integer, List<Passenger>> flightPassangerMap = new HashMap<>();

    private Map<Integer,List<Flight>> bookedFlightByPassanger = new HashMap<>();

    public Map<Integer, List<Flight>> getBookedFlightByPassanger()
    {
        return bookedFlightByPassanger;
    }

    public Map<Integer, List<Passenger>> getFlightPassangerMap()
    {
        return flightPassangerMap;
    }

    public void setFlightPassangerMap(Map<Integer, List<Passenger>> flightPassangerMap) {
        this.flightPassangerMap = flightPassangerMap;
    }

    public Map<Integer, Passenger> getPassengerMap()
    {
        return passengerMap;
    }

    public void setPassengerMap(Map<Integer, Passenger> passengerMap) {
        this.passengerMap = passengerMap;
    }

    public Map<Integer, Flight> getFlightMap()
    {
        return flightMap;
    }

    public void setFlightMap(Map<Integer, Flight> flightMap)
    {
        this.flightMap = flightMap;
    }

    public Map<String, Airport> getAirportMap()
    {
        return airportMap;
    }

    public void setAirportMap(Map<String, Airport> airportMap)
    {
        this.airportMap = airportMap;
    }
}