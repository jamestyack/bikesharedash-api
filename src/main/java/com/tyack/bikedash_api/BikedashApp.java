package com.tyack.bikedash_api;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.tyack.bikedash_api.resource.BikedashResource;
import com.tyack.bikedash_api.service.BikedashService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;
 
public class BikedashApp {
    
    final static Logger logger = LoggerFactory.getLogger(BikedashApp.class);
    
    private static final String MONGO_HOST = "MONGO_HOST";
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 8080;
 
    public static void main(String[] args) throws Exception {
        port(PORT);
        staticFileLocation("/public");
        new BikedashResource(new BikedashService(mongo("bikeshare")));
    }
 
    private static MongoDatabase mongo(String dbName) throws Exception {
	String mongoHost = new ProcessBuilder().environment().get(MONGO_HOST);
	return new MongoClient(mongoHost == null ? "localhost" : mongoHost).getDatabase(dbName);
    }
}