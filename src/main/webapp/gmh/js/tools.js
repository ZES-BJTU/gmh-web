function redirect(url) {
    window.location.href = url;
}

// function setCookie(name, value) {
//     document.cookie = name + "=" + value + "; ";
// }

//写cookies
function setCookie(name, value) {
    //cookie有效时间，30分钟
    var Days = 7;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    //15.10.11 取消cookie生效时间，ie不关闭的情况下一直有效，关闭浏览器立即失效
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    // document.cookie = name + "="+ escape (value);
    //console.log('set cookie='+name+',value='+escape (value));
}

//读取cookies
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg)) {
        //console.log('get cookie='+unescape(arr[2]));
        return unescape(arr[2]);
    } else {
        return null;
    }
}

//删除cookies
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

//判断token是否有效
function verifyToken() {
    if (getCookie('token') == null) {
        alert('请先登录！');
        redirect('index.jsp');
    }
}

function add0(m) {
    return m < 10 ? '0' + m : m
}

function toDatetimeDay(timestamp) {
    //timestamp是整数，否则要parseInt转换  
    var time = new Date(parseInt(timestamp));
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y + '-' + add0(m) + '-' + add0(d);
};
function toDatetimeMin(timestamp) {
    //timestamp是整数，否则要parseInt转换  
    var time = new Date(parseInt(timestamp));
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm);
};

function toTimeStamp(datetime) {
    datetime = new Date(Date.parse(datetime.replace(/-/g, "/")));
    datetime = datetime.getTime();
    return datetime;
}

//激活菜单
function activeMenu(menu){
	$("#left-menu ." + menu).addClass("active").siblings().removeClass("active");
}

//检查权限
function checkAuthority(staffLevel){
    if(staffLevel == 1){//前台
    	redirect('home.jsp');
    }else if(staffLevel == 2){//美容师
    	redirect('checkReserve.jsp');
    }else if(staffLevel == 3){//管理员
    	redirect('staff.jsp');
    }
}

//根据账号类型隐藏菜单
function hideMenu(staffLevel){
    if(staffLevel == 1){//前台
    	$('#left-menu .shop-admin').css('display','block');
    	$('#user-type').text('shop-admin');
//    	remindAppointment();
    }else if(staffLevel == 2){//美容师
    	
    }else if(staffLevel == 3){//管理员
    	$('#left-menu .admin').css('display','block');
    	$('#user-type').text('admin');
    }
}