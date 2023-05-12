package com.example.demo.Domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * ログインUserに関するドメイン.
 * 
 * @author yamasakimanahito
 *
 */
public class LoginUser extends User {
	private static final long serialVersionUID = 1L;
	/** 管理者情報 */
	private final UserInfo userInfo;

	/**
	 * ログインUserを表すコンストラクタ
	 * 
	 * @param userInfo      ログインしているUserの情報
	 * @param authorityList
	 */
	public LoginUser(UserInfo userInfo, Collection<GrantedAuthority> authorityList) {

		super(userInfo.getMailaddress(), userInfo.getPassword(), authorityList);
		this.userInfo = userInfo;
	}

	/**
	 * 現在ログインしているUserの情報
	 * 
	 * @return Userの情報
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}

}
