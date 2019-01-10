package jp.co.rakus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	/**
	 * 引数から適したメソッドを呼び出しアイテム検索を行う（小カテゴリ、カテゴリなし）.
	 * 
	 * @param name
	 *            検索キーとなるアイテム名
	 * @param category
	 *            検索キーとなるカテゴリ
	 * @param brand
	 *            検索キーとなるブランド
	 * @param beginNumber
	 *            検索開始番号
	 * @return アイテム一覧
	 */
	public List<Item> searchItem(String name, Integer category, String brand, Integer beginNumber) {

		List<Item> itemList;

		if (category == null) {
			itemList = itemRepository.findByNameAndBrand(name, brand, beginNumber);

			for (Item preItem : itemList) {
				Item item = itemRepository.findByIdHasCategory(preItem.getId());

				Item forCategory = categoryRepository.findBySmallCategoryId(item.getCategory());
				if (forCategory != null) {
					preItem.setLargeCategory(forCategory.getLargeCategory());
					preItem.setMiddleCategory(forCategory.getMiddleCategory());
					preItem.setSmallCategory(forCategory.getSmallCategory());
				}
			}
		} else {
			itemList = itemRepository.findByNameAndCategoryAndBrand(name, category, brand, beginNumber);
		}

		return itemList;
	}

	/**
	 * 引数から適したメソッドを呼び出しアイテム数検索を行う（小カテゴリ、カテゴリなし）.
	 * 
	 * @param name
	 *            検索キーとなるアイテム名
	 * @param category
	 *            検索キーとなるカテゴリ
	 * @param brand
	 *            検索キーとなるブランド
	 * @param beginNumber
	 *            検索開始番号
	 * @return アイテム数
	 */
	public int searchItemNumber(String name, Integer category, String brand) {

		int pageNumber;

		if (category == null) {
			pageNumber = itemRepository.findByNameAndBrandPageNumber(name, brand);
		} else {
			pageNumber = itemRepository.findByNameAndCategoryAndBrandPageNumber(name, category, brand);
		}

		return pageNumber;
	}

	/**
	 * 引数から適したメソッドを呼び出しアイテム検索を行う（大カテゴリ）.
	 * 
	 * @param name
	 *            検索キーとなるアイテム名
	 * @param category
	 *            検索キーとなるカテゴリ
	 * @param brand
	 *            検索キーとなるブランド
	 * @param beginNumber
	 *            検索開始番号
	 * @return アイテム一覧
	 */
	public List<Item> searchItemByLargeCategory(String name, Integer category, String brand, Integer beginNumber) {

		List<Item> itemList = itemRepository.findByNameAndLargeCategoryAndBrand(name, category, brand, beginNumber);

		return itemList;
	}

	/**
	 * 引数から適したメソッドを呼び出しアイテム数検索を行う（大カテゴリ）.
	 * 
	 * @param name
	 *            検索キーとなるアイテム名
	 * @param category
	 *            検索キーとなるカテゴリ
	 * @param brand
	 *            検索キーとなるブランド
	 * @param beginNumber
	 *            検索開始番号
	 * @return アイテム数
	 */
	public int searchItemNumberByLargeCategory(String name, Integer category, String brand) {

		int pageNumber = itemRepository.findByNameAndLargeCategoryAndBrandPageNumber(name, category, brand);

		return pageNumber;
	}

	/**
	 * 引数から適したメソッドを呼び出しアイテム検索を行う（中カテゴリ）.
	 * 
	 * @param name
	 *            検索キーとなるアイテム名
	 * @param category
	 *            検索キーとなる中カテゴリ
	 * @param brand
	 *            検索キーとなるブランド
	 * @param beginNumber
	 *            検索開始番号
	 * @return アイテム一覧
	 */
	public List<Item> searchItemByMiddleCategory(String name, Integer category, String brand, Integer beginNumber) {

		List<Item> itemList = itemRepository.findByNameAndMiddleCategoryAndBrand(name, category, brand, beginNumber);

		return itemList;
	}

	/**
	 * 引数から適したメソッドを呼び出しアイテム数検索を行う（中カテゴリ）.
	 * 
	 * @param name
	 *            検索キーとなるアイテム名
	 * @param category
	 *            検索キーとなる中カテゴリ
	 * @param brand
	 *            検索キーとなるブランド
	 * @param beginNumber
	 *            検索開始番号
	 * @return アイテム数
	 */
	public int searchItemNumberByMiddleCategory(String name, Integer category, String brand) {

		int pageNumber = itemRepository.findByNameAndMiddleCategoryAndBrandPageNumber(name, category, brand);

		return pageNumber;
	}
	
	/**
	 * ブランド完全一致検索.
	 * 
	 * @param brand	検索キーとなるブランド名
	 * @param beginNumber	検索開始番号
	 * @return	アイテム一覧
	 */
	public List<Item> searchItemForBrand(String brand, Integer beginNumber) {

		List<Item> itemList;

			itemList = itemRepository.findByBrandCompletely(brand, beginNumber);

			for (Item preItem : itemList) {
				Item item = itemRepository.findByIdHasCategory(preItem.getId());

				Item forCategory = categoryRepository.findBySmallCategoryId(item.getCategory());
				if (forCategory != null) {
					preItem.setLargeCategory(forCategory.getLargeCategory());
					preItem.setMiddleCategory(forCategory.getMiddleCategory());
					preItem.setSmallCategory(forCategory.getSmallCategory());
				}
			}

		return itemList;
	}

	/**
	 * 引数から適したメソッドを呼び出しアイテム数検索を行う（小カテゴリ、カテゴリなし）.
	 * 
	 * @param brand
	 *            検索キーとなるブランド
	 * @param beginNumber
	 *            検索開始番号
	 * @return アイテム数
	 */
	public int searchItemNumberForBrand(String brand) {

		int pageNumber = itemRepository.findByBrandCompletelyPageNumber(brand);

		return pageNumber;
	}
}
