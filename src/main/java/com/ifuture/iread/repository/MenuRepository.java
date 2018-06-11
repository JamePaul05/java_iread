package com.ifuture.iread.repository;

import com.ifuture.iread.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by maofn on 2017/3/13.
 */
@Repository
public interface MenuRepository extends PagingAndSortingRepository<Menu, String> {

    Page<Menu> findAll(Specification<Menu> spec, Pageable pageable);

    List<Menu> findAll();

}
