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

function getId(_this) {
    return getByKey(_this, 'id');
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
    return $("#id-globle a:first").attr("repoid");
}


function getRepoId(_this) {
    return getByKey(_this, "repoid");
}

/**
 * 构造路径
 */
function pathContact(first, last) {
    if ('undefined' == typeof first) {
        first = "";
    }
    if ('undefined' == typeof last) {
        last = "";
    }
    
    if (first.length <= 0) {
        return last;
    }    
    return first + '/' + last;
}

function getCurrentPath() {
    var path = $("#id-globle a:last").attr("path");
    var name = $("#id-globle a:last").attr("name");
    return pathContact(path, name);
}

/**
 * 添加到导航
 */
function addToNavigation(_this, repoid, path, name) {
    var nav = '';
    if (path.length <= 0 && name.length <= 0) {
        // 进入资料库的根路径
        // 两种情况进入这里： 1. 导航的资料库名； 2. 资料库列表进入资料库。
        nav = '<a href="javascript:;" repoid="{0}" path="{1}" name="{2}" title="{3}" style="text-decoration:underline;" title"{2}" onclick="showDirectory(this)">{3}</a>';
        $("#id-globle").html(nav.format(repoid, path, name, getByKey(_this, 'title')));
    } else {
        // 移除自身及后续节点
        // 有两种情况可能进入到这里： 1. 导航目录； 2. 文件列表进入目录。
        $(_this).nextAll().remove();
        $(_this).remove();
        nav = '<a href="javascript:;" repoid="{0}" path="{1}" name="{2}" onclick="showDirectory(this)"><span style="padding:5px;">/</span>{2}</a>';
        $("#id-globle").children("a:last").after(nav.format(repoid, path, name));
    }
}

/**
 * 格式化文件信息
 */
function formatFileItemInfo(type, repoId, path, name, size, mtime) {
    var html = '';
    html += '<tr class="tr-border">';
    html += '	<th class="th-1"><input type="checkbox" /></th>';
    if ('file' == type) {
        html += '<th class="th-2"><span title="{0}" repoid="{1}" path="{2}" name="{0}">{0}</span></th>';
        size = uploadFileSizeConvertTip(size);
    } else {
        html += '<th class="th-2"><span title="{0}" repoid="{1}" path="{2}" name="{0}" style="color:#00868B;" onclick="showDirectory(this)">{0}</span></th>';
        size = '--';
    }
    html += '	<th class="th-3">';
    html += '		<i title="{3}" repoid="{1}" path="{2}" name="{0}" class="icon-bin all-icon" onclick="deleteFile(this)"></i>';
    html += '		<i title="{4}" repoid="{1}" path="{2}" name="{0}" class="icon-write-down all-icon" onclick="fwriteNext(this)"></i>';
    html += '		<i>';
    html += '			<a title="{5}" repoid="{1}" path="{2}" name="{0}" class="icon-download3 all-icon" onclick="downloadFile(this)"></a>';
    html += '			<a id="down" style="visibility: hidden;"></a>';
    html += '		</i>';
    html += '		<i title="{6}" repoid="{1}" path="{2}" name="{0}" class="icon-copy all-icon" onclick="fcopyNext(this)"></i>';
    html += '		<i title="{7}" repoid="{1}" path="{2}" name="{0}" class="icon-remove all-icon" onclick="fmoveNext(this)"></i>';
    if ('file' == type) {
        html += '	<i title="{8}" repoid="{1}" path="{2}" name="{0}" class="icon-history all-icon" onclick="getFileHistory(this)"></i>';
    }
    html += '	</th>';
    html += '	<th class="th-4">{9}</th>';
    html += '	<th class="th-5">{10}</th>';
    html += '</tr>';
    return html.format(name, repoId, path, '删除', '重命名', '下载', '复制', '移动', '历史记录', size, formatTime(mtime));
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
function parseFileInfo(repoId, files, path) {
    html = "";
    files.forEach(function(f) {
        html += formatFileItemInfo(f.type, repoId, path, f.filename, f.size, f.mtime);
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

/**
 * 新建目录弹出窗体
 */
$("#popup-new-folder").unbind().bind('click',function(){
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
	    +'<input type="text" name="name"  autocomplete="off" class="layui-input" id="new-folder" />'
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
    var name = $("#new-folder").val();
    console.log(repoId + ":" + path + ":" + name);

    $.ajax({
        url:'/wesign/repos/' + repoId + '/dir',
        type:'POST',
        async: false,
        contentType:'application/json',
        data: JSON.stringify({"path": path, "name": name}),
        success:function(data){
            console.log(data);
            var status = data.status;
            var dir = data.data;
            if(status == 0 && dir){
                updateDirectory(repoId, path);
                layer.msg('文件夹创建成功', {
                    icon: 1,
                    time: 1000, //2秒关闭（如果不配置，默认是3秒）
                    anim:1
                });
                layer.closeAll();
            }
        }
    });
}


/**
 * 目录重命名弹出窗口
 */
function fwriteNext(_this){
    var name = getByKey(_this, 'name');
    layer.open({
	type: 1,
	title:'重命名: ' + name,
	skin: 'layui-layer-rim', //加上边框
	area: ['420px', '240px'], //宽高
	content: 
	'<div class="layui-form">'
	    +'<div class="layui-form-item" style="margin-top:30px;">'
	    +'<label class="layui-form-label">文件夹名：</label>'
	    + '<div class="layui-input-inline">'
	    +'<input type="text" name="name" oldname="' + name + '" autocomplete="off" class="layui-input " id="id-renamefloder"/>'
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
    var new_name = $("#id-renamefloder").val();
    var old_name = $("#id-renamefloder").attr("oldname");
    
    $.ajax({
	url:'/wesign/repos/' + repoId + '/dir',
	async:true,
	type:'PUT',
	contentType:'application/json',
	data: JSON.stringify({"path": path, "name": old_name, "newName": new_name}),
	success:function(data){

	    var status = data.status;
	    var dd = data.data;
	    if(status == 0 && dd){
                
                updateDirectory(repoId, path);

		layer.msg('文件夹创建成功', {
		    icon: 1,
		    time: 1000, //2秒关闭（如果不配置，默认是3秒）
		    anim:1
		});
                layer.closeAll();
                
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

function permanentDelete(_this) {
    var ids = new Array(getByKey(_this, 'repoid'));
    batchPermanentDelete(_this, ids);
}

function batchPermanentDelete(_this, repoid) {
    var param = 'repoId=' + repoid.join('&repoId=');
    $.ajax({
        url: '/wesign/repos/permanent?' + param,
        type: 'DELETE',
        async: true,
        contentType: 'application/json',
        success: function(data) {
            console.log(data);
        }
        
    });
}

/**
 * 恢复已删除的仓库
 * @param repoid - 通过`_this`获取
 */
function restoreRepository(_this) {
    var repoid = getByKey(_this, 'repoid');
    $.ajax({
        url: '/wesign/repos/{0}/restore'.format(repoid),
        type: 'PUT',
        async: false,
        contentType: 'application/json',
        success: function(data) {
            console.log(data);
        }
    });
}
