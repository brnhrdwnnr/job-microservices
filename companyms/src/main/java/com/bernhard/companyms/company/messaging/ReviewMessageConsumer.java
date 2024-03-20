package com.bernhard.companyms.company.messaging;

import com.bernhard.companyms.company.CompanyService;
import com.bernhard.companyms.company.dto.ReviewMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewMessageConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final CompanyService companyService;

    @RabbitListener(queues =  "companyRatingQueue")
    public void consumeMessage(ReviewMessage reviewMessage) {
        companyService.updateCompanyRating(reviewMessage);
    }

}