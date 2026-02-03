package com.bpi.training.M6.Act1;

import java.util.List;

import com.bpi.training.M6.Act1.Entity.Student;

import com.bpi.training.M6.Act1.Entity.Course;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;



public class JPQLOperations {
	public static List<String> findStudentNames(EntityManager em){
		String jpql = "SELECT s.name FROM Student s";
		TypedQuery<String> query = em.createQuery(jpql, String.class);
		return query.getResultList();
		
	}
	
	public static Long countCoursesByStudentId(EntityManager em, Long id) {
		String jpql = "SELECT COUNT(c) FROM Course c WHERE c.student.id = :id";
		TypedQuery<Long> query = em.createQuery(jpql, Long.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	public static List<Student> findStudentsByAgeGreaterThan(EntityManager em, int age){
		String jpql = "SELECT s FROM Student s Where s.age > :age";
		TypedQuery<Student> query = em.createQuery(jpql, Student.class);
		query.setParameter("age", age);
		return query.getResultList();
	}
}
