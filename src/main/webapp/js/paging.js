$(function(){
	
    var file_url = location.href;
    var noPagingUrl = file_url.substring(file_url.lastIndexOf("/")+1,file_url.length);
    var pagingUrl = noPagingUrl.substring(noPagingUrl.indexOf("/")+1,noPagingUrl.indexOf("?"));
    
    if(noPagingUrl=='list' || pagingUrl=='list'){
    	$('.fromSearch').css('display','none');
    }else if(noPagingUrl=='search' || pagingUrl=='search'){
    	$('.fromList').css('display','none');    	
    }
	
});