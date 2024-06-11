package dev.profitsoft.jfd.elasticsample.data;

import dev.profitsoft.jfd.elasticsample.constant.EmailStatus;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(indexName = "email_messages")
public class EmailMessageData {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String recipient;

    @Field(type = FieldType.Text)
    private String subject;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private EmailStatus status;

    @Field(type = FieldType.Text)
    private String errorMessage;

    @Field(type = FieldType.Byte)
    private int attemptCount;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime lastAttemptTime;
}
