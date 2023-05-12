package com.example.demo.Service;

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
public class ItemService {
	@Autowired
	ItemRepository itemRepository;

	/**
	 * 商品の更新を行う.
	 * 
	 * @param item 更新対象の商品
	 */
	public void UpdateItem(Item item) {
		itemRepository.update(item);
	}

}
