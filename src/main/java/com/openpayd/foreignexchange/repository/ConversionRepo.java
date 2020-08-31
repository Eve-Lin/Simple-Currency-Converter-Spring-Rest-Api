package com.openpayd.foreignexchange.repository;

import com.openpayd.foreignexchange.model.entity.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ConversionRepo extends JpaRepository<Conversion, Long> {

    @Query("select c from Conversion c where extract (year from c.date) = :year and extract (month from c.date) = :month and extract (day from c.date) =:day")
    List<Conversion> findAllByYearAndMonthAndDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);

}
