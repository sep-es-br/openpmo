package com.openpmoapi.repository;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.openpmoapi.model.Role;

public interface RoleRepository extends Neo4jRepository <Role, Long>{

	
	@Query("match (a:Actor) -[r:ACTS]-> (s:Scope) where id(s)= {id} return a,r,s")
	Collection<Role> findAllByScopeId(@Param("id") Long id);
	
	@Query("match (a:Actor) -[r:ACTS]-> (s:Scope) where id(a)= {id} return a,r,s")
	Collection<Role> findAllByActorId(@Param("id") Long id);


}
