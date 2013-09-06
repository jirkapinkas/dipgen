package com.dipgen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dipgen.entity.GeneratorString;

public interface GeneratorStringRepository extends JpaRepository<GeneratorString, Integer> {

}
