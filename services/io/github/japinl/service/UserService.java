package io.github.japinl.service;

public interface UserService {
	boolean isUsernameRegister(String name);
	boolean isEamilRegister(String email);
	boolean isPhoneRegister(String phone);
	boolean login(String name, String password);
}
