package dev.profitsoft.jfd.elasticsample.repository;
import dev.profitsoft.jfd.elasticsample.constant.EmailStatus;
import dev.profitsoft.jfd.elasticsample.data.EmailMessageData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends ElasticsearchRepository<EmailMessageData, String> {
    List<EmailMessageData> findByStatus(EmailStatus status);
}

