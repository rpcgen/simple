package com.simple.service;

import com.simple.domain.SimpleComment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleCommentRepository extends PagingAndSortingRepository<SimpleComment, Long> {
}
