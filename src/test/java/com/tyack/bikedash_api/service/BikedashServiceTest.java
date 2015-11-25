package com.tyack.bikedash_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tyack.bikedash_api.exception.BikeSystemNotFoundException;
import com.tyack.bikedash_api.model.BikeSystem;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/**
 * Created by jamestyack on 11/14/15.
 */
public class BikedashServiceTest {
    public static final String INDEGO = "indego";
    public static final String ID = "_id";
    public static final String BABS = "babs";
    public static final String AMERICA_NEW_YORK = "America/New_York";
    public static final String AMERICA_LOS_ANGELES = "America/Los_Angeles";
    public static final String BIKESHARE_DB_NAME = "bikeshare";
    static private ObjectMapper jsonObjectMapper = new ObjectMapper();
    private Fongo fongo;
    private MongoDatabase mockMongoDb;

    @Before
    public void setup() {
        fongo = new Fongo("mock mongo 1");
        mockMongoDb = fongo.getDatabase(BIKESHARE_DB_NAME);
    }

    @Test
    public void testFindAllBikeSystems() {
        final MongoCollection<Document> bikeSysColl = mockMongoDb.getCollection(BikedashService.BIKE_SYSTEMS_COLLECTION);
        bikeSysColl.insertMany(createTestListOfBikeSystems());
        final List<BikeSystem> result = new BikedashService(mockMongoDb).findAllBikeSystems();
        assertEquals("babs", result.get(0).getId());
        assertEquals("indego", result.get(1).getId());
        assertEquals(2, result.size());
    }

    @Test
    public void testFindABikeSystem () {
        final MongoCollection<Document> bikeSysColl = mockMongoDb.getCollection(BikedashService.BIKE_SYSTEMS_COLLECTION);
        bikeSysColl.insertMany(createTestListOfBikeSystems());
        final BikeSystem result = new BikedashService(mockMongoDb).findBikeSystem("babs");
        assertEquals("babs", result.getId());
    }

    @Test(expected = BikeSystemNotFoundException.class)
    public void testFindABikeSys_wrongId() {
        final MongoCollection<Document> bikeSysColl = mockMongoDb.getCollection(BikedashService.BIKE_SYSTEMS_COLLECTION);
        bikeSysColl.insertMany(createTestListOfBikeSystems());
        try {
            new BikedashService(mockMongoDb).findBikeSystem("bad");
            fail("Exception not thrown");
        } catch (BikeSystemNotFoundException e) {
            assertEquals("bad bike system not found.", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFindSnapsOnDate() throws Exception {
        final MongoCollection<Document> bikeSysColl = mockMongoDb.getCollection(BikedashService.BIKE_SYSTEMS_COLLECTION);
        bikeSysColl.insertMany(createTestListOfBikeSystems());
        final MongoCollection<Document> stationSnapsColl = mockMongoDb.getCollection(BikedashService.STATION_SNAPS_SUMMARY_COLLECTION_PREFIX + INDEGO);
        stationSnapsColl.insertMany(createTestListOfStationSnaps());
        final ArrayList<Document> whatsInFongo = stationSnapsColl.find().into(new ArrayList<>());
        System.out.println("What's in Fongo");
        whatsInFongo.forEach(s -> System.out.println(s));
        BikedashService bs = new BikedashService(mockMongoDb);
        final List<Document> snapsOnDate = bs.findSnapsOnDate(INDEGO, LocalDate.of(2015, 11, 2));
        System.out.println("Result");
        snapsOnDate.forEach(s -> System.out.println(s));

    }

    private List<? extends Document> createTestListOfStationSnaps() {
        List<Document> snaps = new ArrayList<>();
        snaps.add(createTestStationSnap(INDEGO,
                getDateTime(LocalDate.of(2015, 11, 1), LocalTime.of(23, 59, 59), ZoneId.of(AMERICA_NEW_YORK))));
        snaps.add(createTestStationSnap(INDEGO,
                getDateTime(LocalDate.of(2015, 11, 2), LocalTime.of(0, 0), ZoneId.of(AMERICA_NEW_YORK))));
        snaps.add(createTestStationSnap(INDEGO,
                getDateTime(LocalDate.of(2015, 11, 2), LocalTime.of(0, 1), ZoneId.of(AMERICA_NEW_YORK))));
        snaps.add(createTestStationSnap(INDEGO,
                getDateTime(LocalDate.of(2015, 11, 2), LocalTime.of(1, 0), ZoneId.of(AMERICA_NEW_YORK))));
        snaps.add(createTestStationSnap(INDEGO,
                getDateTime(LocalDate.of(2015, 11, 2), LocalTime.of(10, 0), ZoneId.of(AMERICA_NEW_YORK))));
        snaps.add(createTestStationSnap(INDEGO,
                getDateTime(LocalDate.of(2015, 11, 2), LocalTime.of(23, 59), ZoneId.of(AMERICA_NEW_YORK))));
        snaps.add(createTestStationSnap(INDEGO,
                getDateTime(LocalDate.of(2015, 11, 3), LocalTime.of(0, 0), ZoneId.of(AMERICA_NEW_YORK))));
        snaps.add(createTestStationSnap(INDEGO,
                getDateTime(LocalDate.of(2015, 11, 3), LocalTime.of(0, 1), ZoneId.of(AMERICA_NEW_YORK))));
        System.out.println("Docs to store in fongo");
        snaps.forEach(s -> System.out.println(s));
        return snaps;
    }

    private Date getDateTime(LocalDate localDate, LocalTime localTime, ZoneId zoneId) {
        return Date.from((ZonedDateTime.of(localDate, localTime, zoneId)).toInstant());
    }

    private Document createTestStationSnap(String stationId, Date date) {
        return new Document("_id", new Document("systemId", stationId).append("ts", date));
    }

    private List<? extends Document> createTestListOfBikeSystems() {
        List<Document> listOfBikeSystems = new ArrayList<>();
        listOfBikeSystems.add(createTestBikeSystem(BABS, AMERICA_LOS_ANGELES));
        listOfBikeSystems.add(createTestBikeSystem(INDEGO, AMERICA_NEW_YORK));
        return listOfBikeSystems;
    }

    private Document createTestBikeSystem(String id, String timezone) {
        Document bs = new Document();
        bs.put(ID, id);
        bs.put("name", id + " name");
        bs.put("operator", id + " operator");
        bs.put("website", id + " website");
        bs.put("timezone", timezone);
        bs.put("Location", id + " location");
        bs.put("api_url", id + " api url");
        bs.put("map_center", id + " map center");
        bs.put("zoom", id + " zoom");
        return bs;
    }

}
