package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.DAO.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
	Loan findByBook_Id(Long bookId);
	Long countByUser_Id(Long userId);
	boolean existsByBook_IdAndUser_Id(Long bookId, Long userId);
	Long deleteByBook_Id(Long bookId);
	Long deleteByBook_IdAndUser_Id(Long bookId, Long userId);
}
