/*
 * 文件及目录操作
 */

function getRepositoryId() {
    return $("#id-globle a:first").attr("id");
}

function getParentPath(_this) {
    var path = $("#id-globle").children("a:gt(0)").text();
    if (path.length <= 0) {
        return "/";
    }
    return path;
}

function getTitle(_this) {
    return $(_this).attr("title");
}

function removeSubdirectory(_this) {
    return $(_this).nextAll().remove();
}

function setDownloadLink(_this, url) {

}

/**
 * 显示目录下的文件及文件夹信息
 */
function showDirectory(_this) {
    var path = getPath(_this);
    var title = getTitle(_this);
    var name = getName(_this);
    var fullpath = pathContact(path, name);
    var repoId = getByKey(_this, 'repoid');
    
    // 添加到 仓库-目录 导航
    if (path.length <= 0 && name.length <= 0) {
        initRepoNavigation(_this);
        // 首次进入仓库时，还未设置全局的仓库ID
        repoId = getId(_this);
    } else {
        addToNavigation(_this, path, name);
    }
    
    var files = fetchDirectory(repoId, fullpath);
    if (files) {
        html = parseFileInfo(repoId, files, fullpath);
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
    var repoId = getRepositoryId();
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

/**
 * 下载文件或目录
 */
function downloadFile(_this) {
    var repoId = getRepoId(_this);
    var path = getPath(_this);
    var name = getName(_this);
    var url = gfALLDATA('baseHref') + '/wesign/repos/' + repoId + '/file?path=' + path + '&name=' + name;
    doDownload(url);
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
