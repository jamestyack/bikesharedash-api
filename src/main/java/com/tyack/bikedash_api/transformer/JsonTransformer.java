package com.tyack.bikedash_api.transformer;


import com.fasterxml.jackson.databind.SerializationFeature;
import spark.Response;
import spark.ResponseTransformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
 
public class JsonTransformer implements ResponseTransformer {
 
    private static ObjectMapper mapper = new ObjectMapper();
	{
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}
 
    @Override
    public String render(Object model) throws JsonProcessingException {
	if (model instanceof Response) {
	    return "{}";
	} else {
	    return mapper.writeValueAsString(model);
	}
    }
 
}