package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.DAO.Loan;
import com.example.demo.DTO.LoanDTO;
import com.example.demo.repository.LoanRepository;

/**
 * Service layer for managing {@link Loan} records.
 * <p>
 * Handles creation, querying, and deletion of loan records that track
 * which user has borrowed which book.
 * </p>
 */
@Service
public class LoanService {

    private static final Logger log = LoggerFactory.getLogger(LoanService.class);

    private final LoanRepository repo;

    /**
     * Constructs a {@code LoanService} with the given repository.
     *
     * @param repo the loan JPA repository
     */
    public LoanService(LoanRepository repo) {
        this.repo = repo;
    }

    /**
     * Persists a new loan record.
     *
     * @param loan the loan entity to save
     * @return the saved loan as a DTO
     */
    @Transactional
    public LoanDTO addLoan(Loan loan) {
        log.info("Creating loan for userId={}, bookId={}", loan.getUser().getId(), loan.getBook().getId());
        return toDto(repo.save(loan));
    }

    /**
     * Finds a loan record by book ID.
     *
     * @param bookId the book ID
     * @return the loan DTO, or {@code null} if not found
     */
    public LoanDTO searchByBookId(Long bookId) {
        log.debug("Searching loan by bookId={}", bookId);
        Loan loan = repo.findByBook_Id(bookId);
        return loan == null ? null : toDto(loan);
    }

    /**
     * Counts the number of active loans for a given user.
     *
     * @param userId the user ID
     * @return the number of loans
     */
    public Long loanByUser(Long userId) {
        log.debug("Counting loans for userId={}", userId);
        return repo.countByUser_Id(userId);
    }

    /**
     * Checks whether a specific user has an active loan for a specific book.
     *
     * @param bookId the book ID
     * @param userId the user ID
     * @return {@code true} if such a loan exists
     */
    public boolean userHasLoanForBook(Long bookId, Long userId) {
        return repo.existsByBook_IdAndUser_Id(bookId, userId);
    }

    /**
     * Deletes all loans associated with a given book.
     * Used before a book is deleted from the system.
     *
     * @param bookId the book ID
     * @return the number of deleted records
     */
    @Transactional
    public long deleteByBookId(Long bookId) {
        log.info("Deleting all loans for bookId={}", bookId);
        return repo.deleteByBook_Id(bookId);
    }

    /**
     * Deletes a specific loan for a given book and user.
     * Used when a user returns a book.
     *
     * @param bookId the book ID
     * @param userId the user ID
     * @return the number of deleted records
     */
    @Transactional
    public long deleteByBookIdAndUserId(Long bookId, Long userId) {
        log.info("Deleting loan for bookId={}, userId={}", bookId, userId);
        return repo.deleteByBook_IdAndUser_Id(bookId, userId);
    }

    /**
     * Converts a {@link Loan} entity to a {@link LoanDTO}.
     *
     * @param l the loan entity
     * @return the corresponding DTO
     */
    private LoanDTO toDto(Loan l) {
        LoanDTO dto = new LoanDTO();
        dto.setId(l.getLoanID());
        dto.setBookId(l.getBook().getId());
        dto.setUserId(l.getUser().getId());
        return dto;
    }
}
