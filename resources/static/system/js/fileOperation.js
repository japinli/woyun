/*
 * 文件及目录操作
 */

function getRepositoryId() {
    return $("#id-globle a:first").attr("repoid");
}

function getTitle(_this) {
    return $(_this).attr("title");
}

function removeSubdirectory(_this) {
    return $(_this).nextAll().remove();
}

function updateDirectory(repoid, path) {
    var files = fetchDirectory(repoid, path);
    if (files) {
        var html = parseFileInfo(repoid, files, path);
        $("#repo-table").html(html);
        $("#newRepos").addClass("hidden");
    }
}

/**
 * 显示目录下的文件及文件夹信息
 * repoid - 仓库ID
 * path - 参考下的路径
 * name - 待显示的目录名
 */
function showDirectory(_this) {
    var path = getByKey(_this, 'path');
    var name = getByKey(_this, 'name');
    var repoid = getByKey(_this, 'repoid');
    var fullpath = pathContact(path, name);
    addToNavigation(_this, repoid, path, name);
    
    var files = fetchDirectory(repoid, fullpath);
    if (files) {
        html = parseFileInfo(repoid, files, fullpath);
        $("#repo-table").html(html);
        $("#newRepos").addClass('hidden');
    }
    $(".lookFile").removeClass('hidden');
    $("#id-repose-content").css('top','120px');
}


/**
 * 删除文件或目录
 */
function deleteFile(_this) {
    var repoId = getRepoId(_this);
    var fullpath = pathContact(getCurrentPath(), getName(_this));
    $.ajax({
        url: '/wesign/repos/' + repoId,
        async: true,
        type: 'DELETE',
        data: JSON.stringify([{"path": fullpath}]),
        dataType: 'json',
        contentType: 'application/json',
        success: function(data) {
            console.log(data);
            if(data.status == 0){
                $(_this).parent().parent().remove();
                layer.msg('删除成功', {
                    icon: 1,
                    time: 1000, //2秒关闭（如果不配置，默认是3秒）
                    anim:1
                });
            }
        }
    });
}

function doDownload(url) {
    $('#down').attr('href', url);
    document.getElementById('down').click();
}

function batchDownloadFiles(_this, names) {
    var path = getPath(_this);
    var repoid = getRepoId(_this);
    var param = "path=" + path + '&name=' + names.join('&name=');
    var url = gfALLDATA('baseHref') + '/wesign/repos/' + repoid + '/file?' + param;
    doDownload(url);
}

/**
 * 下载文件或目录
 */
function downloadFile(_this) {
    var names = new Array(getByKey(_this, 'name'));
    batchDownloadFiles(_this, names);
}


function getFileHistory(_this) {
    var path = getCurrentPath();
    var name = getName(_this);
    var repoId = getGlobalRepoId();
    
    if (typeof name == 'undefined') {
        // 获取仓库的变更记录
        path = "";
        name = "";
    }

    var fullpath = pathContact(path, name);
    $.ajax({
        url: '/wesign/repos/' + repoId + '/history?path=' + fullpath,
        type: 'GET',
        contentType: 'application/json',
        success: function(data) {
            console.log(data);
        }
    });
}
