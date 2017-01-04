function beforeUpload() {
    var repoId = getGlobalRepoId();
    var path = getCurrentPath();
    $('#upload-file').dmUploader({
        url: '/wesign/repos/' + repoId + '/file?path=' + path,
        method: 'POST',
        dataType: 'json',
        allowedTypes: '*',
        onInit: function() {
            console.log("initialized");
        },
        onBeforeUpload: function(id) {
            console.log("starting the upload of #" + id);
        },
        onNewFile: function(id, file) {
            console.log('new file added to queue #' + id);
        },
        onComplete: function() {
            console.log("all pending transfers finished");
        },
        onUploadProgress: function(id, percent) {
            var percentStr = percent + '%';
            console.log("#" + id + " : " + percentStr);
        },
        onUploadSuccess: function(id, data) {
            console.log("upload of file #" + id + " completed");
        },
        onUploadError: function(id, message) {
            console.log("failed to upload file #" + id + ": " + message);
        },
        onFileTypeError: function(file) {
            
        },
        onFileSizeError: function(file) {
            
        },
        onFallbackMode: function(message) {
            alert("browser not supported(do something else here!): " + message);
        }
    });
}
