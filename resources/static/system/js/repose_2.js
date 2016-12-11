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
			if(status == 0){
				result.forEach(function(e){
					if(e.type == "file"){
						html += '<tr class="tr-border">'
						  	+'<th class="th-1"><input type="checkbox"/></th>'
						    +'<th class="th-2"><span title="'+e.filename+'">' +e.filename+ '<span></th>'
						    +'<th class="th-3"><i  title="'+e.filename+'" class="icon-bin all-icon" onclick="fwriteNextDelete(this)"></i><i  title="'+e.filename+'" class="icon-write-down all-icon deal-method" onclick="fwriteNext(this)"></i><i class="icon-download3 all-icon" onclick="fwriteNextDown(this)"></i></th>'
						    +'<th class="th-4">'+uploadFileSizeConvertTip(e.size)+'</th>'
						    +'<th class="th-5">'+ moment(e.mtime).format("YYYY-MM-DD HH:mm:ss") +'</th>'
						    +'</tr>'
					}else{
						html += '<tr class="tr-border">'
						  	+'<th class="th-1"><input type="checkbox"/></th>'
						    +'<th class="th-2"><span style="color:#00868B;" title="'+e.filename+'" onclick="fshowDir(this)">' +e.filename+ '<span></th>'
						    +'<th class="th-3"><i  title="'+e.filename+'" class="icon-bin all-icon" onclick="fwriteNextDelete(this)"></i><i  title="'+e.filename+'" class="icon-write-down all-icon deal-method" onclick="fwriteNext(this)"></i><i class="icon-download3 all-icon" onclick="fwriteNextDown(this)"></i></th>'
						    +'<th class="th-4">'+uploadFileSizeConvertTip(e.size)+'</th>'
						    +'<th class="th-5">'+ moment(e.mtime).format("YYYY-MM-DD HH:mm:ss") +'</th>'
						    +'</tr>'
					}
				});
				$("#repo-table").html(html);
				var file_html = '<a href="javascript:;" id="'+id+'" style="text-decoration:underline;" title="'+name+'">'+name+'</a>';
				$("#newRepos").addClass('hidden');
				$("#id-globle").html(file_html);
			}
		}
	});
}
function fshowDir(_this){
	var name = $(_this).attr('title');
	var path = $("#id-globle").children('a:gt(0)').text()+'/'+name;
	var html = '<a path="'+path+'" href="javascript:;"><span style="padding:5px;">/</span>'+name+'</a>';
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
			if(status == 0){
				console.log(data);
			}
		}
	});
	//var path = $("#id-globle a:last").append();
	//console.log(name+id);
}


















