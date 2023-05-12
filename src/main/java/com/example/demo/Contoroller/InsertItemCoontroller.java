package com.example.demo.Contoroller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Domain.Category;
import com.example.demo.Domain.Item;
import com.example.demo.Domain.LoginUser;
import com.example.demo.Domain.UserInfo;
import com.example.demo.Form.ItemForm;
import com.example.demo.Reoisitory.CategoryRepository;
import com.example.demo.Service.InsertItemService;
import com.example.demo.Service.showItemService;

import jakarta.servlet.http.HttpSession;

/**
 * 商品登録を操作するController.
 * 
 * @author yamasakimanahito
 *
 */
@Controller
@RequestMapping("/insertitem")
public class InsertItemCoontroller {
	@Autowired
	InsertItemService insertItemService;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	showItemService showitemService;
	@Autowired
	ShowItemController showItemController;
	@Autowired
	HttpSession session;

	/**
	 * 商品登録画面を表示.
	 * 
	 * @param form      フォーム
	 * @param model     モデル
	 * @param loginUser 現在ログインしているUser
	 * @return 商品登録画面へ遷移
	 */
	@GetMapping("/toadditem")
	public String toAddItem(ItemForm form, Model model, @AuthenticationPrincipal LoginUser loginUser) {
		UserInfo user = loginUser.getUserInfo();
		List<Category> parentCategoryList = showitemService.showParentCategory();
		model.addAttribute("parentCategoryList", parentCategoryList);
		session.setAttribute("user", user);
		return "item_add";

	}

	/**
	 * 商品を登録する.
	 * 
	 * @param form      フォーム
	 * @param result    入力値チェックの為
	 * @param model     モデル
	 * @param loginUser 現在ログインしているUser
	 * @return 商品を登録して商品一覧画面へ遷移
	 */
	@PostMapping("/toInsert")
	public String toInsert(@Validated ItemForm form, BindingResult result, Model model,
			@AuthenticationPrincipal LoginUser loginUser) {
		if (form.getPrice() == null) {
			result.rejectValue("price", "", "金額を入力してください");
		}

		if (form.getCategory() == null || form.getCategory() == 0) {
			result.rejectValue("parentCategory", "", "カテゴリーを3つ全て選択してください");
		}
		if (result.hasErrors()) {
			return toAddItem(form, model, loginUser);
		}
		Item item = new Item();
		BeanUtils.copyProperties(form, item);
		insertItemService.insert(item);

		return "redirect:/toshowlist";
	}

}
