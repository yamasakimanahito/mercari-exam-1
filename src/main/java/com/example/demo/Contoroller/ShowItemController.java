package com.example.demo.Contoroller;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

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
import com.example.demo.Reoisitory.ItemRepository;
import com.example.demo.Service.showItemService;

import jakarta.servlet.http.HttpSession;

/**
 * 商品一覧を操作するController.
 * 
 * @author yamasakimanahito
 *
 */
@Controller
@RequestMapping("/")
public class ShowItemController {
	@Autowired
	private showItemService showitemService;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	HttpSession session;
	// 1ページに表示する従業員数は10名
	private static final int VIEW_SIZE = 300;

	/**
	 * 商品一覧画面を表示する.
	 * 
	 * @param model     モデル
	 * @param form      フォーム
	 * @param offset    ページングのためのoffset
	 * @param loginUser 現在ログインしているUser情報
	 * @return 商品一覧画面へ
	 * @throws SQLException
	 */
	@GetMapping("/toshowlist")
	public String toShowList(Model model, ItemForm form, Integer offset, @AuthenticationPrincipal LoginUser loginUser)
			throws SQLException {
		// 現在ログインしているUserの情報
		UserInfo user = loginUser.getUserInfo();
		List<Item> allItemList = itemRepository.ItemAmount();
		Item item = new Item();
		Category selectCategory = new Category();

		// ページングの為の処理
		double totalpage = (double) allItemList.size() / VIEW_SIZE;
		System.out.println(totalpage);
		if (totalpage == (int) totalpage) {
			session.setAttribute("totalpage", (int) totalpage);
		} else {
			session.setAttribute("totalpage", (int) totalpage + 1);
		}
		offset = 0;

		List<Category> parentCategoryList = showitemService.showParentCategory();
		List<Item> itemList = showitemService.showList();
		model.addAttribute("itemList", itemList);
		model.addAttribute("offset", offset);
		session.setAttribute("parentCategoryList", parentCategoryList);
		session.setAttribute("user", user);
		session.setAttribute("item", item);
		session.setAttribute("selectCategory", selectCategory);
		return "item_list";
	}

	/**
	 * カテゴリー名をクリックした時にそのカテゴリーに関する商品一覧を表示.
	 * 
	 * @param name  カテゴリー名
	 * @param model モデル
	 * @param form  フォーム
	 * @return カテゴリーで絞った商品一覧画面へ
	 * @throws SQLException
	 */
	@GetMapping("/sarchItemByCategory")
	public String sarchItemByCategory(Integer categoryId, Integer offset,Model model, ItemForm form) throws SQLException {// form書かないとエラーになる
		List<Item> searchItemByCategoryList = showitemService.BycategoryItem(categoryId);
		System.out.println(categoryId);
		offset=0;
		Category selectCategory = new Category();
		selectCategory.setId(categoryId);
		List<Item> itemList = showitemService.showListByCategory(categoryId);
		double totalpage = (double) searchItemByCategoryList.size() / VIEW_SIZE;
		System.out.println(searchItemByCategoryList.size());
		if (totalpage == (int) totalpage) {
			session.setAttribute("totalpage", (int) totalpage);
		} else {
			session.setAttribute("totalpage", (int) totalpage + 1);
		}
		model.addAttribute("itemList", itemList);
		model.addAttribute("offset",offset);
		session.setAttribute("selectCategory",selectCategory);
		return "item_list";
	}

	/**
	 * 検索欄で非同期処理で子カテゴリーの情報を取得.
	 * 
	 * @param parentId 親ID
	 * @return 親のIDに基づいたカテゴリー一覧をjsへ
	 */
	@GetMapping("/getChildren")
	@ResponseBody
	public String getChildren(@RequestParam Integer parentId) {
		List<Category> childCategoryList = showitemService.findChildCategory(parentId);
		StringBuilder sb = new StringBuilder();
		for (Category childCategory : childCategoryList) {
			sb.append("<option value=\"" + childCategory.getId() + "\">" + childCategory.getName() + "</option>");
		}
		return "<option value=" + "0" + ">- childCategory -</option>" + sb.toString();
	}

	/**
	 * 検索欄で非同期処理で孫カテゴリーの情報を取得.
	 * 
	 * @param childId 子ID
	 * @return 子のIDに基づいたカテゴリー一覧をjsへ
	 */
	@GetMapping("/getGrandchildren")
	@ResponseBody
	public String getGrandchildren(@RequestParam Integer childId) {
		List<Category> grandChildCategoryList = showitemService.findGrandChildCategory(childId);
		StringBuilder sb = new StringBuilder();
		for (Category grandchild : grandChildCategoryList) {
			sb.append("<option value=\"" + grandchild.getId() + "\">" + grandchild.getName() + "</option>");
		}
		return "<option value=" + "0" + ">- grandChildCategory -</option>" + sb.toString();
	}

	/**
	 * 検索欄で検索した商品一覧を表示.
	 * 
	 * @param form      フォーム
	 * @param model     モデル
	 * @param offset    ページングで使用するoffset
	 * @param loginUser 現在ログインしているUser
	 * @return 検索した商品を商品一覧画面に表示
	 * @throws SQLException
	 */
	@PostMapping("/sarchItemByItemForm")
	public String searchItemByItemForm(ItemForm form, Model model, Integer offset,
			@AuthenticationPrincipal LoginUser loginUser) throws SQLException {
		offset = 0;
		List<Item> itemList = showitemService.showItemByCondition(form.getName(), form.getParentCategory(),
				form.getChildCategory(), form.getCategory(), form.getBrandName());
		List<Item> searchItemAmountList = itemRepository.searchItemAmount(form.getName(), form.getParentCategory(),
				form.getChildCategory(), form.getCategory(), form.getBrandName());
		List<Category> childcategoryList = showitemService.findChildCategory(form.getParentCategory());
		List<Category> grandchildCategoryList = showitemService.findGrandChildCategory(form.getChildCategory());

		Item item = new Item();
		item.setName(form.getName());
		item.setParentCategory(form.getParentCategory());
		item.setChildCategory(form.getChildCategory());
		item.setCategory(form.getCategory());
		item.setBrandName(form.getBrandName());

		double totalpage = (double) searchItemAmountList.size() / VIEW_SIZE;
		System.out.println(searchItemAmountList.size());
		if (totalpage == (int) totalpage) {
			session.setAttribute("totalpage", (int) totalpage);
		} else {
			session.setAttribute("totalpage", (int) totalpage + 1);
		}

		if (itemList.size() == 0) {
			model.addAttribute("result", "検索結果が0件の為、全件検索します");
			return toShowList(model, form, offset, loginUser);
		} else {
			model.addAttribute("itemList", itemList);
			session.setAttribute("item", item);
			session.setAttribute("childcategoryList", childcategoryList);
			session.setAttribute("grandchildCategoryList", grandchildCategoryList);
			// model.addAttribute("parentCategoryList", parentCategoryList);
			model.addAttribute("offset", offset);
			System.out.println("sarchitem" + itemList.size());
			return "item_list";
		}
	}

	/**
	 * 次ページを押した時に300個毎に商品を表示
	 * 
	 * @param offset             ページングの為のoffset
	 * @param model              モデル
	 * @param form               アイテムフォーム
	 * @param name               商品名
	 * @param parentCategory     親ID
	 * @param childCategory      子ID
	 * @param grandChildCategory 孫ID
	 * @param brand              ブランド名
	 * @param loginUser          現在ログインしているUser
	 * @return ページングで絞った商品を一覧画面へ表示
	 * @throws SQLException
	 */
	@GetMapping("/pagingnext")
	public String pagingnext(Integer offset, Model model, ItemForm form, Integer categoryId,String name, Integer parentCategory,
			Integer childCategory, Integer grandChildCategory, String brand,
			@AuthenticationPrincipal LoginUser loginUser) throws SQLException {
		System.out.println(name);
		System.out.println(parentCategory);
		System.out.println(grandChildCategory);
		System.out.println("カテゴリー"+categoryId);

		Item item = new Item();
		item.setName(name);
		item.setParentCategory(parentCategory);
		item.setChildCategory(childCategory);
		item.setCategory(grandChildCategory);
		item.setBrandName(brand);

		int viewItemcount = 300;
		List<Item> allItemList = itemRepository.ItemAmount();
		List<Item> itemList = new LinkedList<>();
		System.out.println(allItemList.size());
		if (offset == 0) {
			offset = 300;
		} else if (offset + viewItemcount > allItemList.size()) {
			return toShowList(model, form, offset, loginUser);
		} else {
			offset += viewItemcount;
		}
		
		if (name.equals("") && parentCategory == null && childCategory == null && grandChildCategory == null
				&& brand.equals("") && categoryId==null) {
			itemList = showitemService.paging(offset);
		} else if(categoryId!=null) {
			itemList = showitemService.categoryPaging(categoryId, offset);
		} else {
			itemList = showitemService.selectpaging(name, parentCategory, childCategory, grandChildCategory, brand,
					offset);
		}
		System.out.println("next" + offset);
		model.addAttribute("offset", offset);
		model.addAttribute("itemList", itemList);
		session.setAttribute("item", item);

		return "item_list";
	}

	/**
	 * 前ページを押した時に300個毎に商品を表示.
	 * 
	 * @param offset             ページングの為のoffset
	 * @param model              モデル
	 * @param form               アイテムフォーム
	 * @param name               商品名
	 * @param parentCategory     親ID
	 * @param childCategory      子ID
	 * @param grandChildCategory 孫ID
	 * @param brand              ブランド名
	 * @param loginUser          現在ログインしているUser
	 * @return ページングで絞った商品を一覧画面に表示
	 * @throws SQLException
	 */
	@GetMapping("/pagingprev")
	public String pagingprev(Integer offset, Model model, ItemForm form, Integer categoryId,String name, Integer parentCategory,
			Integer childCategory, Integer grandChildCategory, String brand,
			@AuthenticationPrincipal LoginUser loginUser) throws SQLException {
		int viewItemcount = 300;
		List<Item> itemList = new LinkedList<>();

		Item item = new Item();
		item.setName(name);
		item.setParentCategory(parentCategory);
		item.setChildCategory(childCategory);
		item.setCategory(grandChildCategory);
		item.setBrandName(brand);

		if (offset <= 0) {
			return toShowList(model, form, offset, loginUser);
		} else {
			offset -= viewItemcount;
		}
		if (name.equals("") && parentCategory == null && childCategory == null && grandChildCategory == null
				&& brand.equals("") && categoryId==null) {
			itemList = showitemService.paging(offset);
		}else if(categoryId!=null) {
			itemList = showitemService.categoryPaging(categoryId, offset);
		}
		else {
			itemList = showitemService.selectpaging(name, parentCategory, childCategory, grandChildCategory, brand,
					offset);
		}
		System.out.println("prev" + offset);
		model.addAttribute("offset", offset);
		model.addAttribute("itemList", itemList);
		model.addAttribute("item", item);

		return "item_list";
	}

	/**
	 * 入力された数字のページに遷移する.
	 * 
	 * @param form      フォーム
	 * @param result    入力値チェック
	 * @param model     モデル
	 * @param offset    ページングの為のoffset
	 * @param loginUser 現在ログインしているUser
	 * @return 検索された商品のページへ遷移
	 * @throws SQLException
	 */
	@PostMapping("/toselectpage")
	public String toSelectPage(@Validated ItemForm form, BindingResult result, Model model, Integer offset,Integer categoryId, String name, Integer parentCategory,
			Integer childCategory, Integer grandChildCategory, String brand,
			@AuthenticationPrincipal LoginUser loginUser) throws SQLException {
		List<Item> itemList = new LinkedList<>();
		Item item = new Item();
		item.setName(name);
		item.setParentCategory(parentCategory);
		item.setChildCategory(childCategory);
		item.setCategory(grandChildCategory);
		item.setBrandName(brand);
		String regex_only_num = "^[0-9 ０-９]+$";
		Pattern pattern = Pattern.compile(regex_only_num);

		if (!pattern.matcher(form.getPage()).matches()) {
			model.addAttribute("result", "入力に誤りがあります");
			result.rejectValue("page", "", "1以上の正しい値を入力して下さい");
			return toShowList(model, form, offset, loginUser);
		}

		offset = (Integer.parseInt(form.getPage()) - 1) * VIEW_SIZE;
		

		if (form.getName().equals("") && parentCategory == null && childCategory == null && grandChildCategory == null
				&& brand.equals("") && categoryId==null) {
			itemList = showitemService.paging(offset);
		} else if(categoryId!=null) {
			itemList = showitemService.categoryPaging(categoryId, offset);
		}
		else {
			itemList = showitemService.selectpaging(name, parentCategory, childCategory, grandChildCategory, brand,
					offset);
		}

		System.out.println("prev" + offset);
		model.addAttribute("offset", offset);
		model.addAttribute("itemList", itemList);
		session.setAttribute("item", item);

		return "item_list";
	}

	@GetMapping("/totalpage")
	public int totalPage(ItemForm form, String name, Integer parentCategory, Integer childCategory,
			Integer grandChildCategory, String brand) throws SQLException {
		List<Item> allItemList = itemRepository.ItemAmount();
		List<Item> sortItemList = showitemService.showItemByCondition(form.getName(), form.getParentCategory(),
				form.getChildCategory(), form.getCategory(), form.getBrandName());

		if (name.equals("") && form.getParentCategory() == 0 && form.getChildCategory() == 0
				&& form.getCategory() == 0 && form.getBrandName().equals("")) {
			double totalpage = (double) allItemList.size() / VIEW_SIZE;
			if (totalpage == (int) totalpage) {
				return (int) totalpage;
			} else {
				return (int) totalpage + 1;
			}

		} else {
			double totalpage = (double) sortItemList.size() / VIEW_SIZE;
			System.out.println(sortItemList.size());
			if (totalpage == (int) totalpage) {
				return (int) totalpage;
			} else {
				return (int) totalpage + 1;
			}

		}
	}

	public List<Item> paging(Model model, ItemForm form, Integer offset, @AuthenticationPrincipal LoginUser loginUser)
			throws SQLException {
		// 現在ログインしているUserの情報
		List<Item> showItemList = new LinkedList<>();
		Item item = new Item();
		if (form.getName().equals("") && form.getParentCategory() == 0 && form.getChildCategory() == 0
				&& form.getCategory() == 0 && form.getBrandName().equals("")) {
			showItemList = showitemService.showList();
		} else {
			showItemList = showitemService.showItemByCondition(form.getName(), form.getParentCategory(),
					form.getChildCategory(), form.getCategory(), form.getBrandName());
		}
		return showItemList;

	}

}
