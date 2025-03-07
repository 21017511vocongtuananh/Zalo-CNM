package com.Chat.Chat.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "conversation")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
	@Id
	private String id;

	private String name;

	@Field("isGroup")
	private Boolean isGroup;

	@Field("createdAt")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Field("lastMessageAt")
	private LocalDateTime lastMessageAt = LocalDateTime.now();

	@Field("messagesIds")
	@Builder.Default
	private List<String> messagesIds = new ArrayList<>();

	@Field("groupMembers")
	@Builder.Default
	private List<GroupMember> groupMembers = new ArrayList<>();
}
