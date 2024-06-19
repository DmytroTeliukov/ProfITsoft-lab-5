package dev.profitsoft.jfd.elasticsample.controller;

import dev.profitsoft.jfd.elasticsample.dto.EmailReceivedDto;
import dev.profitsoft.jfd.elasticsample.dto.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {
  @Value("${kafka.topic.emailReceived}")
  private String emailReceivedTopic;

  private final KafkaOperations<String, EmailReceivedDto> kafkaOperations;

  /**
   * Endpoint for sending a sample email message to a Kafka topic.
   *
   * <p>This method creates a sample EmailReceivedDto, sends it to the configured Kafka topic, and returns a success response.</p>
   *
   * @return a RestResponse indicating success
   */
  @PostMapping("/test/send")
  public RestResponse sendSampleEmailMessage() {
    EmailReceivedDto email = EmailReceivedDto.builder()
            .recipient("test@example.com")
            .subject("Test Subject")
            .content("Test Content")
            .build();


    kafkaOperations.send(emailReceivedTopic, email);

    return RestResponse.OK;
  }

}
