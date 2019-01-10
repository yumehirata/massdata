package jp.co.rakus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.rakus.domain.Category;
import jp.co.rakus.repository.CategoryRepository;

/**
 * 検索のカテゴリプルダウンを設定するコントローラー.
 * 
 * @author yume.hirata
 *
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/category")
public class SelectCategoryController {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	/**
	 * カテゴリIDからその子のカテゴリを検索してプルダウンに表示する.
	 * 
	 * @param id	検索キーとなる親カテゴリID
	 * @return	検索されたカテゴリ一覧
	 */
	@ResponseBody
	@RequestMapping(value="/forSelect", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public Category[] forSelect(@RequestBody String id){
		
		String parentStr = id.replace("\"id\":", "").replace("\"", "").replace("{", "").replace("}", "");
		Integer parent = Integer.parseInt(parentStr);
		
		List<Category> categoryList = categoryRepository.findByParent(parent);
		
		Category[] categories = categoryList.toArray(new Category[categoryList.size()]);
		
		return categories;
	}


}
