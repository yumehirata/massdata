$(function(){
	
$('.toDetail').on('click',function(){
	$.cookie.json = true;
	
    // パス(/)や有効期限(1日)を指定する
    var COOKIE_PATH = '/';
    var date = new Date();
    date.setTime(date.getTime() + ( 1000 * 60 * 60 * 24 ));
 
    if($.cookie('url')){
    	$.removeCookie('url');
    }
        
    // URLを取得
    var file_url = location.href;
    alert(file_url);

    $.cookie('url',file_url, { path: COOKIE_PATH, expires: date });

});

$('.backList').on('click',function(){
	$.cookie.json = true;
	
    var backURL = $.cookie('url');
    alert(backURL);
 
    if(backURL!=null){
    	location.href = backURL;
    }else{
    	locaion.href = '${pageContext.request.contextPath}/item/list';
    }
        
    $.removeCookie('url');

});

});