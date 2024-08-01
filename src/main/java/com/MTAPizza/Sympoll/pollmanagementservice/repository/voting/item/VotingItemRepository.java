package com.MTAPizza.Sympoll.pollmanagementservice.repository.voting.item;

import com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item.VotingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VotingItemRepository extends JpaRepository<VotingItem, Integer> {
}
