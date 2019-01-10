package jp.co.rakus.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

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
 * アイテム編集画面を操作するコントローラー.
 * 
 * @author yume.hirata
 *
 */
@Controller
@RequestMapping("/item")
public class EditItemController {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private HttpSession session;

	@ModelAttribute
	public ItemForm setUpItemForm() {
		return new ItemForm();
	}

	/**
	 * アイテム編集画面を表示する.
	 * 
	 * @param id
	 *            アイテムID
	 * @param model
	 *            情報を引き渡すための引数
	 * @return アイテム編集画面
	 */
	@RequestMapping("/toEdit")
	public String toEdit(Integer id, Model model) {

		if (id == null) {
			id = Integer.parseInt(session.getAttribute("id").toString());
		}
		Item item = itemRepository.findById(id);
		model.addAttribute("item", item);

		List<Category> largeCategoryList = categoryRepository.findLargeAll();
		model.addAttribute("largeCategoryList", largeCategoryList);

		return "itemEdit";
	}

	/**
	 * アイテム編集を行う.
	 * 
	 * @param form
	 *            フォームに入力された情報
	 * @param id
	 *            アイテムID
	 * @param shipping
	 *            編集元のshipping
	 * @param result
	 *            情報を引き渡すための引数
	 * @param attributes
	 *            情報を引き渡すための引数
	 * @param model
	 *            情報を引き渡すための引数
	 * @return アイテム編集画面
	 */
	@RequestMapping("/edit")
	public String edit(@Validated ItemForm form, BindingResult result, Integer id, Integer shipping,
			RedirectAttributes attributes, Model model) {
		if (result.hasErrors()) {
			return toEdit(id, model);
		}

		Item item = new Item();
		BeanUtils.copyProperties(form, item);

		item.setId(id);
		item.setPrice(Double.parseDouble(form.getPrice()));
		// shippingもセット???
		item.setShipping(shipping);

		itemRepository.update(item);
		attributes.addFlashAttribute("message", "アイテムを編集しました");
		session.setAttribute("id", item.getId());

		return "redirect:/item/toEdit";
	}
}
