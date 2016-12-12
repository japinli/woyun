/*
 * 字符串格式化
 * 使用:
 * 'Hello, {0}! My name is {1}'.format('Lily', 'Bob');
 */
if (!String.prototype.format) {
    String.prototype.format = function() {
        var args = arguments;
        return this.replace(/{(\d+)}/g, function(match, number) {
            return typeof args[number] != 'undefined' ? args[number] : match;
        });
    };
}

/*
 * 格式化时间
 */
function formatTime(tt) {
    return moment(tt).format("YYYY-MM-DD HH:mm:ss");
}

/*
 * 根据HTML属性键获取值
 */
function getByKey(_this, key) {
    return $(_this).attr(key);
}

function getPath(_this) {
    return getByKey(_this, "path");
}

function getName(_this) {
    return getByKey(_this, "name");
}

function getTitle(_this) {
    return getByKey(_this, "title");
}

function getGlobalRepoId() {
    return $("#id-globle a:first").attr("id");
}

/**
 * 构造路径
 */
function pathContact(first, last) {
    if (first.length <= 0) {
        return last;
    }
    if (typeof last == 'undefined' || last.length <= 0) {
        return first;
    }
    return first + '/' + last;
}

function getCurrentPath() {
    var path = $("#id-globle a:last").attr("path");
    var name = $("#id-globle a:last").attr("name");
    return pathContact(path, name);
}

function getRepoId(_this) {
    return getByKey(_this, "id");
}

/**
 * 添加到导航
 */
function addToNavigation(_this, path, name) {
    // 移除自身及后续节点
    $(_this).nextAll().remove();
    $(_this).remove();
    var nav = '<a path="{0}" name="{1}" href="javascript:;" onclick="showDirectory(this)"><span style="padding:5px;">/</span>{1}</a>'.format(path, name);
    $("#id-globle").children("a:last").after(nav);
}

/**
 * 格式化文件信息
 */
function formatFileItemInfo(type, path, name, size, mtime) {
    html  = '<tr class="tr-border">';
    html += '	<th class="th-1"><input type="checkbox" /></th>';

    if ('file' == type) {
        html += '	<th class="th-2"><span title="{0}" name="{0}" path="{1}">{0}<span></th>'.format(name, path);
    } else {
        html += '	<th class="th-2"><span title="{0}" name="{0}" path="{1}" style="color:#00868B;" onclick="showDirectory(this)">{0}</span></th>'.format(name, path);
    }
    
    html += '	<th class="th-3">';
    html += '		<i title="{0}" name="{1}" class="icon-bin all-icon" onclick="fwriteNextDelete(this)"></i>'.format('删除', name);
    html += '		<i title="{0}" name="{1}" class="icon-write-down all-icon deal-method" onclick="fwriteNext(this)"></i>'.format('重命名', name);
    html += '		<i><a title="{0}" name="{1}" class="icon-download3 all-icon" onclick="fwriteNextDown(this)" id="downlink"></a>'.format('下载', name);
    html += '			<a id="down" style="visibility: hidden;">down</a></i>';
    html += '		<i title="{0}" name="{1}" class="icon-copy all-icon" onclick="fcopyNext(this)"></i>'.format('复制', name);
    html += '		<i title="{0}" name="{1}" class="icon-remove all-icon" onclick="fmoveNext(this)"></i>'.format('移动', name);
    html += '		<i title="{0}" name="{1}" class="icon-history all-icon" onclick="getFileHistory(this)"></i>'.format('历史记录', name);
    html += '	</th>';

    // 目录不显示大小
    if ('file' == type) {
        html += '	<th class="th-4">{0}</th>'.format(uploadFileSizeConvertTip(size));
    } else {
        html += '	<th class="th-4">--</th>';
    }
    
    html += '	<th class="th-5">{0}</th>'.format(formatTime(mtime));
    html += '</tr>';

    return html;
}

/**
 * 发送HTTP请求获取目录信息
 * @param repoId 仓库ID
 * @param path 仓库下的路径
 */
function fetchDirectory(repoId, path) {
    var files = null;
    $.ajax({
        url: '/wesign/repos/{0}/dir?path={1}'.format(repoId, path),
        type: 'GET',
        async: false,
        dataType: 'json',
        contentType: 'application/json',
        success: function(data) {
            if (data.status == 0 && data.data) {
                files = data.data;
            } else if (data.status == 0) {
                // 这是一个空目录                
                files = [];
            }
        }
    });
    return files;
}

/**
 * 解析文件数组为HTML字符串
 * @param files 文件信息数组
 * @param path 文件所在路径
 */
function parseFileInfo(files, path) {
    html = "";
    files.forEach(function(f) {
        html += formatFileItemInfo(f.type, path, f.filename, f.size, f.mtime);
    });
    return html;
}

/*
 * 获取资料库导航html
 */
function initRepoNavigation(_this) {
    var repoId = getRepoId(_this);
    var repoName = getTitle(_this);
    var nav = '<a href="javascript:;" id="{0}" path="" name="" style="text-decoration:underline;" title="{1}" onclick="showDirectory(this)">{1}</a>'.format(repoId, repoName);
    $("#id-globle").html(nav);
}

/*
 * 显示目录下的文件及文件夹信息
 */
function showDirectory(_this) {
    var path = getPath(_this);
    var title = getTitle(_this);
    var name = getName(_this);
    var fullpath = pathContact(path, name);
    var repoId = getGlobalRepoId();
    
    // 添加到 仓库-目录 导航
    if (path.length <= 0 && name.length <= 0) {
        initRepoNavigation(_this);
        // 首次进入仓库时，还未设置全局的仓库ID
        repoId = getRepoId(_this);
    } else {
        addToNavigation(_this, path, name);
    }
    
    var files = fetchDirectory(repoId, fullpath);
    if (files) {
        html = parseFileInfo(files, fullpath);
        $("#repo-table").html(html);
        $("#newRepos").addClass('hidden');
    }
    $(".lookFile").removeClass('hidden');
    $("#id-repose-content").css('top','120px');
}

/**
 * 新建目录弹出窗体
 */
$("#id-newfilder").unbind().bind('click',function(){
    layer.open({
	type: 1,
	title:'创建文件夹',
	skin: 'layui-layer-rim', //加上边框
	area: ['420px', '240px'], //宽高
	content: 
	'<div class="layui-form">'
	    +'<div class="layui-form-item" style="margin-top:30px;">'
	    +'<label class="layui-form-label">文件夹名：</label>'
	    + '<div class="layui-input-inline">'
	    +'<input type="text" name="name"  autocomplete="off" class="layui-input " id="id-newfloder"/>'
	    +'</div>'
	    +'</div>'
	    +'<div class="layui-form-item">'
	    +'<div class="layui-input-block">'
	    +'<button class="layui-btn" onclick="fnewNextfolder()">确认创建</button>'
	    +'<button type="reset" class="layui-btn layui-btn-primary" onclick="fnewresetrepose()">重置</button>'
	    +'</div>'
	    + '</div>'
	    +'</div>'
    });
});


/**
 * 新建文件夹
 */
function fnewNextfolder() {
    var repoId = getGlobalRepoId();
    var path = getCurrentPath();
    var name = $("#id-newfloder").val();
    console.log(repoId + ":" + path + ":" + "name");
    
    $.ajax({
        url:'/wesign/repos/' + repoId + '/dir',
        type:'POST',
        async:true,
        contentType:'application/json',
        data: JSON.stringify({"path": path, "name": name}),
        success:function(data){
            console.log(data);
            var status = data.status;
            var dir = data.data;
            if(status == 0 && dir){
                html = formatFileItemInfo(dir.type, path, dir.filename, dir.size, dir.mtime);
                $("#repo-table").html(html);
                layer.close({
                    anim:6
                });
                layer.msg('文件夹创建成功', {
                    icon: 1,
                    time: 1000, //2秒关闭（如果不配置，默认是3秒）
                    anim:1
                });
            }
        }
    });
}


/**
 * 目录重命名弹出窗口
 */
function fwriteNext(_this){
    layer.open({
	type: 1,
	title:'重命名文件夹',
	skin: 'layui-layer-rim', //加上边框
	area: ['420px', '240px'], //宽高
	content: 
	'<div class="layui-form">'
	    +'<div class="layui-form-item" style="margin-top:30px;">'
	    +'<label class="layui-form-label">文件夹名：</label>'
	    + '<div class="layui-input-inline">'
	    +'<input type="text" name="name"  autocomplete="off" class="layui-input " id="id-renamefloder"/>'
	    +'</div>'
	    +'</div>'
	    +'<div class="layui-form-item">'
	    +'<div class="layui-input-block">'
	    +'<button class="layui-btn" onclick="frenameNextfolder()">确认创建</button>'
	    +'<button type="reset" class="layui-btn layui-btn-primary" onclick="fnewresetrepose()">重置</button>'
	    +'</div>'
	    + '</div>'
	    +'</div>'
    });
}

/**
 * 请求重命名目录
 */
function frenameNextfolder(){
    var repoId = getGlobalRepoId();
    var path = getCurrentPath();
    var name = $("#id-renamefloder").val();
    var newName = pathContact(path, name);
    // TODO: 如何获取原文件的原名称

    console.log(repoId + ":" + path + ":" + name);
    
    $.ajax({
	url:'/wesign/repos/' + repoId + '/dir',
	async:true,
	type:'PUT',
	contentType:'application/json',
	data: JSON.stringify({"path": path, "name": name}),
	success:function(data){

	    var status = data.status;
	    var dd = data.data;
	    if(status == 0 && dd){
                html = formatFileItemInfo(dd.type, path, dd.filename, dd.size, dd.mtime);
		$("#repo-table").html(html);
		layer.close({
		    anim:6
		});
		layer.msg('文件夹创建成功', {
		    icon: 1,
		    time: 1000, //2秒关闭（如果不配置，默认是3秒）
		    anim:1
		});
	    } else {
                layer.msg(data.desc, {
                    icon: 2,
                    time: 1000,
                    anim: 1
                });
            }
	}
    });
}
