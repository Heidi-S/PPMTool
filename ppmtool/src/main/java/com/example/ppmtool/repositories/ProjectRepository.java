package com.example.ppmtool.repositories;

import com.example.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{

	/*
	@Override
	default Iterable<Project> findAllById(Iterable<Long> ids) {
		return null;
	}
	*/
	Project findByProjectIdentifier(String projectIdentifier);

	@Override
	Iterable<Project> findAll();
	
	Iterable<Project> findAllByProjectLeader(String username);
	
	
	
	

}
