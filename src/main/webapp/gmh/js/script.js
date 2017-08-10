$(document).ready(function () {
    //下拉框初始化
    $('.ui.dropdown').dropdown();
    //复选框初始化
    $('.ui.checkbox').checkbox();

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
                redirect('index.jsp');
            }
            xhr.setRequestHeader('X-token', getCookie('token'));
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        },
        onSuccess: function (response) {
            if (response.error != null) {
                alert(response.code + ' : ' + response.error);
            } else {
                $('#user-name').text(response.data.name);
                hideMenu(response.data.staffLevel);
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
            	checkAuthority(response.data.staffLevel);
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
                    redirect('index.jsp');
                }
            } else {
                delCookie('token');
                redirect('index.jsp');
            }
        }
    })
})

function remindAppointment(){
	$('#user-type').api({
        action: 'appointment remind',
        method: 'GET',
        on:'now',
        beforeXHR: function (xhr) {
            getCookie('token')
            xhr.setRequestHeader('X-token', getCookie('token'));
        },
        onSuccess: function (response) {
            if (response.error != null) {
                alert(response.code + ' : ' + response.error);
            } else {
            	console.log(response);
                     $('.remind-appointment-modal').modal({
                        closable: false,
                        onDeny: function () {
                          
                        },
                        onApprove: function () {
//                          $('#new-employee').submit();
                          return false;
                        }
                      })
                      .modal('show');
            }
        }
    })
}