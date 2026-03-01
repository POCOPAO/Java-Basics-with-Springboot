package ph.com.bpi.training.service;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import ph.com.bpi.training.dto.MovieDTO;
import ph.com.bpi.training.model.Movie;
import ph.com.bpi.training.repository.MovieRepository;

public class MovieService {

	private final List<Movie> movieList = new ArrayList<>();
	private final EntityManager em;
	
	private final MovieRepository movieRepository;

	public MovieService(EntityManager em) {
		this.em = em;
		this.movieRepository = new MovieRepository(em);
	}
	public List<MovieDTO> listMovies(){
		return movieRepository.findAll().stream().map(Movie::toDTO).collect(Collectors.toList());
	}
	
	public MovieDTO getMovieById(Long id) {
	    if (id == null) return null;

	    Movie movie = movieRepository.findById(id);
	    return (movie == null) ? null : movie.toDTO();
	}
	
	public MovieDTO addMovie(MovieDTO movieDTO) {
		Long id = movieDTO.getId(); // read directly from DTO

		if (id != null) { // only check when client sent an ID
			Movie existing = movieRepository.findById(id); // may be null
			if (existing == null) { // ID was supplied but doesn't exist
				movieDTO.setId(null); // treat as create
			}
		}
		em.getTransaction().begin();
		Movie saved = movieRepository.save(movieDTO.toEntity());
		em.getTransaction().commit();
		MovieDTO savedDTO = saved.toDTO();
		return savedDTO;
	}
}
