package br.com.letscode.eventservice.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface EventRepository extends JpaRepository<Event, Long> {

}
