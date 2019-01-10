package jp.co.rakus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * SpringSecurityを利用したセキュリティ設定.
 * 
 * @author yume.hirata
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	/* 
	 * css,imgなどのセキュリティ設定を無視する.
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/css/**","/img/**","/js/**");
	}
	
	/* 
	 * 認可の設定,ログイン/ログアウトに関する設定.
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		http.authorizeRequests()
			.antMatchers("/item/toRegister").authenticated().anyRequest().permitAll();
		
		http.formLogin()
			.loginPage("/user/toLogin")
			.loginProcessingUrl("/user/login")
			.failureUrl("/user?error=true")
			.defaultSuccessUrl("/item/list",false)
			.usernameParameter("name")
			.passwordParameter("password");
		
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
			.logoutSuccessUrl("/item/list")
			.deleteCookies("JSESSIONID")
			.invalidateHttpSession(true);
		
		http.csrf().disable();
	}
	
	/* 
	 * 認証に関する設定.
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	/**
	 * 暗号化の実装.
	 * 
	 * @return	Bcryptアルゴリズムで暗号化する実装オブジェクト
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
