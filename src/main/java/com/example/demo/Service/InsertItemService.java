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
public class InsertItemService {
	@Autowired
	ItemRepository itemRepository;

	/**
	 * 商品の追加を行う.
	 * 
	 * @param item 追加対象の商品
	 */
	public void insert(Item item) {
		itemRepository.Insert(item);

	}

}
