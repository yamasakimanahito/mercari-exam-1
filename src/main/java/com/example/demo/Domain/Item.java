package com.example.demo.Domain;

import java.util.List;

/**
 * 商品を表すドメイン.
 * 
 * @author yamasakimanahito
 *
 */
public class Item {
	/* id */
	private Integer id;
	/* 名前 */
	private String name;
	/* 状態ID */
	private Integer conditionId;
	/* 親ID */
	private Integer parentCategory;
	/* 子ID */
	private Integer childCategory;
	/* 孫ID */
	private Integer category;
	/* カテゴリー名 */
	private Integer categoryName;
	/* ブランド名 */
	private String BrandName;
	/* 値段 */
	private Double price;
	/* 出荷状態 */
	private Integer shipping;
	/* 商品情報 */
	private String description;
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

	public Integer getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(Integer categoryName) {
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

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
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

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", conditionId=" + conditionId + ", categoryName=" + categoryName
				+ ", BrandName=" + BrandName + ", price=" + price + ", shipping=" + shipping + ", description="
				+ description + ", categoryList=" + categoryList + "]";
	}

}
