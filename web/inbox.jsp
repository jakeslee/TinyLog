<%--
  Created by IntelliJ IDEA.
  User: jakes
  Date: 14-12-23
  Time: 下午10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp"%>
<main class="container" role="main">
  <div class="row">
<div class="col-sm-12 col-md-7 col-sm-push-0 col-md-push-1">
  <c:if test="${currentPage eq 'inbox'}">
    <article class="panel panel-default">
      <div class="panel-body">
        <table class="table table-striped">
          <thead>
            <tr>
              <th>#</th>
              <th>消息内容</th>
              <th>发送人</th>
              <th>发送时间</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${msgs}" var="msg" varStatus="count">
              <tr>
                <td>${count.count}</td>
                <c:choose>
                  <c:when test="${msg.read ne 1}">
                    <td><strong>${msg.content}</strong></td>
                  </c:when>
                  <c:otherwise>
                    <td>${msg.content}</td>
                  </c:otherwise>
                </c:choose>

                <td>${msg.target}</td>
                <td>${msg.time_all}</td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </article>
  </c:if>
</div>
    <%@ include file="rightside.jsp"%>
  </div>
</main>
<%@ include file="footer.jsp"%>
<%@ include file="bottom.jsp"%>