package jp.co.rakus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jp.co.rakus.domain.User;

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

	/**
	 * ログイン画面の表示.
	 * 
	 * @param model	情報を引き渡すための引数
	 * @param error	エラーチェック
	 * @return	ログイン画面
	 */
	@RequestMapping("/toLogin")
	public String toLogin(Model model, @RequestParam(required = false )String error) {
		if(error != null) {
			model.addAttribute("loginError","名前またはパスワードが不正です");
		}
		return "login";
	}
}
