$(document).ready(function () {
    //下拉框初始化
    $('.ui.dropdown').dropdown();
    //复选框初始化
    $('.ui.checkbox').checkbox();

    if(sessionStorage.getItem("remindTime") == null){
    	sessionStorage.setItem("remindTime", 0);
    }
    
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
                if($('#user-type').text() == 'shop-admin'){//只有是前台,才会调用预约提醒	
                	var remindTime = Number(sessionStorage.getItem("remindTime"));
                	if(remindTime == 0){
                		remindAppointment();
                	}
                	setInterval(updateRemindTime,30000);
                }
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

function updateRemindTime(){
	var remindTime = Number(sessionStorage.getItem("remindTime"));
	if(remindTime < 900000){
		remindTime += 30000;
		sessionStorage.setItem("remindTime",remindTime);
	}else{//达到预约提醒时间,调用提醒方法
		sessionStorage.setItem("remindTime",30000);
		remindAppointment();
	}
}

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
            	$('.remind-appointment-modal').modal('hide');
            	if(response.data.length > 0){
            		$('#remind-appointment-list').children().remove();
            		$.each(response.data, function (i, data) {
                        var $tr = $('<tr></tr>');
                        var $memberName = $('<td class="memberName">' + data.memberName + '</td>');
                        var $memberPhone = $('<td class="memberPhone">' + data.phone + '</td>');
                        var $topTypeName = $('<td class="topTypeName" style="display:none">' + data.topTypeName + '</td>');
                        var $typeName = $('<td class="typeName" style="display:none">' + data.typeName + '</td>');
                        var $projectName = $('<td class="projectName">' + data.projectName + '</td>');
                        var $employeeName = $('<td class="employeeName">' + data.employeeName + '</td>');
                        var $beginTime = $('<td class="beginTime">' + toDatetimeMin(data.beginTime) + '</td>');
                        var $endTime = $('<td class="endTime">' + toDatetimeMin(data.endTime) + '</td>');
                        var $line = $('<td class="line">' + data.line + '</td>');
                        var $status = $('<td class="status">' + data.status + '</td>');
                        var remark = (data.remark == null || data.remark == '') ? '无' : String(data.remark);
                        var afterRemark = '';
                        if (remark == '无') {
                          afterRemark = '无'
                        } else if (remark.length > 6) {
                          afterRemark = remark.substring(0, 6) + '...';
                        } else {
                          afterRemark = remark;
                        }
                        var $remark = $('<td class="remark" title="' + remark + '">' + afterRemark + '</td>');

                        $tr.append($memberName);
                        $tr.append($memberPhone);
                        $tr.append($topTypeName);
                        $tr.append($typeName);
                        $tr.append($projectName);
                        $tr.append($employeeName);
                        $tr.append($beginTime);
                        $tr.append($endTime);
                        $tr.append($line);
                        $tr.append($status);
                        $tr.append($remark);
                        $('#remind-appointment-list').append($tr);
                    })
            		$('.remind-appointment-modal').modal({
     	               closable: false,
     	               onDeny: function () {
     	                 
     	               },
     	               onApprove: function () {
     	               }
     	             })
     	             .modal('show');
            	}
            }
        }
    })
}