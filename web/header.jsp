<%@ page import="asia.gkc.tinylog.Util" %>
<%--
  Created by IntelliJ IDEA.
  User: jakes
  Date: 14-12-4
  Time: 下午5:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  request.setAttribute("site", Util.getSite());
  request.setAttribute("latest_cmts", Util.getLatestComments(Integer.toString(Util.getSite().getNewestcmts())));
  request.setAttribute("latest_articles", Util.getLatestArticles(Integer.toString(Util.getSite().getNewestarts())));
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
  <c:choose>
    <c:when test="${currentPage eq 'index'}">
      <title>${site.name} - ${site.motto}</title>
    </c:when>
    <c:when test="${currentPage eq 'article'}">
      <title>${article.title} @ ${site.name} - ${site.motto}</title>
    </c:when>
    <c:when test="${currentPage eq 'inbox'}">
      <title>收件箱 @ ${site.name} - ${site.motto}</title>
    </c:when>
    <c:when test="${currentPage eq 'search'}">
      <title>查找关键字:${keyword} @ ${site.name} - ${site.motto}</title>
    </c:when>
    <c:otherwise>
      <title>Page Not Found 404 - 翔腾苍穹</title>
    </c:otherwise>
  </c:choose>
  <c:if test="">

  </c:if>

  <link rel="Shortcut Icon" href="favicon.ico" type="image/x-icon" media="screen">

  <!-- Bootstrap -->
  <link href="css/tinylog.css" rel="stylesheet">
  <link href="css/bootstrap.min.css" rel="stylesheet">
  <link href="css/font-awesome.css" rel="stylesheet">
  <link href="css/bootstrap-markdown.min.css" rel="stylesheet">

  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
  <![endif]-->

</head>
<body>
<c:if test="${empty sessionScope.username}">
<div id="loginBox" class="modal fade">
  <div class="modal-dialog modal-sm modal-dialog-center">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
          <span aria-hidden="true">&times;</span>
          <span class="sr-only">Close</span>
        </button>
        <h4 class="modal-title">作者登录</h4>
      </div>
      <div class="modal-body">
        <div class="input-group margin-bottom-sm">
          <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
          <input id="username" type="text" class="form-control" placeholder="用户名">
        </div>
        <div class="input-group">
          <span class="input-group-addon"><i class="fa fa-key fa-fw"></i></span>
          <input id="password" type="password" class="form-control" placeholder="密码">
        </div>
      </div>
      <div class="modal-footer">
        <button id="doLogin" type="button" class="btn btn-danger btn-block">登录</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
</c:if>


<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="pull-left navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="/">翔腾苍穹</a>
    </div>
    <div class="fix-top-right navbar-collapse">
      <ul class="nav navbar-nav">
        <li class="light-blue">
          <c:choose>
            <c:when test="${not empty sessionScope.username}">
              <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                <img class="img-circle nav-user-photo avator" src="${user.avatar}" alt="${empty user.realname?"佚名":user.realname}'s Photo">
                <span class="user-info">
                    <small>欢迎光临,</small><br>
                    ${empty user.realname?"佚名":user.realname}
                </span>
                <i class="caret"></i>
              </a>

              <ul class="dropdown-menu" style="position: relative">
                <li>
                  <a id="settingBtn" class="cursor-pointer">
                    <i class="fa fa-cog"></i>
                    设置
                  </a>
                </li>

                <li>
                  <a id="infoBtn" class="cursor-pointer">
                    <i class="fa fa-user"></i>
                    个人资料
                  </a>
                </li>

                <li class="divider"></li>

                <li>
                  <a id="logoutBtn" class="cursor-pointer">
                    <i class="fa fa-power-off"></i>
                    退出
                  </a>
                </li>
              </ul>
            </c:when>
            <c:otherwise>
              <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                <img class="img-circle nav-user-photo avator" src="img/avatar/guest.png" alt="Guest's Photo">
                        <span class="user-info">
                            <small>欢迎光临,</small><br>
                            游客
                        </span>
                <i class="caret right"></i>
              </a>

              <ul class="dropdown-menu" style="position: relative">
                <li>
                  <a id="loginBtn" class="cursor-pointer" data-toggle="modal" data-target="#loginBox">
                    <i class="fa fa-user"></i>
                    登录
                  </a>
                </li>
              </ul>
            </c:otherwise>
          </c:choose>
        </li>
      </ul>
    </div>
    <div class="navbar-left collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li class="active"><a href="${homeurl}/">首页</a></li>
        <li><a href="${homeurl}/">日志</a></li>
        <li><a>留言板</a></li>
        <li><a>关于</a></li>
      </ul>
      <div class="navbar-form navbar-left" role="search">
        <div class="form-group">
          <input id="searchBox" type="text" class="form-control" placeholder="搜索">
        </div>
      </div>
    </div>

  </div>

</nav>
<header class="container">
  <div class="row">
    <div class="col-md-4 col-md-push-1 col-sm-4 header-height hidden-xs">
      <a href="/" title="GKC" class="logo">翔腾苍穹</a>
    </div>

    <div class="col-md-8 col-sm-8 text-center">
      <span class="">站在阳光下，享受我单薄的青春~<small>——Jakes</small></span>
    </div>
  </div>
</header>
