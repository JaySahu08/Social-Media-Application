package com.SocialMediaApplication.SocialMediaApplication.repository;

import com.SocialMediaApplication.SocialMediaApplication.entity.FriendRequest;
import com.SocialMediaApplication.SocialMediaApplication.entity.FriendRequestStatus;
import com.SocialMediaApplication.SocialMediaApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    boolean existsBySenderAndReceiverAndStatus(User sender,
                                               User receiver,
                                               FriendRequestStatus status);

    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status);

    Optional<FriendRequest> findByIdAndReceiver(Long id, User receiver);
}