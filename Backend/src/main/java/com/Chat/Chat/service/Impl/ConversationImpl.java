package com.Chat.Chat.service.Impl;

import com.Chat.Chat.dto.reponse.ConversationResponse;
import com.Chat.Chat.exception.ErrorCode;
import com.Chat.Chat.exception.ErrorException;
import com.Chat.Chat.mapper.ConversationMapper;
import com.Chat.Chat.model.Conversation;
import com.Chat.Chat.model.User;
import com.Chat.Chat.repository.ConversationRepo;
import com.Chat.Chat.service.ConversationService;
import com.Chat.Chat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConversationImpl implements ConversationService {
	private final ConversationRepo conversationRepo;
	private final UserService userService;
	private final ConversationMapper conversationMapper;

	@Override
	public ConversationResponse getConversationId(String id) {
		Conversation conversation = conversationRepo.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND,"conversation id not found"));
		ConversationResponse conversationResponse = conversationMapper.toConversationResponseUser(conversation);
		return conversationResponse;
	}

//	@Override
//	public Response createConversation(ConversationDto conversationDto) {
//		Conversation conversation = Conversation.builder()
//				.name(conversationDto.getName())
//				.isGroup(conversationDto.getIsGroup())
//				.messages(entityMapper.mapMessageDtoBasic(conversationDto.getMessages()))
//				.build();
//		Conversation conversation1 = conversationRepo.save(conversation);
//		ConversationDto conversationDto1 = entityMapper.mapConversationDtoBasic(conversation1);
//		return Response.builder()
//				.status(200)
//				.message("conversation successfully add")
//				.conversation(conversationDto1)
//				.build();
//	}

	@Override
	public List<ConversationResponse> getConversations() {
		User currentUser = userService.getLoginUser();
		if(currentUser == null)
		{
			throw new ErrorException(ErrorCode.UNAUTHORIZED,"Unauthorized: User not logged in");
		}
		List<ConversationResponse> conversationResponses  = conversationRepo.findByGroupMembersUserIdOrderByLastMessageAtDesc(currentUser.getId())
				.stream()
				.map(conversationMapper::toConversationResponse)
				.collect(Collectors.toList());

		return conversationResponses;
	}
}
