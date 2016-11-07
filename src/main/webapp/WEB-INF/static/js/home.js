$(document).ready(function() {
	listDirectory("japin");
})

function convertBytes(bytes) {
	if (bytes == 0) {
		return "0B";
	}
	
	var k = 1024;
	var units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
	var i = Math.floor(Math.log(bytes) / Math.log(k));
	
	return (bytes / Math.pow(k, i)).toPrecision(3) + units[i];
}

function listDirectory(dir) {
	$.ajax({
		url: "/dirs/list",
		type: "POST",
		dataType: "json",
		contentType: "application/json",
		data: JSON.stringify({"path": dir}),
		success: function(data) {
			showItems(data.data);
		},
		error: function() {
			alert("获取目录列表异常");
		}
	});
}

function showItems(data) {
	console.log(data);
	$("#list-container").html("");
	
	$.each(data, function(n, item) {
		name = item.path;
		if (item.dir) {
			var url = "japin/" + item.path;
			name = '<a href="javascript:listDirectory(\'' + url + '\')">' + item.path + '</a>';
		}
		
		size = '<span class="text">';
		if (item.dir) {
			size += '--';
		} else {
			size += convertBytes(item.length);
		}
		size += '</span>';
		
		var mtime = moment(item.mtime).format("YYYY-MM-DD HH:mm:ss");
		context = 
'			<ul id="file-list-container">\
				<li class="col file-name-item first-col">\
					<div class="col-item check">\
						<span class="check-icon"></span>\
						<span class="check-all-text">全选</span>\
						<span class="icon checksmall icon-checksmall"></span>\
					</div>\
					<span class="text">' + name + '</span>\
				</li>\
				<li class="col file-size-item">\
					<span class="text">'+ size + '</span>\
				</li>\
				<li class="col file-mtime-item last-col">\
					<span class="text">' + mtime + '</span>\
				</li>\
			</ul>\
';
		$("#list-container").append(context);
	});
}