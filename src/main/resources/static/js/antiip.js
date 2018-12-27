$(function(){
	$('#addAntiIp').click(function(){
		var ip = $('#ip').text;
		$.ajax({
			type: "GET",
	        url: "/caipiao_quanzi_trunk/circle_addAntiIp.html",  
	        data: "ip="+ip,
	        beforeSend: function(){
	            $("#span_content").text("数据处理中...");
	        },
	        success: function(msg){
	            $("#span_content").text("两个数的和为： " + msg);
	        }
		});
	});
});