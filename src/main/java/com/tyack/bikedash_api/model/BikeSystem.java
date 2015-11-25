package com.tyack.bikedash_api.model;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class BikeSystem {

    /**
    {
        "_id" : "babs",
        "name" : "Bay Area Bike Share",
        "operator" : "Motivate",
        "website" : "http://www.bayareabikeshare.com/",
        "timezone" : "America/Los_Angeles",
        "Location" : "Bay Area, CA",
        "api_url" : "http://www.bayareabikeshare.com/stations/json",
        "map_center" : null,
        "zoom" : null
    }
    **/

    private String id = "";
    private String name = "";
    private String operator = "";
    private String website = "";
    private String timezone = "";
    private String location = "";
    private String apiUrl = "";
    private String mapCenter = "";
    private String zoom = "";

    public BikeSystem(Document doc) {
        this.id = doc.get("_id").toString();
        this.name = doc.getString("name");
        this.operator = doc.getString("operator");
        this.website = doc.getString("website");
        this.timezone = doc.getString("timezone");
        this.location = doc.getString("Location");
        this.apiUrl = doc.getString("api_url");
        this.mapCenter = doc.getString("map_center");
        this.zoom = doc.getString("zoom");
    }

    @Override
    public String toString() {
        return "BikeSystem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", operator='" + operator + '\'' +
                ", website='" + website + '\'' +
                ", timezone='" + timezone + '\'' +
                ", location='" + location + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                ", mapCenter='" + mapCenter + '\'' +
                ", zoom=" + zoom +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getMapCenter() {
        return mapCenter;
    }

    public void setMapCenter(String mapCenter) {
        this.mapCenter = mapCenter;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }
}
