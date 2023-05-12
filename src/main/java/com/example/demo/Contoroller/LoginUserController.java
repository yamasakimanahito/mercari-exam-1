package com.example.demo.Contoroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 顧客のログインを操作するController.
 * 
 * @author yamasakimanahito
 *
 */
@Controller
@RequestMapping("/loginuser")
public class LoginUserController {
	/**
	 * ログイン画面に遷移.
	 * 
	 * @param model
	 * @param error
	 * @return ログイン画面へ
	 */
	@GetMapping("")
	public String index(Model model, @RequestParam(required = false) String error) {
		System.err.println("login error:" + error);
		if (error != null) {
			System.err.println("login failed");
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
		}
		return "login";
	}

}
