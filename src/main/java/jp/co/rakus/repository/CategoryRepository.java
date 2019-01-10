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
 * Categoryテーブルを操作するリポジトリ.
 * 
 * @author yume.hirata
 *
 */
@Repository
public class CategoryRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * categoryに関するROWMAPPER.
	 */
	private final static RowMapper<Category> CATEGORY_ROW_MAPPER = (rs, i) -> {
		Category category = new Category();
		category.setId(rs.getInt("id"));
		category.setParent(rs.getInt("parent"));
		category.setName(rs.getString("name"));
		category.setNameAll(rs.getString("name_all"));

		return category;
	};
	
	private final static RowMapper<Item> FORITEM_CATEGORY_ROW_MAPPER = (rs,i) -> {
		Item item = new Item();
		
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
	 * Categoryをインサートする.
	 * 
	 * @param category
	 *            挿入するデータ
	 */
	public void insert(Category category) {
		String sql = "INSERT INTO category(name_all, parent, name) VALUES(:nameAll, :parent, :name)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(category);
		template.update(sql, param);
	}

	/**
	 * 名前で大カテゴリからカテゴリIDを検索する.
	 * 
	 * @param name
	 *            カテゴリ名
	 * @return 探してきたID
	 */
	public Integer findIdByNameforLargeCategory(String name) {
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", name);

		String sql = "SELECT id FROM category WHERE name LIKE :name AND parent IS NULL AND name_all IS NULL";

		List<Integer> list = template.queryForList(sql, param, Integer.class);

		if (list.size() == 0) {
			return null;
		}

		Integer id = list.get(0);
		return id;
	}

	/**
	 * カテゴリ名と親のIDからカテゴリIDを検索する.
	 * 
	 * @param name
	 *            検索キーとなるカテゴリ名
	 * @param parent
	 *            検索キーとなる親カテゴリのID
	 * @return カテゴリID
	 */
	public Integer findIdByNameAndParent(String name, Integer parent) {
		String sql = "SELECT id FROM category WHERE name LIKE :name AND parent = :parent";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", name).addValue("parent", parent);

		List<Integer> list = template.queryForList(sql, param, Integer.class);

		if (list.size() == 0) {
			return null;
		}

		Integer id = list.get(0);
		return id;
	}

	/**
	 * カテゴリ名の完全一致でカテゴリIDを検索する.
	 * 
	 * @param name	検索キーとなるカテゴリ名
	 * @return	カテゴリID
	 */
	public Integer findIdByNameCompletely(String name) {
		String sql = "SELECT id FROM category WHERE name_all LIKE :name";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", name);

		List<Integer> list = template.queryForList(sql, param, Integer.class);

		if (list.size() == 0) {
			return null;
		}

		Integer id = list.get(0);
		return id;
	}
	
	/**
	 * カテゴリIDからカテゴリを検索する.
	 * 
	 * @param id	検索キーとなるカテゴリ名
	 * @return	カテゴリ
	 */
	public Category findById(Integer id){
		String sql = "SELECT name_all, id, parent, name FROM category WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		
		List<Category> categoryList = template.query(sql,param,CATEGORY_ROW_MAPPER);
		
		if(categoryList.size() == 0) {
			return null;
		}
		
		return categoryList.get(0);
	}
	
	/**
	 * 大カテゴリ一覧を検索する.
	 * 
	 * @return	大カテゴリ一覧
	 */
	public List<Category> findLargeAll(){
		String sql = "SELECT name_all, id, parent, name FROM category WHERE parent IS NULL AND name_all IS NULL ORDER BY name";
		
		List<Category> categoryList = template.query(sql, CATEGORY_ROW_MAPPER);
		return categoryList;
	}
	
	/**
	 * parentIDからカテゴリを検索する.
	 * 
	 * @param parent	検索キーとなる親のカテゴリID
	 * @return	カテゴリ一覧
	 */
	public List<Category> findByParent(Integer parent){
		String sql = "SElECT name_all, id, parent, name FROM category WHERE parent = :parent ORDER BY name";
		SqlParameterSource param = new MapSqlParameterSource().addValue("parent", parent);
		
		List<Category> categoryList = template.query(sql, param, CATEGORY_ROW_MAPPER);
		return categoryList;
	}
	
	public Item findBySmallCategoryId(Integer id){
		String sql = "select " + 
				"sc.id sc_id,sc.name sc_name,sc.parent sc_parent,sc.name_all sc_name_all, " + 
				"mc.id mc_id,mc.name mc_name,mc.parent mc_parent,lc.id lc_id,lc.name lc_name " + 
				"FROM category sc " + 
				"LEFT OUTER JOIN category mc ON sc.parent = mc.id " + 
				"LEFT OUTER JOIN category lc ON mc.parent = lc.id " + 
				"WHERE sc.id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		
		List<Item> itemList = template.query(sql, param, FORITEM_CATEGORY_ROW_MAPPER);
		
		if(itemList.size()==0) {
			return null;
		}
		return itemList.get(0);
	}
}
