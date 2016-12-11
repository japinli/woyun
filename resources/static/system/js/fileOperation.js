/*
 * 文件及目录操作
 */

function getRepositoryId() {
    return $("#id-globle a:first").attr("id");
}

function getParentPath(_this) {
    return $("#id-globle").children("a:gt(0)").text();
}

function getTitle(_this) {
    return $(_this).attr("title");
}

function removeSubdirectory(_this) {
    return $(_this).nextAll().remove();
}

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
	}
    });
}
