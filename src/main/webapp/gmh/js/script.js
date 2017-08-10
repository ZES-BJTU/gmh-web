$(document).ready(function () {
    //下拉框初始化
    $('.ui.dropdown').dropdown();
    //复选框初始化
    $('.ui.checkbox').checkbox();
    //菜单按钮点击事件
    $('#left-menu .item').on('click', function () {
        $(this).addClass('active').siblings().removeClass('active');
    })
    //判断是否登陆
    $('.fake-button').api({
        action: 'staff info',
        method: 'GET',
        on: 'now',
        beforeXHR: function (xhr) {
            //判断token是否有效
            var token = getCookie('token');
            if (token == null || typeof (token) == undefined) {
                alert('请先登录！');
                redirect('index.html');
            }
            xhr.setRequestHeader('X-token', getCookie('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        onSuccess: function (response) {
            if (response.error != null) {
                alert(response.code + ' : ' + response.error);
            } else {
                $('#user-name').text(response.data.name);
                checkAuthority(response.data.staffLevel);
            }
        },
        onFailure: function (response) {
            $('#login').form('add errors', '服务器暂无响应');
        }
    })
    //登录
    $('#login').form({
        fields: {
            account: {
                identifier: 'account',
                rules: [{
                        type: 'empty',
                        prompt: '邮箱不能为空'
                    },
                    {
                        type: 'email',
                        prompt: '请输入有效的邮箱'
                    }
                ]
            },
            password: {
                identifier: 'password',
                rules: [{
                    type: 'empty',
                    prompt: '密码不能为空'
                }]
            }
        },
        onSuccess: function (e) {
            //阻止表单的提交
            e.preventDefault();
        }
    }).api({
        action: 'staff login',
        method: 'POST',
        serializeForm: true,
        onSuccess: function (response) {
            if (response.error != null) {
                $('#login').form('add errors', [response.code, response.error]);
            } else {
                setCookie('token', response.data.token);
                redirect('home.html');
            }
        },
        onFailure: function (response) {
            $('#login').form('add errors', '服务器暂无响应');
        }
    });
    //退出登录
    $('.logout').api({
        action: 'staff logout',
        method: 'GET',
        beforeXHR: function (xhr) {
            getCookie('token')
            xhr.setRequestHeader('X-token', getCookie('token'));
        },
        onSuccess: function (response) {
            if (response.error != null) {
                alert(response.code + ' : ' + response.error);
                if (response.code == 50001) {
                    delCookie('token');
                    redirect('index.html');
                }
            } else {
                delCookie('token');
                redirect('index.html');
            }
        }
    })
    $(document).on('click','.home-item',function(){
        redirect('home.html');
    })
    function checkAuthority(staffLevel){
        if(staffLevel == 1){
            console.log('前台');
        }else if(staffLevel == 2){
            console.log('美容师');
        }else if(staffLevel == 3){
            console.log('管理员');
        }
    }
})