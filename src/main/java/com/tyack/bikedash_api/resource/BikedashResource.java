package com.tyack.bikedash_api.resource;

import com.tyack.bikedash_api.exception.BikeSystemNotFoundException;
import com.tyack.bikedash_api.exception.StationsNotFoundException;
import com.tyack.bikedash_api.service.BikedashService;
import com.tyack.bikedash_api.transformer.JsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.before;

/**
 * Resource where all routes for Bikedash are defined.
 */
public class BikedashResource extends CommonResource {

    final static Logger logger = LoggerFactory.getLogger(BikedashResource.class);

    private final BikedashService bikedashService;

    /**
     * Creates new BikedashResource instance using given BikedashService
     * 
     * @param bikedashService
     */
    public BikedashResource(BikedashService bikedashService) {
	this.bikedashService = bikedashService;
	setupEndpoints();
    }

    private void setupEndpoints() {

	/**
	 * Get all bike systems e.g. GET /api/v1/bikesystems
	 */
	get(API_CONTEXT + "/bikesystems", (request, response) -> {
	    response.type("application/json");
	    return bikedashService.findAllBikeSystems();
	}, new JsonTransformer());

	/**
	 * Get a bike system e.g. /api/v1/bikesystem/babs
	 */
	get(API_CONTEXT + "/bikesystem/:system", (request, response) -> {
	    response.type("application/json");
	    return bikedashService.findBikeSystem(request.params("system"));
	}, new JsonTransformer());

	/**
	 * Get most recent list of stations - will query db to find most recent recorded station date
	 * /api/v1/stations/indego
	 */
	get(API_CONTEXT + "/stations/:system", (request, response) -> {
		response.type("application/json");
		return bikedashService.findStations(request.params("system"));
	}, new JsonTransformer());

	/**
	 * Get all stations for system on given date GET
	 * /api/v1/stations/indego/2015-11-25
	 */
	get(API_CONTEXT + "/stations/:system/:date", (request, response) -> {
	    response.type("application/json");
	    return bikedashService.findStations(request.params("system"), request.params("date"));
	}, new JsonTransformer());

	// GET /api/v1/station/snaps/indego/2015-10-28
	// get all station snapshots for system for date
	get(API_CONTEXT + "/station/snaps/:system/:date", (request, response) -> {
	    response.type("application/json");
	    return bikedashService.findSnapsOnDate(request.params("system"), LocalDate.parse(request.params("date")));
	}, new JsonTransformer());

	options("/*", (request, response) -> {
	    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	    if (accessControlRequestHeaders != null) {
		response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	    }

	    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	    if (accessControlRequestMethod != null) {
		response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	    }

	    return "OK";
	});

	before((request, response) -> {
	    response.header("Access-Control-Allow-Origin", "*");
	});
	
	exception(BikeSystemNotFoundException.class, (e, request, response) -> {
	    response.status(404);
	    response.body(jsonError(e.getMessage()));
	});

	exception(StationsNotFoundException.class, (e, request, response) -> {
		response.status(404);
		response.body(jsonError(e.getMessage()));
	});

	exception(DateTimeParseException.class, (e, request, response) -> {
	    response.status(400);
	    response.body(jsonError(e.getMessage()));
	});
    }

}
