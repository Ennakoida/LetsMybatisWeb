<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>모두 마이바티스 멤버 웹</title>
	</head>
	<body>
<%-- 		로그인이 성공했으면 보여요 : ${ sessionScope.memberId } --%>
		<h1>모두 마이바티스 멤버 웹!</h1>
		<c:if test="${sessionScope.memberId ne null }">
			${ memberName }님 환영합니다. <br>
			<a href="/member/logout.do">로그아웃</a><br>
<!-- 			SELECT * FROM MEMBER_TBL WHERE MEMBER_ID = ? -->
<!-- 			MEMBER_ID의 값을 받아와야 하기 때문에 쿼리스트링을 직접 만들어줘야한다. -->
			<a href="/member/myPage.do?memberId=${ memberId }">마이페이지</a> 
		</c:if>
		<c:if test="${sessionScope.memberId eq null }">
			<h2>로그인 페이지</h2>
			<fieldset>
				<legend>LOGIN</legend>
				<form action="/member/login.do" method="post">
					<input type="text" name="member-id"> <br>
					<input type="password" name="member-pw"> <br>
					<div>
						<input type="submit" value="로그인">
						<input type="reset" value="취소">
						<a href="/member/register.do">회원가입</a>
					</div>
				</form>
			</fieldset>
		</c:if>			
	</body>
</html>