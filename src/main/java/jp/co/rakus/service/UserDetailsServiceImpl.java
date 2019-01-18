package jp.co.rakus.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.rakus.domain.LoginUser;
import jp.co.rakus.domain.User;
import jp.co.rakus.repository.UserRepository;

/**
 * ログインしたユーザーの情報を取得する.
 * 
 * @author yume.hirata
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	/* 
	 * DBから検索をして、ログイン情報を取得して返す.
	 * 
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		User user = userRepository.findByName(name);
		if (user == null) {
			throw new UsernameNotFoundException("そのEmailは登録されていません。");
		}
		
		Collection<GrantedAuthority> authorityList =  new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		
		return new LoginUser(user,authorityList);
	}

}
