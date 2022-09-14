$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	//发送异步请求
	//获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	//发送异步请求
	$.post(
	//访问路径
	"/community/post/addPost",
	//传入的数据
		{"title":title,
		"content":content},
	//回调函数
		function (data) {
			data = $.parseJSON(data);
			//在提示框当中显示返回消息
			$("#hintBody").text(data.msg);
			//显示提示框 两秒后自动隐藏
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				//刷新页面
				if(data.code==0){
					window.location.reload();
				}
			}, 2000);
		}

	);


}