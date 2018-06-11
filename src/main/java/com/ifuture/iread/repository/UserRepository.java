package com.ifuture.iread.repository;

import com.ifuture.iread.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by maofn on 2017/3/13.
 */
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    User findByUserName(String username);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    List<User> findAll();

}
