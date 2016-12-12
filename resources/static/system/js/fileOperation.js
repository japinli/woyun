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

/*
 * 删除文件或目录
 */
function fwriteNextDelete(_this) {
    var repoId = getRepositoryId();
    var fullpath = getParentPath(_this) + '/' + getTitle(_this);
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

/*
 * 下载文件或目录
 */
function fwriteNextDown(_this) {
    var repoId = getGlobalRepoId();
    var path = getCurrentPath();
    var downlink = gfALLDATA('baseHref') + '/wesign/repos/' + repoId + '/file?path=' + path + '&name=' + getName(_this);
    console.log(_this);
    $("#down").attr('href', downlink);
    document.getElementById("down").click();
}
