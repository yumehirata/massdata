package jp.co.rakus.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.domain.Category;
import jp.co.rakus.domain.Item;
import jp.co.rakus.repository.CategoryRepository;
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
	private HttpSession session;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SearchItemService searchItemService;

	/**
	 * アイテム検索.
	 * 
	 * @param name	アイテム名
	 * @param largeCategory	大カテゴリID
	 * @param middleCategory	中カテゴリID
	 * @param smallCategory	小カテゴリID
	 * @param brand	ブランド名
	 * @param model	情報を送る引数
	 * @param pageNumber	表示しているページ番号
	 * @param isBrandSearch	ブランド絞り込みの有無
	 * @return	検索結果画面
	 */
	@RequestMapping("/search")
	public String search(String name, Integer largeCategory, Integer middleCategory, Integer smallCategory,
			String brand, Model model, Integer pageNumber, boolean isBrandSearch) {

		if (pageNumber == null || pageNumber == 0) {
			pageNumber = 1;
		} else {

			if (name == null && session.getAttribute("name") != null) {
				name = session.getAttribute("name").toString();
			}
			if (brand == null && session.getAttribute("brand") != null) {
				brand = session.getAttribute("brand").toString();
			}
			if (largeCategory == null && session.getAttribute("largeCategory") != null) {
				largeCategory = Integer.parseInt(session.getAttribute("largeCategory").toString());
			}
			if (middleCategory == null && session.getAttribute("middleCategory") != null) {
				middleCategory = Integer.parseInt(session.getAttribute("middleCategory").toString());
			}
			if (smallCategory == null && session.getAttribute("smallCategory") != null) {
				smallCategory = Integer.parseInt(session.getAttribute("smallCategory").toString());
			}
			if (isBrandSearch == false && session.getAttribute("isBrandSearch") != null) {
				isBrandSearch = Boolean.valueOf(session.getAttribute("isBrandSearch").toString());
			}
		}
		session.setAttribute("name", name);
		session.setAttribute("brand", brand);
		session.setAttribute("largeCategory", largeCategory);
		session.setAttribute("middleCategory", middleCategory);
		session.setAttribute("smallCategory", smallCategory);
		session.setAttribute("isBrandSearch", isBrandSearch);

		System.out.println("if後" + name + largeCategory + middleCategory + smallCategory + brand + isBrandSearch);

		if ((name == null || name.equals("")) && largeCategory == null && middleCategory == null
				&& smallCategory == null && (brand == null || brand.equals(""))) {
			return "forward:/item/list";
		}

		Integer beginNumber = pageNumber * 30 - 30;

		List<Item> itemList;
		Integer category = null;
		int pageLimit;

		List<Category> largeCategoryList = categoryRepository.findLargeAll();
		model.addAttribute("largeCategoryList", largeCategoryList);

		if (isBrandSearch == true) {
			pageLimit = (int) Math.ceil((double) searchItemService.searchItemNumberForBrand(brand) / 30);
			if (pageNumber > pageLimit) {
				pageNumber = pageLimit;
			} else if (pageNumber < 1) {
				pageNumber = 1;
			}

			itemList = searchItemService.searchItemForBrand(brand, beginNumber);

			model.addAttribute("itemList", itemList);
			model.addAttribute("pageLimit", pageLimit);
			model.addAttribute("pageNumber", pageNumber);
			return "itemList";
		}

		if (smallCategory != null) {
			category = smallCategory;
			pageLimit = (int) Math.ceil((double) searchItemService.searchItemNumber(name, category, brand) / 30);
			itemList = searchItemService.searchItem(name, category, brand, beginNumber);

		} else if (middleCategory != null) {
			category = middleCategory;
			pageLimit = (int) Math
					.ceil((double) searchItemService.searchItemNumberByMiddleCategory(name, category, brand) / 30);
			itemList = searchItemService.searchItemByMiddleCategory(name, category, brand, beginNumber);

		} else if (largeCategory != null) {
			category = largeCategory;
			pageLimit = (int) Math
					.ceil((double) searchItemService.searchItemNumberByLargeCategory(name, category, brand) / 30);
			itemList = searchItemService.searchItemByLargeCategory(name, category, brand, beginNumber);

		} else {
			pageLimit = (int) Math.ceil((double) searchItemService.searchItemNumber(name, category, brand) / 30);
			itemList = searchItemService.searchItem(name, category, brand, beginNumber);
		}

		if (pageNumber > pageLimit) {
			pageNumber = pageLimit;
		} else if (pageNumber < 1) {
			pageNumber = 1;
		}

		model.addAttribute("itemList", itemList);
		model.addAttribute("pageLimit", pageLimit);
		model.addAttribute("pageNumber", pageNumber);
		System.out.println("-----------");

		return "itemList";
	}

}
