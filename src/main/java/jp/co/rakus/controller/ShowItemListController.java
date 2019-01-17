package jp.co.rakus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.domain.Category;
import jp.co.rakus.domain.Item;
import jp.co.rakus.repository.CategoryRepository;
import jp.co.rakus.repository.ItemRepository;

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
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * アイテム一覧を表示する.
	 * 
	 * @param pageNumber	ページ番号
	 * @param model	情報を引き渡すための引数
	 * @return	アイテム一覧画面
	 */
	@RequestMapping("/list")
	public String itemList(Integer pageNumber, Model model) {

		int pageLimit = (int) Math.ceil((double) itemRepository.findAllAmount() / 30);
		if (pageNumber == null || pageNumber < 1) {
			pageNumber = 1;
		}else if(pageNumber > pageLimit) {
			pageNumber = pageLimit;
		}

		Integer beginNumber = pageNumber * 30 - 30;
		List<Item> itemList = itemRepository.findAllForPaging(beginNumber);

		List<Category> largeCategoryList = categoryRepository.findLargeAll();

		model.addAttribute("itemList", itemList);
		model.addAttribute("pageLimit", pageLimit);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("largeCategoryList", largeCategoryList);

		return "itemList";
	}

}
