/*****
 * author: duyanmei
 * last edit time: 2016-5
 * *****/
$(document).ready(function() {

	$(".deal-method-child").jBox("Tooltip", {
		trigger: 'click',
		closeOnClick: "body",
		animation: 'pulse',
		content: $("#deal-method-child"),
		position: {
			x: 'right',
			y: 'top'
		},
		offset: {
			x: 0,
			y: -70
		},
		pointer: 'right:8',
		outside: 'x' // Horizontal Tooltips need to change their outside position
	});

	$(".deal-method").jBox("Tooltip", {
		trigger: 'click',
		closeOnClick: "body",
		animation: 'pulse',
		content: $("#deal-method"),
		position: {
			x: 'right',
			y: 'top'
		},
		offset: {
			x: 0,
			y: -60
		},
		pointer: 'right:8',
		outside: 'x' // Horizontal Tooltips need to change their outside position
	});
	
	
	docsView.addDocUperView();
});
var docsView = {
	views: [],
	docsContainer: null,
	fullDocs: false,
	addDocUperView: function() {
		var that = this;
		var id=new Date().getTime();
		/*if(this.views.length>4) {s
			this.fullDocs=true;
			return;
		}*/
		//监听提交事件
		$('#upload-files').fileupload({
			url: '',
			dataType: 'json',
			type: 'post',
			autoUpload: true,
			singleFileUploads: false,
			acceptFileTypes: /(\.|\/)(pdf|doc|docx|xls|xlsx|ppt|pptx|wps|et|dps|jpeg|jpg|jpe|gif|png)$/i,
			maxFileSize: 2097152, //1024*1024*2(M)
			formAcceptCharset: 'UTF-8',
		}).on('fileuploadadd', function(e, data) {
			console.log("添加文件", data);
			$.each(data.files, function(index, file) {
				var aFileSplit = file.name.split(".");
				aFileSplit.splice(-1);
				var fileName = aFileSplit.join(".");
				console.log("添加文档" + newdiv.className);
			});
		}).on('fileuploadprocessalways', function(e, data) {
			/*var index = data.index;*/
			var file = data.files[0];
			var node = $(data.context.children()[0]);
			if(file.error) {
				node.find('.choose-in').remove().end().append($('<p class ="file-result-error" style="font-size:1.2rem;"></p>').html('<p style="font-size:1.6rem;">上传失败<p>' + file.error));
				node.append('<div class="file-redo"><i class="icon-spinner11 spinner"></i></div>');
				node.parent().parent().css('border', 'none');
				node.children("div:first-child").next().next().bind('click', function() {
					node.parent().parent().css('border', '1px solid #e9e9e9');
					node.parent().parent().next().children("p:first-child").html("");
					node.parent().parent().prev().prev().removeClass("fileNone");
					node.parent().parent().addClass("hidden");
					node.parent().parent().next().children("div:first-child").html("");
					node.remove();
				});
				return false;
			}
		}).on('fileuploadprogressall', function(e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10);
			$('.progress-bar').css('width', progress + '%');
		}).on('fileuploaddone', function(e, data) {
			var statusCode = {
				"100100100": "禁止访问",
				"100130000": "上传成功",
				"100130100": "上传失败，文件为空",
				"100130101": "上传失败，文件名长度不符合要求",
				"100130102": "上传失败，文件尺寸不符合要求",
				"100130103": "上传失败，文件类型不符合要求",
				"100130104": "上传失败，达到已有文件数量限制，不允许再上传",
				"100130105": "上传失败，未预测到的错误或异常，请重新操作或联系我们",
				"100130106": "上传失败，存在失败的上传"
			};
			console.log("上传完成");
			$(newdiv).children('.choose-in').html("");

			if(data.result.resultCode === 0) {
				console.log("上传成功");

				var docdata = data.result.resultData.documents[0];
				$(newdiv).children("div:first-child").remove();
				docsModle.addDate(docdata);

				$(newdiv).children(".choose-in").html('<img src="' + docdata.imageLocation + '" style="width:100%;height:100%">');
				$(newdiv).children(".choose-in").find("img").bind("error", function() {
					$(this).unbind("error");
					this.style = "margin:10px 20px";
					this.src = gfALLDATA("baseHref") + "/static/system/images/doc-upload.png";
				});
				//绑定事件
				$(newdiv).mouseover(function() {
					$(this).children("div:first-child").removeClass("fileNone");
					$(this).children("div:first-child").addClass("fileBlock");
				}).mouseout(function() {
					$(this).children("div:first-child").removeClass("fileBlock");
					$(this).children("div:first-child").addClass("fileNone");
				});
				//预览
				$(newdiv).children(".fade").addClass("fileNone").children('.fade-up').click(function() {
					previewDoc($(this).next().attr("data-docid"), $(this).parent().parent().find('.file-title-font').text());
				});
				//删除
				$(newdiv).children(".fade").children('.fade-down').attr("data-docid", docdata.docId).click(function() {
					docsController.deDoc($(this).attr('data-docid'));
				});

				that.addDocUperView();
			} else {
				console.log("上传失败");
				$(newdiv).children('.choose-in').append($('<p class="file-result-error" style="font-size:1.2rem;"></p>').html('<p style="font-size:1.6rem;">上传失败<p>' + statusCode[data.result.resultStatusCode]))
					.append($('<div class="file-redo"><i class="icon-spinner11 spinner"></i></div>').bind('click', function() {
						$(newdiv).children("div:first-child").removeClass("fileNone");
						$(newdiv).children('.choose-in').html('<div class="upload-files-container"></div>').addClass('hidden');
						$(newdiv).children('.choose-font').html("");
					}));
			}
		});
	},
	update: function() {

	},
	delview: function(index) {
		this.docsContainer.removeChild(this.views[index]);
		this.views.splice(index, 1);
		if(this.fullDocs) {
			this.fullDocs = false;
			this.addDocUperView();
		}
	}
};

var docsController = {
	init: function() {
		requestServer({
			requestURL: gfALLDATA("envelopeHref") + "/" + location.href.split("envelope-id=")[1] + "/documents?sortings=+time",
			requestType: "GET",
			//requestData:JSON.stringify({"docIds": [{"docId":this.parentElement.parentElement.parentElement.parentElement.lastElementChild.lastElementChild.attributes["data-docid"].nodeValue }]}),
			beforeCallback: function() {
				console.log("send!");
			},
			successCallback: function(data) {
				console.log("docs", data);
				if(data.resultCode === 0) {
					docsModle.setInitDate(data.resultData);
				} else {
					console.error(data.resultDesc);
				}
			}
		});
	},
	deDoc: function(docid) {
		requestServer({
			requestURL: gfALLDATA("envelopeHref") + "/" + location.href.split("envelope-id=")[1] + "/documents",
			requestType: "DELETE",
			requestData: JSON.stringify({
				"docIds": [{
					"docId": docid
				}]
			}),
			successCallback: function(data) {
				console.log(data);
				if(data.resultCode === 0) {
					docsModle.deDoc(docid);
				} else {
					console.error(data.resultDesc);
				}
			}
		});
	},
};

var docsModle = {
	data: {},
	//:documents:Array[85],envelopeId:"a00ed5b2-f245-4fb7-bd78-dde58c9ce95d"
	setInitDate: function(initData) {
		this.data = initData;
		if(!this.data.documents) this.data.documents = [];
		docsView.init();
	},
	addDate: function(newData) {
		this.data.documents.push(newData);
		this.dataChange();
	},
	getdocsData: function() {
		return this.data.documents;
	},
	dataChange: function() {
		docsView.update();
	},
	deDoc: function(docid) {
		var docs = this.data.documents;
		docs.forEach(function(docData, index) {
			if(docData.docId == docid) {
				docs.splice(index, 1);
				docsView.delview(index);
				return 0;
			}
		});
	}
};
