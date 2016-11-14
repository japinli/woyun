$(document).ready(function(){
		
	//对话框初始化设置
	initialContactPanel("#contactPanel",480,160);
	initialContactPanel("#contactPeoplePanel",480,370);
	initialContactPanel("#removeContacter",480,220);
	
	//不存在【未分组】，初始化【未分组】
	showContactFolder();//显示联系人列表  && 初始载入用户第一个分组【未分组】的数据
	
	//新建文件夹  &&　新建联系人
	$("#addNewFolderBtn").unbind("click").click(function(){
			//表单重置
			$("#addContacterGroups")[0].reset();
			$('#contactFolderPut-block .contactFolderPanel span').empty().remove('span');   //去掉校验钩钩
			$('#contactFolderPut-block .contactFolderPanel .errorClass').empty().remove('p');
			toggleDialog(true , "#contactPanel");     //打开对话框
			//新建文件夹保存
			$("#contactFolderPut-submit").unbind("click").click(function(){
				folderSubmitPress();
			});
			//新建文件夹绑定回车事件
			$("#folderInput").unbind("keydown").keydown(function(event){
				if(event.keyCode == "13"){
					$("#contactFolderPut-submit").click();
				}
			});
	});
		
//	新建分组列表下的联系人
		$("#addNewPeoBtn").unbind("click").click(function(){
			//表单重置
			$('#addContacterToGroup')[0].reset();
			$('.contactPeoListPanel .contactPeoList span').empty().remove('span');
			$('.contactPeoListPanel .contactPeoList .errorClass').empty().remove('p');
			//弹出对话框
			toggleDialog(true , "#contactPeoplePanel");
		//将联系人归组
				$('.setGroupBtn').unbind("click").click(function(){
						$(".pulldown").empty();
						var groups=$("#doc-id-bar li .lbar-link-title");
						$(groups).each(function(index,ele){
							$(".pulldown").append('<li>'+$(ele).text()+'</li>');
							selectGroup();
						});
						$('.pulldown').toggle();
					});
		//光标移出下拉框区域，隐藏下拉框
				$(".pulldown").mouseleave(function(){
					$(this).hide();
				})
		//新建联系人保存
				$("#contactPeoPut-submit").unbind("click").click(function(){
					var folderId =$("#doc-id-bar > li.active").attr("id");
					peopleSubmitPress(folderId);
				});
		//绑定回车事件
				$('#new-contact-name ,#new-contact-email  ,#new-contact-phone')
				.unbind("keydown").keydown(function(event){
					if(event.keyCode == "13"){
					$("#contactPeoPut-submit").click();
					}
				});
				
		});
		
		
})    //ready的尾部


//
//函数申明
//

//点击指定分组刷新列表 && 切换active焦点
function refreshMenberList(){
	$(".ce li").unbind('click').click(function(){
		var indexLi=$(".ce li").index($(this)[0]);
		$(".ce li").each(function(ele,index){
			$(index).removeClass("active");
		})
		$(".ce li").eq(indexLi).addClass("active");
		var FolderID = $(this).attr("id");
		getOneFolderEles(FolderID);
		oneGroupMenberNumber(FolderID);
	});
}
//分组栏鼠标经过显示删除图标 && 绑定图标删除事件
function showDelIcon(){
	$(".ce li").mouseenter(
			function(){
				var indexI = $(".ce li").index($(this));
				//取消“未分组”的点击事件,序号为0
				if(indexI !== 0){
					var a=$(".ce li a").eq(indexI);
					//显示垃圾箱图标
					$(a).children("i").eq(1).css("visibility","visible");
					//删除某分组
					$(".ce li i.gDel").unbind("click").click(function(event){
						var foName=$(a).children(".lbar-link-title").text();
						var delPos=$(".ce li").eq(indexI);
						var delGroupId = $(delPos).attr("id");
						delFolder(foName,$("#"+delGroupId),delGroupId);
						event.stopPropagation();   //阻止事件冒泡
					})
				}else{
				}
				
			}
	);
	$(".ce li").mouseleave(function(){
		var indexI = $(".ce li").index($(this));
		var a=$(".ce li a").eq(indexI);
		$(a).children("i").eq(1).css("visibility","hidden");
	});
}



function toggleDialog( command ,object){
	if(command){
		$(object).dialog("open");
		$('.setGroupBtn span').text("默认当前分组");   //重置默认分组
	}else{
		$(object).dialog("close");
	}
}

//新建文件夹
function addNewFolder(name, id){

	$('#doc-id-bar').append(
			'<li id="'+id+'">'+'<a href="javascript:;">'+'<i class="icon-play3"></i>'+
			'<span class="lbar-link-title">'+name+'</span>'+'<i class="icon-bin gDel "></i>'+
			'<span class="docNum">'+0+'</span>'+
			'</a>'+'</li>'
	);
	
}

//新建联系人
function addNewContacter(objectPosition,groupID, id, newName, newEmail, newPhone){
	$(objectPosition).append(
			'<div class="contact-list-item clearfix" id="'+id+'">'+'<i class="icon-user-hollow contact-peo"></i>'+'<div class="contacter-info">'
			+'<span><i class="contacter-name">'+newName+'</i></span><br>'+'<span>电话:　<i class="contacter-phone">'+newPhone+'</i></span><br>'+
			'<span>邮箱:　<i class="contacter-email">'+newEmail+'</i></span>'+'</div><i class="icon-clear del-contacter"></i>'
			+'<i class="icon-create edit-contacter">'+'</div>'
	);
	//经过的时候绑定删除事件
	fixEventDelandRemove(groupID);
}

//经过某个联系人,绑定--删除--移动  事件
function fixEventDelandRemove(groupID){
	$("#contact-list-all .contact-list-item > i.del-contacter").mouseenter(function(){
			var delIconIndex = $("#contact-list-all .contact-list-item > i.del-contacter").index($(this));
			var delPersonalIndex = $("#contact-list-all .contact-list-item").eq(delIconIndex).attr("id");
			delOneContact(groupID,delPersonalIndex);   //删除某个联系人groupID,id
		});
	$("#contact-list-all .contact-list-item > i.edit-contacter").mouseenter(function(){
		removeOneContact(groupID);  //移动某个联系人currentGroupID
	});
}


//删除联系人确认框
function delContacter(delName,delPos,groupID,currentMenberID){
	$(".confirmDialog").html('<div class="textCenter ptb10"><i class="active icon-tip2 fs32"></i></div>'
													    +'<div class="confirmContent textCenter"><p style="text-align:center;line-height:1.6;">联系人删除后无法找回<br/>你确定要删除联系人<span class="active">'+delName+'</span> ?</p></div>'
													    +'<div class="confirmBtn textCenter borderTop">'
													    	+'<button class="confirmYes btn-primary mr25">确认</button>'
													        +'<button class="confirmNo btn-default">取消</button>'
													    +'</div>'
											).dialog({autoOpen: true, title: "删除"});
   // $('.confirmContent').html('<p style="text-align:center;line-height:1.6;">联系人删除后无法找回<br/>你确定要删除联系人<span class="active">'+delName+'</span> ?</p>');

    $('.confirmNo').unbind('click').click(function(){ //取消
        $(".ui-dialog-titlebar-close").click();
    });

    $('.confirmYes').unbind('click').click(function(){ //确认
        //向服务器发送删除数据
        requestServer({
        	requestURL : '/user/sys/users/contact/one/'+currentMenberID+'/'+groupID+'/delete',
        	requestType : 'POST',
        	successCallback : function(data){
        		var code = data.resultCode;
    			var desc = data.resultDesc;
    			if(code == 0){
    				$(delPos).empty().remove('div');
    				oneGroupMenberNumber(groupID);
    			}else{
    				showInfo(desc);
    			}
        	}
        });
        
        $(".ui-dialog-titlebar-close").click();
    });
}
//删除分组文件夹
function delFolder(delFolderName,delPos,delGroupId){
	$(".confirmDialog").html('<div class="textCenter ptb10"><i class="active icon-tip2 fs32"></i></div>'
													    +'<div class="confirmContent textCenter"><p style="text-align:center;line-height:1.6;">你确定要删除联系人分组<span class="active">'+delFolderName+'</span> ?</p></div>'
													    +'<div class="confirmBtn textCenter borderTop">'
													    	+'<button class="confirmYes btn-primary mr25">确认</button>'
													        +'<button class="confirmNo btn-default">取消</button>'
													    +'</div>'
											).dialog({autoOpen: true, title: "删除"});
	//$(".confirmContent").html('<p style="text-align:center;line-height:1.6;">你确定要删除联系人分组<span class="active">'+delFolderName+'</span> ?</p>');
	$('.confirmNo').unbind('click').click(function(){ //取消
        $(".ui-dialog-titlebar-close").click();
    });
	
	 $('.confirmYes').unbind('click').click(function(){ //确认
		 //操作描述：删除用户联系人分组（如果该分组下有联系人，则全部删除）
	        $(delPos).empty().remove('li');
	        submitDelFolderName(delGroupId,delFolderName);
	        $(".ui-dialog-titlebar-close").click();
	    });
}
//删除分组下的指定联系人
function delOneContact(groupID,currentMenberID){
	$(".contact-list-item .del-contacter").unbind("click").click(function(){
		var indexPos =$(".del-contacter").index($(this));
		var nameCollect = $('.contacter-name');
		var delName = $(nameCollect).eq(indexPos).text();
		var delPos = $('.contact-list-item').eq(indexPos);
		delContacter(delName, delPos,groupID,currentMenberID);    //删除确认框
	});
}
//移动分组中的指定联系人
function removeOneContact(groupID){
	$("#contact-list-all .contact-list-item > i.edit-contacter").unbind("click").click(function(){
		var indexPos =$("#contact-list-all .contact-list-item > i.edit-contacter").index($(this));
		var nameCollect = $('.contacter-name');
		var removeName = $(nameCollect).eq(indexPos).text();
		var removePosID = $('.contact-list-item').eq(indexPos).attr("id");
		removeContacter(groupID,removePosID);    //向后台传数据
	});
}
//移动联系人，向后台传数据
function removeContacter(groupID,currentMenberID){     //当前的用户组id，当前成员id
	toggleDialog(true , "#removeContacter");     //打开对话框
		//显示文件夹
		$('#removeContacter .setGroupBtn-remove').unbind("click").click(function(){
			$(".pulldown-remove").empty();
			var groups=$("#doc-id-bar li .lbar-link-title");
			$(groups).each(function(index,ele){
				$(".pulldown-remove").append('<li>'+$(ele).text()+'</li>');
				//显示选择分组
				$('.pulldown-remove li').unbind('click').click(function(){
					var setG=$(this).text();
					$('.setGroupBtn-remove span').text(setG);
				});
			});
			$('.pulldown-remove').toggle();
		});
		//光标移出下拉框区域，隐藏下拉框
		$(".pulldown-remove").mouseleave(function(){
			$(this).hide();
		});
		//移动完成按钮点击
	$("#removePeoPut-submit").unbind("click").click(function(){
		var removeToGroupID= "";
		var removeToGroupName = $('.setGroupBtn-remove span').eq(0).text();
		if(removeToGroupName == "默认未分组"){
			removeToGroupName = removeToGroupName.slice(2);
		}
		var groups=$("#doc-id-bar li .lbar-link-title");
		$(groups).each(function(index,ele){
			if($(ele).text() == removeToGroupName ){
				removeToGroupID =$("#doc-id-bar li").eq(index).attr("id");
			}
		});
		//提交数据到后台
		if(groupID != removeToGroupID){     //当前组移动到当前组，数据不提交
				requestServer({
		        	requestURL : '/user/sys/users/contact/one/'+currentMenberID+'/'+removeToGroupID+'/move',
		        	requestType : 'POST',
		        	successCallback : function(data){
		        		var code = data.resultCode;
		    			var desc = data.resultDesc;
		    			if(code == 0){
		    				$('.contact-list #'+currentMenberID).empty().remove('div');
		    				oneGroupMenberNumber(groupID);
		    				oneGroupMenberNumber(removeToGroupID);
		    			}else{
		    				showInfo(desc);
		    			}
		        	}
		        });
		}		
		//对话框关闭
		toggleDialog(false, "#removeContacter");
		
	})
}
//初始化对话框
function initialContactPanel(object,width,height){
	$(object).dialog({ //新建联系人&&文件夹 弹出框设置
	    autoOpen: false,
	    minHeight: height,
	    width: width,
	    modal: true,
	    resizable: false,
	    position: { my: "center", at: "center", of: window }
	});
}

//确定归组位置
function selectGroup(){
	$('.pulldown li').unbind('click').click(function(){
		var setG=$(this).text();
		$('.setGroupBtn span').text(setG);
	});
}

//校验信息
function  InfoValidator(){
	var _this = this,
	appendHtml = function(elem, flag, text){   //添加校验提示信息
		if(elem.parent().next().hasClass("errorClass")){
			if(flag){
				elem.parent().next().remove();
				elem.parent().parent().append('<span class="errorClass" style="padding-left:4px;"><i class="icon-checkmark" style="color:#39b94e;"></i></span>');
			}else{
				elem.parent().next().remove();
				elem.parent().parent().append('<p class="errorClass" style="color:red;font-size:12px;display:block;">'+ text +'</p>');
			}
		}else{
			if(flag){
				if(flag && text==""){
					
				}else{
					elem.parent().parent().append('<span class="errorClass" style="padding-left:4px;"><i class="icon-checkmark" style="color:#39b94e;"></i></span>');
				}
			}else{
				elem.parent().parent().append('<p class="errorClass" style="color:red;font-size:12px;display:block;">'+ text +'</p>');
			}
		}
	};
	
	_this.validName =function($obj){ 	//用户名
		var reg = /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{1,20}$/;
		var value = $obj.val();
		var pass = false;
		var tip = "";
		if( value == ""){
			tip = "必填项";
			pass = false;
		}else if(reg.test(value)){
			tip = "正确";
			pass = true;
		}else{
			tip = "1-20位包含汉字、字母、数字、下划线且下划线不能开头或结尾";
			//(1)长度3~20位;(2)只能包含汉字、字母、数字或下划线(3)下划线不能开头或结尾
			pass = false;
		}
		appendHtml($obj, pass, tip);
	    return pass;
	};
	
	_this.validPhone = function($obj){	//手机号码
		var reg =/^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
		var value = $obj.val();
		var pass = false;
		var tip = "";
		if( value == "" ){
			pass = true;
		}else if( reg.test(value) ){
			pass = true;
			tip = "正确"
		}else{
			tip = "请输入11位合法手机号码";
			pass = false;
		}
		appendHtml($obj, pass, tip);
	    return pass;
	};
	//邮箱校验 
	_this.validEmail = function($obj){
		var reg =	/^([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.|-]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		var value = $obj.val();
		var pass = false;
		var tip ="";
		if(value==""){
			tip = "邮箱必须填写";
			pass = false;
		}else if(reg.test(value) ){
			pass = true;
			tip = "正确"
		}else{
			tip = "邮箱格式不正确";
			pass = false;
		}
		appendHtml($obj, pass, tip);
	    return pass;
	}
}

//获取所有联系人文件夹
function showContactFolder(){
	var currentPage = 1;
	var perPageRows = 50;  //每页条数
	requestServer({
		requestURL : '/user/sys/users/contact/group/'+currentPage+'/'+perPageRows+'/get' ,
		requestType : 'get' ,
		successCallback : function(data){
			var code = data.resultCode;
			var desc = data.resultDesc;
			var listArray = data.contactGroupList;
			var len = data.size;
			if(code ==0){
				if( len > 0 ){     // 存在分组，展示分组文件夹
					var FolderListName=getMyContactFolder(listArray,"name");
					var FolderListId=getMyContactFolder(listArray,"id");
					for(var i=0;i<len;i++){
						addNewFolder(FolderListName[i],FolderListId[i]);
						oneGroupMenberNumber(FolderListId[i]);
					}
					$("#"+FolderListId[0]).addClass("active");
					showDelIcon();   //显示垃圾箱icon
					refreshMenberList(); //点击指定分组，active状态切换，刷新成员列表
					getOneFolderEles(FolderListId[0]);   //【未分组】的成员列表
					
				}else{
					//不存在【未分组】，创建未分组
					submitNewFolderName({"name":"未分组","remarks":""});   //创建未分组，向后台发送数据
					$(listArray)[0].addClass("active");     //设置为当前活动对象    
					$(listArray)[0].unbind("click").click();
				}
				
			}else{
				showInfo(desc);
			}
		}
		});
}
//将分组列表保存成数组，返回数组
function getMyContactFolder(array,nameArg){
	var listLength = array.length;
	var allFolder= [];
	for(var i=0;i<listLength;i++){
		allFolder.push(array[i][nameArg]);
	}
	return allFolder;
}
//查询指定组下的成员总数
function oneGroupMenberNumber(contactGroupId){
	requestServer({
		requestURL : '/user/sys/users/contact/multiple/'+contactGroupId+'/count',
		requestType : 'get' ,
		successCallback : function(data){
			var code = data.resultCode;
			var desc = data.resultDesc;
			var count =data.count;
			if(code == 0){
				$("#"+contactGroupId).find("span").eq(1).text(count);
			}else{
				showInfo(desc);
			}
		}
	})
}

//指定分组的详细信息列表
function getOneFolderEles(folderId){
	var currentPage = 1;
	var perPageRows = 50;
	requestServer({
		requestURL : '/user/sys/users/contact/multiple/'+folderId+'/'+currentPage+'/'+perPageRows+'/get' ,
		requestType : 'get' ,
		beforeCallback : function(){
            showLoading();
		},
		successCallback : function(data){
			var code = data.resultCode;
			var desc = data.resultDesc;
			var len = data.size;
			var allList = data.contactList;
			if(code==0){
					var newContacter =$("#contact-list-all");
					$(newContacter).empty();    //清空当前列表
					if(len>0){
						for(var i=0; i<len ;i++){
							addNewContacter(                //列出该文件夹下的所有成员
									newContacter, 
									folderId,
									allList[i].id,
									allList[i].realName, 
									allList[i].email, 
									allList[i].phone
									);
						}
						//经过的时候绑定删除事件
//						$("#contact-list-all .contact-list-item > i.del-contacter").mouseenter(function(){
//									var delIconIndex = $("#contact-list-all .contact-list-item > i.del-contacter").index($(this));
//									var delPersonalIndex = $("#contact-list-all .contact-list-item").eq(delIconIndex).attr("id");
//									delOneContact(folderId,delPersonalIndex);   //删除某个联系人groupID,id
//									removeOneContact(folderId);  //移动某个联系人currentGroupID
//								});
					}else{
						occupContact();
					}
					hideLoading();
			}else{
				showInfo(desc);
			}
		}
	});
}

//联系人无空白处理
function occupContact(){
	var html = '';
	 html += '<div class="occup-container textCenter">'
						+'<div class="contact-occup occup-bg"></div>'
						+'<div class="occup-info"><p>本组还没有任何联系人</P><p>赶快添加联系人吧！</p></div>'
				   +'</div>';
	 
	 $("#contact-list-all").html(html);
}

//新增分组提交事件
function folderSubmitPress(){
		var newFolder=$(".inputSignature").val();
		var flag =0;
		var hasedVals = $("#doc-id-bar > li .lbar-link-title");
		for(var i=0,len=hasedVals.length;i<len;i++){
			if( $(hasedVals[i]).text() === newFolder){
				flag = 1;
				alert("该分组已经存在！");
				break;
			}
		}
//		字段校验
		if(flag == 0){
			var  vInfo=new  InfoValidator();
			var fname=vInfo.validName($(".inputSignature"));
			if(fname == true){
			var jsonDate = {
				"name" : newFolder,
				"remarks" : " " 
			}
			submitNewFolderName(jsonDate);   //向后台发送数据
			toggleDialog(false, "#contactPanel");
			}
		}else{}
		
}
//新增联系人提交事件
function peopleSubmitPress(folderId){
		var selfGroup = $(".setGroupBtn span").text();   //自己选择的分组
		var gs = $("#doc-id-bar li");
		var gl =$(gs).length;
		var selGroupID;     //获取自己手动选择分组对应的ID
		for(var i =0;i<gl;i++){
			if($(gs).eq(i).find(".lbar-link-title").text() == selfGroup){
				selGroupID = $(gs).eq(i).attr("id");
				break;
			}
		}
		var newContacterInfo={
				newName: $("#new-contact-name").val(),
				newEmail: $("#new-contact-email").val(),
				newPhone: $("#new-contact-phone").val(),
				};
		//字段校验
		var  vInfo=new  InfoValidator();
		var fname=vInfo.validName($("#new-contact-name"));
		var femail=vInfo.validEmail($("#new-contact-email"));
		var fphone=vInfo.validPhone($("#new-contact-phone"));
		if(fname && femail && fphone){
			var newContacter =$("#contact-list-all");
			//数据提交保存 
			var jsonData = {
					"phone" : newContacterInfo.newPhone,
					"email" : newContacterInfo.newEmail,
					"realName" : newContacterInfo.newName,
					"remarks" : " ",
					"contactGroupId" : selGroupID || folderId     // 指定分组的id
			}
			submitAddFolderMenber(jsonData,newContacter);
			//清除选择样式
			$(".ce li").each(function(ele,index){
				$(index).removeClass("active");
			})
			$("#"+jsonData.contactGroupId).addClass("active");
			toggleDialog(false, "#contactPeoplePanel");
		}else{
			return false;
		}
}

//创建用户联系人分组
function submitNewFolderName(jsonData){
	requestServer({
		requestURL : '/user/sys/users/contact/group/add',
		requestType : 'POST',
		requestData : JSON.stringify( jsonData),
		successCallback : function(data){
			var code = data.resultCode;
			var desc = data.resultDesc;
			var contactGroupId = data.contactGroupId;
			if(code ==0){
				addNewFolder(jsonData.name,contactGroupId);
				showDelIcon();   //显示垃圾箱icon
				refreshMenberList(); //点击指定分组，active状态切换，刷新成员列表
			}else{
				showInfo(desc);
			}
		}
	})
}
//删除用户联系人分组 -提交
function submitDelFolderName(delGroupId,foName){
	requestServer({
		requestURL : '/user/sys/users/contact/group/'+delGroupId+'/delete',
		requestType : 'POST',
		successCallback : function(data){
			var code = data.resultCode;
			var desc = data.resultDesc;
			if(code ==0){
				$("#doc-id-bar li:first-child ").click();
			}else{
				showInfo(desc);
			}
		}
	})
}
//指定分组里面创建联系人
function submitAddFolderMenber(jsonData,newContacter){
	requestServer({
		requestURL : '/user/sys/users/contact/one/'+jsonData.contactGroupId+'/add',
		requestType : 'POST',
		requestData : JSON.stringify( jsonData),
		successCallback : function(data){
			var code = data.resultCode;
			var desc = data.resultDesc;
			var newMenberID = data.contactId;
			if(code ==0){
				addNewContacter(
						newContacter, 
						newMenberID,       //将后台返回数据作为  新建联系人的ID
						jsonData.contactGroupId,
						jsonData.realName, 
						jsonData.email,
						jsonData.phone
						);
				getOneFolderEles(jsonData.contactGroupId);   //刷新当前分组列表
				oneGroupMenberNumber(jsonData.contactGroupId);
			}else{
				showInfo(desc);
			}
		}
	})
}





