package jp.co.rakus.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * アイテム登録フォームの情報を取得する.
 * 
 * @author yume.hirata
 *
 */
public class ItemForm {

	/** アイテム名 */
	@Size(max = 255, message = "255文字以内で入力してください")
	@NotBlank(message = "アイテム名が空欄です")
	private String name;
	/** 状態 */
	@NotNull(message = "conditionを選択してください")
	private Integer condition;
	/** 小カテゴリ */
	@NotNull(message = "categoryを正しく選択してください")
	private Integer category;
	/** ブランド名 */
	@Size(max = 255, message = "255文字以内で入力してください")
	@NotBlank(message = "ブランド名が空欄です")
	private String brand;
	/** 価格 */
	@NotNull(message = "価格を入力してください")
	@Pattern(regexp = "\\d+(\\.\\d+)?", message = "価格は半角数字で入力してください")
	private String price;
	/** 説明 */
	@NotBlank(message = "説明が空欄です")
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
