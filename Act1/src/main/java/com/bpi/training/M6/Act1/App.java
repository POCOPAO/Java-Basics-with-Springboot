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
	    		M6Activity4Soulution(em);

	    		
	    		
	    	} finally {
	    		EntityManagerUtil.getInstance().closeEntityManager(em);
	    		EntityManagerUtil.getInstance().shutdownFactory();
	    	}
	  }
	   
	   static void M6Activity4Soulution(EntityManager em) {
			
			
				em.getTransaction().begin();

				Student newStudent = new Student();
				newStudent.setName("Baby Santos");
				newStudent.setAge(20);
				newStudent.setEmail("baby@email.com");
				
				em.persist(newStudent);
				
				em.flush();
				
				em.detach(newStudent);
				
				System.out.println("is newStudent inside persistence context: " + em.contains(newStudent));
				
				Student managedAgain = em.merge(newStudent);
				
				managedAgain.setAge(21);
				managedAgain.setEmail("baby.update@email.com");
				
				em.flush();
				
				System.out.println("is newStudent inside persistence context: " + em.contains(managedAgain));
				
				em.remove(managedAgain);
				
				em.flush();
				
				System.out.println("is newStudent inside persistence context: " + em.contains(managedAgain));
				
				em.getTransaction().commit();
			

		}

}
