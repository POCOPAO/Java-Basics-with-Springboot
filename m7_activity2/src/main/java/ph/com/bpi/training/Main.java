package ph.com.bpi.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;


public class Main {
	
	private static final Logger logger =  LoggerFactory.getLogger(Main.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final List<M7Exercise1> entityList = new ArrayList();
	private static final List<String> apiCalls = new ArrayList();
	private static String authorizationToken = null;

    public static void main(String[] args) throws Exception {
    	
    	 // Start server on port 4567 (default)
        port(4568);
        
        // We create a route for checking if the server is active.
        get("/check-connection", (req, res) -> {
            res.type("application/json");	// define the MIME type of the response
            								// we're telling the client that the response
            								// is of JSON format

            // return a map (A map is basically a JSON formatted object)
            Map<String, String> response = new HashMap<>();
            response.put("status", "Server is running");
            
            return JsonUtil.toJson(response);
        });
        
        // A basic hello world route
        get("/hello-world", (req, res) -> {
            res.type("application/json");

            // if a 'name' parameter was provided, we use that in the message.
            // else, we default to World
            String name = req.queryParams("name");
            if (name == null) {
                name = "World";
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "Hello " + name);

            return JsonUtil.toJson(response);
        });
        
        // An example POST method that returns the payload that you send.
        post("/echo", (req, res) -> {
            res.type("application/json");

            // Just return the request body
            return req.body();
        });
        
        // Exercise 1 routes
        
        // init list
        M7Exercise1 entry1 = new M7Exercise1(1L, "Jose Rizal", "Illustrados", null);
       
        M7Exercise1 entry2 = new M7Exercise1(2L, "Andres Bonifacio", "KKK", null);

        M7Exercise1 entry3 = new M7Exercise1(3L, "Emilio Aguinaldo", "Magdalo", null);
        
        entityList.add(entry1);
        entityList.add(entry2);
        entityList.add(entry3);
        // Get Profile List
        get("/profiles", (req, res) -> {
            Map<String, Object> response = new HashMap<String, Object>();
        	apiCalls.add("GET: /profiles");

            res.type("application/json");
            response.put("status", "Success");
            response.put("data", entityList);
            
            return JsonUtil.toJson(response);
        });
        
        // Create Profile List
        post("/profiles", (req, res) -> {
            Map<String, Object> response = new HashMap<String, Object>();
        	apiCalls.add("POST: /profiles");

        	
            res.type("application/json");
            
            if(req.body() == null || req.body().isBlank() ) {            	
            	res.status(400);
            	response.put("status", "ERROR");
                response.put("data", null);
                response.put("message", "Request body cannot be null");
                
                return JsonUtil.toJson(response);
            }
            // form the data into M7Exercise1
            M7Exercise1 data = JsonUtil.fromJson(req.body(), M7Exercise1.class);
            data.setApiCallsMade(apiCalls);
            
            entityList.add(data);
            
            response.put("status", "Success");
            response.put("data", entityList);
            
            return JsonUtil.toJson(response);
        });
        
        // Create Profile List
        put("/profiles", (req, res) -> {
            Map<String, Object> response = new HashMap<String, Object>();
        	apiCalls.add("PUT: /profiles");

        	
            res.type("application/json");
            String name = req.queryParams("name");
            
            if (name == null || name.isBlank() ) {
            	response.put("status", "ERROR");
                response.put("data", null);
                response.put("message", "Name query param is required!");
                
                return JsonUtil.toJson(response);
            }
            
            if(req.body() == null || req.body().isBlank() ) {            	
            	response.put("status", "ERROR");
                response.put("data", null);
                response.put("message", "Request body cannot be null!");
                
                return JsonUtil.toJson(response);
            }
            // parse the request payload
            M7Exercise1 data = JsonUtil.fromJson(req.body(), M7Exercise1.class);
            
            // search for the entity in the list
            M7Exercise1 searchEntity = entityList
            .stream()
            .filter(x -> x.getName().toLowerCase().contains(name.toLowerCase() ) )
            .findFirst().orElse(null);
                        
           
            // update the name and group
            searchEntity.setName(data.getName() );
            searchEntity.setGroup(data.getGroup() );
            
            response.put("status", "Success");
            response.put("data", entityList);
            
            return JsonUtil.toJson(response);
        });
        
     // Create Profile List
        delete("/profiles/:id", (req, res) -> {
            Map<String, Object> response = new HashMap<String, Object>();
        	apiCalls.add("DELETE: /profiles");
            res.type("application/json");
            
            String paramId = req.params("id");
            
            if (paramId == null || paramId.isBlank() ) {
            	response.put("status", "ERROR");
                response.put("data", null);
                response.put("message", "ID is required!");
                
                return JsonUtil.toJson(response);
            }
            
            Long id = Long.valueOf(paramId);

            // search for the entity in the list
            M7Exercise1 searchEntity = entityList
            .stream()
            .filter(x -> x.getId().equals(id) )
            .findFirst().orElse(null);
            
            if (searchEntity == null ) {
            	response.put("status", "ERROR");
                response.put("data", null);
                response.put("message", "Could not find profile with ID = " + paramId);
                
                return JsonUtil.toJson(response);
            }
            entityList.remove(searchEntity);
            
            response.put("status", "Success");
            response.put("data", entityList);
            
            return JsonUtil.toJson(response);
        });
        
        logger.info("Server started at port {}", port() );
    }

}
