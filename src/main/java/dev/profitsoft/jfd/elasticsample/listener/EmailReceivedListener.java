package dev.profitsoft.jfd.elasticsample.listener;

import dev.profitsoft.jfd.elasticsample.data.EmailMessageData;
import dev.profitsoft.jfd.elasticsample.dto.EmailReceivedDto;
import dev.profitsoft.jfd.elasticsample.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailReceivedListener {
    private final EmailService emailService;

    /**
     * Handles received email messages from the Kafka topic.
     *
     * <p>This method listens for messages from the specified Kafka topic, logs the received email,
     * converts the received DTO into an EmailMessageData object, and sends the email using the EmailService.</p>
     *
     * @param emailReceivedDto the data transfer object containing the received email information
     */
    @KafkaListener(topics = "${kafka.topic.emailReceived}", groupId = "${spring.kafka.consumer.group-id}")
    public void emailReceived(EmailReceivedDto emailReceivedDto) {
        EmailMessageData emailMessageData = new EmailMessageData();
        emailMessageData.setRecipient(emailReceivedDto.getRecipient());
        emailMessageData.setSubject(emailReceivedDto.getSubject());
        emailMessageData.setContent(emailReceivedDto.getContent());

        emailService.sendMessage(emailMessageData);
    }
}
