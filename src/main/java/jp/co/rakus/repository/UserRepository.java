package jp.co.rakus.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.rakus.domain.User;

@Repository
public class UserRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * アイテムに関するROWMAPPER.
	 */
	private final static RowMapper<User> USER_ROW_MAPPER = (rs,i) ->{
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		user.setAuthority(rs.getInt("authority"));
		return user;
	};
	
	/**
	 * 新規ユーザー登録する.
	 * 
	 * @param user	インサートするユーザー情報
	 */
	public void insert(User user) {
		String sql = "INSERT INTO users(name,password,authority) VALUES(:name,:password,:authority)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		
		template.update(sql, param);
	}
	
	/**
	 * 名前からuserを検索する.
	 * 
	 * @param name	検索キーとなる名前
	 * @return	ユーザー
	 */
	public User findByName(String name) {
		String sql = "SELECT id,name,password,authority FROM users WHERE name LIKE :name";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", name);
		
		List<User> userList = template.query(sql, param,USER_ROW_MAPPER);
		
		if(userList.size()==0) {
			return null;
		}
		
		return userList.get(0);
	}

}
