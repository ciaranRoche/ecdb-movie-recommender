package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.User;
import models.Movie;
import utils.DataInput;
import utils.Serializer;

import models.Rating;


public class MovieRecommenderAPI implements Recommender{

	public Map<Long, User> userIndex = new HashMap<>();
	public Map<Long, Movie> movieIndex = new HashMap<>();
	public Map<Long, Rating> ratingIndex = new HashMap<>();
	
	private Serializer serializer;
	
	public MovieRecommenderAPI(){
		
	}
	
	public MovieRecommenderAPI(Serializer serializer){
		this.serializer = serializer;
	}
	

	public User addUser(String firstName, String lastName, String gender, String age, String occupation) {
		User user = new User(firstName, lastName, gender, age, occupation);
		userIndex.put(user.id, user);
		return user;
	}
	
//
//	public User addUser(Long id, String firstName, String lastName, String age, String gender, String occupation){
//		User user = new User(id, firstName, lastName, age, gender, occupation);
//		userIndex.put(user.id, user);
//		return user;
//	}
//	
	
	public void removeUser(Long userID) throws Exception{
		userIndex.remove(userID);
	}
	
	
	public User getUser(Long id){
		for(User user : userIndex.values())
			if(user.id == id)
				return user;
	    return null;
	  }
	
	
	public Collection<User> getUsers(){
		return userIndex.values();
	}
	

	public Movie addMovie(String title, String year, String url) {
		Movie movie = new Movie(title, year, url);
		movieIndex.put(movie.id, movie);
		return movie;
	}
	
	
	public void removeMovie(Movie movie) throws Exception{
		movieIndex.remove(movie.id);	
	}
	
	
	public Collection<Movie> getMovies(){
		return movieIndex.values();
	}


	public Rating addRating(long userID, long movieID, double rating) {
		Rating r = new Rating(userID, movieID, rating);
		Movie movie = getMovie(movieID);
		//movie.rating.add(r);
		ratingIndex.put(movieID, r);
		return r;	
	}

	
	public Movie getMovie(long movieID) {	
		for(Movie movie : movieIndex.values())
			if(movie.id == movieID)
				return movie;
			
		return null;	
	}

	
	public double getUserRating(long userID) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public Rating getRating(long movieID){
		return ratingIndex.get(movieID);
	}


	public double getUserRecommendations(long userID) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public Collection<Rating> getRatings(){
		return ratingIndex.values();
	}
	
	public double averageRating(){
		List<Rating> ratedMovies = new ArrayList<Rating>();
		double total = 0;
		int count = 0;
		for(Rating rating : ratingIndex.values()){
			total += rating.rating;
			count++;
		}
		if(count != 0){
			return total/count;
		}else{
			return 0;
		}
			
	}
	
	public List<Rating> ratings(){
		ArrayList<Rating> ratingIndex = new ArrayList<Rating>();
		for(User user : userIndex.values()){
			for(Rating rating : user.getRatings()){
				ratingIndex.add(rating);
			}
		}return ratingIndex;
	}
	
	@Override
	public List<Movie> getTopTenMovie(){
		List<Movie> movies = new ArrayList<Movie>();
		for(Movie movie : movieIndex.values()){
			movies.add(movie);
		}
		Collections.sort(movies);
		Collections.reverse(movies);
		if(movies.size()<10){
			return movies;
		}else{
			return movies.subList(0, 10);
		}
	}
	
	
	
//	public double averageRatings(long movieID) throws Exception{
//		double rating = 0;
//		int count = 0;
//		
//		Movie movie = getMovie(movieID);
//		
//		for(Rating r : ratingIndex.values()){
//			rating += movie.rating;
//			count ++;
//		}
//		if(count != 0){
//			return rating/count;
//		}else{
//			return 0;
//		}
//		
//	}
	
//	public List<Movie>topTopTenMovie() throws Exception{
//		for (Movie m : movieIndex.values())
//			averageRatings(m.id);
//		
//		List<Movie> movie = new ArrayList<>();
//		
//		Collections.sort(movie, new Comparator<Movie>(){
//			public int compare(Movie movie, Movie other){
//				return (int) (movie.rating - other.rating);
//			}
//		});
//		
//		if(movie.size()<10){
//			return movie;
//		}else{
//		return movie.subList(0, 10);
//		}
//	}
	

//	@Override
//	public List<Movie>getTopTenMovie(){
//		Collection<Movie> allMovie = getMovies();
//		List<Movie> movieList = new ArrayList<Movie>(allMovie);
//		//Collections.sort(movieList);
//		//Collections.reverse(movieList);
//		List<Movie> sub = movieList.subList(0, 10 > movieList.size() ? movieList.size() : 10);
//		return sub;	
//	}
	

	@SuppressWarnings("unchecked")
	public void load() throws Exception {
		serializer.read();
		ratingIndex		= (Map<Long, Rating>)	serializer.pop();
		movieIndex		= (Map<Long, Movie>)	serializer.pop();
		userIndex		= (Map<Long, User>)		serializer.pop();	
	}

	
	public void write() throws Exception {
		serializer.push(userIndex);
		serializer.push(movieIndex);
		serializer.push(ratingIndex);
		serializer.write();	
	}
	

	@Override
	public void prime() throws Exception {
		DataInput loader = new DataInput();
		List<User> users = loader.loadUsers("././data/users5.dat");
		for (User user : users){
			userIndex.put(user.id, user);
		}
		List<Movie> movies = loader.loadMovies("././data/items5.dat");
		for (Movie movie : movies){
			movieIndex.put(movie.id, movie);
		}
		List<Rating> ratings = loader.loadRatings("././data/ratings5.dat");
		for (Rating rating : ratings){
			addRating(rating.userId, rating.movieId, rating.rating);
			//addRatingList(rating.userId, rating.movieId, rating.rating);
		}
	}

	
	
}
