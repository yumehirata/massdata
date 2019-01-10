package jp.co.rakus.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.rakus.domain.Category;
import jp.co.rakus.domain.Item;
import jp.co.rakus.form.ItemForm;
import jp.co.rakus.repository.CategoryRepository;
import jp.co.rakus.repository.ItemRepository;

/**
 * アイテム登録に関するコントローラー.
 * 
 * @author yume.hirata
 *
 */
@Controller
@RequestMapping("/item")
public class RegisterItemController {
	
	@Autowired
	private ItemRepository ItemRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	@ModelAttribute
	private ItemForm setUpItemForm() {
		return new ItemForm();
	}
	
	/**
	 * アイテム追加画面へ遷移.
	 * 
	 * @param model	情報を引き渡すための引数
	 * @return	アイテム追加画面
	 */
	@RequestMapping("/toRegister")
	public String toRegister(Model model) {
		
		List<Category> largeCategoryList = categoryRepository.findLargeAll();
		model.addAttribute("largeCategoryList", largeCategoryList);
		
		return "registerItem";
	}
	
	/**
	 * アイテム追加.
	 * 
	 * @param form	フォームに入力された情報
	 * @param result	情報を引き渡すための引数
	 * @param model	情報を引き渡すための引数
	 * @param attributes 情報を引き渡すための引数
	 * @return	アイテム追加画面
	 */
	@RequestMapping("/register")
	public String register(@Validated ItemForm form,BindingResult result,Model model,RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return toRegister(model);
		}
		
		Item item = new Item();
		BeanUtils.copyProperties(form, item);
		
		item.setId(ItemRepository.findMaxId()+1);
		item.setPrice(Double.parseDouble(form.getPrice()));
		item.setShipping(999);	//仮shipping
		
		ItemRepository.insert(item);
		attributes.addFlashAttribute("message","アイテムを追加しました");
		
		return "redirect:/item/toRegister";
	}

}
