package com.ifuture.iread.repository;

import com.ifuture.iread.entity.BookletTrace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by maofn on 2017/5/18.
 */
@Repository
public interface BookletTraceRepository extends CrudRepository<BookletTrace, String> {
}
