<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <!-- Standard Meta -->
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <!-- Site Properties -->
    <title>光美焕-修改密码</title>
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
    <div class="ui center aligned grid modify-pwd-grid">
        <div class="four wide column">
            <h2 class="ui image header">
                <div class="content">修改密码</div>
            </h2>
            <form id="modify-pwd" class="ui large form">
                <div class="ui">
                    <div class="field">
                        <div class="ui left icon input">
                            <i class="lock icon"></i>
                            <input type="password" name="originalPassword" placeholder="请输入旧密码">
                        </div>
                    </div>
                    <div class="field">
                        <div class="ui left icon input">
                            <i class="lock icon"></i>
                            <input type="password" name="newPassword" placeholder="请输入新密码">
                        </div>
                    </div>
                    <div class="ui fluid large submit button light-blue-btn">确认修改</div>
                </div>

                <div class="ui error message">
                </div>

            </form>
        </div>
    </div>
    <script>
        //修改密码
        $('#modify-pwd').form({
            fields: {
                originalPassword: {
                    identifier: 'originalPassword',
                    rules: [{
                        type: 'empty',
                        prompt: '旧密码不能为空'
                    }]
                },
                newPassword: {
                    identifier: 'newPassword',
                    rules: [{
                        type: 'empty',
                        prompt: '新密码不能为空'
                    }]
                }
            },
            onSuccess: function (e) {
                //阻止表单的提交
                e.preventDefault();
            }
        }).api({
            action: 'staff changePassword',
            method: 'POST',
            serializeForm: true,
            beforeXHR: function (xhr) {
                verifyToken();
                xhr.setRequestHeader('X-token', getSessionStorage('token'));
                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            },
            onSuccess: function (response) {
                if (response.error != null) {
                    $('#modify-pwd').form('add errors', [response.error]);
                } else {
                    alert('修改成功');
                    sessionStorage.removeItem('token');
                    redirect('index.jsp');
                }
            },
            onFailure: function (response) {
            	$('#modify-pwd').form('add errors', ['服务器开小差了']);
            }
        });
    </script>
</body>

</html>