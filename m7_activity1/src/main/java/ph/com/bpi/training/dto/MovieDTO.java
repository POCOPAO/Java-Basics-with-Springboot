package ph.com.bpi.training.dto;

import ph.com.bpi.training.model.Movie;

public class MovieDTO {
	
	private Long id;
	private String title;
	private String director;
	private String showingDate;
	
	public MovieDTO() {}
	
	public MovieDTO(Long id, String title, String director, String showingDate) {
		this.id = id;
		this.title = title;
		this.director = director;
		this.showingDate = showingDate;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDirector() {
		return director;
	}
	
	public void setDirector(String director) {
		this.director = director;
	}
	
	public String getShowingDate() {
		return showingDate;
	}
	
	public void setShowingDate(String showingDate) {
		this.showingDate = showingDate;
	}
	
	public MovieDTO(Movie movieEntity) {
		this.setId(movieEntity.getId());
		this.setTitle(movieEntity.getTitle());
		this.setDirector(movieEntity.getDirector());
		this.setShowingDate(movieEntity.getShowingDate());
	}
	
	public Movie toEntity() {
		Movie newMovie = new Movie();
		newMovie.setId(this.id);
		newMovie.setTitle(this.title);
		newMovie.setDirector(this.director);
		newMovie.setShowingDate(this.showingDate);
		
		return newMovie;
	}
}
