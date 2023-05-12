package com.example.demo.Form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * User情報を表すドメイン.
 * 
 * @author yamasakimanahito
 *
 */
public class UserInfoForm {
	/* ID */
	private Integer id;
	/* 名前 */
	private String name;
	@Pattern(regexp = "^([a-zA-Z0-9])+([a-zA-Z0-9\\._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\\._-]+)$", message = "メールアドレスを入力して下さい")
	/* メールアドレス */
	private String mailaddress;
	@NotBlank(message = "パスワードを入力してください")
	/* パスワード */
	private String password;

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", name=" + name + ", mailaddress=" + mailaddress + ", password=" + password
				+ "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMailaddress() {
		return mailaddress;
	}

	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
