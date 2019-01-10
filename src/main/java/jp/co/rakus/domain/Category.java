package jp.co.rakus.domain;

/**
 * Categoryを設定するドメイン.
 * 
 * @author yume.hirata
 *
 */
public class Category {

	/**	カテゴリID */
	private Integer id;
	/**	親カテゴリID */
	private Integer parent;
	/**	カテゴリ名 */
	private String name;
	/**	上位カテゴリ名 */
	private String nameAll;
	
	public Category(Integer id, Integer parent, String name, String nameAll) {
		super();
		this.id = id;
		this.parent = parent;
		this.name = name;
		this.nameAll = nameAll;
	}
	
	public Category() {
		
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", parent=" + parent + ", name=" + name + ", nameAll=" + nameAll + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameAll() {
		return nameAll;
	}

	public void setNameAll(String nameAll) {
		this.nameAll = nameAll;
	}
	
	
}
