package jp.co.rakus.form;

import javax.validation.constraints.Pattern;

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
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z\\-]{8,32}$", message = "パスワードは半角数字8~32文字で、数字、大文字小文字それぞれ１文字以上含めて入力してください")
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
