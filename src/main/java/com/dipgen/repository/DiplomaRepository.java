package com.dipgen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dipgen.entity.Diploma;

public interface DiplomaRepository extends JpaRepository<Diploma, Integer> {

	public Diploma findByDiplomaIdAndUserName(int diplomaId, String username);

	@Query("select d from Diploma d where user.name = :username order by d.diplomaId")
	public List<Diploma> findByUserName(@Param("username") String username);

}
