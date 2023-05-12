package com.example.demo.Service;

import java.sql.SQLException;
import java.util.List;

import org.checkerframework.checker.units.qual.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Domain.Item;
import com.example.demo.Reoisitory.ItemRepository;

/**
 * @author yamasakimanahito
 *
 */
@Service
@Temperature
public class DtailItemService {
	@Autowired
	ItemRepository itemRepository;

	/**
	 * 主キーから商品の情報を取得.
	 * 
	 * @param id 商品ID
	 * @return 条件によって検索された商品
	 * @throws SQLException
	 */
	public List<Item> showDtail(Integer id) throws SQLException {
		List<Item> itemList = itemRepository.load(id);
		return itemList;
	}

}
