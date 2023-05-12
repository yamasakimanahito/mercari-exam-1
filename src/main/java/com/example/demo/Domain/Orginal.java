package com.example.demo.Domain;

/**
 * tsvファイルからの情報を一時的に表すドメイン.
 * 
 * @author yamasakimanahito
 *
 */
public class Orginal {
	/* 商品ID */
	private Integer trainId;
	/* 商品名 */
	private String name;
	/* 商品状態ID */
	private Integer itemConditionId;
	/* カテゴリー名 */
	private String categoryName;
	/* ブランド名 */
	private String brandName;
	/* 値段 */
	private Double price;
	/* 出荷状態 */
	private Integer shipping;
	/* 商品情報 */
	private String itemDescription;

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public Integer getTrainId() {
		return trainId;
	}

	public void setTrainId(Integer trainId) {
		this.trainId = trainId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getItemConditionId() {
		return itemConditionId;
	}

	public void setItemConditionId(Integer itemConditionId) {
		this.itemConditionId = itemConditionId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	@Override
	public String toString() {
		return "Orginal [trainId=" + trainId + ", name=" + name + ", itemConditionId=" + itemConditionId
				+ ", categoryName=" + categoryName + ", brandName=" + brandName + ", price=" + price + ", shipping="
				+ shipping + ", itemDescription=" + itemDescription + "]";
	}

}
