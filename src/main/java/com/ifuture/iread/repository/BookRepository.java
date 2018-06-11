package com.ifuture.iread.repository;

import com.ifuture.iread.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by maofn on 2017/3/26.
 */
@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, String> {

    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

    List<Book> findAll();

    Book findByIsbn(String isbn);

    @Query("from Book where isbn = :isbn")
    Book findOneByIsbn(@Param("isbn") String isbn);
}
