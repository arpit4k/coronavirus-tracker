package io.javabrains.coronavirustracker.models;

//Created a model instance to save the responses/information to be displayed on the page 
public class LocationStats {
    
    private String state;
    private String country;
    private int latestTotalCases;

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getState() {
        return state;
    }
    public int getLatestTotalCases() {
        return latestTotalCases;
    }
    public String getCountry() {
        return country;
    }
    @Override
    public String toString() {
        return "LocationStats [country=" + country + ", latestTotalCases=" + latestTotalCases + ", state=" + state
                + "]";
    }
    

}
