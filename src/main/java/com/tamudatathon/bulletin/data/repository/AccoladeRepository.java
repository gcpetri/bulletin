package com.tamudatathon.bulletin.data.repository;

import com.tamudatathon.bulletin.data.entity.Accolade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccoladeRepository extends JpaRepository<Accolade, Long> {
}
