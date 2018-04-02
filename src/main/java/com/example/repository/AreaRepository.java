package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.Area;

public interface AreaRepository extends JpaRepository<Area, Integer> {

}
