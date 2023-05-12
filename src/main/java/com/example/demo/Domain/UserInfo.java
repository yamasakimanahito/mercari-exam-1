package com.example.demo.Domain;

/**
 * Userのドメイン.
 * 
 * @author yamasakimanahito
 *
 */
public class UserInfo {
	/* UserID */
	private Integer id;
	/* User名 */
	private String name;
	/* メールアドレス */
	private String mailaddress;
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
