package com.bpi.training.M6.Act1.Entity;


import jakarta.persistence.*;
@Entity
@Table(name = "courses")
public class Course {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name= "courseName")
	private String courseName;
	
	private String grade;
	
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;
	
	//Getters and Setters
	public Long getId() { return id; }
	public void setId(Long id) {this.id = id;}

    public String getcourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
	
}
