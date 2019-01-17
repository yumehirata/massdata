package jp.co.rakus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import jp.co.rakus.service.LoginLogService;

@Component
public class MyLogoutHandler implements LogoutHandler {
	
	@Autowired
	private LoginLogService loginLogService;
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {

		loginLogService.logoutLog(authentication.getName());
	}

}
