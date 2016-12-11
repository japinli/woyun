function fgetinRepose(_this){
	var id = $(_this).attr('id');
	var name = $(_this).attr('title');
	$.ajax({
		url:'/wesign/repos/'+id+'/dir?path=',
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
			if(status == 0 && result){
				result.forEach(function(e){
					if(e.type == "file"){
						html += '<tr class="tr-border">'
						  	+'<th class="th-1"><input type="checkbox"/></th>'
						    +'<th class="th-2"><span title="'+e.filename+'">' +e.filename+ '<span></th>'
						    +'<th class="th-3"><i  title="'+e.filename+'" class="icon-bin all-icon" onclick="fwriteNextDelete(this)"></i>'
						    +'<i  title="'+e.filename+'" class="icon-write-down all-icon deal-method" onclick="fwriteNext(this)"></i>'
						    +'<i class="icon-download3 all-icon" onclick="fwriteNextDown(this)"></i>'
						    +'<i class="icon-copy all-icon" onclick="fcopyNext(this)"></i>'
						    +'<i class="icon-remove all-icon" onclick="fmoveNext(this)"></i>'
						    +'<i class="icon-history all-icon" onclick="fhistoryNext(this)"></i></th>'
						    +'<th class="th-4">'+uploadFileSizeConvertTip(e.size)+'</th>'
						    +'<th class="th-5">'+ moment(e.mtime).format("YYYY-MM-DD HH:mm:ss") +'</th>'
						    +'</tr>'
					}else{
						html += '<tr class="tr-border">'
						  	+'<th class="th-1"><input type="checkbox"/></th>'
						    +'<th class="th-2"><span style="color:#00868B;" title="'+e.filename+'" onclick="fshowDir(this)">' +e.filename+ '<span></th>'
						    +'<th class="th-3"><i  title="'+e.filename+'" class="icon-bin all-icon" onclick="fwriteNextDelete(this)"></i>'
						    +'<i  title="'+e.filename+'" class="icon-write-down all-icon deal-method" onclick="fwriteNext(this)"></i>'
						    +'<i class="icon-download3 all-icon" onclick="fwriteNextDown(this)"></i>'
						    +'<i class="icon-copy all-icon" onclick="fcopyNext(this)"></i>'
						    +'<i class="icon-remove all-icon" onclick="fmoveNext(this)"></i>'
						    +'<i class="icon-history all-icon" onclick="fhistoryNext(this)"></i></th>'
						    +'<th class="th-4">'+uploadFileSizeConvertTip(e.size)+'</th>'
						    +'<th class="th-5">'+ moment(e.mtime).format("YYYY-MM-DD HH:mm:ss") +'</th>'
						    +'</tr>'
					}
				});
				$("#repo-table").html(html);
				var file_html = '<a href="javascript:;" id="'+id+'" style="text-decoration:underline;" title="'+name+'" onclick="fgetinRepose(this)">'+name+'</a>';
				$("#newRepos").addClass('hidden');
				$("#id-globle").html(file_html);
			}else{
				$("#repo-table").html("");
				layer.msg('还没有内容哦！', {
					  icon: 0,
					  time: 2000, //2秒关闭（如果不配置，默认是3秒）
					  anim:5
					});
				var file_html = '<a href="javascript:;" id="'+id+'" style="text-decoration:underline;" title="'+name+'" onclick="fgetinRepose(this)">'+name+'</a>';
				$("#newRepos").addClass('hidden');
				$("#id-globle").html(file_html);
			}
		}
	});
	$(".lookFile").removeClass("hidden");
	$("#id-repose-content").css('top','120px');
}
function fshowDir(_this){
	var name = $(_this).attr('title');
	var path = $("#id-globle").children('a:gt(0)').text()+'/'+name;
	var html = '<a path="'+path+'" href="javascript:;" onclick="fshowLoadDir(this)"><span style="padding:5px;">/</span>'+name+'</a>';
	$("#id-globle").children('a:last').after(html);
	var id= $("#id-globle a:first").attr('id');
	console.log('/wesign/repos/'+id+'/dir?path='+path);
	$.ajax({
		url:'/wesign/repos/'+id+'/dir?path='+path,
		async:true,
		type:'GET',
		contentType:'application/json',
		success:function(data){
			console.log(data);
			var status = data.status;
			var Data = data.data;
			var html = "";
			if(status == 0 && Data){
				console.log(data);
				Data.forEach(function(e){
					if(e.type == "file"){
						html += '<tr class="tr-border">'
							+'<th class="th-1"><input type="checkbox"/></th>'
						    +'<th class="th-2"><span title="'+e.filename+'">' +e.filename+ '<span></th>'
						    +'<th class="th-3"><i path="'+path+'"  title="'+e.filename+'" class="icon-bin all-icon" onclick="fwriteNextDelete(this)"></i>'
						    +'<i  title="'+e.filename+'" class="icon-write-down all-icon deal-method" onclick="fwriteNext(this)"></i>'
						    +'<i class="icon-download3 all-icon" onclick="fwriteNextDown(this)"></i>'
						    +'<i class="icon-copy all-icon" onclick="fcopyNext(this)"></i>'
						    +'<i class="icon-remove all-icon" onclick="fmoveNext(this)"></i>'
						    +'<i class="icon-history all-icon" onclick="fhistoryNext(this)"></i></th>'
						    +'<th class="th-4">'+uploadFileSizeConvertTip(e.size)+'</th>'
						    +'<th class="th-5">'+ moment(e.mtime).format("YYYY-MM-DD HH:mm:ss") +'</th>'
						    +'</tr>'
					}else{
						html += '<tr class="tr-border">'
							  	+'<th class="th-1"><input type="checkbox"/></th>'
							    +'<th class="th-2"><span style="color:#00868B;" title="'+e.filename+'" onclick="fshowDir(this)">' +e.filename+ '<span></th>'
							    +'<th class="th-3"><i  title="'+e.filename+'" class="icon-bin all-icon" onclick="fwriteNextDelete(this)"></i>'
							    +'<i  title="'+e.filename+'" class="icon-write-down all-icon deal-method" onclick="fwriteNext(this)"></i>'
							    +'<i class="icon-download3 all-icon" onclick="fwriteNextDown(this)"></i>'
							    +'<i class="icon-copy all-icon" onclick="fcopyNext(this)"></i>'
							    +'<i class="icon-remove all-icon" onclick="fmoveNext(this)"></i>'
							    +'<i class="icon-history all-icon" onclick="fhistoryNext(this)"></i></th>'
							    +'<th class="th-4">'+uploadFileSizeConvertTip(e.size)+'</th>'
							    +'<th class="th-5">'+ moment(e.mtime).format("YYYY-MM-DD HH:mm:ss") +'</th>'
							    +'</tr>'
					}
				});
				$("#repo-table").html(html);
			}else{
				$("#repo-table").html("");
				layer.msg('还没有内容哦！', {
					  icon: 0,
					  time: 2000, //2秒关闭（如果不配置，默认是3秒）
					  anim:5
					});
			}
		},
		error:function(data){
			alert(data);
		}
	});
	$(".lookFile").removeClass("hidden");
}
//点击获取资料库名再次获取目录
function fshowLoadDir(_this){
	var slib = $(_this).nextAll().remove();
	console.log(slib);
	var id= $("#id-globle a:first").attr('id');
	var path = $(_this).attr('path');
	$.ajax({
		url:'/wesign/repos/'+id+'/dir?path='+path,
		async:true,
		type:'GET',
		contentType:'application/json',
		success:function(data){
			console.log(data);
			var status = data.status;
			var Data = data.data;
			var html = "";
			if(status == 0 && Data){
				console.log(data);
				Data.forEach(function(e){
					if(e.type == "file"){
						html += '<tr class="tr-border">'
							+'<th class="th-1"><input type="checkbox"/></th>'
						    +'<th class="th-2"><span title="'+e.filename+'">' +e.filename+ '<span></th>'
						    +'<th class="th-3"><i  path="'+path+'" title="'+e.filename+'" class="icon-bin all-icon" onclick="fwriteNextDelete(this)"></i>'
						    +'<i  title="'+e.filename+'" class="icon-write-down all-icon deal-method" onclick="fwriteNext(this)"></i>'
						    +'<i class="icon-download3 all-icon" onclick="fwriteNextDown(this)"></i>'
						    +'<i class="icon-copy all-icon" onclick="fcopyNext(this)"></i>'
						    +'<i class="icon-remove all-icon" onclick="fmoveNext(this)"></i>'
						    +'<i class="icon-history all-icon" onclick="fhistoryNext(this)"></i></th>'
						    +'<th class="th-4">'+uploadFileSizeConvertTip(e.size)+'</th>'
						    +'<th class="th-5">'+ moment(e.mtime).format("YYYY-MM-DD HH:mm:ss") +'</th>'
						    +'</tr>'
					}else{
						html += '<tr class="tr-border">'
							html += '<tr class="tr-border">'
							  	+'<th class="th-1"><input type="checkbox"/></th>'
							    +'<th class="th-2"><span style="color:#00868B;" title="'+e.filename+'" onclick="fshowDir(this)">' +e.filename+ '<span></th>'
							    +'<th class="th-3"><i  title="'+e.filename+'" class="icon-bin all-icon" onclick="fwriteNextDelete(this)"></i>'
							    +'<i  title="'+e.filename+'" class="icon-write-down all-icon deal-method" onclick="fwriteNext(this)"></i>'
							    +'<i class="icon-download3 all-icon" onclick="fwriteNextDown(this)"></i>'
							    +'<i class="icon-copy all-icon" onclick="fcopyNext(this)"></i>'
							    +'<i class="icon-remove all-icon" onclick="fmoveNext(this)"></i>'
							    +'<i class="icon-history all-icon" onclick="fhistoryNext(this)"></i></th>'
							    +'<th class="th-4">'+uploadFileSizeConvertTip(e.size)+'</th>'
							    +'<th class="th-5">'+ moment(e.mtime).format("YYYY-MM-DD HH:mm:ss") +'</th>'
							    +'</tr>'
					}
				});
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

















