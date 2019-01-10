package jp.co.rakus.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.rakus.domain.Category;
import jp.co.rakus.domain.Item;
import jp.co.rakus.domain.Original;
import jp.co.rakus.repository.CategoryRepository;
import jp.co.rakus.repository.ItemRepository;
import jp.co.rakus.repository.OriginalRepository;

/**
 * DBにデータ挿入用のコントローラー.
 * 
 * @author yume.hirata
 *
 */
@Controller
public class TestController {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private OriginalRepository originalRepository;
	@Autowired
	private ItemRepository itemRepository;

	/**
	 * Categoryテーブルの中身を詰める.
	 * 
	 * @return test画面
	 */
	public String testSelect() {
		List<String> categoryNameList = originalRepository.findAllCategory();

		for (String categoryName : categoryNameList) {
			if (categoryName != null) {
				String[] categoryArray = categoryName.split("/");

				Integer largeId = categoryRepository.findIdByNameforLargeCategory(categoryArray[0]);

				// findIdByNameの第二引数は大カテゴリ・中カテゴリ・小カテゴリの判断基準。配列の番号と同一
				if (largeId == null) {
					Category category = new Category();
					category.setName(categoryArray[0]);
					categoryRepository.insert(category);
				}

				largeId = categoryRepository.findIdByNameforLargeCategory(categoryArray[0]);

				if (categoryRepository.findIdByNameAndParent(categoryArray[1], largeId) == null) {
					Category category = new Category();
					category.setName(categoryArray[1]);
					category.setParent(largeId);
					categoryRepository.insert(category);
				}

				Integer middleId = categoryRepository.findIdByNameAndParent(categoryArray[1], largeId);

				if (categoryRepository.findIdByNameAndParent(categoryArray[2], middleId) == null) {

					Category category = new Category();
					category.setName(categoryArray[2]);
					category.setParent(middleId);
					category.setNameAll(categoryArray[0] + "/" + categoryArray[1] + "/" + categoryArray[2]);
					categoryRepository.insert(category);
				}
			}
		}

		return "test";
	}

	/**
	 * Itemsテーブルの中身を詰める.
	 * 
	 * @return test画面
	 */
	@RequestMapping("/testItem")
	public String testItem() {
		List<Original> originalList = originalRepository.findAll();
		for (Original original : originalList) {
			
			Item item = new Item();

			BeanUtils.copyProperties(original, item);
			item.setCondition(original.getConditionId());

			// ここでカテ名からidを取ってきてセットする
			if (original.getCategoryName() != null) {
				String[] categoryNameArray = original.getCategoryName().split("/");
				String categoryName= categoryNameArray[0] + "/" + categoryNameArray[1] +"/" + categoryNameArray[2];
				Integer categoryId = categoryRepository.findIdByNameCompletely(categoryName);
				item.setCategory(categoryId);
			}

			itemRepository.insert(item);

			if(original.getId()%10000 == 0) {
				System.out.println(original.getId());
			}
		}

		return "test";
	}

}
