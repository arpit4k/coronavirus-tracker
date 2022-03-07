package io.javabrains.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.javabrains.coronavirustracker.models.LocationStats;
// This service is going to give us the data, and it will make the call to the URL provided and fetch the data 
@Service
public class CoronaVirusDataService {
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStats> allStats = new ArrayList<>();
 // Created an array os location stats where the Location Stats will be stored 
    @PostConstruct //Tells spring that when you construct the instance of this service ^, after that just execute this method below
    @Scheduled(cron = "* * 1 * * *") // runs every 1st hour of every day (s, m, h, d, w, y)

    //Made the actual function which makes and HTTP call to the given URL

    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();  // These new stats are used to populate the above allStats 
        //Why? If the below code requires a fraction of a second to execute, and someone requests data in that
        //fraction of a second, the the above all stats can be fetched 
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(VIRUS_DATA_URL)) //Creating a URI of a string (the given URL)
                    .build(); 
        HttpResponse<String> httpResponse = client.send(request,HttpResponse.BodyHandlers.ofString()); //There can be 2 types of sends - Synchronous and Asynchronous
        //Handler receives the response as a string
        // System.out.println(httpResponse.body());
        
       StringReader csvBodyReader = new StringReader(httpResponse.body()); // A reader instance to read the data/Resonse 
       //Used the Apache CSV Commons library, which detects and parses the CSV file, added the maven dependency in pom.xml

       Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) { //Looping, retrieving header values for each record
            //Creating instances of location stats list and populating it every time I run the app
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
            System.out.println(locationStat); //Prints the actual data to be displayed or you can say this is the console log
            newStats.add(locationStat);
            String state = record.get("Province/State");
            System.out.println(state);

        }
        this.setAllStats(newStats);
}

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    public void setAllStats(List<LocationStats> allStats) {
        this.allStats = allStats;
    }   
}
