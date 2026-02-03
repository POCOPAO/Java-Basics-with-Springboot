package com.bpi.training.M6.Act1;

import java.util.List;

import com.bpi.training.M6.Act1.Entity.Student;

import jakarta.persistence.EntityManager;


public class App {
	
	   public static void main(String[] args) {
	    	testConnection();
	   }
	   
	   static void testConnection() {
	    	EntityManager em = EntityManagerUtil.getInstance().createEntityManager();
	    	
	    	try {
	    		M6Activity5Soulution(em);

	    		
	    		
	    	} finally {
	    		EntityManagerUtil.getInstance().closeEntityManager(em);
	    		EntityManagerUtil.getInstance().shutdownFactory();
	    	}
	  }
	   
	   static void M6Activity5Soulution(EntityManager em) {
			
			
				List<String> names = JPQLOperations.findStudentNames(em);
				System.out.println("Student Name:");
				names.forEach(System.out::println);
				
				Long courseCount = JPQLOperations.countCoursesByStudentId(em, 1L);
				System.out.println("\nCourses for student id = 1 : " + courseCount);
				
				List<Student> olderStudents = JPQLOperations.findStudentsByAgeGreaterThan(em, 18);
				System.out.println("\nStudents older than 18:");
				for (Student s : olderStudents) {
					System.out.println(s.getId() + " - " + s.getName() + " - " + s.getAge()  );
				}
		 }

}
