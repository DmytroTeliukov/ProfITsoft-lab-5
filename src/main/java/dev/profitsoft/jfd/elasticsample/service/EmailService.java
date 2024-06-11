package dev.profitsoft.jfd.elasticsample.service;

import dev.profitsoft.jfd.elasticsample.data.EmailMessageData;

public interface EmailService {
    /**
     * Sends an email message based on the provided EmailMessageData.
     *
     * <p>The method updates the attempt count and last attempt time, attempts to send the email,
     * and updates the status and error message based on the outcome. It then saves the email message
     * data to the repository and returns the ID of the saved entity.</p>
     *
     * @param emailMessageData the email message data
     * @return the ID of the saved email message data
     */
    String sendMessage(EmailMessageData emailMessageData);
}
