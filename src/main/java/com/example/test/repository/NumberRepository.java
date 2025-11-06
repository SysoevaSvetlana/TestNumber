package com.example.test.repository;

import com.example.test.entity.NumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NumberRepository extends JpaRepository<NumberEntity, Long> {
    
    @Query("SELECT n.value FROM NumberEntity n ORDER BY n.value ASC")
    List<Integer> findAllValuesSorted();
}

