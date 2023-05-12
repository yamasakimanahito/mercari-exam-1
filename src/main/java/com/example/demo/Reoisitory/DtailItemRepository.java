package com.example.demo.Reoisitory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.Domain.Item;

/**
 * @author yamasakimanahito
 *
 */
@Repository
public class DtailItemRepository {
	@Autowired
	NamedParameterJdbcTemplate template;

	private static final RowMapper<Item> ITEM_ROW_MAPPER = new BeanPropertyRowMapper<>(Item.class);

	/**
	 * 商品の主キー検索を行う.
	 * 
	 * @param id
	 * @return 検索された商品の情報
	 */
	public Item findByItemId(Integer id) {
		String sql = "select id,name,category,brand,price,condition,shipping,description from items where id=:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);
		return item;
	}

}
