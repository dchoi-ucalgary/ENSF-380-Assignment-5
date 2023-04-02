package edu.ucalgary.oop;

import java.util.*;
import java.time.LocalDate;

public class VisitorParking {
    private LinkedList<String> visitorLicence = new LinkedList<String>();

    private String licence;
    private LocalDate date;

    // constructors
    public VisitorParking() {
        this.licence = null;
        this.date = null;
    }
    public VisitorParking(String Lice) throws IllegalArgumentException {
        this.licence = Parking.standardizeAndValidateLicence(Lice);
    }

    public VisitorParking(String Lice, LocalDate Date) 
                        throws IllegalArgumentException{
        this.licence = Parking.standardizeAndValidateLicence(Lice);
        this.date = Date;
    }

    // Visitor Reservation
    public void addVisitorReservation(String Lice) 
                        throws IllegalArgumentException {
        this.licence = Parking.standardizeAndValidateLicence(Lice);
    }

    public void addVisitorReservation(String Lice, LocalDate Date) 
                    throws IllegalArgumentException {
        this.licence = Parking.standardizeAndValidateLicence(Lice);
        this.date = Date;
    }

    public boolean licenceIsRegisteredForDate(String Lice){
        if (this.licence == Lice){
            return true;
        } else {
            return false;
        }
    }
    public boolean licenceIsRegisteredForDate(String Lice, LocalDate Date){
        if (this.licence == Lice && this.date == Date){
            return true;
        } else {
            return false;
        }
    }

    public String[] getLicencesRegisteredForDate() {
        String result[] = this.visitorLicence.toArray(new String[3]);
        return result;
    }
}

