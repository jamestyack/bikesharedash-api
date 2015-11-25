package com.tyack.bikedash_api.service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tyack.bikedash_api.exception.BikeSystemNotFoundException;
import com.tyack.bikedash_api.model.BikeSystem;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class BikedashService {

    final static Logger logger = LoggerFactory.getLogger(BikedashService.class);
    public static final String ID_TS = "_id.ts";
    public static final String BIKE_SYSTEMS_COLLECTION = "bike_systems";
    public static final String ID_SYSTEM_ID = "_id.systemId";
    public static final String STATION_SNAPS_SUMMARY_COLLECTION_PREFIX = "station_snaps_summary_";
    public static final String STATIONS_COLLECTION = "stations";

    private final MongoDatabase db;
 
    public BikedashService(MongoDatabase db) {
        this.db = db;
    }

    /**
     * Get a list of all bike systems.
     * @return List of BikeSystem
     */
    public List<BikeSystem> findAllBikeSystems() {
        MongoCollection<Document> collection = db.getCollection(BIKE_SYSTEMS_COLLECTION);
        return collection.find().map(document -> new BikeSystem(document)).into(new ArrayList<>());
    }

    /**
     * Find a specific bike system's metadata
     * @param systemId id of the bike system
     * @return A bike system
     */
    // TODO cache BikeSystems?
    public BikeSystem findBikeSystem(String systemId) {
        MongoCollection<Document> collection = db.getCollection(BIKE_SYSTEMS_COLLECTION);
        final Document doc = collection.find(eq("_id", systemId)).first();
        if (doc != null) {
            return new BikeSystem(doc);
        } else {
            throw new BikeSystemNotFoundException(systemId);
        }
    }

    /**
     * Get bike snapshots list for the given date in the given system.
     * Takes into account the timezone of the system's location (and daylight savings).
     * @param systemId Bike system to query
     * @param queryDate Date to use in query
     * @return list of station snap documents
     */
    public List<Document> findSnapsOnDate(String systemId, LocalDate queryDate) throws BikeSystemNotFoundException {
        ZoneId tz = ZoneId.of(findBikeSystem(systemId).getTimezone());
        ZonedDateTime timeFrom = ZonedDateTime.of(LocalDateTime.of(queryDate, LocalTime.of(0,0)), tz);
        ZonedDateTime timeTo = timeFrom.plusHours(24);
        MongoCollection<Document> collection = db.getCollection(STATION_SNAPS_SUMMARY_COLLECTION_PREFIX + systemId);
        return collection.find(and(eq(ID_SYSTEM_ID, systemId), gte(ID_TS, Date.from(timeFrom.toInstant())),
                lte(ID_TS, Date.from(timeTo.toInstant())))).into(new ArrayList<>());
    }

    /**
     * Get all stations
     * @param systemId name of the bike system
     * @param date to query (stations move)
     * @return List of station documents
     */
    public List<Document> findStations(String systemId, LocalDate date) {
        // TODO implement the date filter
        // TODO create index on collection?
        MongoCollection<Document> collection = db.getCollection(STATIONS_COLLECTION);
        final FindIterable<Document> docs = collection.find(eq("_id.systemId", systemId));
        return docs.into(new ArrayList<>());
    }
}
