package com.example.demo.Service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.checkerframework.checker.units.qual.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.Domain.Category;
import com.example.demo.Domain.Item;
import com.example.demo.Reoisitory.CategoryRepository;
import com.example.demo.Reoisitory.ItemRepository;

/**
 * @author yamasakimanahito
 *
 */
@Service
@Temperature
public class showItemService {
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * 商品の全件検索を行う.
	 * 
	 * @return 全件検索された商品List
	 * @throws SQLException
	 */
	public List<Item> showList() throws SQLException {
		List<Item> itemList = itemRepository.findAll();
		return itemList;
	}

	/**
	 * カテゴリー名から商品を検索.
	 * 
	 * @param name カテゴリー名
	 * @return 検索された商品
	 * @throws SQLException
	 */
	public List<Item> showListByCategory(Integer id) throws SQLException {
		List<Item> itemList = itemRepository.findByCategoryId(id);
		return itemList;
	}

	/**
	 * 親カテゴリーを検索.
	 * 
	 * @return 検索されたカテゴリー
	 */
	public List<Category> showParentCategory() {
		List<Category> parentCategoryList = categoryRepository.findAllParentCategory();
		return parentCategoryList;
	}

	/**
	 * 子カテゴリーを検索.
	 * 
	 * @param id 親ID
	 * @return 検索された子カテゴリー
	 */
	public List<Category> findChildCategory(Integer id) {
		List<Category> categoryList = categoryRepository.findchildCategoryByParentId(id);
		return categoryList;
	}

	/**
	 * 孫カテゴリーを検索.
	 * 
	 * @param id 子ID
	 * @return 検索された孫カテゴリー
	 */
	public List<Category> findGrandChildCategory(Integer id) {
		List<Category> categoryList = categoryRepository.findGrandchildCategoryByParentId(id);
		return categoryList;
	}

	/**
	 * 名前、カテゴリー、ブランド名から商品を検索.
	 * 
	 * @param name               商品名
	 * @param parentId           親ID
	 * @param childId            子ID
	 * @param grandChildcategory 孫ID
	 * @param brand              ブランド名
	 * @return 検索された商品
	 * @throws SQLException
	 */
	public List<Item> showItemByCondition(String name, Integer parentId, Integer childId, Integer grandChildcategory,
			String brand) throws SQLException {
		List<Item> itemList = itemRepository.findByNameAndCategoryIdAndBrand(name, parentId, childId,
				grandChildcategory, brand);
		return itemList;
	}

	public static Page<Item> showListPaging(int page, int size, List<Item> itemList) {
		// 表示させたいページ数を-1しなければうまく動かない
		page--;
		// どのItemから表示させるかと言うカウント値
		int startItemCount = page * size;
		// 絞り込んだ後の従業員リストが入る変数
		List<Item> list;

		if (itemList.size() < startItemCount) {
			// (ありえないが)もし表示させたい従業員カウントがサイズよりも大きい場合は空のリストを返す
			list = Collections.emptyList();
		} else {
			// 該当ページに表示させるItem一覧を作成
			int toIndex = Math.min(startItemCount + size, itemList.size()); // 全件検索の数か、ページ×100＋100のどちらか小さい方
			list = itemList.subList(startItemCount, toIndex); // Listの中から必要な部分だけを切り出して新しいListを作る
		}

		// 上記で作成した該当ページに表示させる従業員一覧をページングできる形に変換して返す
		Page<Item> itemPage = new PageImpl<Item>(list, PageRequest.of(page, size), itemList.size());
		return itemPage;
	}

	/**
	 * 全件表示時のページングを行う.
	 * 
	 * @param offset
	 * @return ページングによって検索された商品
	 * @throws SQLException
	 */
	public List<Item> paging(Integer offset) throws SQLException {
		List<Item> itemList = itemRepository.paging(offset);
		return itemList;
	}

	/**
	 * 検索商品表示時のページングを行う.
	 * 
	 * @param name               商品名
	 * @param parentId           親ID
	 * @param childId            子ID
	 * @param grandChildcategory 孫ID
	 * @param brand              ブランド名
	 * @param offset
	 * @return ページングによって検索された商品List
	 * @throws SQLException
	 */
	public List<Item> selectpaging(String name, Integer parentId, Integer childId, Integer grandChildcategory,
			String brand, Integer offset) throws SQLException {
		List<Item> itemList = itemRepository.findByNameAndCategoryIdAndBrandPaging(name, parentId, childId,
				grandChildcategory, brand, offset);
		return itemList;

	}

	public List<Item> BycategoryItem(Integer id) throws SQLException {
		List<Item> itemList = itemRepository.searchByCategoryItemAmoount(id);
		return itemList;
	}

	public List<Item> categoryPaging(Integer id, Integer offset) throws SQLException {
		// TODO Auto-generated method stub
		List<Item> itemList = itemRepository.findByCategoryIdPaging(id, offset);
		return itemList;
	}

}
