package jp.co.rakus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import jp.co.rakus.service.LoginLogService;

/**
 * SpringSecurityを用いたログアウトに関する設定;
 * 
 * @author yume.hirata
 *
 */
@Component
public class MyLogoutHandler implements LogoutHandler {
	
	@Autowired
	private LoginLogService loginLogService;
	
	/* 
	 * ログアウト時に情報を書き出す.
	 * 
	 * (non-Javadoc)
	 * @see org.springframework.security.web.authentication.logout.LogoutHandler#logout(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {

		loginLogService.logoutLog(authentication.getName());
	}

}
