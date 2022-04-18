package com.edpadron.gateways.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class Common {
    /**
     * Common asserts that are required in each request in order to get the proper data.
     * @param mvcResult
     * @return HashMap with the data needed. example: {"response": {} }
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> commonAsserts(MvcResult mvcResult) throws UnsupportedEncodingException {
        Map<String, Object> res = getResponseFromMvcResult(mvcResult);
        if(res.isEmpty()){
            System.out.println("Current response is: " + mvcResult.getResponse().getContentAsString());
            fail("Couldn't get response from request");
        }

        Assert.isTrue(res.containsKey("success"), "property 'success' not found");
        Assert.isTrue(res.containsKey("response"), "property 'response' not found");
        Assert.isInstanceOf(HashMap.class, res.get("response"));
        Map<String, Object> response = (Map<String, Object>) res.get("response");
        Assert.isTrue(response.containsKey("data"), "property 'data' not found");
        return response;
    }

    /**
     * Get the response from MvcResult
     * @param mvcResult Object MvcResult
     * @return HashMap with full response. example: {"success": true, "response": {} }
     */
    public static Map<String, Object> getResponseFromMvcResult(MvcResult mvcResult){
        try{
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class);
            boolean success = (boolean) map.getOrDefault("success", false);
            Map<String, Object> response = (Map<String, Object> ) map.getOrDefault("response", "null");
            return new HashMap<String, Object>(){{
                put("success", success);
                put("response", response);
            }};
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return new HashMap<>();
        }
    }
}
