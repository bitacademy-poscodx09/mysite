package mysite.repository;

import mysite.vo.SiteVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class SiteRepository {
	private SqlSession sqlSession;
	
	public SiteRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public SiteVo find() {
		return sqlSession.selectOne("site.find");
	}

	public void update(SiteVo vo) {
		sqlSession.update("site.update", vo);
	}	
}
