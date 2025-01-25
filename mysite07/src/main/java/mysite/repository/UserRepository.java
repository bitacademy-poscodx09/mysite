package mysite.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import mysite.vo.UserVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserRepository {
	private SqlSession sqlSession;
	
	public UserRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public int insert(UserVo vo) {
		return sqlSession.insert("user.insert", vo);
	}

	public UserVo findByEmailAndPassword(String email, String password) {
		return sqlSession.selectOne("user.findByEmailAndPassword", Map.of("email", email, "password", password));
	}	

	public UserVo findById(Long userId) {
		return sqlSession.selectOne("user.findById", userId);
	}

	public <R> R findByEmail(String email, Class<R> resultType) {
		Map<String, Object> resultMap = sqlSession.selectOne("user.findByEmail", email);
		return new ObjectMapper().convertValue(resultMap, resultType);
	}

	public int update(UserVo vo) {
		return sqlSession.update("user.update", vo);
	}
}
