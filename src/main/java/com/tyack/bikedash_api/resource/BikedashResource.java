package com.tyack.bikedash_api.resource;

import com.tyack.bikedash_api.exception.BikeSystemNotFoundException;
import com.tyack.bikedash_api.service.BikedashService;
import com.tyack.bikedash_api.transformer.JsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static spark.Spark.exception;
import static spark.Spark.get;

/**
 * Resource where all routes for Bikedash are defined.
 */
public class BikedashResource extends CommonResource {

    final static Logger logger = LoggerFactory.getLogger(BikedashResource.class);

    private final BikedashService bikedashService;

    /**
     * Creates new BikedashResource instance and using given BikedashService
     * @param bikedashService
     */
    public BikedashResource(BikedashService bikedashService) {
        this.bikedashService = bikedashService;
        setupEndpoints();
    }

    private void setupEndpoints() {

        // GET /api/v1/bikesystems
        get(API_CONTEXT + "/bikesystems",
            (request, response) -> {
                response.type("application/json");
                return bikedashService.findAllBikeSystems();
        }, new JsonTransformer());

        // GET /api/v1/bikesystem/indego
        get(API_CONTEXT + "/bikesystem/:system",
                (request, response) -> {
                    response.type("application/json");
                    return bikedashService.findBikeSystem(request.params("system"));
                }, new JsonTransformer());

        // GET /api/v1/stations/indego/2015-11-25
        // get all stations for system for date
        get(API_CONTEXT + "/stations/:system/:date",
                (request, response) -> {
                    response.type("application/json");
                    return bikedashService.findStations(request.params("system"), request.params("date"));
                }, new JsonTransformer());

        // GET /api/v1/station/snaps/indego/2015-10-28
        // get all station snapshots for system for date
        get(API_CONTEXT + "/station/snaps/:system/:date",
                (request, response) -> {
                    response.type("application/json");
                    return bikedashService.findSnapsOnDate(request.params("system"), LocalDate.parse(request.params("date")));
                },
                new JsonTransformer());

        exception(BikeSystemNotFoundException.class, (e, request, response) -> {
            response.status(404);
            response.body(jsonError(e.getMessage()));
        });

        exception(DateTimeParseException.class, (e, request, response) -> {
            response.status(400);
            response.body(jsonError(e.getMessage()));
        });
    }

}
