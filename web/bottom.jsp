<%--
  Created by IntelliJ IDEA.
  User: jakes
  Date: 14-12-5
  Time: 下午10:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="js/jquery-1.11.1.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-markdown.js"></script>
<script src="js/bootstrap-markdown.zh.js"></script>
<script src="js/to-markdown.js"></script>
<script src="js/marked.js"></script>
<script src="js/sha1.js"></script>
<script type="text/javascript">
  function removeElement(obj, t){
    setTimeout(function(){
      obj.remove();
    }, t);
  }
  function sendComment(article_id, content, nickname, email){

    $.post('/comment?action=new',{
      'type': article_id,
      'target': article_id,
      'content': content,
      'username': nickname,
      'email': email
    },function(data){
      console.log(data);
      if(data.status == 'success'){
        $("#saveCmtBtn").text('发表成功');
        setTimeout(function(){
          $(".cancel-comment").click();
          location.reload();
          $("#saveCmtBtn").prop('disabled', false);
          $("#saveCmtBtn").text('发表');
          $("#comment-post-content").val("");
        }, 1000);
      }else{
        $("#saveCmtBtn").text('发表失败');
        $("#saveCmtBtn").addClass("btn-danger");
        setTimeout(function(){
          $("#saveCmtBtn").removeClass('btn-danger');
          $("#saveCmtBtn").prop('disabled', false);
          $("#saveCmtBtn").text('发表');
        }, 1000);
      }
    },'json');
  }

  function commentTo(comment_id, username, nickname, email){

  }

  function showDialog(title, content, delay){
    var body = '<div id="dialog" class="modal fade">'+
            '  <div class="modal-dialog modal-sm modal-dialog-center">'+
            '    <div class="modal-content">'+
            '      <div class="modal-header">'+
            '        <button type="button" class="close" data-dismiss="modal">'+
            '          <span aria-hidden="true">&times;</span>'+
            '          <span class="sr-only">Close</span>'+
            '        </button>'+
            '        <h4 class="modal-title">' + title +'</h4>'+
            '      </div>'+
            '      <div class="modal-body">'+
            '        <p>'+
            '          '+ content +
            '        </p>'+
            '      </div>'+
            '      <div class="modal-footer">'+
            '        <button type="button" onclick="location.reload();" data-dismiss="modal" class="btn btn-danger btn-block">确定</button>'+
            '      </div>'+
            '    </div><!-- /.modal-content -->'+
            '  </div><!-- /.modal-dialog -->'+
            '</div><!-- /.modal -->';
    $('body').prepend(body);
    $('#dialog').modal();
    removeElement($('#dialog'), delay);
  }

  function postArticle(title, content, modify){
    var tags = '';
    $('.new-tags span').each(function(){
      tags += $(this).text() + ' ';
    });

    var url = '/article?action=';
    var param = {
      'title': title,
      'content': content,
      'tags': tags
    };
    if (modify){
      url += 'edit';
      $.extend(param, {'id': $('#modifying').val()});
    }else{
      url += 'new';
    }

    $.post(url, param, function(data){
      console.log(data);
      if(data.status == 'success'){
        $("#post-new i").removeClass("fa-refresh fa-spin").addClass("fa-check");
        $("#post-new span").text("发表成功");
        $("#post-new").addClass("btn-success");
        setTimeout(function(){
          location.reload();
        }, 1000);
      }else{
        $("#post-new i").removeClass("fa-refresh fa-spin").addClass("fa-close");
        $("#post-new span").text("发表失败");
        $("#post-new").addClass("btn-danger");
        setTimeout(function(){
          $("#post-new i").removeClass("fa-close");
          $("#post-new span").text("发表");
          $("#post-new").addClass("btn-primary");
          $("#post-new").prop('disabled', false);
        }, 1000);
      }
    },'json');

  }
  function markdownEditor(obj){
    obj.markdown({
      savable:true,
      language:'zh',
      footer: '<button id="" type="button" class="cancel-comment btn btn-danger btn-sm pull-right">取消</button>',
      onSave: function(e) {
        var cmt_id = $("#comment-post-content").parents("article").children("input").val();

        if(!$.trim($("#nickname").val())){
          console.log($.trim($("#nickname").val()));
          $("#nickname").focus();
          return;
        }
        if(!$.trim($("#email").val())){
          $("#email").focus();
          return;
        }
        var regx_str = /^[\w\-\.]+@[\w\-\.]+(\.\w+)+$/;
        var email_val = $("#email").val();
        if (!regx_str.test(email_val)){
          $("#email").focus();
          return;
        }
        if(!$.trim($("#comment-post-content").val())){
          $("#comment-post-content").focus();
          return;
        }
        $("#saveCmtBtn").prop('disabled', true);
        $("#saveCmtBtn").text('正在发表');
        sendComment(cmt_id, e.getContent(), $.trim($("#nickname").val()), $.trim($("#email").val()));
        //commentTo.....
        console.log("Saving '"+e.getContent()+"'...to :"+ cmt_id + $("#nickname").val() +"|"+$.trim($("#nickname").value));
      }
    });
  }

  $(document).ready(function(){
    //search editor click
    $('#searchBox').keyup(function(event){
      console.log("haha"+event.keyCode+'|'+$('#searchBox').val());
      //window.location.href = "http://stackoverflow.com";
      if (event.keyCode == 13){
        //window.location.href = "http://stackoverflow.com";
        window.location = '${homeurl}/search?key=' + encodeURIComponent($('#searchBox').val());
      }
    });

    //edit/delete article
    <c:if test="${(currentPage eq 'article') and not empty article and (username eq article.username or user.auth eq 2)}">
    $('#editCurrent').click(function () {
      var title = '${article.title}';
      var content = toMarkdown($('.article-content').html());
      $("#addPostBtn").click();
      $('#post-article').append('<input id="modifying" hidden="hidden" value="${article.id}">');
      $('#post-title').val(title);
      $('#post-content').val(content);
      <c:forEach items="${article.tags}" var="tag">
      $(".new-tags").append("<span class=\"label label-success\" title=\"点击删除\">"+'${tag}'+"</span>");
      </c:forEach>

    });
    $('#delCurrent').click(function () {
      var article_id = '${article.id}';
      var title = '${article.title}';
      var r = confirm('你确定要删除标题为《' + title + '》的文章?');
      if(r == true){
        $.post('/article?action=delete',
                {'id': article_id},
                function (data) {
                  if (data.status == 'success'){
                    showDialog('删除文章', '文章《' + title + '》删除成功！', 1500);
                  }else{
                    showDialog('删除文章', '文章《' + title + '》删除失败！', 1000);
                  }
                }, 'json');
      }
    });
    </c:if>

    //markdown to html
    $('.article-content').each(function(){
      $(this).html(marked($(this).text()));
    });
    //add @ to edit
    $(".reply-comment").click(function(){
      var comment_id = $(this).parents("li").children("input").val();
      var username = $(this).parents("li").children("span").children(".article-comment-author").attr("data-username");
      console.log(comment_id + "|" + username);
      var content = $("#comment-post-content").data("markdown").getContent() + "@" + username + " ";
      $("#comment-post-content").data("markdown").setContent(content);
      $("#comment-post-content").focus();
    });
    //logined user post
    <c:if test="${sessionScope.user.auth ge 1}">

    $('#logoutBtn').click(function () {
      $.post('/auth?action=out', function (data) {
        if(data.status == 'success'){
          showDialog('退出登录', '退出登录成功！', 2000);

          setTimeout(function () {
            location.reload();
          }, 2000);
        }
      }, 'json');
    });
    $("#addPostBtn").click(function(){
      if($("#post-article").length){
        if (!$("#post-article").is(":visible")){
          $("#post-article").slideDown("slow");
        }
        return;
      }
      var date = new Date();
      $("main>div>div.col-md-7").prepend("<article id=\"post-article\" class=\"panel panel-default\">"+
      "                <div class=\"panel-heading clearfix\">"+
      "                    <div class=\"input-group panel-title pull-left\">"+
      "                        <span class=\"input-group-addon\">标题</span>"+
      "                        <input id=\"post-title\" type=\"text\" class=\"form-control\">"+
      "                    </div>"+
      "                    <div class=\"post-info pull-right\">"+
      "                        Posted by <span class='text-uppercase'>${user.realname}</span> at"+
      "                        "+date.getFullYear()+"-"+date.getMonth()+"-"+date.getDate()+
      "                    </div>"+
      "                </div>"+
      "                <div class=\"panel-body\">"+
      "                    <form>"+
      "                        <textarea id=\"post-content\" name=\"content\" data-provide=\"markdown\" rows=\"10\"></textarea>"+
      "                    </form>"+
      "                </div>"+
      "                <div class=\"panel-footer post-info clearfix\">"+
      "                    <span class=\"pull-left\">"+
      "                        <button id=\"cancel-post\" type=\"button\" class=\"btn btn-danger btn-sm\">取消</button>"+
      "                        <span class=\"new-tags\">"+
      "                            标签："+
      "                        </span>"+
      "                    </span>"+
      "                    <div class=\"pull-right\">"+
      "                        <button id=\"post-new\" class=\"btn btn-primary btn-sm\" >"+
      "                            <i class=\"fa fa-check\"></i>"+
      "                            <span>发表</span>"+
      "                        </button>"+
      "                    </div>"+
      "                </div>"+
      "            </article>");
      $("#post-content").markdown({
        language:'zh',
        additionalButtons: [
          [{
            name: "addTag",
            data: [{
              name: "cmdTag",
              toggle: false, // this param only take effect if you load bootstrap.js
              title: "添加标签",
              icon: "glyphicon glyphicon-tag",
              callback: function(e){
                // Replace selection with some drinks
                var selected = e.getSelection();
                if(selected.text != "" && $(".new-tags").children().length <= 4){
                  $(".new-tags").append("<span class=\"label label-success\" title=\"点击删除\">"+selected.text+"</span>");
                  $(".new-tags>span").click(function(){
                    $(this).remove();
                  })
                }
                e.setSelection(selected.end,selected.end);
              }
            }]
          }]
        ]

      });
      $("#post-article").hide().slideDown("slow");
      $("#post-new").click(function(){
        $("#post-new").prop('disabled', true);
        $("#post-new i").addClass("fa-refresh fa-spin");
        $("#post-new span").text("正在发表");
        var title = $.trim($('#post-title').val());
        var content = $('#post-content').data('markdown').getContent();
        if($('#modifying').length)
          postArticle(title, content, true);
        else
          postArticle(title, content, false);
        console.log($("#post-content").data("markdown").parseContent());
      });
      $("#cancel-post").click(function(){
        $("#post-article").slideUp("slow");
        if($('#modifying').length){
          removeElement($("#post-article"), 650);
        }
      });
    });
    </c:if>
    <c:if test="${empty username}">
    $('#loginBox').on('shown.bs.modal', function () {
      $('#username').focus();
    })
    $("#doLogin").click(function(){
      var user = $("#username").val();
      var pass = $("#password").val();
      if(user == null || user == ""){
        $("#username").focus();
        return;
      }else if(pass == ""){
        $("#password").focus();
        return;
      }
      pass = hex_sha1(pass);
      console.log(pass);
      $("#doLogin").html('<span class="fa fa-refresh fa-spin"></span>正在登录');
      $("#doLogin").removeClass('btn-danger').addClass("btn-success");
      $("#doLogin").prop("disabled", true);

      $.post('/auth?action=in',
              {'username' : user, 'password' : pass},
              function(data){
                console.log(data);
                if(data.status == "success"){
                  $("#doLogin").html('<span class="fa fa-check"></span>登录成功');
                  setTimeout(function(){
                    location.reload();
                  }, 1000);
                }else{
                  $("#doLogin").html('<span class="fa fa-close"></span>登录失败');
                  $("#doLogin").removeClass('btn-success').addClass("btn-danger");
                  setTimeout(function(){
                    $("#doLogin").html('登录');
                    $("#doLogin").prop("disabled", false);
                  }, 1000);
                }
              },'json'
      );
    })
    </c:if>

    //reply current
    <c:choose>
    <c:when test="${currentPage eq 'article'}">
    markdownEditor($("#comment-post-content"));
    $("#comment-post-content").parents(".post-info").hide().slideDown("slow");
    $(".cancel-comment").click(function(){
      $("#comment-post-content").parents(".post-info").slideUp("slow");
    });
    $(".newReply").click(function(){
      $("#comment-post-content").parents(".post-info").slideDown("slow");
      $("#comment-post-content").focus();
    });
    </c:when>
    <c:otherwise>
    $(".newReply").click(function(){
      if($('#comment-post-content').length){
        var index = Math.floor((Math.random()*100000)+1)
        $("#comment-post-content").attr("id", "comment-post-content-tmp" + index);
        $("#saveCmtBtn").attr("id", "saveCmtBtn-tmp" + index);
        $("#nickname").attr("id", "nickname-tmp" + index);
        $("#emal").attr("id", "emal-tmp" + index);
        removeElement($("#comment-post-content-tmp" + index).parents(".post-info").slideUp("slow"), 650);
      }
      var editor ="<div class=\"panel-footer post-info clearfix\">"+
              "    <div class=\"row\">"+
              "        <div class=\"col-sm-5\">"+
              "            <div class=\"input-group input-group-sm margin-bottom-sm\">"+
              "                <span class=\"input-group-addon\"><i class=\"fa fa-user fa-fw\"></i></span>"+
              <c:choose>
              <c:when test="${empty username}">
              "                <input id=\"nickname\" class=\"form-control\" type=\"text\" placeholder=\"昵称\">"+
              "            </div>"+
              "            <div class=\"input-group input-group-sm margin-bottom-sm\">"+
              "                <span class=\"input-group-addon\"><i class=\"fa fa-envelope-o fa-fw\"></i></span>"+
              "                <input id=\"email\" class=\"form-control\" type=\"email\" placeholder=\"邮箱\">"+
              </c:when>
              <c:otherwise>
              "                <input id=\"nickname\" class=\"form-control\" type=\"text\" placeholder=\"昵称\" " +
              "       value=\"${empty user.realname?"佚名":user.realname}\" disabled>"+
              "            </div>"+
              "            <div class=\"input-group input-group-sm margin-bottom-sm\">"+
              "                <span class=\"input-group-addon\"><i class=\"fa fa-envelope-o fa-fw\"></i></span>"+
              "                <input id=\"email\" class=\"form-control\" value=\"${empty user.email?"保密":user.email}\" disabled type=\"email\" placeholder=\"邮箱\">"+
              </c:otherwise>
              </c:choose>
              "            </div>"+
              "        </div>"+
              "        <div class=\"col-sm-7 hidden-xs\">"+
              "            1. 未登录用户请填写左边编辑框的内容后再评论。<br>"+
              "            2. 请勿谈论违反所有国家法律相关规定的内容。<br>"+
              "            3. 请不要发表政治、宗教和反人类等言论。<br>"+
              "            4. 我们不接受垃圾、广告评论，请确保你的评论是有价值的"+
              "        </div>"+
              "    </div>"+
              "    <form>"+
              "        <textarea id=\"comment-post-content\" required name=\"content\" style=\"height: 60px;\" data-provide=\"markdown\" rows=\"10\"></textarea>"+
              "    </form>"+
              "</div>";

      $(this).parents("article").append(editor);
      markdownEditor($("#comment-post-content"));
      //.markdown();
      $("#comment-post-content").parents(".post-info").hide().slideDown("slow");
      $(".cancel-comment").click(function(){
        removeElement($("#comment-post-content").parents(".post-info").slideUp("slow"), 650);
      });
    });
    </c:otherwise>
    </c:choose>



  });

</script>

</body>
</html>
