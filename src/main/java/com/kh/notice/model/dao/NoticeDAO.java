package com.kh.notice.model.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.kh.notice.model.vo.Notice;

public class NoticeDAO {
	// JDBC랑 다르게 1줄이면 코드 끝
	// insert를 할거면 session에서 insert() 메소드 호출
	// mapper.xml의 name값(NoticeMapper)과 쿼리문의 id값(insertNotice)을 호출!
	// 한 줄 쓰고 끝나는게 아니라 notice-mapper.xml을 만들고 태그를 이용해서 쿼리문 작성해주고
	// mapper.xml을 사용한다고 mybatis-config.xml에 신고/등록해야함!

	public int insertNotice(SqlSession session, Notice notice) {
		int result = session.insert("NoticeMapper.insertNotice", notice);
		
		return result;
	}

	public int updateNotice(SqlSession session, Notice notice) {
		int result = session.update("NoticeMapper.updateNotice", notice);
		return result;
	}

	public int deleteNotice(SqlSession session, int noticeNo) {
		int result = session.delete("NoticeMapper.deleteNotice", noticeNo);
		return result;
	}

	public List<Notice> selectNoticeList(SqlSession session, int currentPage) {
		// select를 할거면 session에서 selectList, selectOne 메소드 중에서 필요에 맞게 호출
		// mapper.xml의 name값(NoticeMapper)과 쿼리문의 id값(selectNoticeList)을 호출
		// 넘겨주는 값(매개변수)은 없으므로 name.id값 만 selectList() 메소드의 전달값으로 넘겨줌
		// mapper.xml에서는 select이기 때문에 rsetToNotice에 해당하는 ResultMap을 작성해줘야함!!!!
		
		// ********** 마이바티스 페이징처리 ***********
		/*
		 * 
		 * RowBounds : 행에 대한 바운더리를 설정
		 * RowBounds를 왜 쓰나요? 
		 * 	ㄴ 쿼리문을 변경하지 않고도 페이징을 처리할 수 있게 해주는 클래스
		 * RowBounds의 동작은 offset값과 limit값을 이용해서 동작함
		 * limit : 한 페이지당 보여주고 싶은 게시물의 갯수
		 * offset : 시작값. 변하는 값
		 * 
		 * ex)
		 * 1 페이지 : 0  (= 0 * 10)부터 시작해서 10개 -> 1 ~ 10
		 * 2 페이지 : 10 (= 1 * 10)부터 시작해서 10개 -> 11 ~ 20
		 * 3 페이지 : 20 (= 2 * 10)부터 시작해서 10개 -> 21 ~ 30
		 * 
		 */
		int limit = 10; // 한 번에 10개씩 보여줌
		int offset = (currentPage - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		// 넘겨받은 값(넘겨줄 값) 있었으면 null에 넣어줌 (where절)
		List<Notice> nList = session.selectList("NoticeMapper.selectNoticeList", null, rowBounds);
		
		return nList;
	}

	public Notice selectOneByNo(SqlSession session, int noticeNo) {
		Notice notice = session.selectOne("NoticeMapper.selectOneByNo", noticeNo);
		return notice;
	}
	
	public String generatePageNavi (SqlSession session, int currentPage) {
		int totalCount = getTotalCount(session); // 전체 게시물의 갯수
		int recordCountPerPage = 10; // 한 페이지당 보여줄 게시물의 갯수
		int naviCountPerPage = 5; // 한 페이지당 보여줄 범위(네비)의 갯수
		int totalNaviCount;
		if(totalCount % recordCountPerPage > 0)
			totalNaviCount = totalCount / recordCountPerPage + 1;
		else
			totalNaviCount = totalCount / recordCountPerPage;
		
		// currentPage		startNavi
		// 1,2,3,4,5			1
		// 6,7,8,9,10			6
		// 11,12,13,14,15		11
		
		// currentPage		  endNavi
		// 1,2,3,4,5			5
		// 6,7,8,9,10			10
		// 11,12,13,14,15		15
		
		int startNavi = ((currentPage - 1)/naviCountPerPage) * naviCountPerPage + 1;
		int endNavi = startNavi + naviCountPerPage - 1;
		if(endNavi > totalNaviCount) {
			endNavi = totalNaviCount;
		}
		
		StringBuffer result = new StringBuffer();
		boolean needPrev = true;
		boolean needNext = true;
		if(startNavi != 1) {
			result.append("<a href='/notice/list.do?currentPage=" + (currentPage - 1) + "'>[이전]</a>&nbsp");
		}
		
		for(int i = startNavi; i <= endNavi; i++) {
			result.append("<a href='/notice/list.do?currentPage=" + i + "'>" + i + "</a>&nbsp");
		}
		
		if(endNavi != totalNaviCount) {
			result.append("<a href='/notice/list.do?currentPage=" + (currentPage + 1) + "'>[다음]</a>&nbsp");
		}
		return result.toString();
	}
	
	private int getTotalCount(SqlSession session) {
		int totalCount = session.selectOne("NoticeMapper.getTotalCount");
		return totalCount;
	}

}
