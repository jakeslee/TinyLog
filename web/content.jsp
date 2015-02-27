<%--
  Created by IntelliJ IDEA.
  User: jakes
  Date: 14-12-4
  Time: 下午5:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <div class="col-sm-12 col-md-7 col-sm-push-0 col-md-push-1">
    <c:if test="${currentPage eq 'search'}">
      <article class="panel panel-default">
        <div class="panel-heading clearfix">
          以下是正文按照关键字&nbsp;&nbsp;“<a class="cursor-pointer">${keyword}</a>”&nbsp;&nbsp;查询匹配到的所有文章
        </div>
      </article>
    </c:if>
    <c:choose>
      <c:when test="${currentPage ne 'article' and requestScope.articlelist ne null}">
        <c:forEach items="${requestScope.articlelist}" var="article">
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
      <c:when test="${currentPage eq 'article' and article ne null}">
        <article class="panel panel-default">
          <div class="panel-heading clearfix">
            <h3 class="panel-title pull-left">${article.title}</h3>
            <div class="post-info pull-right">
              Posted by <a href="#">${article.realname}</a> at
                ${article.time_all}
            </div>
          </div>
          <div class="panel-body article-content"><c:out value="${article.content}"></c:out></div>
          <input hidden="hidden" value="${article.id}">
          <div class="panel-footer post-info clearfix">
            <span class="pull-left">
              <c:forEach items="${article.tags}" var="tag" varStatus="cur">
                <a href="${homeurl}/tags?tag=${tag}">${tag}</a>${!cur.last?',':''}
              </c:forEach>
            </span>
            <div class="pull-right">
              <a class="newReply cursor-pointer">
                REPLY
              </a>
            </div>
          </div>
          <div class="panel-footer post-info clearfix">
            <div class="list-title clearfix">
              <h3>文章评论</h3>
            </div>
            <div class="article-comments">
              <ul class="article-comment">
                <c:choose>
                  <c:when test="${comments ne null}">
                    <c:forEach items="${comments}" var="comment">
                      <li>
                  <span class="article-comment-photo">
                      <img src="${comment.avatar}" class="img-circle center-block nav-user-photo avator">
                      <span data-username="${comment.username}" class="text-uppercase article-comment-author">${comment.nickname}</span>
                  </span>
                        <input hidden="hidden" value="${comment.id}">
                        <div class="article-comment-content">
                            ${comment.content}
                        </div>
                        <div class="article-comment-control">
                    <span>
                        <a class="cursor-pointer reply-comment" title="回复"><i class="fa fa-mail-reply"></i></a>
                    </span>
                          <span class="article-comment-date">${comment.time_all}</span>
                        </div>
                      </li>
                    </c:forEach>
                  </c:when>
                  <c:otherwise>
                    没有评论
                  </c:otherwise>
                </c:choose>
              </ul>
            </div>
          </div>
          <div class="panel-footer post-info clearfix">
            <form>
              <div class="row">
                <div class="col-sm-5">
                  <div class="input-group input-group-sm margin-bottom-sm">
                    <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
                    <c:choose>
                    <c:when test="${empty username}">
                    <input id="nickname" name="nickname" class="form-control" type="text" placeholder="昵称">
                  </div>
                  <div class="input-group input-group-sm margin-bottom-sm">
                    <span class="input-group-addon"><i class="fa fa-envelope-o fa-fw"></i></span>
                    <input id="email" name="email" class="form-control" type="email" placeholder="邮箱">
                    </c:when>
                    <c:otherwise>
                    <input id="nickname" class="form-control" name="nickname" type="text" placeholder="昵称" value="${empty user.realname?"佚名":user.realname}" disabled>
                  </div>
                  <div class="input-group input-group-sm margin-bottom-sm">
                    <span class="input-group-addon"><i class="fa fa-envelope-o fa-fw"></i></span>
                    <input id="email" class="form-control" name="email" value="${empty user.email?"保密":user.email}" disabled type="email" placeholder="邮箱">
                    </c:otherwise>
                    </c:choose>

                  </div>
                </div>
                <div class="col-sm-7 hidden-xs">
                  1. 未登录用户请填写左边编辑框的内容后再评论。<br>
                  2. 请勿谈论违反所有国家法律相关规定的内容。<br>
                  3. 请不要发表政治、宗教和反人类等言论。<br>
                  4. 我们不接受垃圾、广告评论，请确保你的评论是有价值的。
                </div>
              </div>
              <textarea id="comment-post-content" name="content" style="height: 60px;" rows="10"></textarea>
            </form>
          </div>
        </article>
      </c:when>
      <c:when test="${currentPage eq 'inbox'}">
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
      </c:when>
    </c:choose>
    <c:if test="${not empty pages}">
      <nav>
        <ul class="pagination">
          <li ${empty prevPages?'class="disabled"':''}>
            <a href="${site.homeurl}/?page=${empty prevPages?'0':prevPages}">&laquo;</a>
          </li>
          <c:forEach items="${pages}" var="page">
            <li ${page.current?'class="active"':''}>
              <a href="${site.homeurl}/?page=${page.index}">${page.index}
                <span class="sr-only">(current)</span>
              </a>
            </li>
          </c:forEach>
          <li ${empty nextPages?'class="disabled"':''}>
            <a href="${site.homeurl}/?page=${empty nextPages?'0':nextPages}">&raquo;</a>
          </li>
        </ul>
      </nav>
    </c:if>
  </div>
