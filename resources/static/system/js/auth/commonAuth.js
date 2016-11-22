$(document).ready(function(){
	/*$(document).bind("keydown",function(e){ 
		e=window.event||e; 
		if(e.keyCode==116){ 
		e.keyCode = 0; 
		return false; 
		} 
	}); 
	$(document).bind("contextmenu",function(e) { 
		return false; 
	}); */
	 $.datepicker.setDefaults( $.datepicker.regional[ "zh-CN" ] );
	 $(".js_card_date").datepicker({
		 minDate: new Date(),
		 changeMonth: true,
	     changeYear: true
	 });
});

function dateToTime($obj){
	return new Date( $obj.val() ).getTime();
}

function stepOne(callback) {
	confirmDialog("信息提交将不能再修改，您确定现在提交？");
	$(".confirmYes").unbind('click').click(function(){
		$(".ui-dialog-titlebar-close").click();
		callback();
	});
	
	$(".confirmNo").unbind('click').click(function(){
		$(".ui-dialog-titlebar-close").click();
	});
}

function stepTwo(callback) {
	confirmDialog("信息提交将不能再修改，您确定现在提交？");
	$(".confirmYes").unbind('click').click(function(){
		$(".ui-dialog-titlebar-close").click();
		callback();
	});
	
	$(".confirmNo").unbind('click').click(function(){
		$(".ui-dialog-titlebar-close").click();
	});
}

function stepThree(callback) {
	confirmDialog("信息提交将不能再修改，您确定现在提交？");
	$(".confirmYes").unbind('click').click(function(){
		$(".ui-dialog-titlebar-close").click();
		callback();
	});
	
	$(".confirmNo").unbind('click').click(function(){
		$(".ui-dialog-titlebar-close").click();
	});
}

function reStepOne() {
	if (confirmDialog("确定返回？")) {
		$("#js_step_one").show();
		$("#js_step_two").hide();
		$("#js_step_listone").addClass("active-step");
		$("#js_step_listtwo").removeClass("active-step");
	}
}

//提示框
function confirmDialog(text){
	$(".confirmDialog").dialog("open").html(
			'<div class="confirmContent textCenter">'+text+'</div>'+
		    '<div class="confirmBtn textCenter borderTop">'+
		        '<span class="confirmNo btn-border-default-m mr25">取消</span>'+
		        '<span class="confirmYes btn-primary-m">确认</span>'+
		    '</div>'
	 );
    $('.confirmNo').unbind('click').click(function(){ //取消
        $(".ui-dialog-titlebar-close").click();
    });

    $('.confirmYes').unbind('click').click(function(){ //确认
        $(".ui-dialog-titlebar-close").click();
    });
}

//提示框
function alertDialog(text){
	$(".confirmDialog").dialog("open").html(
			'<div class="confirmContent textCenter">'+text+'</div>'+
		    '<div class="confirmBtn textCenter borderTop">'+
		        '<span class="confirmNo btn-border-default-m">我知道了</span>'+
		    '</div>'
	 );
	
	$(".confirmNo").unbind('click').click(function(){
		$(".ui-dialog-titlebar-close").click();
	});
}

//检测表单
function checkForm( roleType, callback ){
	var userRoleType = roleType;  //person , enterprise
	var user_id=$("#USER_ID").val();
    var user_role=$("#USER_ROLE").val();
    var personInfo = {
      "type":userRoleType,
  };
	requestServer({
        requestURL:gfALLDATA("userInfoHref")+'/auth-check',
        requestType: 'GET',
        requestData:personInfo,
        //async: 'false',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
            	hideLoading();
            	callback(true, desc);
            }else{
            	hideLoading();
            	callback(false, desc);
            } 
            
        }
    });
}

//异步上传并实时显示上传图片缩略图
function uploadPicAndDisplay( $idCard, url,imgData){
	
	var name = $idCard.attr("name");
	var elem = 'fileList' + name;
	$idCard.find('.js_uploadview').html('<div id="'+ elem +'"></div>');
	
	ajaxImageUpload( $idCard, elem ,url ,imgData);
}

//字段空判断
function filedIsEmpty(str){
	if( str!=null && str!="null" && str!=' ' ){
		return true;
	}else{
		return false;
	}
}

//校验
function Validator(){
	var _this = this;
	
	//校验结果的显示处理——展示给用户的界面效果
	appendHtml = function(elem, flag, text){
		if(elem.next().hasClass("errorClass")){
			if(flag){
				elem.next().html('<span class="errorClass"><i class="icon-checkmark" style="color:#39b94e;"></i></span>');
			}else{
				elem.next().html( '<span class="errorClass">'+ text +'</span>');
			}
			
		}else{
			elem.after('<span class="errorClass"></span>');
			if(flag){
				elem.next().html('<span class="errorClass"><i class="icon-checkmark" style="color:#39b94e;"></i></span>');
			}else{
				elem.next().html( '<span class="errorClass">'+ text +'</span>');
			}
		}
		
	}
	
	_this.validName =function($obj){ 	//用户名
		var reg = /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{1,10}$/;
		var value = $obj.val();
		var pass = false;
		var tip = "";
		
		if( value == ""){
			pass = true;
		}else if(reg.test(value)){
			pass = true;
		}else{
			tip = "（1）长度1~10位；（2）只能包含汉字、字母、数字或下划线；（3）下划线不能开头或结尾";
			pass = false;
		}
		
		appendHtml($obj, pass, tip);
        return pass;
	}
	
	_this.validPhone = function($obj){	//手机号码
		var reg =/^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
		var value = $obj.val();
		var pass = false;
		var tip = "";
		
		if( value == "" ){
			tip = "请输入手机号码";
			pass = false;
			
		}else if( reg.test(value) ){
			pass = true;
			
		}else{
			tip = "请输入合法的11位手机号码";
			pass = false;
		}
		
		appendHtml($obj, pass, tip);
        return pass;
	}
	
	_this.code = function( $obj , $tip){	//校验码
		
		if( $obj.val() == '' ){
			$tip.html('请输入验证码') ;
			return false;
		}else{
			$tip.html('');
			return true;
		}
	}
	
	_this.validPassword = function($obj){	//密码
		var minMaxLength = /^[\S]{6,50}$/,
        		upper = /^[A-Z]+$/,
        		lower = /^[a-z]+$/,
        		lowerUpper = /^[a-zA-Z]+$/,
        		number = /^[0-9]+$/,
        		special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;
		
		var value = $obj.val();
		var pass = false;
		var tip = "";
		
		if( value == ""){
			tip = "请输入密码";
			pass = false;
			
		}else if(!minMaxLength.test(value)){
			if(value.length > 50){
				tip = "密码长度不能超过50位" ;
			}else{
				tip = "密码长度至少6位不含空格的任意字符的组合" ;
			}
			pass = false;
			
		}else if(upper.test(value)){
			tip = "密码不能全为大写字母" ;
			pass = false;
			
		}else if(lower.test(value)){
			tip = "密码不能全为小写字母" ;
			pass = false;
			
		}else if(lowerUpper.test(value)){
			tip = "密码不能全为字母" ;
			pass = false;
			
		}else if(number.test(value)){
			tip = "密码不能全为数字" ;
			pass = false;
			
		}else{
			pass = true;
		}
		
		appendHtml($obj, pass, tip);
        return pass;
	}
	
	_this.confirmPassword = function($prev, $obj){	//确认密码
		 var pass = false;
		 var tip = "";
		
		 if( $prev.val() == $confirm.val() ){
			 pass = true;
		 }else{
			 tip = "密码确认错误" ;
			 pass =  false;
		 }
		 
		 appendHtml($obj, pass, tip);
	     return pass;
	}
	
	_this.realName = function($obj){	//真实姓名
		var str1 = /^[\u4e00-\u9fa5]{1,10}$/;  
        var str2 = /^[A-Za-z]+$/;
        var pass = false;
        var tip = "";
        var value = $obj.val();
        
        if( str1.test(value) ){
        	pass = true;
        	
        }else if( str2.test(value) ){
        	pass = true;
        	
        }else if(!value){
        	 tip = "请输入姓名" ;
        	 pass = false;
        }else{
        	 tip = "姓名格式书写错误" ;
        	 pass = false;
        }
        
        appendHtml($obj, pass, tip);
        return pass;
	}
	
	_this.identityCard = function($obj){	//身份证号码
		 var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
         var tip = "";
         var pass= true;
         var code = $obj.val();
         
         if(!code){
             tip = "请输入身份证号";
             pass = false;
         }else if(!/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
        	 tip = "身份证号格式错误";
             pass = false;
         }else if(!city[code.substr(0,2)]){
             tip = "地址编码错误";
             pass = false;
         } else{
             //18位身份证需要验证最后一位校验位
             if(code.length == 18){
                 code = code.split('');
                 //∑(ai×Wi)(mod 11)
                 //加权因子
                 var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
                 //校验位
                 var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
                 var sum = 0;
                 var ai = 0;
                 var wi = 0;
                 for (var i = 0; i < 17; i++)
                 {
                     ai = code[i];
                     wi = factor[i];
                     sum += ai * wi;
                 }
                 var last = parity[sum % 11];
                 if(parity[sum % 11] != code[17]){
                     tip = "校验位错误";
                     pass =false;
                 }
             }
         }
         
        appendHtml($obj, pass, tip);
        return pass;
	}
	
	_this.orgName = function($obj){	//企业名称
		var str1 = /^[\u4e00-\u9fa5]{1,50}$/;  
        var str2 = /^[A-Za-z]+$/;
        var pass = false;
        var tip = "";
        var value = $obj.val();
        
        if( str1.test(value) ){
        	pass = true;
        	
        }else if( str2.test(value) ){
        	pass = true;
        	
        }else if(!value){
        	 tip = "请输入名称" ;
        	 pass = false;
        }else{
        	 tip = "名称格式书写错误" ;
        	 pass = false;
        }
        
        appendHtml($obj, pass, tip);
        return pass;
	}
	
	_this.registCode = function($obj){
			var pass = false;
			var tip = "";
			var busCode = $obj.val();
			
			if(busCode.length==15){
				    var sum=0;
	                var s=[];
	                var p=[];
	                var a=[];
	                var m=10;
	                 p[0]=m;
				    for(var i=0;i<busCode.length;i++){
				       a[i]=parseInt(busCode.substring(i,i+1),m);
		                       s[i]=(p[i]%(m+1))+a[i];
		                       if(0==s[i]%m){
		                         p[i+1]=10*2;
		                       }else{
		                         p[i+1]=(s[i]%m)*2;
		                        }    
				    }                                       
				    if(1==(s[14]%m)){
		                //tip = "营业执照编号正确!";
				        pass = true;
				        
				    }else{
				        tip = "企业注册号错误" ;
		                pass = false;
		                
		             }
			  }else if(busCode == ""){
				  tip = "企业注册号不能为空" ; 
				  pass = false;
				  
			  }else{
				  tip = "注册号长度为15位";
				  pass = false;
			  }
			 appendHtml($obj, pass, tip);
	         return pass;
		}
	
	//组织机构代码校验
	_this.orgCode = function($obj){
           var pass = false;
           var tip = "";
           var orgCode = $obj.val();
           var codeVal = ["0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
           var intVal = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35];
           var crcs =[3,7,9,10,5,8,4,2];
           
           if(!(""==orgCode) && orgCode.length==10){
              var sum=0;
              for(var i=0;i<8;i++){
                 var codeI=orgCode.substring(i,i+1);
                 var valI=-1;
                 for(var j=0;j<codeVal.length;j++){
                     if(codeI==codeVal[j]){
                        valI=intVal[j];
                        break;
                     }
                 }
                 sum+=valI*crcs[i];
              }
              var crc=11- (sum%11);
                       
              switch (crc){
                   case 10:{
                       crc="X";
                       break;
                   }default:{
                       break;
                   }
               }
             
              //最后位验证码正确
              if(crc==orgCode.substring(9)){
                   pass = true;
                 
              }else{
                   pass = false;
                   tip = "组织机构代码不正确！" ;
                 
               }

           }else if(""==orgCode){
                pass = false;
                tip = "组织机构代码不能为空!" ;

           }else{
                pass = false;
                tip = "组织机构代码格式不正确" ;	//组织机构代码格式不正确，组织机构代码为8位数字或者拉丁字母+“-”+1位校验码，并且字母必须大写！
            }
           
           appendHtml($obj, pass, tip);
           return pass;
        }
	
	//银行卡校验
	_this.bankCode = function($obj){
		var pass = false;
		var tip = "";
		var bankno = $obj.val();
		var lastNum=bankno.substr(bankno.length-1,1);//取出最后一位（与luhm进行比较）  
		   
	    var first15Num=bankno.substr(0,bankno.length-1);//前15或18位  
	    var newArr=new Array();  
	    for(var i=first15Num.length-1;i>-1;i--){    //前15或18位倒序存进数组  
	        newArr.push(first15Num.substr(i,1));  
	    }  
	    var arrJiShu=new Array();  //奇数位*2的积 <9  
	    var arrJiShu2=new Array(); //奇数位*2的积 >9  
	       
	    var arrOuShu=new Array();  //偶数位数组  
	    for(var j=0;j<newArr.length;j++){  
	        if((j+1)%2==1){//奇数位  
	            if(parseInt(newArr[j])*2<9)  
	            arrJiShu.push(parseInt(newArr[j])*2);  
	            else  
	            arrJiShu2.push(parseInt(newArr[j])*2);  
	        }  
	        else //偶数位  
	        arrOuShu.push(newArr[j]);  
	    }  
	       
	    var jishu_child1=new Array();//奇数位*2 >9 的分割之后的数组个位数  
	    var jishu_child2=new Array();//奇数位*2 >9 的分割之后的数组十位数  
	    for(var h=0;h<arrJiShu2.length;h++){  
	        jishu_child1.push(parseInt(arrJiShu2[h])%10);  
	        jishu_child2.push(parseInt(arrJiShu2[h])/10);  
	    }          
	       
	    var sumJiShu=0; //奇数位*2 < 9 的数组之和  
	    var sumOuShu=0; //偶数位数组之和  
	    var sumJiShuChild1=0; //奇数位*2 >9 的分割之后的数组个位数之和  
	    var sumJiShuChild2=0; //奇数位*2 >9 的分割之后的数组十位数之和  
	    var sumTotal=0;  
	    for(var m=0;m<arrJiShu.length;m++){  
	        sumJiShu=sumJiShu+parseInt(arrJiShu[m]);  
	    }  
	       
	    for(var n=0;n<arrOuShu.length;n++){  
	        sumOuShu=sumOuShu+parseInt(arrOuShu[n]);  
	    }  
	       
	    for(var p=0;p<jishu_child1.length;p++){  
	        sumJiShuChild1=sumJiShuChild1+parseInt(jishu_child1[p]);  
	        sumJiShuChild2=sumJiShuChild2+parseInt(jishu_child2[p]);  
	    }        
	    //计算总和  
	    sumTotal=parseInt(sumJiShu)+parseInt(sumOuShu)+parseInt(sumJiShuChild1)+parseInt(sumJiShuChild2);  
	       
	    //计算Luhm值  
	    var k= parseInt(sumTotal)%10==0?10:parseInt(sumTotal)%10;          
	    var luhm= 10-k;  
	       
	    if(lastNum==luhm && lastNum.length != 0){  
	       //Luhm验证通过且不为空 
	       tip = "";
	       pass = true;
	       
	    }else{   
	       //不符合Luhm校验
	       tip = "银行卡号不正确";
	       pass = false;  
	    }    
	    appendHtml($obj, pass, tip);
	    return pass;
	}
	
}

function verifyPhoneCode(type, activateCode, jsonData,token, $tip, callback){
	$.ajax({
		url : gfALLDATA("publicHref")+'/phone/verify',
		type : 'post',
		async: 'false',
		dataType : 'json',
		contentType: 'application/json',
		data: JSON.stringify( jsonData ),
		success : function(data) {
			var code = data.resultCode;
			var desc = data.resultDesc;
			
			if(code == 0){
				$tip.html("");
				callback(true);
			}else{
				$tip.html(desc);
				callback(false);
			}
		},
		error : function() {
			callback(false);
			confirm("对不起，网络不给力，连接失败！");
		}
	});
}

function getPhoneActivate(type,jsonData, $getInput, $showError){
	$.ajax({
		url : gfALLDATA("publicHref")+'/phone/send',
		type : 'POST',
		dataType : 'json',
		contentType: 'application/json',
		async: 'false',
		data: JSON.stringify( jsonData ),
		beforeSend:function(){
			$(".js_telcode_btn").addClass("disabled");
			$(".js_telcode_btn").html("正在获取验证码");
		},
		success : function(data) {
			var code = data.resultCode;
			var desc = data.resultDesc;
			var token = data.token;
			
			if(code == 0){
				$getInput.data('data', token).prop("disabled",false);
				$showError.text("").data("limit", false);
				loadTime(60);
				return true;
			}else if(code == 1) {
				$showError.text(desc).data("limit", true);
				$(".js_telcode_btn").html("获取验证码");
				$(".js_telcode_text").text('验证码获取次数超过限制，请更换手机再试或明天再试');
			}else{
				$showError.text("系统繁忙");
			}
			$getInput.data('data', token).prop("disabled",true);
		},
		error : function() {
			alert("对不起，网络不给力，连接失败！");
		}
	});
}

function loadTime(secs){
	for(var i=secs;i>=0;i--) { 
	  window.setTimeout('doUpdateTime(' + i + ')', (secs-i) * 1000); 
	} 
}
function doUpdateTime(num) { 
	$(".js_telcode_btn").html("重新获取（"+num+"）");
	
	if(num == 0) { 
		$(".js_telcode_btn").html("重新获取");
		$(".js_telcode_btn").removeClass("disabled");
		$(".js_telcode_text").data("limit", true);
	}
}

//上传图像相关
function ajaxImageUpload(fireUploadZoneId,onId,url,imgData){
  return ajaxUpload(fireUploadZoneId,onId,url,imgData,'image/*');
}

//进行文件上传
function ajaxUpload(fireUploadZoneId,onId,url,imgData,types){
	//动态加载css
	var $onId = $("#" +onId);
	$("<link>")
  .attr({ rel: "stylesheet",
      type: "text/css",
      href: "/static/system/css/uploader.css"
  }).appendTo("head");
	
	//动态加载js
	$.getScript("/static/common/js/jquery-dmuploader/dmuploader.min.js",function(){
		fireUploadZoneId.dmUploader({
	        url: url,
	        dataType: 'json',
	        method:'post',
	        allowedTypes: types,
	        fileName:'file',
	        /*data: JSON.stringify( imgData ),*/
	        /*extFilter: 'jpg;png;gif',*/
	          //初始化事件
	        onInit: function(){
	        },
	        //选择新文件后的事件
	        onNewFile: function(id, file){
	        	addFile(onId, id, file);
	        },
	        //上传之前事件
	        onBeforeUpload: function(id){
	         updateFileStatus(onId, id,"uploading","上传中...");
	        },
	        //所有待上传文件发送完成后的事件
	        onComplete: function(){
	        },
	        //上传处理中的事件
	        onUploadProgress: function(id, percent){
	          var percentStr = percent + '%';
	          updateFileProgress(onId, id, percentStr);
	        },
	        //上传成功后的事件
	        onUploadSuccess: function(id, data){
	         
	          
	        updateFileProgress(onId, id, '100%');
	          
	            if(data.resultCode == 0){
	        	    updateFileStatus(onId, id, 'success', '上传成功');
	        	    var uri=data.resultData[0].resultData.uri;
	        	    $.get(uri,function(data,status){
	        	    	$onId.append('<img class="set-upload-viewImg" src='+uri +'>');
	        	    });
	            }else{
	        	    var desc = data.resultDesc;
	        	    updateFileStatus(onId, id, 'error', desc);
	            }
	        },
	        //上传失败后的事件
	        onUploadError: function(id, message){
	          
	        	updateFileStatus(onId, id, 'error', message);
	        },
	        //选择上传文件的类型错误事件
	        onFileTypeError: function(file){
	          
	        },
	        //选择上传文件的尺寸错误事件
	        onFileSizeError: function(file){
	         
	        },
	        /*onFileExtError: function(file){
	          $.danidemo.addLog('#demo-debug', 'error', 'File \'' + file.name + '\' has a Not Allowed Extension');
	        },*/
	        //浏览器不支持该插件上传文件的事件
	        onFallbackMode: function(message){
	          alert('Browser not supported(do something else here!): ' + message);
	        }
	      });
	});
}

//更新文件状态
function updateFileStatus(onId, addFileOrder, status, message){
	  $('#'+ onId + addFileOrder).find('.set-upload-fileStatus').html(message).addClass(status);
	  
	  if( status == "success"){
		  $("#" + onId).data("flag", true);
		  $('#'+ onId + addFileOrder).find('.set-reUpload-fileInfo').show();

	  }else if( status == "error" ){
		  $("#" + onId).parent().siblings('.set-upload-title').text('重新上传');
		  $("#" + onId).data("flag", false);
		  $('#'+ onId + addFileOrder).find(".set-upload-back").hide();
		  $('#'+ onId + addFileOrder).find('.set-reUpload-fileInfo').hide();
	  }else{
		  $("#" + onId).data("flag", false);
		  $('#'+ onId + addFileOrder).find(".set-upload-back").hide();
		  $('#'+ onId + addFileOrder).find('.set-reUpload-fileInfo').hide();
	  }
}

//更新上传进度
function updateFileProgress(onId, addFileOrder, percent){
	$('#'+ onId + addFileOrder).find('.set-upload-progress').width(percent);
}

//添加上传文件
function addFile(onId,addFileOrder, file){
var template = '' +
  '<div class="set-upload-fileContent" id="' + onId + addFileOrder + '">' +
    '<div class="set-upload-fileInfo">' +
      '<p title="Size: ' + file.size + 'bytes - Mimetype: ' + file.type + '"><span class="set-upload-fileStatus">等待上传</span>' +
      '<span id="back' + onId + addFileOrder + '" class="set-reUpload-fileInfo uploading hidden">重新上传</span></p>'+
    '</div>' +
    '<div class="set-upload-bar">' +
      '<div class="set-upload-progress" style="width:0%"></div>' +
    '</div>' +
  '</div>' ;
 // '<div id="back' + onId + addFileOrder + '" class="set-upload-back">重传</div>';
  
  $("#" + onId).html(template);
  $("#back"+onId + addFileOrder).click(function(){
	  $("#" + onId).html('');
	  return false;
  });
}

/* 加密密码 */
function getEncryptPassword(realPasswordInputVal) {
	return SparkMD5.hash(realPasswordInputVal);
}