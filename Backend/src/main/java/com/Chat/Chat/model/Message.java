package com.Chat.Chat.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "message")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	@Id
	private String id;

	private String body;

	private String image;

	@Field("createdAt")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Field("seenIds")
	@Builder.Default
	private List<String> seenIds = new ArrayList<>();

	@Field("conversationId")
	private String conversationId;

	@Field("senderId")
	private String senderId;
}
