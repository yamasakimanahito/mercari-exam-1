package com.example.demo.Contoroller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Domain.Item;
import com.example.demo.Domain.LoginUser;
import com.example.demo.Domain.UserInfo;
import com.example.demo.Service.DtailItemService;

/**
 * 商品詳細を操作するController.
 * 
 * @author yamasakimanahito
 *
 */
@Controller
@RequestMapping("/showItemDtail")
public class ShowItemDtailController {
	@Autowired
	DtailItemService dtailItemService;

	/**
	 * 商品詳細画面を表示.
	 * 
	 * @param id        商品ID
	 * @param model     モデル
	 * @param loginUser 現在ログインしているUser
	 * @return 商品詳細画面へ遷移
	 * @throws SQLException
	 */
	@GetMapping("/sarchItemById")
	public String sarchItemById(Integer id, Model model, @AuthenticationPrincipal LoginUser loginUser)
			throws SQLException {
		UserInfo user = loginUser.getUserInfo();
		List<Item> itemList = dtailItemService.showDtail(id);
		System.out.println(itemList.get(0));
		model.addAttribute("itemList", itemList);
		model.addAttribute("user", user);
		return "item_dtail";

	}

}
