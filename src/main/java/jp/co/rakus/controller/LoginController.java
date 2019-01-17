package jp.co.rakus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jp.co.rakus.domain.LoginUser;
import jp.co.rakus.domain.User;
import jp.co.rakus.service.LoginLogService;

/**
 * ログインを行うコントローラー.
 * 
 * @author yume.hirata
 *
 */
@Controller
@RequestMapping("/user")
@SessionAttributes(types = { User.class })
public class LoginController {
	
	@Autowired
	private LoginLogService loginLogService;
	
	/**
	 * ログイン画面の表示.
	 * 
	 * @param model	情報を引き渡すための引数
	 * @param error	エラーチェック
	 * @return	ログイン画面
	 */
	@RequestMapping(value="/toLogin", method = RequestMethod.GET)
	public String toLogin(Model model, @RequestParam(required = false) String error) {
		if(error != null) {
			model.addAttribute("loginError","名前またはパスワードが不正です");
		}
		return "login";
	}
	
	/**
	 * ログイン後遷移してくる.ログの書き込み.
	 * 
	 * @param authentication	情報を引き出すための引数
	 * @return	一覧表示画面
	 */
	@RequestMapping("/writeLog")
	public String writeLog(@AuthenticationPrincipal LoginUser loginUser) {
		loginLogService.loginLog(loginUser.getUsername());
		return "forward:/item/list";
	}

}
