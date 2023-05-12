package com.example.demo.Service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Domain.LoginUser;
import com.example.demo.Domain.UserInfo;
import com.example.demo.Reoisitory.UserRepository;

/**
 * ログイン後の管理者情報に権限情報を付与するサービスクラス.
 * 
 * @author yamasakimanahito
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	/** DBから情報を得るためのリポジトリ */
	@Autowired
	private UserRepository userRepository;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 *      loadUserByUsername(java.lang.String) DBから検索をし、ログイン情報を構成して返す。
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("email:" + email);
		UserInfo userInfo = userRepository.findByMailaddress(email);
		if (userInfo == null) {
			throw new UsernameNotFoundException("そのEmailは登録されています。");
			// 権限付与の例
		}
		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new LoginUser(userInfo, authorityList);
	}
}
