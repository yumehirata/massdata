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

	private final static String START_OF_SQL_FOR_SEARCH = "SELECT i.id item_id,i.name item_name,condition,category,brand,price,shipping,description,"
			+ "sc.id sc_id,sc.name sc_name,sc.parent sc_parent,sc.name_all sc_name_all, "
			+ "mc.id mc_id,mc.name mc_name,mc.parent mc_parent,lc.id lc_id,lc.name lc_name " + "FROM items i "
			+ "LEFT OUTER JOIN category sc ON category = sc.id " + "LEFT OUTER JOIN category mc ON sc.parent = mc.id "
			+ "LEFT OUTER JOIN category lc ON mc.parent = lc.id ";

	private final static String END_OF_SQL_FOR_SEARCH = " ORDER BY i.id " + "OFFSET :beginNumber LIMIT 30";

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
	public Integer findAllAmount() {
		String sql = "SELECT COUNT(*) FROM items";
		SqlParameterSource param = new MapSqlParameterSource();
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}

	/**
	 * 一覧表示のための全件検索.
	 * 
	 * @param beginNumber
	 *            開始ナンバー
	 * @return Itemリスト
	 */
	public List<Item> findAllForPaging(Integer beginNumber) {

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}

	/**
	 * IDでItemのみを検索する.
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
	 * IDでカテゴリを含むItemを検索する.
	 * 
	 * @param id
	 *            検索キーとなるID
	 * @return Item
	 */
	public Item findByIdHasCategory(Integer id) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append(" WHERE i.id = :id");

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList.get(0);
	}

	/**
	 * ブランド名完全一致検索のアイテム数取得.
	 * 
	 * @param brand
	 *            検索キーとなるカテゴリID
	 * @return アイテム数
	 */
	public Integer findAmountByBrandCompletely(String brand) {

		String sql = "SELECT COUNT(*) FROM items WHERE brand ILIKE :brand";
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", brand);

		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}

	/**
	 * ブランド名完全一致のアイテム検索.
	 * 
	 * @param brand
	 *            検索キーとなるカテゴリID
	 * @return アイテム一覧
	 */
	public List<Item> findByBrandCompletely(String brand, Integer beginNumber) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE brand ILIKE :brand ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", brand).addValue("beginNumber",
				beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}

	/**
	 * アイテム名あいまい検索のアイテム数取得.
	 * 
	 * @param name
	 *            アイテム名
	 * @return アイテム数
	 */
	public Integer findAmountByName(String name) {
		String sql = "SELECT COUNT(*) FROM items i WHERE name ILIKE :name";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");

		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}

	/**
	 * 小カテゴリ検索のアイテム数取得.
	 * 
	 * @param smallCategory	小カテゴリID
	 * @return	アイテム数
	 */
	public Integer findAmountBySmallCategory(Integer smallCategory) {
		String sql = "SELECT COUNT(*) FROM items i WHERE category = :smallCategory";
		SqlParameterSource param = new MapSqlParameterSource().addValue("smallCategory", smallCategory);

		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	/**
	 * 中カテゴリ検索のアイテム数取得.
	 * 
	 * @param middleCategory	中カテゴリID
	 * @return	アイテム数
	 */
	public Integer findAmountByMiddleCategory(Integer middleCategory) {
		String sql = "SELECT COUNT(*) FROM items i "
				+ "LEFT OUTER JOIN category sc ON category = sc.id " 
				+ "WHERE parent = :middleCategory";
		SqlParameterSource param = new MapSqlParameterSource().addValue("middleCategory", middleCategory);
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	/**
	 * 大カテゴリ検索のアイテム数取得.
	 * 
	 * @param largeCategory	大カテゴリID
	 * @return	アイテム数
	 */
	public Integer findAmountByLargeCategory(Integer largeCategory) {
		String sql= "SELECT COUNT(*) FROM items i "
				+ "LEFT OUTER JOIN category sc ON category = sc.id " 
				+ "LEFT OUTER JOIN category mc ON sc.parent = mc.id " 
				+ "WHERE mc.parent = :largeCategory";
		SqlParameterSource param = new MapSqlParameterSource().addValue("largeCategory", largeCategory);
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	/**
	 * ブランド名あいまい検索のアイテム数取得.
	 * 
	 * @param brand	ブランド名
	 * @return	アイテム数
	 */
	public Integer findAmountByBrand(String brand) {
		String sql = "SELECT COUNT(*) FROM items i WHERE brand ILIKE :brand";
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%");
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	/**
	 * アイテム名ブランド名検索のアイテム数取得.
	 * 
	 * @param name	アイテム名
	 * @param brand	ブランド名	
	 * @return　アイテム数
	 */
	public Integer findAmountByNameAndBrand(String name,String brand) {
		String sql = "SELECT COUNT(*) FROM items i WHERE name ILIKE :name AND brand ILIKE :brand";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name",  "%" + name + "%").addValue("brand", "%" + brand + "%");
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	
	/**
	 * アイテム名小カテゴリ検索のアイテム数取得.
	 * 
	 * @param name	アイテム名
	 * @param smallCategory	小カテゴリID
	 * @return	アイテム数
	 */
	public Integer findAmountByNameAndSmallCategory(String name,Integer smallCategory) {
		String sql = "SELECT COUNT(*) FROM items i WHERE name ILIKE :name AND category = :smallCategory";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+ name +"%").addValue("smallCategory", smallCategory);
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	/**
	 * アイテム名中カテゴリ検索のアイテム数取得.
	 * 
	 * @param name	アイテム名
	 * @param middleCategory	中カテゴリID
	 * @return	アイテム数
	 */
	public Integer findAmountByNameAndMiddleCategory(String name,Integer middleCategory) {
		String sql = "SELECT COUNT(*) FROM items i "
				+ "LEFT OUTER JOIN category sc ON category = sc.id " 
				+ "WHERE i.name ILIKE :name AND parent = :middleCategory";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+ name +"%").addValue("middleCategory", middleCategory);
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	/**
	 * アイテム名大カテゴリ検索のアイテム数取得.
	 * 
	 * @param name	アイテム名
	 * @param largeCategory	大カテゴリID
	 * @return	アイテム数
	 */
	public Integer findAmountByNameAndLargeCategory(String name,Integer largeCategory) {
		String sql= "SELECT COUNT(*) FROM items i "
				+ "LEFT OUTER JOIN category sc ON category = sc.id " 
				+ "LEFT OUTER JOIN category mc ON sc.parent = mc.id " 
				+ "WHERE i.name ILIKE :name AND mc.parent = :largeCategory";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+ name +"%").addValue("largeCategory", largeCategory);
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	/**
	 * 小カテゴリブランド検索のアイテム数取得.
	 * 
	 * @param smallCategory	小カテゴリID
	 * @param brand	ブランド名
	 * @return	アイテム数
	 */
	public Integer findAmountBySmallCategoryAndBrand(Integer smallCategory,String brand) {
		String sql = "SELECT COUNT(*) FROM items i WHERE category = :smallCategory AND brand ILIKE :brand";
		SqlParameterSource param = new MapSqlParameterSource().addValue("smallCategory", smallCategory).addValue("brand", "%"+ brand +"%");
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	/**
	 * 中カテゴリブランド検索のアイテム数取得.
	 * 
	 * @param middleCategory	中カテゴリID
	 * @param brand	ブランド名
	 * @return	アイテム数
	 */
	public Integer findAmountByMiddleCategoryAndBrand(Integer middleCategory,String brand) {
		String sql = "SELECT COUNT(*) FROM items i "
				+ "LEFT OUTER JOIN category sc ON category = sc.id " 
				+ "WHERE parent = :middleCategory AND brand ILIKE :brand";
		SqlParameterSource param = new MapSqlParameterSource().addValue("middleCategory", middleCategory).addValue("brand", "%"+ brand+"%");
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	/**
	 * 大カテゴリブランド検索のアイテム数取得.
	 * 
	 * @param largeCategory	大カテゴリID
	 * @param brand	ブランド名
	 * @return	アイテム数
	 */
	public Integer findAmountByLargeCategoryAndBrand(Integer largeCategory,String brand) {
		String sql= "SELECT COUNT(*) FROM items i "
				+ "LEFT OUTER JOIN category sc ON category = sc.id " 
				+ "LEFT OUTER JOIN category mc ON sc.parent = mc.id " 
				+ "WHERE mc.parent = :largeCategory AND brand ILIKE :brand";
		SqlParameterSource param = new MapSqlParameterSource().addValue("largeCategory", largeCategory).addValue("brand", "%"+ brand +"%");
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	/**
	 * アイテム名小カテゴリブランド検索のアイテム数取得.
	 * 
	 * @param name	アイテム名
	 * @param smallCategory	小カテゴリID
	 * @param brand	ブランド名
	 * @return	アイテム数
	 */
	public Integer findAmountByNameAndSmallCategoryAndBrand(String name,Integer smallCategory,String brand) {
		String sql = "SELECT COUNT(*) FROM items i WHERE name ILIKE :name AND category = :smallCategory AND brand ILIKE :brand";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+ name +"%").addValue("smallCategory", smallCategory).addValue("brand", "%"+ brand +"%");
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	/**
	 * アイテム名中カテゴリブランド検索のアイテム数取得.
	 * 
	 * @param name	アイテム名
	 * @param middleCategory	中カテゴリID
	 * @param brand	ブランド名
	 * @return	アイテム数
	 */
	public Integer findAmountByNameAndMiddleCategoryAndBrand(String name,Integer middleCategory,String brand) {
		String sql = "SELECT COUNT(*) FROM items i "
				+ "LEFT OUTER JOIN category sc ON category = sc.id " 
				+ "WHERE i.name ILIKE :name AND parent = :middleCategory AND brand ILIKE :brand";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+ name +"%").addValue("middleCategory", middleCategory).addValue("brand", "%"+ brand+"%");
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}
	
	/**
	 * アイテム名大カテゴリブランド検索のアイテム数取得.
	 * 
	 * @param name	アイテム名
	 * @param largeCategory	大カテゴリID
	 * @param brand	ブランド名
	 * @return	アイテム数
	 */
	public Integer findAmountByNameAndLargeCategoryAndBrand(String name,Integer largeCategory,String brand) {
		String sql= "SELECT COUNT(*) FROM items i "
				+ "LEFT OUTER JOIN category sc ON category = sc.id " 
				+ "LEFT OUTER JOIN category mc ON sc.parent = mc.id " 
				+ "WHERE i.name ILIKE :name AND  mc.parent = :largeCategory AND brand ILIKE :brand";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+ name +"%").addValue("largeCategory", largeCategory).addValue("brand", "%"+ brand +"%");
		
		Integer itemAmount = template.queryForObject(sql, param, Integer.class);
		return itemAmount;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * アイテム名あいまい検索で取得したアイテム.
	 * 
	 * @param name	アイテム名
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByName(String name, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE i.name ILIKE :name ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;	}
	
	/**
	 * 小カテゴリ検索で取得したアイテム.
	 * 
	 * @param smallCategory	小カテゴリID
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findBySmallCategory(Integer smallCategory, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE category = :smallCategory ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("smallCategory", smallCategory).addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 中カテゴリ検索で取得したアイテム.
	 * 
	 * @param middleCategory	中カテゴリID
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByMiddleCategory(Integer middleCategory, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE mc.id = :middleCategory ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("middleCategory", middleCategory).addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
		}
	
	/**
	 * 大カテゴリ検索で取得したアイテム.
	 * 
	 * @param largeCategory	大カテゴリID
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByLargeCategory(Integer largeCategory, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE lc.id = :largeCategory ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("largeCategory", largeCategory).addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
		}
	
	/**
	 * ブランド名あいまい検索で取得したアイテム.
	 * 
	 * @param brand	ブランド名
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByBrand(String brand, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE brand ILIKE :brand ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%").addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
		}
	
	/**
	 * アイテム名ブランド名検索で取得したアイテム.
	 * 
	 * @param name	アイテム名
	 * @param brand	ブランド名
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByNameAndBrand(String name,String brand, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE i.name ILIKE :name AND brand ILIKE :brand ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("brand", "%" + brand + "%").addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
		}
	
	/**
	 * アイテム名小カテゴリ検索で取得したアイテム.
	 * 
	 * @param name	アイテム名
	 * @param smallCategory	小カテゴリ
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByNameAndSmallCategory(String name, Integer smallCategory, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE i.name ILIKE :name AND category = :smallCategory ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("smallCategory", smallCategory).addValue("name", "%" + name + "%").addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}

	
	/**
	 * アイテム名中カテゴリ検索で取得したアイテム.
	 * 
	 * @param name	アイテム名
	 * @param middleCategory	中カテゴリ
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByNameAndMiddleCategory(String name,Integer middleCategory, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE i.name ILIKE :name AND mc.id = :middleCategory ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("middleCategory", middleCategory).addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * アイテム名大カテゴリ検索で取得したアイテム.
	 * 
	 * @param name	アイテム名
	 * @param largeCategory	大カテゴリ
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByNameAndLargeCategory(String name,Integer largeCategory, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE i.name ILIKE :name AND lc.id = :largeCategory ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("largeCategory", largeCategory).addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 小カテゴリブランド名検索で取得したアイテム.
	 * 
	 * @param smallCategory	小カテゴリID
	 * @param brand	ブランド名
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findBySmallCategoryAndBrand(Integer smallCategory, String brand, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE category = :smallCategory AND i.brand ILIKE :brand ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("smallCategory", smallCategory).addValue("brand", "%" + brand + "%").addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 中カテゴリブランド名検索で取得したアイテム.
	 * 
	 * @param middleCategory	中カテゴリID
	 * @param brand	ブランド名
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByMiddleCategoryAndBrand(Integer middleCategory, String brand, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE mc.id = :middleCategory AND i.brand ILIKE :brand ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("middleCategory", middleCategory).addValue("brand", "%" + brand + "%").addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 大カテゴリブランド名検索で取得したアイテム.
	 * 
	 * @param largeCategory	大カテゴリID
	 * @param brand	ブランド名
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByLargeCategoryAndBrand(Integer largeCategory, String brand, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE lc.id = :largeCategory AND i.brand ILIKE :brand ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("largeCategory", largeCategory).addValue("brand", "%" + brand + "%").addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * アイテム名小カテゴリブランド名検索で取得したアイテム.
	 * 
	 * @param name	アイテム名
	 * @param smallCategory	小カテゴリID
	 * @param brand	ブランド名
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByNameAndSmallCategoryAndBrand(String name,Integer smallCategory, String brand, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE i.name ILIKE :name AND category = :smallCategory AND i.brand ILIKE :brand ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("smallCategory", smallCategory).addValue("brand", "%" + brand + "%").addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * アイテム名中カテゴリブランド名検索で取得したアイテム.
	 * 
	 * @param name	アイテム名
	 * @param middleCategory	中カテゴリID
	 * @param brand	ブランド名
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByNameAndMiddleCategoryAndBrand(String name,Integer middleCategory, String brand, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE i.name ILIKE :name AND mc.id = :middleCategory AND i.brand ILIKE :brand ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("middleCategory", middleCategory).addValue("brand", "%" + brand + "%").addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * アイテム名大カテゴリブランド名検索で取得したアイテム.
	 * 
	 * @param name	アイテム名
	 * @param largeCategory	大カテゴリID
	 * @param brand	ブランド名
	 * @param beginNumber	開始位置
	 * @return	アイテムリスト
	 */
	public List<Item> findByNameAndLargeCategoryAndBrand(String name,Integer largeCategory, String brand, Integer beginNumber){
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(START_OF_SQL_FOR_SEARCH);
		sqlStr.append("WHERE i.name ILIKE :name AND lc.id = :largeCategory AND i.brand ILIKE :brand ");
		sqlStr.append(END_OF_SQL_FOR_SEARCH);

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("largeCategory", largeCategory).addValue("brand", "%" + brand + "%").addValue("beginNumber", beginNumber);

		List<Item> itemList = template.query(sqlStr.toString(), param, ITEM_CATEGORY_ROW_MAPPER);
		return itemList;
	}

}
