package jp.co.rakus.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.domain.User;
import jp.co.rakus.form.UserForm;
import jp.co.rakus.repository.UserRepository;

/**
 * ユーザー登録に関するコントローラー.
 * 
 * @author yume.hirata
 *
 */
@Controller
@RequestMapping("/user")
public class RegisterUserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute
	public UserForm setUpUserForm() {
		return new UserForm();
	}
	
	/**
	 * ユーザー登録画面へ遷移.
	 * 
	 * @return	ユーザー登録画面
	 */
	@RequestMapping("/toRegister")
	public String toRegisterUser() {
		return "registerUser";
	}
	
	/**
	 * ユーザー登録.
	 * 
	 * @return	ログイン画面
	 */
	@RequestMapping("/Register")
	public String register(UserForm form) {
		
		User user = new User();
		BeanUtils.copyProperties(form, user);
		//TODO　パスワードのハッシュ化
		//TODO　権限の設定
		
		userRepository.insert(user);
		
		return "login";
	}

}
