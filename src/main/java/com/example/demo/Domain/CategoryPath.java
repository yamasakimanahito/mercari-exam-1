package com.example.demo.Domain;

/**
 * 閉包テーブルを表すドメイン.
 * 
 * @author yamasakimanahito
 *
 */
public class CategoryPath {
	/* 先祖 */
	private String ancestor;
	/* 子孫 */
	private String decendent;
	/* 階層 */
	private Integer depth;

	@Override
	public String toString() {
		return "CategoryPath [ancestor=" + ancestor + ", decendent=" + decendent + "]";
	}

	public String getAncestor() {
		return ancestor;
	}

	public void setAncestor(String ancestor) {
		this.ancestor = ancestor;
	}

	public String getDecendent() {
		return decendent;
	}

	public void setDecendent(String decendent) {
		this.decendent = decendent;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

}
