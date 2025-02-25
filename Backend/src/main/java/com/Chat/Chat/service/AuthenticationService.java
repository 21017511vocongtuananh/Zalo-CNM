package com.Chat.Chat.service;

import com.Chat.Chat.dto.reponse.AuthResponse;
import com.Chat.Chat.dto.reponse.UserResponse;
import com.Chat.Chat.dto.request.*;

public interface AuthenticationService {

	UserResponse registerUser(UserRequest request);
	AuthResponse loginUser(AuthRequest authRequest);
	AuthResponse resetPassword(AuthRequest request);
}
