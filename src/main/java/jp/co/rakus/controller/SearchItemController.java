package jp.co.rakus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.domain.Category;
import jp.co.rakus.domain.Item;
import jp.co.rakus.service.SearchItemService;

/**
 * アイテム検索のためのコントローラー.
 * 
 * @author yume.hirata
 *
 */
@Controller
@RequestMapping("/item")
public class SearchItemController {

	@Autowired
	private SearchItemService searchItemService;

	/**
	 * アイテム検索.
	 * 
	 * @param name
	 *            アイテム名
	 * @param largeCategory
	 *            大カテゴリID
	 * @param middleCategory
	 *            中カテゴリID
	 * @param smallCategory
	 *            小カテゴリID
	 * @param brand
	 *            ブランド名
	 * @param model
	 *            情報を送る引数
	 * @param pageNumber
	 *            表示しているページ番号
	 * @param isBrandSearch
	 *            ブランド絞り込みの有無
	 * @return 検索結果画面
	 */
	@RequestMapping("/search")
	public String search(String name, Integer largeCategory, Integer middleCategory, Integer smallCategory,
			String brand, Model model, Integer pageNumber, boolean isBrandSearch) {

		pageNumber = searchItemService.checkArgument(name, smallCategory, middleCategory, largeCategory, brand, pageNumber, isBrandSearch);
		
		if ((name == null || name.equals("")) && largeCategory == null && middleCategory == null
				&& smallCategory == null && (brand == null || brand.equals(""))) {
			return "forward:/item/list";
		}

		List<Item> itemList = searchItemService.searchItem(name, smallCategory, middleCategory, largeCategory, brand, pageNumber, isBrandSearch);
		int pageLimit = searchItemService.forPageLimit(name, smallCategory, middleCategory, largeCategory, brand, isBrandSearch);

		searchItemService.checkPageNumber(pageNumber, pageLimit);
		
		List<Category> largeCategoryList = searchItemService.forCategorySelect();

		model.addAttribute("itemList", itemList);
		model.addAttribute("pageLimit", pageLimit);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("largeCategoryList", largeCategoryList);

		return "itemList";
	}

}
