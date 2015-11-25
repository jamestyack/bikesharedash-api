package com.tyack.bikedash_api.exception;

/**
 * Created by jamestyack on 11/15/15.
 */
public class BikeSystemNotFoundException extends RuntimeException {
    public BikeSystemNotFoundException(String systemId) {
        super(systemId + " bike system not found.");
    }
}
