package com.example.demo.Form;

import java.util.List;

import com.example.demo.Domain.Category;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 商品を表すフォーム.
 * 
 * @author yamasakimanahito
 *
 */
public class ItemForm {
	/* ID */
	private Integer id;
	@NotBlank(message = "商品名を入力してください")
	/* 名前 */
	private String name;
	@NotNull(message = "状態を入力してください")
	/* 商品状態ID */
	private Integer conditionId;
	/* 小カテゴリー */
	private Integer category;
	/* 親カテゴリー */
	private Integer parentCategory;
	/* 子カテゴリー */
	private Integer childCategory;
	/* カテゴリー名 */
	private String categoryName;
	@NotBlank(message = "ブランド名を入力してください")
	/* ブランド名 */
	private String BrandName;
	@Digits(integer = 10000, fraction = 1, message = "正しく金額を入力してください")
	/* 値段 */
	private Double price;
	/* 出品状態 */
	private Integer shipping;
	/* 商品情報 */
	private String description;
	/* ページ */
	private String page;

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	private List<Category> categoryList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getConditionId() {
		return conditionId;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBrandName() {
		return BrandName;
	}

	public void setBrandName(String brandName) {
		BrandName = brandName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getShipping() {
		return shipping;
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public String getDescription() {
		return description;
	}

	public Integer getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Integer parentCategory) {
		this.parentCategory = parentCategory;
	}

	public Integer getChildCategory() {
		return childCategory;
	}

	public void setChildCategory(Integer childCategory) {
		this.childCategory = childCategory;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", conditionId=" + conditionId + ", categoryName=" + categoryName
				+ ", BrandName=" + BrandName + ", price=" + price + ", shipping=" + shipping + ", description="
				+ description + ", categoryList=" + categoryList + "]";
	}

}
