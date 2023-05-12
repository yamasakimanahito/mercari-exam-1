package com.example.demo.Reoisitory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.Domain.Category;

/**
 * 
 * @author yamasakimanahito
 *
 */
@Repository
public class CategoryRepository {
	@Autowired
	NamedParameterJdbcTemplate template;

	private static final RowMapper<Category> CATEGORY_ROW_MAPPER = new BeanPropertyRowMapper<>(Category.class);

	/**
	 * 商品一覧を取得する為のRepository.
	 * 
	 * @return 全件検索した情報
	 */
	public List<Category> findAllParentCategory() {
		String sql = "select id,name,parent,name_all,depth from category where depth=1;";
		List<Category> parentCategoryList = template.query(sql, CATEGORY_ROW_MAPPER);
		return parentCategoryList;

	}

	/**
	 * 親カテゴリーを取得する為のRepository.
	 * 
	 * @param id
	 * @return 親カテゴリーの情報
	 */
	public Category showParentCategoryById(Integer id) {
		String sql = "select id,name,parent,name_all,depth from category where id=:id and depth=1;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Category category = template.queryForObject(sql, param, CATEGORY_ROW_MAPPER);
		return category;

	}

	/**
	 * 子カテゴリーを取得する為のRepository.
	 * 
	 * @param id
	 * @return 子カテゴリーの情報
	 */
	public List<Category> findchildCategoryByParentId(Integer id) {
		String sql = "select c2.id,c2.name from category as c1 "
				+ "left outer join category_path as cp1 on c1.id=cp1.ancestor and cp1.depth=1"
				+ "left outer join category as c2 on c2.id = cp1.decendent where c1.id=:id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Category> categoryList = template.query(sql, param, CATEGORY_ROW_MAPPER);
		return categoryList;

	}

	/**
	 * 孫カテゴリーを取得する為のRepository.
	 * 
	 * @param id
	 * @return 孫カテゴリーの情報
	 */
	public List<Category> findGrandchildCategoryByParentId(Integer id) {
		String sql = "select c3.id,c3.name from category as c1 "
				+ "left outer join category_path as cp1 on c1.id=cp1.ancestor and cp1.depth=1 "
				+ "left outer join category as c2 on c2.id = cp1.decendent "
				+ "left outer join category_path as cp2 on cp2.ancestor = c2.id and cp2.depth=1 "
				+ "left outer join category as c3 on c3.id = cp2.decendent where c2.id=:id ";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Category> categoryList = template.query(sql, param, CATEGORY_ROW_MAPPER);
		return categoryList;
	}

	/**
	 * カテゴリーを主キー検索.
	 * 
	 * @param id
	 * @return 検索されたカテゴリーの情報
	 */
	public Category findCategoryById(Integer id) {
		String sql = "select id,name,depth from category where id=:id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Category category = template.queryForObject(sql, param, CATEGORY_ROW_MAPPER);
		return category;

	}
}
