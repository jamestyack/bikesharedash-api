package com.tyack.bikedash_api.resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * Created by jamestyack on 11/13/15.
 */
public class BikedashResourceTest {

    @Before
    public void setUp() throws Exception {

    }


    /**
     * Learning test for Java 8 time library.
     */
    @Test
    public void queryForTime() {
        ZoneId tz = ZoneId.of("America/New_York");
        ZonedDateTime timeFrom = ZonedDateTime.of(LocalDateTime.of(2015, 3, 13, 0, 0), tz);
        ZonedDateTime timeTo = timeFrom.plusHours(24);
        System.out.println(timeFrom + " -> " + timeTo);
    }


    /**
     * Learning test for Java 8 time library.
     */
    @Test
    public void testTime() {
        Set<String> allZones = ZoneId.getAvailableZoneIds();
        LocalDateTime dt = LocalDateTime.now();

        // Create a List using the set of zones and sort it.
        List<String> zoneList = new ArrayList<String>(allZones);
        Collections.sort(zoneList);
        zoneList.stream().filter(a -> a.contains("US")).forEach(a -> System.out.println(a));

        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy  hh:mm a");

        // Leaving from San Francisco on July 20, 2013, at 7:30 p.m.
        LocalDateTime leaving = LocalDateTime.of(2013, Month.JULY, 20, 19, 30);
        ZoneId leavingZone = ZoneId.of("America/Los_Angeles");
        ZonedDateTime departure = ZonedDateTime.of(leaving, leavingZone);

        try {
            String out1 = departure.format(format);
            System.out.printf("LEAVING:  %s (%s)%n", out1, leavingZone);
        } catch (DateTimeException exc) {
            System.out.printf("%s can't be formatted!%n", departure);
            throw exc;
        }

        // Flight is 10 hours and 50 minutes, or 650 minutes
        ZoneId arrivingZone = ZoneId.of("Asia/Tokyo");
        ZonedDateTime arrival = departure.withZoneSameInstant(arrivingZone)
                .plusMinutes(650);

        try {
            String out2 = arrival.format(format);
            System.out.printf("ARRIVING: %s (%s)%n", out2, arrivingZone);
        } catch (DateTimeException exc) {
            System.out.printf("%s can't be formatted!%n", arrival);
            throw exc;
        }

        if (arrivingZone.getRules().isDaylightSavings(arrival.toInstant()))
            System.out.printf("  (%s daylight saving time will be in effect.)%n",
                    arrivingZone);
        else
            System.out.printf("  (%s standard time will be in effect.)%n",
                    arrivingZone);


    }

    @After
    public void tearDown() throws Exception {

    }
}