var fwriteID = null;
var layer_newrepose = null;
$(document).ready(function() {
	fgetInit();
	$("#doc-id-bar li").click(function(){
			$(this).siblings().removeClass('active');
			$(this).addClass('active');
			var text = $(this).find('span').text();
			$("#id-globle").text(text);
			//$("#id-globle").text() = $(this).find('span').text();
	});
	$("#id-myrepose").unbind().bind('click',function(){
		fgetInit();
		$("#newRepos").removeClass('hidden');
		$(".lookFile").addClass("hidden");
		$("#id-repose-content").css('top','50px');
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
				      +'<button class="layui-btn" onclick="fnewcheckRepose()">确认创建</button>'
				      +'<button type="reset" class="layui-btn layui-btn-primary" onclick="fnewresetrepose()">重置</button>'
				    +'</div>'
				 + '</div>'
				  +'</div>'
			});
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
			if(status == 0 && Data){
				for(var file in Data){
					html += '<tr class="tr-border">'
						  	+'<th class="th-1"><input type="checkbox"/></th>'
						    +'<th class="th-2"><i class="icon-ownsign-hollow all-icon"></i><span id="'+Data[file].repoId+'" title="'+Data[file].repoName+'" onclick="fgetinRepose(this)">' +Data[file].repoName+ '<span></th>'
						    +'<th class="th-3"><i id="'+Data[file].repoId+'" title="'+Data[file].repoName+'" class="icon-bin all-icon" onclick="fdelete(this)"></i><i id="'+Data[file].repoId+'" title="'+Data[file].repoName+'" class="icon-write-down all-icon deal-method" onclick="fwrite(this)"></i></th>'
						    +'<th class="th-4">--</th>'
						    +'<th class="th-5">'+ moment(Data[file].modifyTime).format("YYYY-MM-DD HH:mm:ss") +'</th>'
						    +'</tr>'
				}
				$("#repo-table").html(html);
			}else{
				$("#repo-table").html("");
				layer.msg('还没有内容哦！', {
					  icon: 0,
					  time: 2000, //2秒关闭（如果不配置，默认是3秒）
					  anim:5
					});
			}
		}
	});
}
//检测仓库已存在并新建
function fnewcheckRepose(){
	var result = null;
	result = fcheckExitRepose();
	if(result == 1){
		layer.msg('该资料库已经存在', {
			  icon: 2,
			  time: 2000, //2秒关闭（如果不配置，默认是3秒）
			  anim:5
			});
	}else if(result == 2){
		layer.msg('该资料库存在回收站', {
			  icon: 2,
			  time: 2000, //2秒关闭（如果不配置，默认是3秒）
			  anim:5
			});
	}else{
		fnewRepose();
	}
}
//检测仓库已存在并重命名frenameRepose
function frenamecheckRepose(){
	var result = null;
	result = fnewRenameRepose();
	if(result == 1){
		layer.msg('该资料库已经存在', {
			  icon: 2,
			  time: 2000, //2秒关闭（如果不配置，默认是3秒）
			  anim:5
			});
	}else if(result == 2){
		layer.msg('该资料库存在回收站', {
			  icon: 2,
			  time: 2000, //2秒关闭（如果不配置，默认是3秒）
			  anim:5
			});
	}else{
		frenameRepose();
	}
}
//资料库检测
//检测仓库是否存在
function fcheckExitRepose(){
	var name = $("#id-newrepose").val();
	var statu = false;
	console.log(name);
	$.ajax({
		url:'/wesign/repos/check?repoName='+name,
		async:false,
		type:'GET',
		dataType:'json',
		contentType:'application/json',
		success:function(data){
			console.log(data);
			var status = data.status;
			var Data = data.desc;
			var result = data.data;
			var html = "";
			if(status == 0 && result == -1){	
				statu =  0;
			}else if(result == 0){
				statu =  1;
			}else{
				statu =  2;
			}
		},
		error:function(data){
			console.log('ok'+data.status);
		}
	});
	return statu;
}
//新建仓库
function fnewRepose(){
	var name = $("#id-newrepose").val();
	console.log(name);
	if(name ==null || name ==""){
		layer.msg('资料库名不能为空', {
			  icon: 2,
			  time: 2000, //2秒关闭（如果不配置，默认是3秒）
			  anim:5
			});
		return;
	}
		$.ajax({
			url:'/wesign/repos',
			async:false,
			type:'POST',
			dataType:'json',
			contentType:'application/json',
			data: JSON.stringify({"repoName": name}),
			success:function(data){
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
//重命名资料库
function fnewRenameRepose(){
	var name = $("#id-renamerepose").val();
	console.log(name);
	var statu = false;
	console.log(name);
	$.ajax({
		url:'/wesign/repos/check?repoName='+name,
		async:false,
		type:'GET',
		dataType:'json',
		contentType:'application/json',
		success:function(data){
			console.log(data);
			var status = data.status;
			var Data = data.desc;
			var result = data.data;
			var html = "";
			if(status == 0 && result == -1){	
				statu =  0;//不存在
			}else if(result == 0){
				statu =  1;//存在
			}else{
				statu =  2;//回收
			}
		},
		error:function(data){
			console.log('ok'+data.status);
		}
	});
	return statu;
}
//删除仓库
function fdelete(_this){
	console.log(_this);
	var id = $(_this).attr('id');
	var name = $(_this).attr('title');
	$.ajax({
		url:'/wesign/repos',
		async:false,
		type:'DELETE',
		dataType:'json',
		contentType:'application/json',
		data: JSON.stringify({"repoId":id,"repoName": name}),
		success:function(data){
			console.log(data);
			var status = data.status;
			var Data = data.desc;
			var html = "";
			if(status == 0){
				console.log(data);
				layer.msg(name+'：移至回收站成功', {
					  icon: 1,
					  time: 1000, //2秒关闭（如果不配置，默认是3秒）
					  anim:5
					});
				fgetInit();
			}
		}
	});
	$(".lookFile").addClass("hidden");
	$("#id-repose-content").css('top','50px');
}
//重命名仓库
function fwrite(_this){
	console.log(_this);
	var id = $(_this).attr('id');
	var name = $(_this).attr('title');
	fwriteID = id;
	layer_newrepose = layer.open({
		  type: 1,
		  title:'重命名: '+name,
		  skin: 'layui-layer-rim', //加上边框
		  area: ['400px', '240px'], //宽高
		  content: 
			  '<div class="layui-form">'
			  +'<div class="layui-form-item" style="margin-top:30px;">'
			    +'<label class="layui-form-label">资料库：</label>'
			   + '<div class="layui-input-inline">'
			     +'<input type="text" name="name"  autocomplete="off" class="layui-input " id="id-renamerepose"/>'
			    +'</div>'
			  +'</div>'
			  +'<div class="layui-form-item">'
			    +'<div class="layui-input-block">'
			      +'<button class="layui-btn" onclick="frenamecheckRepose()">确认重命名</button>'
			      +'<button type="reset" class="layui-btn layui-btn-primary" onclick="resetrenameRepose()">重置</button>'
			    +'</div>'
			 + '</div>'
			  +'</div>'
		});
}
//执行重命名
function frenameRepose(){
	console.log(fwriteID);
	var name = $("#id-renamerepose").val();
	if(name ==null || name ==""){
		layer.msg('资料名不能为空', {
			  icon: 2,
			  time: 2000, //2秒关闭（如果不配置，默认是3秒）
			  anim:5
			});
		return;
	}
	$.ajax({
		url:'/wesign/repos',
		async:false,
		type:'PUT',
		dataType:'json',
		contentType:'application/json',
		data: JSON.stringify({"repoId":fwriteID,"repoName": name}),
		success:function(data){
			console.log(data);
			var status = data.status;
			var Data = data.desc;
			var html = "";
			if(status == 0){
				console.log(data);
				layer.close(layer_newrepose,{
					anim:6
				});
				layer.msg('重命名成功', {
					  icon: 1,
					  time: 2000, //2秒关闭（如果不配置，默认是3秒）
					  anim:5
					});	
				fgetInit();
			}
		}
	});
	fwriteID = null;
}
