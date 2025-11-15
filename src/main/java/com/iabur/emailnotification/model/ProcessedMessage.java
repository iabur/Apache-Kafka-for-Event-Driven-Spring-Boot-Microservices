package com.iabur.emailnotification.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_message")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessedMessage {

    @Id
    @Column(name = "message_id", nullable = false, length = 200)
    private String messageId;

    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;

}

