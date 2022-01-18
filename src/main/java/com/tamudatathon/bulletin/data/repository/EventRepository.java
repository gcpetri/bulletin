package com.tamudatathon.bulletin.data.repository;

import com.tamudatathon.bulletin.data.entity.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
