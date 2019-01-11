package jp.co.rakus.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.rakus.domain.Category;
import jp.co.rakus.domain.Item;

/**
 * Itemsテーブルの中身を操作するリポジトリ.
 * 
 * @author yume.hirata
 *
 */
@Repository
public class ItemRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * itemに関するROWMAPPER.
	 */
	private final static RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setCondition(rs.getInt("condition"));
		item.setBrand(rs.getString("brand"));
		item.setPrice(rs.getDouble("price"));
		item.setShipping(rs.getInt("shipping"));
		item.setDescription(rs.getString("description"));

		return item;
	};

	/**
	 * categoryを持つitemに関するROWMAPPER.
	 */
	private final static RowMapper<Item> ITEM_CATEGORY_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("item_id"));
		item.setName(rs.getString("item_name"));
		item.setCondition(rs.getInt("condition"));
		item.setCategory(rs.getInt("category"));
		item.setBrand(rs.getString("brand"));
		item.setPrice(rs.getDouble("price"));
		item.setShipping(rs.getInt("shipping"));
		item.setDescription(rs.getString("description"));

		Category smallCategory = new Category();
		smallCategory.setId(rs.getInt("sc_id"));
		smallCategory.setName(rs.getString("sc_name"));
		smallCategory.setParent(rs.getInt("sc_parent"));
		smallCategory.setNameAll(rs.getString("sc_name_all"));
		item.setSmallCategory(smallCategory);

		Category middleCategory = new Category();
		middleCategory.setId(rs.getInt("mc_id"));
		middleCategory.setName(rs.getString("mc_name"));
		middleCategory.setParent(rs.getInt("mc_parent"));
		item.setMiddleCategory(middleCategory);

		Category largeCategory = new Category();
		largeCategory.setId(rs.getInt("lc_id"));
		largeCategory.setName(rs.getString("lc_name"));
		item.setLargeCategory(largeCategory);

		return item;
	};

	/**
	 * Itemsテーブルのidの最大値を求める.
	 * 
	 * @return 最大値
	 */
	public Integer findMaxId() {
		String sql = "SELECT MAX(id) FROM items";
		SqlParameterSource param = new MapSqlParameterSource();
		Integer maxId = template.queryForObject(sql, param, Integer.class);
		return maxId;
	}

	/**
	 * Itemsテーブルにインサートする.
	 * 
	 * @param item
	 *            インサート内容
	 */
	public void insert(Item item) {
		String sql = "INSERT INTO items(id,name,condition,category,brand,price,shipping,description) VALUES(:id,:name,:condition,:category,:brand,:price,:shipping,:description)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		template.update(sql, param);
	}

	/**
	 * Itemの情報を更新する.
	 * 
	 * @param item
	 *            更新するアイテム情報
	 */
	public void update(Item item) {
		String sql = "UPDATE items SET id=:id,name=:name,condition=:condition,category=:category,brand=:brand,price=:price,shipping=:shipping,description=:description WHERE id=:id";
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);

		template.update(sql, param);
	}

	/**
	 * 全件検索の際の総アイテム数を取得する.
	 * 
	 * @return アイテム数
	 */
	public Integer findAllPageNumber() {
		String sql = "SELECT COUNT(*) FROM items";
		SqlParameterSource param = new MapSqlParameterSource();
		Integer itemNum = template.queryForObject(sql, param, Integer.class);
		return itemNum;
	}

	/**
	 * 一覧表示のためのページング検索.
	 * 
	 * @param beginNumber
	 *            開始ナンバー
	 * @return Itemリスト
	 */
	public List<Item> findAllForPaging(Integer beginNumber) {

		String sql = "select i.id item_id,i.name item_name,condition,category,brand,price,shipping,description,"
				+ "sc.id sc_id,sc.name sc_name,sc.parent sc_parent,sc.name_all sc_name_all, "
				+ "mc.id mc_id,mc.name mc_name,mc.parent mc_parent," + "lc.id lc_id,lc.name lc_name " + "FROM items i "
				+ "LEFT OUTER JOIN category sc ON category = sc.id "
				+ "LEFT OUTER JOIN category mc ON sc.parent = mc.id "
				+ "LEFT OUTER JOIN category lc ON mc.parent = lc.id " + "ORDER BY i.id "
				+ "OFFSET :beginNumber LIMIT 30";

		SqlParameterSource param = new MapSqlParameterSource().addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sql, param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}

	/**
	 * IDでItemを検索する.
	 * 
	 * @param id
	 *            検索キーとなるID
	 * @return Item
	 */
	public Item findById(Integer id) {
		String sql = "select id ,name ,condition,category,brand,price,shipping,description FROM items WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		return itemList.get(0);
	}

	/**
	 * IDでカテゴリを持つItemを検索する.
	 * 
	 * @param id
	 *            検索キーとなるID
	 * @return Item
	 */
	public Item findByIdHasCategory(Integer id) {
		String sql = "select i.id item_id,i.name item_name,condition,category,brand,price,shipping,description,"
				+ "sc.id sc_id,sc.name sc_name,sc.parent sc_parent,sc.name_all sc_name_all, "
				+ "mc.id mc_id,mc.name mc_name,mc.parent mc_parent," + "lc.id lc_id,lc.name lc_name " + "FROM items i "
				+ "LEFT OUTER JOIN category sc ON category = sc.id "
				+ "LEFT OUTER JOIN category mc ON sc.parent = mc.id "
				+ "LEFT OUTER JOIN category lc ON mc.parent = lc.id " + "WHERE i.id=:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		List<Item> itemList = template.query(sql, param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList.get(0);
	}
	
	/**
	 * ブランド名完全一致検索のアイテム数取得.
	 * 
	 * @param brand
	 *            検索キーとなるカテゴリID
	 * @return アイテム数
	 */
	public Integer findByBrandCompletelyPageNumber(String brand) {

		String sql = "SELECT COUNT(*) FROM items WHERE brand ILIKE :brand";
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", brand);

		Integer itemNum = template.queryForObject(sql, param, Integer.class);
		return itemNum;
	}

	/**
	 * ブランド名完全一致のアイテム検索.
	 * 
	 * @param brand
	 *            検索キーとなるカテゴリID
	 * @return アイテム一覧
	 */
	public List<Item> findByBrandCompletely(String brand, Integer beginNumber) {

		String sql = "select id ,name ,condition,category,brand,price,shipping,description " + "FROM items "
				+ "WHERE brand ILIKE :brand " + "ORDER BY id " + "OFFSET :beginNumber LIMIT 30";
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", brand).addValue("beginNumber",
				beginNumber);

		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * アイテム名とブランド名検索のアイテム数取得.
	 * 
	 * @param name
	 *            検索キーとなるアイテム名
	 * @param brand
	 *            検索キーとなるカテゴリID
	 * @return アイテム数
	 */
	public Integer findByNameAndBrandPageNumber(String name, String brand) {

		String sql = "SELECT COUNT(*) FROM items WHERE name ILIKE :name AND brand ILIKE :brand";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("brand",
				"%" + brand + "%");

		if (name == null || name.equals("")) {
			sql = "SELECT COUNT(*) FROM items WHERE brand ILIKE :brand";
			param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%");
		} else if (brand == null || brand.equals("")) {
			sql = "SELECT COUNT(*) FROM items WHERE name ILIKE :name";
			param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		}

		Integer itemNum = template.queryForObject(sql, param, Integer.class);
		return itemNum;
	}

	/**
	 * アイテム名とブランド名でアイテム検索.
	 * 
	 * @param name
	 *            検索キーとなるアイテム名
	 * @param brand
	 *            検索キーとなるカテゴリID
	 * @return アイテム一覧
	 */
	public List<Item> findByNameAndBrand(String name, String brand, Integer beginNumber) {

		StringBuilder sqlStr = new StringBuilder();
		SqlParameterSource param;
		sqlStr.append("SELECT id,name,condition,category,brand,price,shipping,description FROM items ");

		if (name == null || name.equals("")) {
			sqlStr.append("WHERE brand ILIKE :brand ");
			param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%").addValue("beginNumber",
					beginNumber);
		} else if (brand == null || brand.equals("")) {
			sqlStr.append("WHERE name ILIKE :name ");
			param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("beginNumber", beginNumber);
		} else {
			sqlStr.append("WHERE name ILIKE :name AND brand ILIKE :brand ");
			param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("brand", "%" + brand + "%")
					.addValue("beginNumber", beginNumber);
		}

		sqlStr.append(" ORDER BY id OFFSET :beginNumber LIMIT 30");

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_ROW_MAPPER);
		return itemList;
	}



	/**
	 * カテゴリ使用時のアイテム数取得.
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
	 * @return アイテム一覧
	 */
	public Integer findByNameAndCategoryAndBrandPageNumber(String name, Integer largeCategory, Integer middleCategory,
			Integer smallCategory, String brand) {

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT COUNT(*) FROM items i " + "LEFT OUTER JOIN category sc ON category = sc.id "
				+ "LEFT OUTER JOIN category mc ON sc.parent = mc.id "
				+ "LEFT OUTER JOIN category lc ON mc.parent = lc.id ");
		SqlParameterSource param;
		Integer category;

		if ((name == null && brand == null) || (name.equals("") && brand.equals(""))) {
			if (smallCategory != null) {
				category = smallCategory;
				sqlStr.append("WHERE category = :category");
			} else if (middleCategory != null) {
				category = middleCategory;
				sqlStr.append("WHERE mc.id = :category");
			} else {
				category = largeCategory;
				sqlStr.append("WHERE lc.id = :category");
			}
			param = new MapSqlParameterSource().addValue("category", category);

		} else if (name == null || name.equals("")) {
			if (smallCategory != null) {
				category = smallCategory;
				sqlStr.append("WHERE category = :category AND i.brand ILIKE :brand");
			} else if (middleCategory != null) {
				category = middleCategory;
				sqlStr.append("WHERE mc.id = :category AND i.brand ILIKE :brand");
			} else {
				category = largeCategory;
				sqlStr.append("WHERE lc.id = :category AND i.brand ILIKE :brand");
			}
			param = new MapSqlParameterSource().addValue("category", category).addValue("brand", "%" + brand + "%");

		} else if (brand == null || brand.equals("")) {
			if (smallCategory != null) {
				category = smallCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND category = :category");
			} else if (middleCategory != null) {
				category = middleCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND mc.id = :category");
			} else {
				category = largeCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND lc.id = :category");
			}
			param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("category", category);
		} else {
			if (smallCategory != null) {
				category = smallCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND category = :category AND i.brand ILIKE :brand");
			} else if (middleCategory != null) {
				category = middleCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND mc.id = :category AND i.brand ILIKE :brand");
			} else {
				category = largeCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND lc.id = :category AND i.brand ILIKE :brand");
			}
			param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("category", category)
					.addValue("brand", "%" + brand + "%");
		}
		Integer itemNum = template.queryForObject(sqlStr.toString(), param, Integer.class);
		return itemNum;
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
	public List<Item> findByNameAndCategoryAndBrand(String name, Integer largeCategory, Integer middleCategory, Integer smallCategory,
			String brand, Integer beginNumber) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select i.id item_id,i.name item_name,condition,category,brand,price,shipping,description,"
				+ "sc.id sc_id,sc.name sc_name,sc.parent sc_parent,sc.name_all sc_name_all, "
				+ "mc.id mc_id,mc.name mc_name,mc.parent mc_parent," + "lc.id lc_id,lc.name lc_name " + "FROM items i "
				+ "LEFT OUTER JOIN category sc ON category = sc.id "
				+ "LEFT OUTER JOIN category mc ON sc.parent = mc.id "
				+ "LEFT OUTER JOIN category lc ON mc.parent = lc.id ");
		SqlParameterSource param;
		Integer category;

		if ((name == null && brand == null) || (name.equals("") && brand.equals(""))) {
			if (smallCategory != null) {
				category = smallCategory;
				sqlStr.append("WHERE category = :category ");
			} else if (middleCategory != null) {
				category = middleCategory;
				sqlStr.append("WHERE mc.id = :category ");
			} else {
				category = largeCategory;
				sqlStr.append("WHERE lc.id = :category ");
			}
			param = new MapSqlParameterSource().addValue("category", category).addValue("beginNumber", beginNumber);

		} else if (name == null || name.equals("")) {
			if (smallCategory != null) {
				category = smallCategory;
				sqlStr.append("WHERE category = :category AND i.brand ILIKE :brand ");
			} else if (middleCategory != null) {
				category = middleCategory;
				sqlStr.append("WHERE mc.id = :category AND i.brand ILIKE :brand ");
			} else {
				category = largeCategory;
				sqlStr.append("WHERE lc.id = :category AND i.brand ILIKE :brand ");
			}
			param = new MapSqlParameterSource().addValue("category", category).addValue("brand", "%" + brand + "%")
					.addValue("beginNumber", beginNumber);

		} else if (brand == null || brand.equals("")) {
			if (smallCategory != null) {
				category = smallCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND category = :category ");
			} else if (middleCategory != null) {
				category = middleCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND mc.id = :category ");
			} else {
				category = largeCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND lc.id = :category ");
			}
			param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("category", category)
					.addValue("beginNumber", beginNumber);

		} else {
			if (smallCategory != null) {
				category = smallCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND category = :category AND i.brand ILIKE :brand ");
			} else if (middleCategory != null) {
				category = middleCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND mc.id = :category AND i.brand ILIKE :brand ");
			} else {
				category = largeCategory;
				sqlStr.append("WHERE i.name ILIKE :name AND lc.id = :category AND i.brand ILIKE :brand ");
			}
			param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("category", category)
					.addValue("brand", "%" + brand + "%").addValue("beginNumber", beginNumber);
		}

		sqlStr.append("ORDER BY i.id OFFSET :beginNumber LIMIT 30");

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}
}
