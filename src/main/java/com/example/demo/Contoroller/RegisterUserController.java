package com.example.demo.Contoroller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Domain.UserInfo;
import com.example.demo.Form.UserInfoForm;
import com.example.demo.Service.RegisterUserService;

/**
 * User登録を操作するController.
 * 
 * @author yamasakimanahito
 *
 */
@Controller
@RequestMapping("")
public class RegisterUserController {
	@Autowired
	RegisterUserService registerUserService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * User登録画面を表示.
	 * 
	 * @param form
	 * @return User登録画面へ遷移.
	 */
	@GetMapping("/register")
	public String index(UserInfoForm form) {
		return "register";
	}

	/**
	 * User登録を行う.
	 * 
	 * @param form   フォーム
	 * @param result 入力値チェックを行う為のresult
	 * @param model  モデル
	 * @return 失敗時は登録画面へ、成功時はログイン画面へ遷移
	 */
	@PostMapping("/registerUser")
	public String registerUser(@Validated UserInfoForm form, BindingResult result, Model model) {
		UserInfo existUser = registerUserService.findUserByMailaddress(form.getMailaddress());
		if (existUser != null) {
			result.rejectValue("mailaddress", "", "既に登録されています");

		}
		if (result.hasErrors()) {
			return index(form);
		}
		UserInfo user = new UserInfo();
		System.out.println(form.getMailaddress());
		System.out.println(form.getPassword());
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		user.setMailaddress(form.getMailaddress());
		registerUserService.insert(user);

		return "redirect:/loginuser";
	}
}
