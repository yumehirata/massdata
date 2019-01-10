package jp.co.rakus.repository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.rakus.domain.Original;

/**
 * Originalテーブルを操作するリポジトリ.
 * 
 * @author yume.hirata
 *
 */
@Repository
public class OriginalRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	
	
	/**
	 *	orderに関するROWMAPPER. 
	 */
	private final static RowMapper<Original> ORIGINAL_ROW_MAPPER = (rs,i) ->{
		Original original = new Original();
		original.setId(rs.getInt("id"));
		original.setName(rs.getString("name"));
		original.setConditionId(rs.getInt("condition_id"));
		original.setCategoryName(rs.getString("category_name"));
		original.setBrand(rs.getString("brand"));
		original.setPrice(rs.getDouble("price"));
		original.setShipping(rs.getInt("shipping"));
		original.setDescription(rs.getString("description"));
		
		return original;
	};
	
	/**
	 * originalテーブルの全件検索.
	 * 
	 * @return	originalのリスト.
	 */
	public List<Original> findAll() {
		String sql = "SELECT id,name,condition_id,category_name,brand,price,shipping,description FROM original ORDER BY id";
		List<Original> list = template.query(sql, ORIGINAL_ROW_MAPPER);
		
		return list;
	}
	
	/**
	 * カテゴリの全件検索.
	 * 
	 * @return	カテゴリ名のリスト
	 */
	public List<String> findAllCategory(){
		String sql = "SELECT category_name FROM original ORDER BY id";
		SqlParameterSource param = new MapSqlParameterSource();
		List<String> duplicateList = template.queryForList(sql, param,String.class);
		List<String> list = new ArrayList<String>(new LinkedHashSet<>(duplicateList));
		
		return list;
	}

}
