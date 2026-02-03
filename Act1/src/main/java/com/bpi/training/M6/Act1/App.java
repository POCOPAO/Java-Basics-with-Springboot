package com.bpi.training.M6.Act1;

import com.bpi.training.M6.Act1.Entity.Student;
import jakarta.persistence.EntityManager;

public class App {
	
	   public static void main(String[] args) {
	    	testConnection();
	   }
	   
	   static void testConnection() {
	    	EntityManager em = EntityManagerUtil.getInstance().createEntityManager();
	    	
	    	try {
	    		runM6Activity2(em);

	    		
	    		
	    	} finally {
	    		EntityManagerUtil.getInstance().closeEntityManager(em);
	    		EntityManagerUtil.getInstance().shutdownFactory();
	    	}
	  }
	   
	   static void runM6Activity2(EntityManager em) {
			
			
				em.getTransaction().begin();

				Student newStudent = new Student();
				newStudent.setName("Pedro Dela Rosa");
				newStudent.setAge(50);
				newStudent.setEmail("pedrodelarosa@gmail.com");

				em.persist(newStudent);
				em.getTransaction().commit();
			

		}

}
