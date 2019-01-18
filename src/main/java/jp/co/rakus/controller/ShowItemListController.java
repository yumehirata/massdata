package jp.co.rakus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.domain.Category;
import jp.co.rakus.domain.Item;
import jp.co.rakus.service.SearchItemService;
import jp.co.rakus.service.ShowItemService;

/**
 * アイテム一覧表示に関するコントローラー.
 * 
 * @author yume.hirata
 *
 */
@Controller
@RequestMapping("/item")
public class ShowItemListController {

	@Autowired
	private SearchItemService searchItemService;
	@Autowired
	private ShowItemService showItemService;
	
	/**
	 * アイテム一覧を表示する.
	 * 
	 * @param pageNumber	ページ番号
	 * @param model	情報を引き渡すための引数
	 * @return	アイテム一覧画面
	 */
	@RequestMapping("/list")
	public String itemList(Integer pageNumber,String message, Model model) {

		int pageLimit = showItemService.setPageLimit();
		if (pageNumber == null || pageNumber < 1) {
			pageNumber = 1;
		}else if(pageNumber > pageLimit) {
			pageNumber = pageLimit;
		}

		List<Item> itemList = showItemService.itemList(pageNumber);
		List<Category> largeCategoryList = searchItemService.forCategorySelect();

		if(message!=null) {
			model.addAttribute("message",message);
		}
		model.addAttribute("itemList", itemList);
		model.addAttribute("pageLimit", pageLimit);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("largeCategoryList", largeCategoryList);

		return "itemList";
	}

}
