package jp.co.rakus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.domain.Item;
import jp.co.rakus.repository.ItemRepository;

/**
 * アイテムリストを表示する.
 * 
 * @author yume.hirata
 *
 */
@Service
public class ShowItemService {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private SearchItemService searchItemService;
	
	/**
	 * 全件検索の最大ページ数を取得.
	 * 
	 * @return	最大ページ数
	 */
	public int setPageLimit() {
		return searchItemService.setPageLimit(itemRepository.findAllAmount());
	}
	
	/**
	 * 全件検索のアイテム一覧を取得.
	 * 
	 * @param pageNumber	現在ページ
	 * @return	アイテム一覧
	 */
	public List<Item> itemList(Integer pageNumber){
		Integer beginNumber = pageNumber * 30 - 30;
		List<Item> itemList = itemRepository.findAllForPaging(beginNumber);

		return itemList;
	}
}
