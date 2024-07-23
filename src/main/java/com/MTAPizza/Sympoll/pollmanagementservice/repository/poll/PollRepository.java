package com.MTAPizza.Sympoll.pollmanagementservice.repository.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.model.poll.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface PollRepository extends JpaRepository<Poll, UUID> {
}
