package com.ifuture.iread.repository;

import com.ifuture.iread.entity.City;
import com.ifuture.iread.entity.MemberLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by maofn on 2017/3/23.
 */
@Repository
public interface MemberLevelRepository extends PagingAndSortingRepository<MemberLevel, String> {

    Page<MemberLevel> findAll(Specification<MemberLevel> spec, Pageable pageable);

    List<MemberLevel> findAll();

    @Query(" from MemberLevel where enable = true ")
    List<MemberLevel> findAllEnable();
}
