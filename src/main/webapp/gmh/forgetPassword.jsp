<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <!-- Standard Meta -->
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <!-- Site Properties -->
    <title>光美焕-忘记密码</title>
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
    <script src="js/echarts.js"></script>
    <script src="js/script.js"></script>
</head>

<body>
    <!-- <button class="ui blue button fake-button" style="display:none;"></button> -->
    <div class="ui top fixed menu">
        <div class="item home-item">
            <img src="css/images/logo.png">光美焕科技皮肤护理
        </div>
    </div>
    <div class="ui center aligned grid forget-pwd-grid">
        <div class="four wide column">
            <h2 class="ui image header">
                <div class="content">忘记密码</div>
            </h2>
            <form id="forget-pwd-email" class="ui large form">
                <div class="ui">
                    <div class="field">
                        <div class="ui action input">
                            <input type="text" placeholder="请输入邮箱" name="email" id="email">
                            <button class="ui button" id="sendAuthCode">获取验证码</button>
                        </div>
                    </div>
                </div>
            </form>
            <form id="forget-pwd-auth" class="ui large form">
                <div class="ui">
                    <div class="field">
                        <div class="ui left input">
                            <input type="text" name="authCode" placeholder="请输入验证码">
                        </div>
                    </div>
                    <div class="ui fluid large submit button light-blue-btn">重置密码</div>
                </div>

                <div class="ui error message">
                </div>

            </form>
        </div>
    </div>
    <script>
        $('#forget-pwd-email').form({
            fields: {
                email: {
                    identifier: 'email',
                    rules: [{
                        type: 'empty',
                        prompt: '邮箱不能为空'
                    }]
                }
            },
            onSuccess: function (e) {
                //阻止表单的提交
                e.preventDefault();
            }
        }).api({
            action: 'staff getAuthCode',
            method: 'GET',
            serializeForm: true,
            beforeXHR: function (xhr) {
                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            },
            onSuccess: function (response) {
                if (response.error != null) {
                    $('#forget-pwd-auth').form('add errors', [response.error]);
                } else {
                    alert('验证码已发送到您的邮箱，请及时查收');
                }
            },
            onFailure: function (response) {
            	$('#forget-pwd-auth').form('add errors', ['服务器开小差了']);
            }
        });
        //忘记密码
        $('#forget-pwd-auth').form({
            fields: {
                authCode: {
                    identifier: 'authCode',
                    rules: [{
                        type: 'empty',
                        prompt: '验证码不能为空'
                    }]
                }
            },
            onSuccess: function (e) {
                //阻止表单的提交
                e.preventDefault();
            }
        }).api({
            action: 'staff validateAuthCode',
            method: 'POST',
            serializeForm: true,
            beforeXHR: function (xhr) {
                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            },
            beforeSend:function(settings){
                if($('#email').val() == ''){
                    $('#forget-pwd-auth').form('add errors', '邮箱不能为空');
                }else{
                    settings.data.email = $('#email').val();
                }
            },
            onSuccess: function (response) {
                if (response.error != null) {
                    $('#forget-pwd').form('add errors', [response.code, response.error]);
                } else {
                    alert('您的密码已重置为初始密码，请登录后进行修改');
                    redirect('index.jsp');
                }
            },
            onFailure: function (response) {
            	$('#forget-pwd').form('add errors', ['服务器开小差了']);
            }
        });
    </script>
</body>

</html>