<%--
  Created by IntelliJ IDEA.
  User: jakes
  Date: 14-12-4
  Time: 下午5:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-sm-12 col-md-3 col-sm-push-0 col-md-push-1">
  <div class="panel panel-default side-list">
    <div class="panel-heading list-title clearfix">
      <h3>公告</h3>
    </div>
    <div class="content">
      ${site.notice}
    </div>
  </div>
  <div class="panel panel-default side-list">
    <div class="panel-heading list-title clearfix">
      <h3>最新日志</h3>
      <a class="pull-right">更多</a>
    </div>
    <ul class="side-list-control">
      <c:choose>
        <c:when test="${latest_articles ne null}">
          <c:forEach items="${latest_articles}" var="latest_article">
            <li class="clearfix">
              <a href="${homeurl}/articleView?id=${latest_article.id}">${latest_article.title}</a>
              <span>${latest_article.time_md}</span>
            </li>
          </c:forEach>
        </c:when>
        <c:otherwise>
          <li class="clearfix">
            没有文章
          </li>
        </c:otherwise>
      </c:choose>
    </ul>
  </div>
  <div class="panel panel-default side-list">
    <div class="panel-heading list-title clearfix">
      <h3>最新留言</h3>
      <a class="pull-right">更多</a>
    </div>
    <div class="commentscontrol">
      <ul class="comments">
        <c:choose>
          <c:when test="${latest_cmts ne null}">
            <c:forEach items="${latest_cmts}" var="cmt">
              <li>
                    <span class="comment-photo">
                        <img src="${cmt.avatar}" class="img-circle nav-user-photo avator">
                    </span>
                <a href="${homeurl}/articleView?id=${cmt.target}">${cmt.content}</a><br>
                <div class="comment-info">
                  <span class="text-uppercase comment-author">${cmt.nickname}</span>
                  <span class="comment-date">${cmt.time_md}</span>
                </div>
              </li>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <li>
              <a>没有评论</a><br>
            </li>
          </c:otherwise>
        </c:choose>
        <c:if test="">

        </c:if>
      </ul>
    </div>
  </div>

  <c:if test="${user.auth ge 1}">
    <div class="affix side-bar hidden-xs">
      <a id="addPostBtn" class="btn btn-default"><span class="fa fa-plus-square-o fa-lg"></span></a>
      <c:if test="${(currentPage eq 'article') and not empty article.id and (username eq article.username or user.auth eq 2)}">
        <a id="editCurrent" class="btn btn-default">
          <span class="fa fa-edit"></span>
        </a>
        <a id="delCurrent" class="btn btn-default">
          <span class="fa fa-trash-o fa-lg"></span>
        </a>
      </c:if>
      <a href="${site.homeurl}/inbox" class="btn btn-default">
        <span class="fa fa-envelope-o ${user.newMsg?"icon-animated-vertical-infinite":""}"></span>
      </a>
    </div>
  </c:if>
</div>