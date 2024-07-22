package com.MTAPizza.Sympoll.pollmanagementservice.repository.answer;

import com.MTAPizza.Sympoll.pollmanagementservice.model.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
