var noValueMiddle = $('#middleCategory').html();
var noValueSmall = $('#smallCategory').html();

$('#largeCategory').on('change',function(){

	if($('#largeCategory').val()!=""){
		$('#middleCategory').empty();
		$('#middleCategory').html(noValueMiddle);
		
		var data = {
			id : $('#largeCategory').val()
		};
	
		$.ajax({               
			url:'/category/forSelect',    
			type:'POST', 
			async   : true,
			data:JSON.stringify(data), 
			contentType: 'application/json', 
			dataType: 'json',
        
			success: function(categories){
				var option;
				var category;
				for(var i = 0; i<=categories.length;i++){
					category = categories[i];
					if(category){
						option = '<option value="' + category['id'] + '">' + category['name'] + '</option>';
						$('#middleCategory').append(option);
					}
				}
			}
		});
	
	}else{
		$('#middleCategory').html(noValueMiddle);
	}
});


$('#middleCategory').on('change',function(){

	if($('#middleCategory').val()!=""){
		$('#smallCategory').empty();
		$('#smallCategory').html(noValueSmall);
		
		var data = {
			id : $('#middleCategory').val()
		};
	
		$.ajax({               
			url:'/category/forSelect',    
			type:'POST', 
			async   : true,
			data:JSON.stringify(data), 
			contentType: 'application/json', 
			dataType: 'json',
        
			success: function(categories){
				var option;
				var category;
				for(var i = 0; i<=categories.length;i++){
					category = categories[i];
					if(category){
						option = '<option value="' + category['id'] + '">' + category['name'] + '</option>';
						$('#smallCategory').append(option);
					}
				}
			}
		});
	
	}else{
		$('#smallCategory').html(noValueSmall);
	}
});