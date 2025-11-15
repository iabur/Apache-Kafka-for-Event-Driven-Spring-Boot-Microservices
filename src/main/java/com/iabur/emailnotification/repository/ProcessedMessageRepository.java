package com.iabur.emailnotification.repository;

import com.iabur.emailnotification.model.ProcessedMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedMessageRepository extends CrudRepository<ProcessedMessage, String> {
}

