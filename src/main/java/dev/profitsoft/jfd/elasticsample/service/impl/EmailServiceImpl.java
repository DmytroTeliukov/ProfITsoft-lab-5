package dev.profitsoft.jfd.elasticsample.service.impl;

import dev.profitsoft.jfd.elasticsample.constant.EmailStatus;
import dev.profitsoft.jfd.elasticsample.data.EmailMessageData;
import dev.profitsoft.jfd.elasticsample.repository.EmailRepository;
import dev.profitsoft.jfd.elasticsample.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableAsync(proxyTargetClass = true)
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;

    @Override
    public String sendMessage(EmailMessageData emailMessageData) {
        emailMessageData.setAttemptCount(emailMessageData.getAttemptCount() + 1);
        emailMessageData.setLastAttemptTime(LocalDateTime.now());

        try {
            sendEmail(emailMessageData);
            emailMessageData.setStatus(EmailStatus.SENT);
            emailMessageData.setErrorMessage(null);
        } catch (MailException | MessagingException e) {
            emailMessageData.setStatus(EmailStatus.ERROR);
            emailMessageData.setErrorMessage(e.getClass().getName() + ": " + e.getMessage());
        }

        return emailRepository.save(emailMessageData).getId();
    }

    /**
     * Sends an email asynchronously.
     *
     * <p>This method constructs a MimeMessage using the provided EmailMessageData and sends it using
     * the JavaMailSender. It throws a MessagingException if the email sending fails.</p>
     *
     * @param emailMessageData the email message data
     * @throws MessagingException if the email sending fails
     */
    @Async
    protected void sendEmail(EmailMessageData emailMessageData) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(emailMessageData.getRecipient());
        helper.setSubject(emailMessageData.getSubject());
        helper.setText(emailMessageData.getContent(), true);
        mailSender.send(mimeMessage);
    }

    /**
     * Retries sending failed emails at fixed intervals.
     *
     * <p>This method is scheduled to run at a fixed rate specified by the `email.retry.interval`
     * property. It retrieves all emails with ERROR status and attempts to resend them.</p>
     */
    @Scheduled(fixedRateString = "${email.retry.interval}")
    public void retryFailedEmails() {
        List<EmailMessageData> failedEmails = emailRepository.findByStatus(EmailStatus.ERROR);

        failedEmails.forEach(this::sendMessage);
    }
}
