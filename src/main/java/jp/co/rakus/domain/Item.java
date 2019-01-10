package jp.co.rakus.domain;

/**
 * Itemを設定するドメイン.
 * 
 * @author yume.hirata
 *
 */
public class Item {

	/** アイテムID */
	private Integer id;
	/** アイテム名 */
	private String name;
	/** 状態 */
	private Integer condition;
	/** カテゴリID */
	private Integer category;
	/** ブランド名 */
	private String brand;
	/** 価格 */
	private double price;
	/** 配送状況 */
	private Integer shipping;
	/** 説明 */
	private String description;
	
	/**	大カテゴリ */
	private Category largeCategory;
	/**	中カテゴリ */
	private Category middleCategory;
	/**	小カテゴリ */
	private Category smallCategory;

	public Item(Integer id, String name, Integer condition, Integer category, String brand, double price,
			Integer shipping, String description) {
		super();
		this.id = id;
		this.name = name;
		this.condition = condition;
		this.category = category;
		this.brand = brand;
		this.price = price;
		this.shipping = shipping;
		this.description = description;
	}

	public Item() {

	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", condition=" + condition + ", category=" + category + ", brand="
				+ brand + ", price=" + price + ", shipping=" + shipping + ", description=" + description + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Integer getShipping() {
		return shipping;
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getLargeCategory() {
		return largeCategory;
	}

	/* 我追加 **/
	public void setLargeCategory(Category largeCategory) {
		this.largeCategory = largeCategory;
	}

	public Category getMiddleCategory() {
		return middleCategory;
	}

	public void setMiddleCategory(Category middleCategory) {
		this.middleCategory = middleCategory;
	}

	public Category getSmallCategory() {
		return smallCategory;
	}

	public void setSmallCategory(Category smallCategory) {
		this.smallCategory = smallCategory;
	}

	


	
	
}
