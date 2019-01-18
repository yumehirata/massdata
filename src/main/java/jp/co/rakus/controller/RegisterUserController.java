package jp.co.rakus.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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
	public String toRegisterUser(Model model) {
		return "registerUser";
	}
	
	/**
	 * ユーザー登録.
	 * 
	 * @return	ログイン画面
	 */
	@RequestMapping("/register")
	public String register(@Validated UserForm form,BindingResult result,Model model) {
		if(result.hasErrors()) {
			return toRegisterUser(model);
		}
		
		User checkUser = userRepository.findByName(form.getName());
		
		if(checkUser != null) {
			result.rejectValue("name", null, "このアドレスは既に登録されています");
		}
		if(result.hasErrors()) {
			return toRegisterUser(model);
		}
		
		User insertUser= new User();
		BeanUtils.copyProperties(form, insertUser);

		String rawPassword = form.getPassword();
		String encodePassword = passwordEncoder.encode(rawPassword);
		
		insertUser.setPassword(encodePassword);
		insertUser.setAuthority(1); //仮権限
		
		userRepository.insert(insertUser);
		
		return "redirect:/user/toLogin";
	}

}
