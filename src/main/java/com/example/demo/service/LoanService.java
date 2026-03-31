package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.DAO.Loan;
import com.example.demo.DTO.LoanDTO;
import com.example.demo.repository.LoanRepository;

@Service
public class LoanService {
	
	private final LoanRepository repo;
	
	public LoanService(LoanRepository repo) {
		this.repo = repo;
	}
	
	@Transactional
	public LoanDTO addLoan(Loan loan) {
		return toDto(repo.save(loan));
	}
	
	public LoanDTO searchByBookId(Long bookId) {
		Loan loan = repo.findByBook_Id(bookId);
		return loan == null ? null : toDto(loan);
	}

	public Long loanByUser(Long userId) {
		return repo.countByUser_Id(userId);
	}
	
	public boolean userHasLoanForBook(Long bookId, Long userId) {
		return repo.existsByBook_IdAndUser_Id(bookId, userId);
	}
	
	@Transactional
	public long deleteByBookId(Long bookId) {
		return repo.deleteByBook_Id(bookId);
	}
	
	@Transactional
	public long deleteByBookIdAndUserId(Long bookId, Long userId) {
		return repo.deleteByBook_IdAndUser_Id(bookId, userId);
	}
	
	private LoanDTO toDto(Loan l) {
		LoanDTO dto = new LoanDTO();
		dto.setId(l.getLoanID());
		dto.setBookId(l.getBook().getId());
		dto.setUserId(l.getUser().getId());
		return dto;
	}
}
