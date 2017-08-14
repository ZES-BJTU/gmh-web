<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properties -->
  <title>光美焕-登录</title>
  <link rel="shortcut icon" type="image/x-icon" href="css/images/favicon.ico">

  <!--JQuery-->
  <script src="js/jquery.min.js"></script>
  <script src="js/jquery.serialize-object.min.js"></script>

  <!--Semantic-UI-->
  <link rel="stylesheet" type="text/css" href="css/semantic-ui/semantic.css">
  <script src="css/semantic-ui/semantic.js"></script>

  <!--Style-->
  <link rel="stylesheet" href="css/style.css">

  <!--Script-->
  <script src="js/action.js"></script>
  <script src="js/tools.js"></script>
  <script src="js/script.js"></script>
</head>

<body>
  <div class="ui middle aligned center aligned righted grid login-grid">
    <div class="column login-column">
      <h2 class="ui image header">
        <img src="css/images/logo-white-2.png" class="image">
        <div class="content">
          <p>光美焕-管理系统</p>
          <p class="second">Management System</p>
        </div>
      </h2>
      <form id="login" class="ui large form">
        <div class="ui">
          <div class="field">
            <div class="ui left icon input">
              <i class="user icon"></i>
              <input type="text" name="account" placeholder="请输入邮箱">
            </div>
          </div>
          <div class="field">
            <div class="ui left icon input">
              <i class="lock icon"></i>
              <input type="password" name="password" placeholder="请输入密码">
            </div>
          </div>
          <div class="ui fluid large submit button">登录</div>
          <div class="login-link">
          	忘记密码了？点此
            <a href="forgetPassword.jsp" class="forget-pwd">重置密码</a>
          </div>
        </div>

        <div class="ui error message">
        </div>

      </form>
    </div>
  </div>
</body>

</html>