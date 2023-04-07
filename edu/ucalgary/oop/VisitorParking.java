package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class VisitorParking {
    private HashMap<String, TreeSet<LocalDate>> parkingRecord;

    // constructors
    public VisitorParking() {
        parkingRecord = new HashMap<String, TreeSet<LocalDate>>();

    }
    public VisitorParking(String licence) throws IllegalArgumentException{
        try{
            Parking.standardizeAndValidateLicence(licence);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Licence Number");
        }

        parkingRecord = new HashMap<String, TreeSet<LocalDate>>();
        addVisitorReservation(licence);
    }
    public VisitorParking(String licence, LocalDate date) throws IllegalArgumentException{
        try{
            Parking.standardizeAndValidateLicence(licence);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Licence Number");
        }
        
        parkingRecord = new HashMap<String, TreeSet<LocalDate>>();
        addVisitorReservation(licence, date);
        
    }

    // Visitor Reservation
   public void addVisitorReservation(String licence) throws IllegalArgumentException {
        addVisitorReservation(licence, LocalDate.now());
    }
    public void addVisitorReservation(String licence, LocalDate date) throws IllegalArgumentException {
        // Testing for invalid date
        if(LocalDate.now().compareTo(date) > 0)
            throw new IllegalArgumentException("Given date is in the past");
        
        // Testing for invalid licence plate
        String validatedLicence;
        try{
            validatedLicence = Parking.standardizeAndValidateLicence(licence);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid licence plate");
        }
        // Testing for invalid date
        if(LocalDate.now().compareTo(date) > 0){
            throw new IllegalArgumentException("Given date is in the past");
        }
        
        // Checking if there is 2 permits at the same time
        checkVehicleOverlap(date);
        // Checking if overlap for the same licence and throws exception if found
        checkOverlap(validatedLicence, date);
        
        if (!parkingRecord.containsKey(validatedLicence)){
            TreeSet<LocalDate> dates = new TreeSet<LocalDate>(Comparator.reverseOrder());
            dates.add(date);
            parkingRecord.put(validatedLicence, dates);
            return;
        }
        parkingRecord.get(validatedLicence).add(date);
        
    }

    public boolean licenceIsRegisteredForDate(String licence) throws IllegalArgumentException{
        return licenceIsRegisteredForDate(licence, LocalDate.now());
    }
    public boolean licenceIsRegisteredForDate(String licence, LocalDate date) throws IllegalArgumentException{
        String validatedLicence = Parking.standardizeAndValidateLicence(licence);
        if(!parkingRecord.containsKey(validatedLicence)){
            return false;
        }
        
        for(LocalDate iterDate: parkingRecord.get(validatedLicence)){
            LocalDate beforeReserve = iterDate.plusDays(-1);
            LocalDate afterReserve = iterDate.plusDays(3);
            if((date.isAfter(beforeReserve)) && (date.isBefore(afterReserve))){
                return true;
            }
        }
        return false;
    }

    // Getters
    public HashMap<String, TreeSet<LocalDate>> getParkingRecord() {
        return parkingRecord;
    }

    public ArrayList<String> getLicencesRegisteredForDate(){
        return getLicencesRegisteredForDate(LocalDate.now());
    }
    public ArrayList<String> getLicencesRegisteredForDate(LocalDate date){
        ArrayList<String> dates = new ArrayList<String>();
        LocalDate afterDate = date.plusDays(2);
        for(String licence: parkingRecord.keySet()){

            for(LocalDate iterDate: parkingRecord.get(licence)){
                if(date.isAfter(iterDate.plusDays(2))){break;} 
                
                LocalDate beforeReserve = iterDate.plusDays(-1);
                LocalDate afterReserve = iterDate.plusDays(3);
                if ((date.isAfter(beforeReserve)) && (date.isBefore(afterReserve))
                    || (afterDate.isAfter(beforeReserve)) && (afterDate.isBefore(afterReserve))){
                    dates.add(licence.toString());
                }
            }
        }
        return dates;
    }

    public ArrayList<LocalDate> getStartDaysLicenceIsRegistered(String licence) throws IllegalArgumentException{
        String validatedLicence = Parking.standardizeAndValidateLicence(licence);
        ArrayList<LocalDate> startDaysList 
            = new ArrayList<LocalDate>(parkingRecord.get(validatedLicence).descendingSet());
        return startDaysList;
    }
    public ArrayList<LocalDate> getAllDaysLicenceIsRegistered(String licence) throws IllegalArgumentException{
        String validatedLicence = Parking.standardizeAndValidateLicence(licence);
        ArrayList<LocalDate> allDaysList = new ArrayList<LocalDate>();

        for(LocalDate iterDate: parkingRecord.get(validatedLicence).descendingSet()){
            allDaysList.add(iterDate);
            allDaysList.add(iterDate.plusDays(1));
            allDaysList.add(iterDate.plusDays(2));
        }
        return allDaysList;
    }

    // Helper Methods 
    private void checkOverlap(String licence, LocalDate date) throws IllegalArgumentException{
    
        if(!parkingRecord.containsKey(licence)){
            return;
        }
    
        LocalDate afterDate = date.plusDays(2);
        for(LocalDate iterDate: parkingRecord.get(licence)){
            LocalDate beforeReserve = iterDate.plusDays(-1);
            LocalDate afterReserve = iterDate.plusDays(3);

            if ((date.isAfter(beforeReserve)) && (date.isBefore(afterReserve)) || 
                (afterDate.isAfter(beforeReserve)) && (afterDate.isBefore(afterReserve))){
                throw new IllegalArgumentException("Licence already registered for this time");
            }
        }
    }

    private void checkVehicleOverlap(LocalDate date) {
        
        int permitCount = 0;
        for(String licence: parkingRecord.keySet()){
            try{
                checkOverlap(licence, date);

            }catch(IllegalArgumentException e){
                permitCount++;
            }
        }
        if(permitCount >= 2){
            throw new IllegalArgumentException("Max 2 licences registered");
        }
        
    }
}