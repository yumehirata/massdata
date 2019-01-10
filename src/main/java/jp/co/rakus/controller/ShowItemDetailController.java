package jp.co.rakus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.domain.Item;
import jp.co.rakus.repository.ItemRepository;

/**
 * アイテム詳細表示に関するコントローラー.
 * 
 * @author yume.hirata
 *
 */
@Controller
@RequestMapping("/item")
public class ShowItemDetailController {

	@Autowired
	private ItemRepository itemRepository;
	
	/**
	 * アイテム詳細画面を表示する.
	 * 
	 * @param id	アイテムID
	 * @param model	情報を引き渡すための引数
	 * @return	アイテム詳細画面
	 */
	@RequestMapping("/detail")
	public String toItemDetail(Integer id,Model model) {
		
		Item item = itemRepository.findByIdHasCategory(id);
		model.addAttribute("item",item);
		
		return "itemDetail";
	}
	
}
