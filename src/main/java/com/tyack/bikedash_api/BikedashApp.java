package com.tyack.bikedash_api;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.tyack.bikedash_api.resource.BikedashResource;
import com.tyack.bikedash_api.service.BikedashService;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;
 
public class BikedashApp {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 8080;
 
    public static void main(String[] args) throws Exception {
        ipAddress(IP_ADDRESS);
        port(PORT);
        staticFileLocation("/public");
        new BikedashResource(new BikedashService(mongo("bikeshare")));
    }
 
    private static MongoDatabase mongo(String dbName) throws Exception {
        return new MongoClient("localhost").getDatabase(dbName);
    }
}