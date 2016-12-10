
var layer_newrepose = null;
$(document).ready(function() {
	fgetInit();
	$("#doc-id-bar li").click(function(){
			$(this).siblings().removeClass('active');
			$(this).addClass('active');
	});
	$("#id-myrepose").unbind().bind('click',function(){
		fgetInit();
	});
	$("#newRepos").unbind().bind('click',function(){
		console.log("click");
		layer_newrepose = layer.open({
			  type: 1,
			  title:'新建资料库',
			  skin: 'layui-layer-rim', //加上边框
			  area: ['400px', '240px'], //宽高
			  content: 
				  '<div class="layui-form">'
				  +'<div class="layui-form-item" style="margin-top:30px;">'
				    +'<label class="layui-form-label">资料库：</label>'
				   + '<div class="layui-input-inline">'
				     +'<input type="text" name="name"  autocomplete="off" class="layui-input " id="id-newrepose"/>'
				    +'</div>'
				  +'</div>'
				  +'<div class="layui-form-item">'
				    +'<div class="layui-input-block">'
				      +'<button class="layui-btn" onclick="fnewRepose()">确认创建</button>'
				      +'<button type="reset" class="layui-btn layui-btn-primary">重置</button>'
				    +'</div>'
				 + '</div>'
				  +'</div>'
			});
		fgetInit();
	});
});
//初始化仓库
function fgetInit(){
	$.ajax({
		url:'/wesign/repos',
		async:true,
		type:'GET',
		contentType:'application/x-www-form-urlencoded',
		success:function(data){
			console.log(data);
			var status = data.status;
			var Data = data.data;
			var html = "";
			if(status == 0){
				for(var file in Data){
					html += '<tr class="tr-border">'
						  	+'<th class="th-1"><input type="checkbox"/></th>'
						    +'<th class="th-2"><i class="icon-ownsign-hollow all-icon"></i>' +Data[file].repoName+ '</th>'
						    +'<th class="th-3"><i id="'+Data[file].repoId+'" class="icon-bin all-icon" onclick="fdelete(this)"></i><i class="icon-write-down all-icon deal-method" onclick="fwrite(this)"></i></th>'
						    +'<th class="th-4">--</th>'
						    +'<th class="th-5">'+ moment(Data[file].modifyTime).format("YYYY-MM-DD HH:mm:ss") +'</th>'
						    +'</tr>'
				}
				$("#repo-table").html(html);
			}
		}
	});
}
//检测仓库是否存在
function fcheckExitRepose(){
	var name = $("#id-newrepose").val();
	console.log(name);
	$.ajax({
		url:'/wesign/repos/check?repoName='+name,
		async:true,
		type:'POST',
		dataType:'json',
		contentType:'application/json',
		success:function(data,e){
			console.log(data);
			var status = data.status;
			var Data = data.desc;
			var result = data.data;
			var html = "";
			if(status == 0 && result == true){	
				return true;
			}else{
				return false;
			}
		}
	});
}
//新建仓库
function fnewRepose(){
	var name = $("#id-newrepose").val();
	console.log(name);
	if(fcheckExitRepose()){
		layer.alert('该仓库已经存在', {
			  icon: 1,
			  time: 1000, //2秒关闭（如果不配置，默认是3秒）
			  anim:1
			});
	}else{
		$.ajax({
			url:'/wesign/repos',
			async:true,
			type:'POST',
			dataType:'json',
			contentType:'application/json',
			data: JSON.stringify({"repoName": name}),
			success:function(data,e){
				console.log(data);
				var status = data.status;
				var Data = data.desc;
				var html = "";
				if(status == 0){	
					layer.close(layer_newrepose,{
						anim:6
					});
					layer.msg(Data, {
						  icon: 1,
						  time: 1000, //2秒关闭（如果不配置，默认是3秒）
						  anim:1
						});
					fgetInit();
				}
			}
		});
	}
}
//删除仓库
function fdelete(_this){
	console.log(_this);
	//var id = _this.attr('id');
	console.log(id);
}