package com.example.resource.service.repository;

import com.example.resource.service.entity.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, Long> {
	List<Resource> findByIdIn(Set<Long> resourceIds);
	List<Resource> findByIdBetween(Long startId, Long endId);
}
