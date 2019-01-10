package jp.co.rakus.form;

/**
 * ユーザーフォームの情報を取得する.
 * 
 * @author yume.hirata
 *
 */
public class UserForm {

	/** ユーザー名 */
	private String name;
	/** パスワード */
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
