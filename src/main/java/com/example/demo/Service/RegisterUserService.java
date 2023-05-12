package com.example.demo.Service;

import org.checkerframework.checker.units.qual.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Domain.UserInfo;
import com.example.demo.Reoisitory.UserRepository;

/**
 * @author yamasakimanahito
 *
 */
@Service
@Temperature
public class RegisterUserService {
	@Autowired
	UserRepository userRepository;

	/**
	 * Userの登録を行う.
	 * 
	 * @param user 登録対象のUser
	 */
	public void insert(UserInfo user) {
		userRepository.insert(user);
	}

	/**
	 * メールアドレスによってUser情報を検索.
	 * 
	 * @param email メールアドレス
	 * @return 条件によって検索されたUser
	 */
	public UserInfo findUserByMailaddress(String email) {
		UserInfo user = userRepository.findByMailaddress(email);
		return user;
	}

}
