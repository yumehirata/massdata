package jp.co.rakus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.rakus.domain.User;

@Repository
public class UserRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
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
	 * @param user
	 */
	public void insert(User user) {
		String sql = "INSERT INTO users(name,password,authority) VALUES(:name,:password,:authority)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		template.update(sql, param);
	}

}
