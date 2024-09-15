package com.roster.backned.Repository;

import com.roster.backned.Entity.Roster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RosterRepository  extends JpaRepository<Roster, Long> {
}
