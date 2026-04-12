package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.DTO.BookDTO;
import com.example.demo.DTO.BookDTOMasked;
import com.example.demo.DTO.LoanDTO;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.LibaryService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit/integration tests for {@link BookController}.
 * <p>
 * Uses {@link WebMvcTest} to load only the web layer. {@link LibaryService} is mocked.
 * Tests verify HTTP status codes, response bodies, and security role enforcement.
 * </p>
 */
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibaryService libaryService;

    // ===== GET /api/books =====

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /api/books: USER can browse books")
    void getBooks_asUser_returns200() throws Exception {
        BookDTOMasked masked = new BookDTOMasked("Clean Code", "Robert Martin", true);
        when(libaryService.getBooks(null, null, null)).thenReturn(List.of(masked));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[0].author").value("Robert Martin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/books: ADMIN can browse books")
    void getBooks_asAdmin_returns200() throws Exception {
        when(libaryService.getBooks(null, null, null)).thenReturn(List.of());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/books: unauthenticated returns 401")
    void getBooks_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
    }

    // ===== POST /api/books/addBook =====

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /addBook: ADMIN can add a book")
    void addBook_asAdmin_returns201() throws Exception {
        BookDTO dto = new BookDTO(1L, "Clean Code", "Robert Martin", true, LocalDate.now());
        when(libaryService.addBook("Clean Code", "Robert Martin")).thenReturn(dto);

        mockMvc.perform(post("/api/books/addBook")
                        .param("title", "Clean Code")
                        .param("author", "Robert Martin")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /addBook: USER is forbidden")
    void addBook_asUser_returns403() throws Exception {
        mockMvc.perform(post("/api/books/addBook")
                        .param("title", "Clean Code")
                        .param("author", "Robert Martin")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    // ===== DELETE /api/books/delBook =====

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /delBook: ADMIN successfully deletes a book")
    void delBook_asAdmin_returns200() throws Exception {
        when(libaryService.deleteBookByTitleAndAuthor("Clean Code", "Robert Martin")).thenReturn(1L);

        mockMvc.perform(delete("/api/books/delBook")
                        .param("title", "Clean Code")
                        .param("author", "Robert Martin")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted 1 record(s)."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /delBook: returns 400 when book is too new")
    void delBook_tooNew_returns400() throws Exception {
        when(libaryService.deleteBookByTitleAndAuthor(any(), any()))
                .thenThrow(new BusinessException("cannot be deleted: it was created less than 1 week ago."));

        mockMvc.perform(delete("/api/books/delBook")
                        .param("title", "Clean Code")
                        .param("author", "Robert Martin")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /delBook: returns 400 when book is older than 1 year")
    void delBook_tooOld_returns400() throws Exception {
        when(libaryService.deleteBookByTitleAndAuthor(any(), any()))
                .thenThrow(new BusinessException("cannot be deleted: it is older than 1 year."));

        mockMvc.perform(delete("/api/books/delBook")
                        .param("title", "Old Book")
                        .param("author", "Old Author")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /delBook: returns 404 when book not found")
    void delBook_notFound_returns404() throws Exception {
        when(libaryService.deleteBookByTitleAndAuthor(any(), any()))
                .thenThrow(new ResourceNotFoundException("No book found"));

        mockMvc.perform(delete("/api/books/delBook")
                        .param("title", "Unknown")
                        .param("author", "Nobody")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("DELETE /delBook: USER is forbidden")
    void delBook_asUser_returns403() throws Exception {
        mockMvc.perform(delete("/api/books/delBook")
                        .param("title", "Clean Code")
                        .param("author", "Robert Martin")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    // ===== PATCH /api/books/updateBook =====

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /updateBook: ADMIN can update a book")
    void updateBook_asAdmin_returns200() throws Exception {
        BookDTO dto = new BookDTO(1L, "Updated Title", "Updated Author", true, LocalDate.now().minusWeeks(2));
        when(libaryService.updateBook(1L, "Updated Title", "Updated Author")).thenReturn(dto);

        mockMvc.perform(patch("/api/books/updateBook")
                        .param("id", "1")
                        .param("title", "Updated Title")
                        .param("author", "Updated Author")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    // ===== POST /api/books/borrowBook =====

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /borrowBook: USER can borrow an available book")
    void borrowBook_asUser_returns201() throws Exception {
        LoanDTO loan = new LoanDTO();
        loan.setId(1L);
        loan.setBookId(5L);
        loan.setUserId(2L);
        when(libaryService.borrowBook(5L)).thenReturn(loan);

        mockMvc.perform(post("/api/books/borrowBook")
                        .param("bookId", "5")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").value(5));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /borrowBook: returns 400 when book is already loaned")
    void borrowBook_alreadyLoaned_returns400() throws Exception {
        when(libaryService.borrowBook(5L))
                .thenThrow(new BusinessException("Book is already loaned"));

        mockMvc.perform(post("/api/books/borrowBook")
                        .param("bookId", "5")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /borrowBook: ADMIN is forbidden from borrowing")
    void borrowBook_asAdmin_returns403() throws Exception {
        mockMvc.perform(post("/api/books/borrowBook")
                        .param("bookId", "5")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    // ===== POST /api/books/returnBook =====

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /returnBook: USER can return a book")
    void returnBook_asUser_returns200() throws Exception {
        when(libaryService.returnBook(5L)).thenReturn(1L);

        mockMvc.perform(post("/api/books/returnBook")
                        .param("bookId", "5")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /returnBook: returns 400 when user does not have this book on loan")
    void returnBook_noActiveLoan_returns400() throws Exception {
        when(libaryService.returnBook(5L))
                .thenThrow(new BusinessException("Current user does not have this book on loan"));

        mockMvc.perform(post("/api/books/returnBook")
                        .param("bookId", "5")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
