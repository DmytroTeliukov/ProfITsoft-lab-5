package dev.profitsoft.jfd.elasticsample.service;

import dev.profitsoft.jfd.elasticsample.ElasticSampleApplication;
import dev.profitsoft.jfd.elasticsample.config.TestElasticsearchConfiguration;
import dev.profitsoft.jfd.elasticsample.constant.EmailStatus;
import dev.profitsoft.jfd.elasticsample.data.EmailMessageData;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@SpringBootTest
@ContextConfiguration(classes = {ElasticSampleApplication.class, TestElasticsearchConfiguration.class})
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9093", "port=9093" })
public class EmailReceivedListenerTest {


    @Autowired
    private EmailService emailService;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    @DisplayName("should saved email with status SENT when send email")
    void testSuccessfulEmailSend() {
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        EmailMessageData email = new EmailMessageData();

        email.setRecipient("test@example.com");
        email.setSubject("Test Subject");
        email.setContent("Test Content");

        var id = emailService.sendMessage(email);

        // Fetch the updated email message from the repository
        EmailMessageData sentEmail = elasticsearchOperations.get(id, EmailMessageData.class);

        assertEquals(EmailStatus.SENT, sentEmail.getStatus());
        assertEquals(1, sentEmail.getAttemptCount());
        assertNull(sentEmail.getErrorMessage());
    }

    @Test
    @DisplayName("should saved email with status ERROR when send email")
    void testFailedEmailSend() {
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailSendException("Test Exception")).when(mailSender).send(any(MimeMessage.class));

        EmailMessageData email = new EmailMessageData();

        email.setRecipient("test@example.com");
        email.setSubject("Test Subject");
        email.setContent("Test Content");

        var id = emailService.sendMessage(email);
        EmailMessageData failedEmail = elasticsearchOperations.get(id, EmailMessageData.class);

        assertEquals(EmailStatus.ERROR, failedEmail.getStatus());
        assertEquals(1, failedEmail.getAttemptCount());
        assertEquals("org.springframework.mail.MailSendException: Test Exception", failedEmail.getErrorMessage());
    }
}

