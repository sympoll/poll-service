package com.MTAPizza.Sympoll.pollmanagementservice.model.repository;

import com.MTAPizza.Sympoll.pollmanagementservice.model.poll.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PollRepository extends JpaRepository<Poll, Integer> {
}
