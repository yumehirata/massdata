package jp.co.rakus.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rakus.domain.Category;
import jp.co.rakus.domain.Item;
import jp.co.rakus.repository.CategoryRepository;
import jp.co.rakus.repository.ItemRepository;

/**
 * アイテム検索時にデータ操作するクラス.
 * 
 * @author yume.hirata
 *
 */
@Service
public class SearchItemService {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private HttpSession session;
	
	/**
	 * ページ遷移用引数チェック
	 * 
	 * @param name	アイテム名
	 * @param smallCategory	小カテゴリID
	 * @param middleCategory	中カテゴリID
	 * @param largeCategory	大カテゴリID
	 * @param brand	ブランド名
	 * @param pageNumber	現在位置
	 * @param isBrandSearch	ブランド検索
	 */
	public Integer checkArgument(String name,Integer smallCategory, Integer middleCategory,Integer largeCategory,String brand,Integer pageNumber, boolean isBrandSearch) {
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
		
		return pageNumber;
	}
	
	/**
	 * ページ位置の設定.
	 * 
	 * @param pageNumber	現在位置
	 * @param pageLimit	ページ上限
	 */
	public void checkPageNumber(Integer pageNumber,Integer pageLimit) {
		if (pageNumber > pageLimit) {
			pageNumber = pageLimit;
		} else if (pageNumber < 1) {
			pageNumber = 1;
		}
	}
	
	/**
	 * カテゴリ選択用のカテゴリリスト.
	 * 
	 * @return	カテゴリリスト.
	 */
	public List<Category> forCategorySelect() {
		List<Category> largeCategoryList = categoryRepository.findLargeAll();
		return largeCategoryList;
	}
	
	/**
	 * アイテムを各種検索する.
	 * 
	 * @param name	アイテム名
	 * @param smallCategory	小カテゴリID
	 * @param middleCategory	中カテゴリID
	 * @param largeCategory	大カテゴリID
	 * @param brand	ブランド名
	 * @param pageNumber	現在位置
	 * @param isBrandSearch	ブランド検索
	 * @return	アイテムリスト
	 */
	public List<Item> searchItem(String name,Integer smallCategory, Integer middleCategory,Integer largeCategory,String brand,Integer pageNumber, boolean isBrandSearch){
		Integer beginNumber = pageNumber * 30 - 30;
		if(isBrandSearch==true) {
			return itemRepository.findByBrandCompletely(brand, beginNumber);
		}

		if((name==null&&brand==null )||( name.equals("")&&brand.equals(""))) {
			if(hasCategory(smallCategory)){
				return itemRepository.findBySmallCategory(smallCategory, beginNumber);
			}
			if(hasCategory(middleCategory)) {
				return itemRepository.findByMiddleCategory(middleCategory, beginNumber);
			}
			return itemRepository.findByLargeCategory(largeCategory, beginNumber);			
		}
		if(name==null||name.equals("")) {
			if(hasCategory(smallCategory)) {
				return itemRepository.findBySmallCategoryAndBrand(smallCategory, brand, beginNumber);
			}
			if(hasCategory(middleCategory)) {
				return itemRepository.findByMiddleCategoryAndBrand(middleCategory, brand, beginNumber);
			}
			if(hasCategory(largeCategory)) {
				return itemRepository.findByLargeCategoryAndBrand(largeCategory, brand, beginNumber);				
			}
			return itemRepository.findByBrand(brand, beginNumber);
		}
		if(brand==null||brand.equals("")) {
			if(hasCategory(smallCategory)) {
				return itemRepository.findByNameAndSmallCategory(name, smallCategory, beginNumber);
			}
			if(hasCategory(middleCategory)) {
				return itemRepository.findByNameAndMiddleCategory(name, middleCategory, beginNumber);
			}
			if(hasCategory(largeCategory)) {
				return itemRepository.findByNameAndLargeCategory(name, largeCategory, beginNumber);				
			}
			return itemRepository.findByName(name, beginNumber);
		}
		if(hasCategory(smallCategory)) {
			return itemRepository.findByNameAndSmallCategoryAndBrand(name, smallCategory, brand, beginNumber);
		}
		if(hasCategory(middleCategory)) {
			return itemRepository.findByNameAndMiddleCategoryAndBrand(name, middleCategory, brand, beginNumber);
		}
		if(hasCategory(largeCategory)) {
			return itemRepository.findByNameAndLargeCategoryAndBrand(name, largeCategory, brand, beginNumber);			
		}
		return itemRepository.findByNameAndBrand(name, brand, beginNumber);
	}

	/**
	 * 指定カテゴリが存在するかチェックするメソッド.
	 * 
	 * @param category	カテゴリID
	 * @return	if文の条件式
	 */
	private boolean hasCategory(Integer category) {
		return category!=null;
	}
	
	/**
	 * ページ上限を取得する.
	 * 
	 * @param name	アイテム名
	 * @param smallCategory	小カテゴリID
	 * @param middleCategory	中カテゴリID
	 * @param largeCategory	大カテゴリID
	 * @param brand	ブランド名
	 * @param isBrandSearch	ブランド検索
	 * @return
	 */
	public Integer forPageLimit(String name,Integer smallCategory, Integer middleCategory,Integer largeCategory,String brand, boolean isBrandSearch) {
		if(isBrandSearch==true) {
			return setPageLimit(itemRepository.findAmountByBrandCompletely(brand));
		}
		
		if((name==null&&brand==null )||( name.equals("")&&brand.equals(""))) {
			if(hasCategory(smallCategory)){
				return setPageLimit(itemRepository.findAmountBySmallCategory(smallCategory));
			}
			if(hasCategory(middleCategory)) {
				return setPageLimit(itemRepository.findAmountByMiddleCategory(middleCategory));
			}
			return setPageLimit(itemRepository.findAmountByLargeCategory(largeCategory));
		}
		if(name==null||name.equals("")) {
			if(hasCategory(smallCategory)) {
				return setPageLimit(itemRepository.findAmountBySmallCategoryAndBrand(smallCategory, brand));
			}
			if(hasCategory(middleCategory)) {
				return setPageLimit(itemRepository.findAmountByMiddleCategoryAndBrand(middleCategory, brand));
			}
			if(hasCategory(largeCategory)) {
				return setPageLimit(itemRepository.findAmountByLargeCategoryAndBrand(largeCategory, brand));
			}
			return setPageLimit(itemRepository.findAmountByBrand(brand));
		}
		if(brand==null||brand.equals("")) {
			if(hasCategory(smallCategory)) {
				return setPageLimit(itemRepository.findAmountByNameAndSmallCategory(name, smallCategory));
			}
			if(hasCategory(middleCategory)) {
				return setPageLimit(itemRepository.findAmountByNameAndMiddleCategory(name, middleCategory));
			}
			if(hasCategory(largeCategory)) {
				return setPageLimit(itemRepository.findAmountByNameAndLargeCategory(name, largeCategory));
			}
			return setPageLimit(itemRepository.findAmountByName(name));
		}
		if(hasCategory(smallCategory)) {
			return setPageLimit(itemRepository.findAmountByNameAndSmallCategoryAndBrand(name, smallCategory, brand));
		}
		if(hasCategory(middleCategory)) {
			return setPageLimit(itemRepository.findAmountByNameAndMiddleCategoryAndBrand(name, middleCategory, brand));
		}
		if(hasCategory(largeCategory)) {
			return setPageLimit(itemRepository.findAmountByNameAndLargeCategoryAndBrand(name, largeCategory, brand));			
		}
		return setPageLimit(itemRepository.findAmountByNameAndBrand(name, brand));
	}

	/**
	 * ページ上限を設定する.
	 * 
	 * @param itemAmount	アイテム数
	 * @return	ページ上限
	 */
	private int setPageLimit(Integer itemAmount) {
		return (int)Math.ceil((double)itemAmount / 30);
	}

}
