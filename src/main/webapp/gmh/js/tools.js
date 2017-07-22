function redirect(url) {
    window.location.href = url;
}

function setCookie(name, value) {
    document.cookie = name + "=" + value + "; ";
}

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
        redirect('index.html');
    }
}