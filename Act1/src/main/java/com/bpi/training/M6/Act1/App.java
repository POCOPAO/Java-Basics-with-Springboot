package com.bpi.training.M6.Act1;

import com.bpi.training.M6.Act1.Entity.Student;

import com.bpi.training.M6.Act1.Entity.Course;
import jakarta.persistence.EntityManager;


public class App {
	
	   public static void main(String[] args) {
	    	testConnection();
	   }
	   
	   static void testConnection() {
	    	EntityManager em = EntityManagerUtil.getInstance().createEntityManager();
	    	
	    	try {
	    		persistOneToMany(em);

	    		
	    		
	    	} finally {
	    		EntityManagerUtil.getInstance().closeEntityManager(em);
	    		EntityManagerUtil.getInstance().shutdownFactory();
	    	}
	  }
	   
	   static void persistOneToMany(EntityManager em) {
			
			
				em.getTransaction().begin();

				Student student1 = em.find(Student.class, 1L);
				
				
				Course newCourse = new Course();
				newCourse.setCourseName("Math");
				newCourse.setGrade("90");
				newCourse.setStudent(student1);
				
				
				
				em.persist(newCourse);
				em.getTransaction().commit();
			

		}

}
