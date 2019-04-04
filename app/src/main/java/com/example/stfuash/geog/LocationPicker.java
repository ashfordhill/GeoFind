package com.example.stfuash.geog;

import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;
import java.util.Random;

public class LocationPicker {

    //Contains top 100 us cities by population
    //TO DO: Figure out how to deal with state codes
    //Add non-US locations
    String[] cities_US = {"New York","Los Angeles","Chicago","Houston","Philadelphia","Phoenix","San Antonio","San Diego","Dallas","San Jose","Austin","Indianapolis",
            "Jacksonville","San Francisco","Columbus","Charlotte","Fort Worth","Detroit","El Paso","Memphis","Seattle","Denver","Washington","Boston","Nashville-Davidson",
            "Baltimore","Oklahoma City","Louisville/Jefferson County","Portland","Las Vegas","Milwaukee","Albuquerque","Tucson","Fresno","Sacramento","Long Beach","Kansas City",
            "Mesa","Virginia Beach","Atlanta","Colorado Springs","Omaha","Raleigh","Miami","Oakland","Minneapolis","Tulsa","Cleveland","Wichita","Arlington","New Orleans","Bakersfield",
            "Tampa","Honolulu","Aurora","Anaheim","Santa Ana","St. Louis","Riverside","Corpus Christi","Lexington-Fayette","Pittsburgh","Anchorage","Stockton","Cincinnati","St. Paul",
            "Toledo","Greensboro","Newark","Plano","Henderson","Lincoln","Buffalo","Jersey City","Chula Vista","Fort Wayne","Orlando","St. Petersburg","Chandler","Laredo","Norfolk","Durham",
            "Madison","Lubbock","Irvine","Winston-Salem","Glendale","Garland","Hialeah","Reno","Chesapeake","Gilbert","Baton Rouge","Irving","Scottsdale","North Las Vegas","Fremont","Boise City",
            "Richmond","San Bernardino"};

    String[] states_US = {"NY","CA","IL","TX","PA","AZ","TX","CA","TX","CA","TX","IN","FL","CA","OH","NC","TX","MI","TX","TN","WA","CO","DE","MA","TN","MD","OK","KY",
            "OR","NV","WI","NM","AZ","CA","CA","CA","MO","AZ","VA","GA","CO","NE","NC","FL","CA","MN","OK","OH","KS","TX","LA","CA","FL","HI","CO","CA","CA","MO","CA",
            "TX","KY","PA","AK","CA","OH","MN","OH","NC","NJ","TX","NV","NE","NY", "NJ","CA","IN","FL","FL","AZ","TX","VA","NC","WI","TX","CA","NC","AZ","TX","FL","NV",
            "VA","AZ","LA","TX","AZ","NV","CA","ID","VA","CA"};

    String[] cities_EU = {"London"

    };

    String[] cities_CN = {"Beijing"

    };




    public String pickCity(int c) {
        Random rng = new Random();
        int i = 0;

        switch(c) {
            case (0):
                i = rng.nextInt(cities_US.length);
                return cities_US[i] + ", " + states_US[i];
            case (1):
                i = rng.nextInt(cities_EU.length);
                return cities_EU[i];
            case (2):
                i = rng.nextInt(cities_CN.length);
                return cities_CN[i];
            default:
                i = rng.nextInt(cities_US.length);
                return cities_US[i] + ", " + states_US[i];


        }
    }

}
