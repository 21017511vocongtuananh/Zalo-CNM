package com.Chat.Chat.service.Impl;

import com.Chat.Chat.dto.reponse.MessageResponse;
import com.Chat.Chat.dto.request.MessageRequest;
import com.Chat.Chat.exception.ErrorCode;
import com.Chat.Chat.exception.ErrorException;
import com.Chat.Chat.mapper.MessageMapper;
import com.Chat.Chat.model.Conversation;
import com.Chat.Chat.model.Message;
import com.Chat.Chat.model.User;
import com.Chat.Chat.repository.ConversationRepo;
import com.Chat.Chat.repository.MessageRepo;
import com.Chat.Chat.service.MessageService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

	private final MessageRepo messageRepo;
	private final MessageMapper messageMapper;
	private final ConversationRepo conversationRepo;
	@Override
	public MessageResponse createMessage(String conversationId, MessageRequest request, User currentUser) {
		Message message = new Message();
		message.setBody(request.getMessage());
		message.setImage(request.getImage());
		message.setConversationId(conversationId);
		message.setSenderId(currentUser.getId());
		message.setSeenIds(Collections.singletonList(currentUser.getId()));
		Message savedMessage = messageRepo.save(message);

		Conversation conversation = conversationRepo.findById(conversationId)
				.orElseThrow(() -> new RuntimeException("Conversation not found"));
		List<String> messageIds = new ArrayList<>(conversation.getMessagesIds());
		messageIds.add(savedMessage.getId());
		conversation.setMessagesIds(messageIds);
		conversationRepo.save(conversation);
		return messageMapper.toMessageResponse(savedMessage);
	}

	@Override
	public List<MessageResponse> getAllMessage(String conversationId) {
		List<Message> messages = messageRepo.findByConversationId(
				conversationId,
				Sort.by(Sort.Direction.ASC, "createdAt")
		);
		if(messages.isEmpty())
		{
			throw new ErrorException(ErrorCode.NOT_FOUND,"conversationId not found");
		}
		return messages.stream()
				.map(messageMapper::toMessageResponse)
				.collect(Collectors.toList());
	}

	public MultipartFile convertBase64ToMultipartFile(String base64) throws IOException {
		String[] parts = base64.split(",");
		if (parts.length < 2) {
			throw new IllegalArgumentException("Invalid Base64 format");
		}
		byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
		return new MockMultipartFile("image", "image.jpg", "image/jpeg", decodedBytes);
	}

}
