<%--
  Created by IntelliJ IDEA.
  User: jakes
  Date: 14-12-8
  Time: 下午7:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="header.jsp"%>
<main class="container" role="main">
  <div class="row">
    <div class="col-sm-12 col-md-7 col-sm-push-0 col-md-push-1">
      <c:choose>
        <c:when test="${articlelist ne null}">
          <c:forEach items="${articlelist}" var="article">
            <article class="panel panel-default">
              <div class="panel-heading clearfix">
                <h3 class="panel-title pull-left"><a href="${homeurl}/articleView?id=${article.id}" class="article-title">${article.title}</a></h3>
                <div class="post-info pull-right">
                  Posted by <a href="#">${article.realname}</a> at
                    ${article.time}
                </div>
              </div>
              <div class="panel-body article-content">${article.summary}</div>
              <input hidden="hidden" value="${article.id}">
              <div class="panel-footer post-info clearfix">
            <span class="pull-left">
              <c:forEach items="${article.tags}" var="tag" varStatus="cur">
                <a href="${homeurl}/tags?tag=${tag}">${tag}</a>${!cur.last?',':''}
              </c:forEach>
            </span>
                <div class="pull-right">
                  <a class="newReply cursor-pointer">
                    <c:choose>
                      <c:when test="${article.replies eq 0}">
                        NO REPLY
                      </c:when>
                      <c:otherwise>
                        ${article.replies}&nbsp;REPLY
                      </c:otherwise>
                    </c:choose>
                  </a>
                </div>
              </div>
            </article>
          </c:forEach>
        </c:when>
      </c:choose>
    </div>
    <%@ include file="rightside.jsp"%>
  </div>
</main>
<%@ include file="footer.jsp"%>
<%@ include file="bottom.jsp"%>