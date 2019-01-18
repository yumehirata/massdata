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
	
}
