package ph.com.bpi.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ph.com.bpi.training.controller.MovieController;
import ph.com.bpi.training.repository.MovieRepository;
import ph.com.bpi.training.service.MovieService;
import ph.com.bpi.training.util.EntityManagerUtil;

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

		MovieService movieService = new MovieService(em);
		MovieController movieController = new MovieController(movieService);
		movieController.registerRoutes();

		logger.info("Server started at port {}", port() );
}}
