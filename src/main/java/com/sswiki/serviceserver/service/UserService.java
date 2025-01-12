package com.sswiki.serviceserver.service;

import com.sswiki.serviceserver.repository.BreadRepository;
import com.sswiki.serviceserver.repository.ReviewLikesRepository;
import com.sswiki.serviceserver.repository.ReviewRepository;
import com.sswiki.serviceserver.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final S3Service s3Service;
    private final ReviewRepository reviewRepository;
    private final ReviewLikesRepository reviewLikesRepository;
    private final BreadRepository breadRepository;
    private final UserRepository userRepository;

    public UserService(
            S3Service s3Service,
            ReviewRepository reviewRepository,
            ReviewLikesRepository reviewLikesRepository,
            BreadRepository breadRepository,
            UserRepository userRepository
    ) {
        this.s3Service = s3Service;
        this.reviewRepository = reviewRepository;
        this.reviewLikesRepository = reviewLikesRepository;
        this.breadRepository = breadRepository;
        this.userRepository = userRepository;
    }


}
