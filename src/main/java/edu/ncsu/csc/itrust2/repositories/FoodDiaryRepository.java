package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.models.User;

import java.util.List;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodDiaryRepository extends JpaRepository<FoodDiary, Long> {
    
    public List<FoodDiary> findByPatient(@NotNull User patient);
}
