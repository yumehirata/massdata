package jp.co.rakus.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * 利用者のログイン情報を格納するエンティティ.
 * 
 * @author yume.hirata
 *
 */
public class LoginUser extends org.springframework.security.core.userdetails.User{

	private static final long serialVersionUID = 1L;
	
	/** ログインしたユーザー */
	private final User user;
	
	public LoginUser(User user,Collection<GrantedAuthority> authorityList) {
		super(user.getName(),user.getPassword(), authorityList);
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
}
