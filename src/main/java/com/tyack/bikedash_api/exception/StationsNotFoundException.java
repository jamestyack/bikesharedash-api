package com.tyack.bikedash_api.exception;

/**
 * Created by jamestyack on 11/15/15.
 */
public class StationsNotFoundException extends RuntimeException {
    public StationsNotFoundException(String systemId) {
        super(systemId + " bike system: no stations found.");
    }
}
