package io.github.japinl.service;

import io.github.japinl.woyun.domain.User;

public interface UserService {
	boolean isUsernameRegister(String name);
	boolean isEamilRegister(String email);
	boolean isPhoneRegister(String phone);
	boolean login(String name, String password);
	boolean register(User user);
}
