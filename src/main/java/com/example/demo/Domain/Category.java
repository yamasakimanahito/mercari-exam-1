package com.example.demo.Domain;

/**
 * Categoryのドメインクラス.
 * 
 * @author yamasakimanahito
 *
 */
public class Category {
	/* id */
	private Integer id;
	/* カテゴリー名 */
	private String name;
	/* 親から孫までの一連のカテゴリー名 */
	private String nameAll;
	/* 階層 */
	private Integer depth;

	public String getNameAll() {
		return nameAll;
	}

	public void setNameAll(String nameAll) {
		this.nameAll = nameAll;
	}

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

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", nameAll=" + nameAll + ", depth=" + depth + "]";
	}

}
