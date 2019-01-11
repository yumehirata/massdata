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
	 * カテゴリ不使用時のアイテム数検索.
	 * 
	 * @param name
	 *            検索キーとなるアイテム名
	 * @param brand
	 *            検索キーとなるブランド
	 * @param beginNumber
	 *            検索開始番号
	 * @return アイテム一覧
	 */
	public List<Item> searchItemNoCategory(String name, Integer category, String brand, Integer beginNumber) {

		List<Item> itemList;

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

		return itemList;
	}

	/**
	 * カテゴリ不使用時のアイテム数検索.
	 * 
	 * @param name
	 *            検索キーとなるアイテム名
	 * @param brand
	 *            検索キーとなるブランド
	 * @param beginNumber
	 *            検索開始番号
	 * @return アイテム数
	 */
	public int searchItemNumberNoCategory(String name, Integer category, String brand) {

		int pageNumber = itemRepository.findByNameAndBrandPageNumber(name, brand);

		return pageNumber;
	}

	/**
	 * カテゴリ使用時のアイテム検索.
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
	 * @param beginNumber
	 *            検索開始位置
	 * @return アイテム一覧
	 */
	public List<Item> searchItemUsingCategory(String name, Integer largeCategory, Integer middleCategory,
			Integer smallCategory, String brand, Integer beginNumber) {
		List<Item> itemList = itemRepository.findByNameAndCategoryAndBrand(name, largeCategory, middleCategory,
				smallCategory, brand, beginNumber);

		return itemList;
	}

	/**
	 * カテゴリ使用時のアイテム検索.
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
	 * @return アイテム数
	 */
	public Integer searchItemNumberUsingCategory(String name, Integer largeCategory, Integer middleCategory,
			Integer smallCategory, String brand) {
		int pageNumber = itemRepository.findByNameAndCategoryAndBrandPageNumber(name, largeCategory, middleCategory,
				smallCategory, brand);

		return pageNumber;

	}

	/**
	 * ブランド完全一致検索.
	 * 
	 * @param brand
	 *            検索キーとなるブランド名
	 * @param beginNumber
	 *            検索開始番号
	 * @return アイテム一覧
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
	 * ブランド完全一致アイテム数検索.
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
