package com.example.demo.Contoroller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Domain.Category;
import com.example.demo.Domain.Item;
import com.example.demo.Form.ItemForm;
import com.example.demo.Reoisitory.CategoryRepository;
import com.example.demo.Reoisitory.ItemRepository;
import com.example.demo.Service.DtailItemService;
import com.example.demo.Service.showItemService;

/**
 * 商品の更新を操作するController.
 * 
 * @author yamasakimanahito
 *
 */
@Controller
@RequestMapping("/updateItem")
public class UpdateItemController {
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	showItemService showitemService;
	@Autowired
	DtailItemService dtailItemService;
	@Autowired
	CategoryRepository categoryRepository;

	/**
	 * 商品更新画面を表示.
	 * 
	 * @param form  フォーム
	 * @param model モデル
	 * @param id    商品ID
	 * @return 商品更新画面へ遷移
	 * @throws SQLException
	 */
	@GetMapping("/")
	public String index(ItemForm form, Model model, Integer id) throws SQLException {
		List<Item> itemList = dtailItemService.showDtail(id);
		// 初期表示をするための各々のList
		List<Category> parentCategoryList = showitemService.showParentCategory();
		List<Category> childCategoryList = showitemService
				.findChildCategory(itemList.get(0).getCategoryList().get(0).getId());
		List<Category> grandChildCategoryList = showitemService
				.findGrandChildCategory(itemList.get(0).getCategoryList().get(1).getId());
		// 先頭に表示させるアイテムのidを照らし合わせるためのCategory
		Category parentcategory = categoryRepository.findCategoryById(itemList.get(0).getCategoryList().get(0).getId());
		Category childcategory = categoryRepository.findCategoryById(itemList.get(0).getCategoryList().get(1).getId());
		Category grandchildcategory = categoryRepository
				.findCategoryById(itemList.get(0).getCategoryList().get(2).getId());
		System.out.println(itemList.get(0).getCategoryList().get(2).getId());
		System.out.println(itemList.get(0).getCategoryList().get(2).getId());
		// 初期表示させるためにItemの情報をformに格納
		form.setId(itemList.get(0).getId());
		form.setName(itemList.get(0).getName());
		form.setPrice(itemList.get(0).getPrice());
		form.setBrandName(itemList.get(0).getBrandName());
		form.setConditionId(itemList.get(0).getConditionId());
		form.setDescription(itemList.get(0).getDescription());

		model.addAttribute("itemList", itemList);
		model.addAttribute("parentCategoryList", parentCategoryList);
		model.addAttribute("childCategoryList", childCategoryList);
		model.addAttribute("grandChildCategoryList", grandChildCategoryList);
		model.addAttribute("parentcategory", parentcategory);
		model.addAttribute("childcategory", childcategory);
		model.addAttribute("grandchildcategory", grandchildcategory);
		return "item_edit";

	}

	/**
	 * 商品の更新情報を登録.
	 * 
	 * @param form
	 * @return 商品の更新を行い、商品一覧画面へ遷移
	 */
	@PostMapping("/update")
	public String Update(ItemForm form) {
		Item item = new Item();
		BeanUtils.copyProperties(form, item);
		itemRepository.update(item);
//		System.out.println(item);
		return "redirect:/toshowlist";

	}

}
