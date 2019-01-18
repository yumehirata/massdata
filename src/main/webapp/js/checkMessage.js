$(function(){
	
	$('#downloadButton').on('click',function(){
		if($('.notFoundMessage').text()!=null){
			alert('ダウンロードできるリストがありません');
			$("#downloadButton").attr("href", '/item/list')
		}
	});
	
});