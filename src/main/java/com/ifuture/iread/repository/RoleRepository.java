package com.ifuture.iread.repository;

import com.ifuture.iread.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by maofn on 2017/3/16.
 */
@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, String> {
    List<Role> findAll();

    Page<Role> findAll(Specification<Role> spec, Pageable pageable);

    Role findOneByCode(String code);
}
