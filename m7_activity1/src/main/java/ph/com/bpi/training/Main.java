package ph.com.bpi.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;


public class Main {
	
	private static final Logger logger =  LoggerFactory.getLogger(Main.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	 
    public static void main(String[] args) {
    	// intialize entityManager;
        EntityManager em = EntityManagerUtil.getInstance().createEntityManager();
    	
        // initialize movieRepository
    	MovieRepository movieRepository = new MovieRepository(em);
    	
    	 // Start server on port 4567 (default)
        port(4567);
        
        // add routes here
        after((req,res) -> res.type("application/json"));
        // Get Profile List
        get("/movies",(req,res)->{
        	try {
        		List<Movie> movies = movieRepository.findAll();
        		res.status(200);
        		return mapper.writeValueAsString(movies);
        	} catch (Exception e) {
        		logger.error("Error fetching movies", e);
        		res.status(500);
        		return mapper.writeValueAsString(new ErrorResponse("Failed to fetch movies"));
        	}
        });
        
        // Create Profile List
        post("/movies",(req,res)->{
        	EntityTransaction tx = em.getTransaction();
        	try {
        		Movie movie = mapper.readValue(req.body(), Movie.class);
        		tx.begin();
        		Movie saved = movieRepository.save(movie);
        		tx.commit();
        		res.status(201);
        		return mapper.writeValueAsString(saved);
        	} catch (Exception e) {
        		logger.error("Error saving movie", e);
        		if(tx.isActive()) tx.rollback();
        		res.status(400);
        		return mapper.writeValueAsString(new ErrorResponse("Failed to save movie"));
        	}
        });
        
        get("/health", (req,res)->"{\"status\":\"ok\"}");
    }
    
    static class ErrorResponse{
    	public String message;
    	public ErrorResponse(String message) {this.message = message;}
    }

}
