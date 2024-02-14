package com.driver.service;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AirportSerivce {
    @Autowired
    private AirportRepository repoObj;
    public void addAirport(Airport airport){
        repoObj.addAirport(airport);
    }
    public String getLargestAirport(){
        List<Airport> allAirports = repoObj.getAirports();
        Airport first = allAirports.get(0);
        String name = first.getAirportName();
        int curTerminals = first.getNoOfTerminals();
        for (Airport airport : allAirports) {//List<Airport>
            if(curTerminals < airport.getNoOfTerminals()){
                curTerminals = airport.getNoOfTerminals();
                name = airport.getAirportName();
            }else if(curTerminals == airport.getNoOfTerminals()){
                char[] one = name.toCharArray();
                char[] two = airport.getAirportName().toCharArray();
                Arrays.sort(one);
                Arrays.sort(two);
                // Compare lexicographically
                int cmp = String.valueOf(one).compareTo(String.valueOf(two));//if one > two, it returns +ve value
                if (cmp > 0) {
                    name = airport.getAirportName();
                }
            }
        }
        return name;
    }
    public double getShortestPossibleDurationBetweenTwoCities(City fromCity, City toCity){
        List<Flight> flights = repoObj.getFlights();
        List<Flight> flightsBetween2Cities = new ArrayList<>();
        for(Flight flight : flights){
            if(flight.getToCity() == toCity && flight.getFromCity() == fromCity){
                flightsBetween2Cities.add(flight);
            }
        }
        if(flightsBetween2Cities.isEmpty()){
            return -1;
        }
        double duration = flightsBetween2Cities.get(0).getDuration();//got the duration of first flight
        for(Flight flight : flightsBetween2Cities){
            if(duration > flight.getDuration()){
                duration = flight.getDuration();
            }
        }
        return duration;
    }


    //InComplete maybe, check
    public int getNumberOfPeopleSameDayFlights(Date date, String airportName){
//        List<Passenger> passengers = repoObj.getPassengers();
        int count = 0;//ek hi airport, ek hi din mei kitne passengers
        HashMap<Integer, List<Passenger>> passengersOnAFlight = repoObj.getPassengersOnSameFlight();//ek airport mei kitne passengers
        HashMap<Integer, Flight> flightDb = repoObj.getFlightDb();//flightDb
        HashMap<String, Airport> airportDb = repoObj.getAirportDb();//airportDb
        for(Integer flightId:passengersOnAFlight.keySet())
        {
            if((flightDb.get(flightId).getFromCity().equals(airportDb.get(airportName).getCity()) || flightDb.get(flightId).getToCity().equals(airportDb.get(airportName).getCity())) && !flightDb.get(flightId).getFlightDate().before(date)&& !flightDb.get(flightId).getFlightDate().after(date))
            {
                count++;
            }
        }
        return count;
    }
    //InComplete maybe, check
    public String bookATicket(int flightId, int passengerId){
        HashMap<Integer, Flight> flightDb = repoObj.getFlightDb();//flightDb
        HashMap<Integer, List<Passenger>> passengersOfFlights = repoObj.getPassengersOnSameFlight();
        HashMap<Integer, List<Flight>> bookedFlightByPassengers = repoObj.getBookedFlightByPassengers();
        HashMap<Integer, Passenger> passengerDb = repoObj.getPassengerDb();

        List<Flight> tempFlight = bookedFlightByPassengers.getOrDefault(passengerId, new ArrayList<>());
        Flight newFlight = repoObj.getFlight(flightId);
        if(newFlight == null || tempFlight.contains(newFlight)){
            return "FAILURE";
        }
        tempFlight.add(newFlight);
        bookedFlightByPassengers.put(passengerId, tempFlight);
        repoObj.setBookedFlightsByPassengers(bookedFlightByPassengers);

        List<Passenger> tempPassenger = passengersOfFlights.getOrDefault(flightId, new ArrayList<>());
        Passenger newPassenger = repoObj.getPassenger(passengerId);

        if(tempPassenger.size() > flightDb.get(flightId).getMaxCapacity() || newPassenger == null || tempPassenger.contains(newPassenger)){//if  that particular flight is full
            return "FAILURE";
        }
        tempPassenger.add(newPassenger);
        passengersOfFlights.put(flightId, tempPassenger);
        repoObj.setPassengersOnSameFlight(passengersOfFlights);
        passengerDb.put(passengerId, newPassenger);
        return "SUCCESS";


    }
    //InComplete....
    public int calculateFare(int flightId){
        HashMap<Integer, List<Passenger>> flightDb =  repoObj.getPassengersOnSameFlight();
        //price = 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        List<Passenger> noOfPeopleWhoHaveAlreadyBooked = flightDb.getOrDefault(flightId, new ArrayList<>());
        int fare = 3000 + (noOfPeopleWhoHaveAlreadyBooked.size() * 50);
        return fare;
    }

    public String cancelATicket(int flightId, int passengerId){
        HashMap<Integer, Flight> flightDb = repoObj.getFlightDb();//flightDb
        HashMap<Integer, List<Passenger>> passengersOfFlights = repoObj.getPassengersOnSameFlight();
        HashMap<Integer, List<Flight>> bookedFlightByPassengers = repoObj.getBookedFlightByPassengers();
        HashMap<Integer, Passenger> passengerDb = repoObj.getPassengerDb();

        List<Flight> tempFlights = bookedFlightByPassengers.getOrDefault(passengerId, new ArrayList<>());
        if(tempFlights.isEmpty() || !flightDb.containsKey(flightId)){
            return "FAILURE";
        }
        Flight oldFlight = repoObj.getFlight(flightId);
        tempFlights.remove(oldFlight);
        bookedFlightByPassengers.put(passengerId, tempFlights);
        repoObj.setBookedFlightsByPassengers(bookedFlightByPassengers);


        List<Passenger> tempPassenger = passengersOfFlights.getOrDefault(flightId, new ArrayList<>());
        if(tempPassenger.isEmpty() || !passengerDb.containsKey(passengerId)){//if  that particular flight is full
            return "FAILURE";
        }
        Passenger oldPassenger = repoObj.getPassenger(passengerId);
        tempPassenger.remove(oldPassenger);
        passengersOfFlights.put(flightId, tempPassenger);
        repoObj.setPassengersOnSameFlight(passengersOfFlights);
        passengerDb.remove(passengerId);
        return "SUCCESS";
    }
    public int getTotalBookingsOfPassenger(int passengerId){
        HashMap<Integer, List<Flight>> bookedFlightByPassengers = repoObj.getBookedFlightByPassengers();
        if(bookedFlightByPassengers.containsKey(passengerId)){
            return bookedFlightByPassengers.get(passengerId).size();
        }
        return 0;
    }

    public String addFlight(Flight flight){
        HashMap<Integer, Flight> flightDb = repoObj.getFlightDb();//flightDb
        if(flightDb.containsKey(flight)){
            return "FAILURE";
        }
        int flightId = flight.getFlightId();
        flightDb.put(flightId, flight);
        return "SUCCESS";
    }
    public String getAirportNameFromFlightId(int flightId){
        HashMap<Integer, Flight> flightDb = repoObj.getFlightDb();
        HashMap<String, Airport> airportDb = repoObj.getAirportDb();
        if(!flightDb.containsKey(flightId)) return "";
        City fromCity = flightDb.get(flightId).getFromCity();
        String ans = "";
        for(Airport airport : airportDb.values()){
            if(airport.getCity() == fromCity){
                ans = airport.getAirportName();
                break;
            }
        }
        return ans;
    }

    public String addPassenger(Passenger passenger){
        HashMap<Integer, Passenger> passengerDb = repoObj.getPassengerDb();
        int passengerId = passenger.getPassengerId();
        if(passengerDb.containsKey(passengerId)) return "";
        passengerDb.put(passengerId, passenger);
        return "SUCCESS";
    }

    public int getRevenue(int flightId){
        //Calculate the total revenue that a flight could have
        //That is of all the passengers that have booked a flight till now and then calculate the revenue
        //Revenue will also decrease if some passenger cancels the flight

        HashMap<Integer, Flight> flightDb = repoObj.getFlightDb();
        HashMap<Integer, List<Passenger>> passengersOfFlights = repoObj.getPassengersOnSameFlight();
        List<Passenger> passengerList = passengersOfFlights.get(flightId);
        if(!flightDb.containsKey(flightId)) return 0;//flight is not even there
        int numOfPassengers = passengerList.size();
//        int totalRevenue = 3000 + (numOfPassengers * 50);
        int totalRevenue = 0;
        for(int i=0;i<numOfPassengers;i++)
        {
            totalRevenue+=(3000+i*50);
        }
        return totalRevenue;

    }

}
