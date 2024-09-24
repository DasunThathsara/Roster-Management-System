package com.roster.backned.Repository;

import com.roster.backned.Dto.AttendanceDetails;
import com.roster.backned.Dto.WeekShiftDto;
import com.roster.backned.Entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    @Query("SELECT new com.roster.backned.Dto.WeekShiftDto(s.id,s.duty,s.startTime,s.endTime,s.user.firstName || ' ' || s.user.lastName) " +
            "FROM Shift s " +
            "JOIN User u ON u = s.user " +
            "WHERE s.roster.date " +
            "BETWEEN :startDate " +
            "AND :endDate " +
            "AND s.roster.restaurant.id = :restaurantId")
    List<WeekShiftDto> findShiftsByDateRangeAndRestaurant(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("restaurantId") long restaurantId);

    @Query("SELECT new com.roster.backned.Dto.AttendanceDetails(s.id, s.duty, s.startTime, s.endTime, s.roster.restaurant.id,s.roster.restaurant.name) " +
            "FROM Shift s " +
            "WHERE s.user.userId = :userId")
    List<AttendanceDetails> findShiftsByUserId(@Param("userId") long userId);
}
