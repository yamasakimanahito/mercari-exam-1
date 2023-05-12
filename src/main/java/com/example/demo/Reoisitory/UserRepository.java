package com.example.demo.Reoisitory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.Domain.UserInfo;

/**
 * @author yamasakimanahito
 *
 */
@Repository
public class UserRepository {
	private static final RowMapper<UserInfo> USER_ROW_MAPPER = new BeanPropertyRowMapper<>(UserInfo.class);

	@Autowired
	NamedParameterJdbcTemplate template;

	/**
	 * Userの登録を行う.
	 * 
	 * @param user 登録対象のUser
	 */
	public void insert(UserInfo user) {
		String sql = "insert into users(name,mailaddress,password)values(:name,:mailaddress,:password);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		template.update(sql, param);
	}

	/**
	 * メールアドレスとパスワードからUserの情報を検索する.
	 * 
	 * @param mailaddress ネールアドレス
	 * @param password    パスワード
	 * @return 条件によって検索されたUser
	 */
	public UserInfo findByMailaddressAndPassword(String mailaddress, String password) {
		String sql = "select id,name,mailaddres,password from users where mailaddress=:mailaddress and password=:password;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("mailaddress", mailaddress).addValue("password",
				password);
		UserInfo user = template.queryForObject(sql, param, USER_ROW_MAPPER);
		return user;

	}

	/**
	 * メールアドレスからUserの情報を検索.
	 * 
	 * @param mailaddress メールアドレス
	 * @return 条件によって検索されたUser
	 */
	public UserInfo findByMailaddress(String mailaddress) {
		String sql = "select id,name,mailaddress,password from users where mailaddress=:mailaddress";
		SqlParameterSource param = new MapSqlParameterSource().addValue("mailaddress", mailaddress);
		List<UserInfo> userList = template.query(sql, param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}
}
