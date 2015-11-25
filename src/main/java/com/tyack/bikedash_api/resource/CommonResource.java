package com.tyack.bikedash_api.resource;

public class CommonResource {

    static final String API_CONTEXT = "/api/v1";


    protected String jsonError(String message) {
        return "{ \"error\" : \"" + message + "\"}";
    }
}
