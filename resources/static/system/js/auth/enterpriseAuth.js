var GLOBLEOBJ = {
		"bankName": ""
};
$(document).ready(function(){
	 //初始化企业用户认证表单元素
	 checkEnterAuthState();
	 
	 //企业认证第一步
	 enterpriseAuthFirst();
	 //firstTest();
	 
	 //企业认证第二步
	 enterpriseAuthTwo();
	 //enterpriseAuthTwoTest();
	 
	 //企业认证第三步-填写银行卡
	 // enterpriseAuthThree();
});

//测试
function firstTest(){
	$("#js_enterstep_onebtn").unbind('click').click(function(){
		 stepOne(function(){
			 $("#js_enterForm_one").hide();
			 $("#js_enterForm_two").show();
			 $("#js_step_listtwo").addClass("active-step");
			 $("#js_step_linetwo").addClass("active-line");
			 $("#js_step_linethree").addClass("active-line");
			 GLOBLEOBJ.bankName =  jsonData.legalPersonName;
		 });
	});
}
function enterpriseAuthTwoTest(){
	$("#js_enterstep_twobtn").unbind('click').click(function(){
		 stepTwo(function(){
			 $("#js_enterForm_two").hide();
			 $("#js_enterForm_three").show();
			 $("#js_step_linethree").addClass("active-line");
			 //开户名
			$("#js_enter_bankName").attr("value", GLOBLEOBJ.bankName);
			$(".js_bankName_desc").text('易企签将会向以"'+GLOBLEOBJ.bankName+'"的开户名的对公账户中汇入一笔确认资金，若公司名称和账户开户姓名不一致，确认资金将汇款失败');
		 });
	});
}

//查询用户是否已认证
function checkState(callback){
  var personInfo={
    "fields":"state"
  };
  requestServer({
    requestURL:gfALLDATA("userInfoHref")+"/auth",
    requestType:'GET',
    resultData:personInfo,
    beforeCallback:function(){
      showLoading();
    },
    successCallback:function(data){
      var code = data.resultCode;
      var resultData = data.resultData;
      if (code==0) {
        hideLoading();
        //检测认证表单是否填写完成
        callback(resultData.state);
      }else{
        hideLoading();
        //检测认证表单是否填写完成	暂时解决后台接口中无认证资料仍返回为1的bug,返回1表示尚未认证
        callback(resultData.state);
        return false;
      }
    }
  })
}

//根据认证状态展示
function checkEnterAuthState(data){
	checkState(function(state){
    if (state==2||state==1) {
      //进度条100%
        $("#js_step_listtwo").addClass("active-step");
        $("#js_step_linetwo").addClass("active-line");
        $("#js_step_linethree").addClass("active-line");
        $("#js_step_listfour").addClass("active-step");
        $("#js_step_linefour").addClass("active-line");

        $("#js_route_name").text("实名认证");
        $("#js_route_desc").html(
        	'<span id="js_route_desc" class="fs12 plr10">*认证通过，<span class="free">免费</span>颁发专有证书</span>'
        );
        $("#js_step_twoItem").text("详细信息验证");
        $("#js_enter_prov").show();//显示个人认证
        $("#js_enter_prov_tab").hide();//隐藏认证选择
        if (state==2) {
			$("#js_enterForm_four").html(
				'<p class="step-final-title">资料正在审核中，请您耐心等待！</p>'
		    	+'<p>我们将在1-2个工作日完成审核,并将结果以短信或邮件的方式通知到您。</p>'
		    	+'<p class="mt60"><a class="return-link-css formItemBtn nextBtn" href="'
		    	+gfALLDATA("alipayHref")
		    	+'/my-info.html">返回我的账户</a></p>'
			).show();
		}else{
			$("#js_enterForm_four").html(
				'<p class="step-final-title">恭喜您，您已完成实名认证！</p>'
				+'<p>易企签已为您颁发专有证书，<a class="successClass" href="'
          		+gfALLDATA("certHref")
          		+'/details.html">点击查看</a></p>'
		    	+'<p class="mt60"><a class="return-link-css formItemBtn nextBtn" href="'
		    	+gfALLDATA("alipayHref")
		    	+'/my-info.html">返回我的账户</a></p>'
			).show();
		}
    }else if (state==3) {
    	$("#js_route_desc").html(
        	'<span id="js_route_desc" class="fs12 plr10 errorClass">*认证未通过，请重新认证！</span>'
        );
    	$("#js_enterForm_one").show();
        return;
    }else{
    	$("#js_enterForm_one").show();
        return;
    }
  })
}

//银行验证的状态处理-待完善和测试2016-1-28
function authenStatusUi( flagStutas ){
	var stutas1 = '<p class="step-final-title">提交成功！</p>'
		    			+'<p class="mt10">我们将在1-2个工作日完成审核，并将结果以短信或邮件的方式通知到您。</p>'
		    			+'<p class="mt30"><a class="return-link-css" href="user/sys/document">返回主页</a></p>' ;
	
	var stutas2 = '<p class="step-final-title">提交成功！</p>'
						+'<p class="mt10">易企签将在三个工作日给你的对公账号汇入一笔1元以下的金额，请耐心等待。</p>'
						+'<p class="mt30"><a class="return-link-css" href="user/sys/document">返回主页</a></p>' ;
	
	var stutas3 = '<p class="step-final-title">验证</p>'
						+'<p class="mt10">易企签已向“工商银行”（尾号4566）汇入一笔一元以下金额，请确认。</p>'
						+'<div class="formItem">'
							+'<span>收到金额</span>'
							+'<input id="js_enter_bankCode" name="" value="" type="text" />'
							+'<span class="bankCode_css">元</span>'
						+'</div>'
						+'<div><span  id="js_enterfinal_confirmBank" class="formItemBtn mt10">确认</span></div>' ;
	
	var stutas4 = '<p class="step-final-title">审核通过</p>'
						+'<p class="mt10">恭喜您，实名认证审核已通过，已为您颁发证书。</p>'
						+'<div class="mt30">'
							+'<a href="/user/sys/account/combo" class="formItemBtn">购买套餐</a>'
							+'<a href="user/sys/document" class="formItemBtn">返回文档中心</a>'
						+'</div>' ;
	
	//根据返回情况依次加载状态
	$("#js_enterForm_four").html();
}

//企业认证第一步
function enterpriseAuthFirst(){
	var validator = new Validator();
	var enterFlag = {
			"flag": false,
			"name": false,
			"registCode": false,
			"phone": false,
      		"code": false,
      		"codeResult": false
	};
	
	//企业名称
	$("#js_enter_name").focusout(function(){	
		enterFlag.flag = enterFlag.name = validator.orgName( $(this) );
	});
	
	//企业注册号
	$("#js_enter_registCode").focusout(function(){
		enterFlag.flag = enterFlag.registCode = validator.registCode( $(this) );
	});
	
	//联系人手机号码
	$("#js_enter_linkPhone").focusout(function(){
		enterFlag.flag = enterFlag.phone = validator.validPhone($(this));
		 //获取验证码按钮控制
    	if( $(".js_telcode_text").data("limit") ){
	        $("#js_person_getCodebtn").removeClass("disabled");
	        $(".js_telcode_text").removeData("limit");
	        $("#js_person_code").prop("disabled",false);
	    }
	});

	//输入验证码
  	$("#js_person_code").focusout(function(){
	    var activateCode = $("#js_person_code").val();
	    var phoneNumber = $("#js_enter_linkPhone").val();
	    var token = $("#js_person_code").data("data");
	    var $tip = $(".js_code_desc");
	    enterFlag.code = validator.code($(this), $tip);
	    var jsonData={
	      "data":activateCode,
	      "type":"code",
	      "sendDest":phoneNumber
	    }
	    if( enterFlag.code ){
	        if(enterFlag.codeResult){
	        	return;
	        }else{
	        	verifyPhoneCode("person",activateCode, jsonData, token, $tip, function(dataResult){
		         	enterFlag.codeResult = dataResult;
		        	if(dataResult){
		            	$("#js_step_onebtn").removeClass("disabled");
		            }else{
		            	$("#js_step_onebtn").addClass("disabled");
		            }
	        	});
	      	}
	    }else{
	      return false;
	    }
	});
  
  //获取验证码
  	$("#js_person_getCodebtn").unbind('click').click(function(){
	    setTimeout(function(){
	      	if($("#js_person_getCodebtn").hasClass("disabled")){
	        	return false;
	      	}else{
	        	if(enterFlag.phone){
	        		//清除验证码输入框中的信息与错误提示信息
	      		    $("#js_person_code").val("");
	      		    $(".js_code_desc").text("");
	      		    enterFlag.codeResult=false;
		          	var phoneNumber = $("#js_enter_linkPhone").val();
			        var $getInput = $("#js_person_code");
			        var $showError = $(this).next();
			        var jsonData={
			            "sendDest":phoneNumber,
			            "type":"1000"
			        }
			        getPhoneActivate("person",jsonData, $getInput, $showError);
		        }else{
		          return false;
		        }
	      	}
	    },700);
	});
	 
	 //提交
	 $("#js_enterstep_onebtn").unbind('click').click(function(){
		 var jsonData = {};
		 if( enterFlag.flag&&enterFlag.name&&enterFlag.registCode&&enterFlag.phone&&enterFlag.codeResult ){
			 var name = $("#js_enter_name").val();
			 var registCode = $("#js_enter_registCode").val();
			 var phone = $("#js_enter_linkPhone").val();
			 jsonData = {
					"name": name,
					"number":registCode,
					"phone": phone,
					"type": "enterprise-prove"
			};			 
		 }else{
			 return false;
		 }
		 stepOne(function(){
			 reqEnterpriseFirst(jsonData, function(result, text){
				 if(result){
					// checkForm( "enterprise-prove", function(checkResult, checkDesc){
					//	 if(checkResult){
							$("#js_enterForm_one").hide();
							$("#js_enterForm_two").show();
							$("#js_step_listtwo").addClass("active-step");
							$("#js_step_linetwo").addClass("active-line");
							$("#js_step_linethree").addClass("active-line");
							GLOBLEOBJ.bankName =  jsonData.legalPersonName;
						// }else{
						//	alertDialog("对不起，提交失败，请重新提交");
						// }
					 //});
					
				 }else{
					 alertDialog("对不起，信息有误，请重新填写提交");
					 return false;
				 }
			 });
		 });
		 
	 });
}

//企业认证第二步
function enterpriseAuthTwo(){
 	var user_id=$("#USER_ID").val();
  	var user_role=$("#USER_ROLE").val();
  	jsonData={
  		"type":"enterprise-prove",
  		"imgCode":"2000"
  	};
	 //营业执照
	 uploadPicAndDisplay($("#js_enter_id_registCode"), "/wesign/"+user_role+"/users/"+user_id+"/auth-pics?type=enterprise-prove&&imgCode=2000",jsonData);
	 
	 $("#js_enterstep_twobtn").unbind('click').click(function(){
		 var jsonData = {};
		 var registPic = $( '#fileList' + $("#js_enter_id_registCode").attr("name") ).data("flag");
		 var hasRegistPic = $( '#fileList' + $("#js_enter_id_registCode").attr("name") ).find('img').length;
		 
		 if( registPic == true && hasRegistPic){
			 jsonData = {
					 "legalPersonIdCardType": 0
			 }
		 }else{
			 alertDialog("对不起，您还有未填写的信息！");
			 return false;
		 }
		 stepTwo(function(){
			// reqEnterpriseFirst(jsonData, function(result, text){
			checkForm( "enterprise-prove", function(checkResult, checkDesc){
				if(checkResult){
					$("#js_enterForm_two").hide();
					$("#js_enterForm_four").html(
						'<p class="step-final-title">提交成功！</p>'
				    	+'<p>我们将在1-2个工作日完成审核,并将结果以短信或邮件的方式通知到您。</p>'
				    	+'<p class="mt60"><a class="return-link-css formItemBtn nextBtn" href="'
				    	+gfALLDATA("alipayHref")
				    	+'/my-info.html">返回我的账户</a></p>'
					).show();
					$("#js_step_listfour").addClass("active-step");
					$("#js_step_linefour").addClass("active-line");
					}else{
					 alertDialog("对不起，信息有误，请重新填写提交");
					 return false;
				 }
			 });
		 });
		 
	 });
}

//企业认证第三步
function enterpriseAuthThree(){
	var threeValidator = new Validator();
	var flagbankId = false; 

	//银行卡
	$("#js_enter_bankCode").focusout(function(){
		 flagbankId = threeValidator.bankCode($(this));
	});
	
	$("#js_enterstep_threebtn").unbind('click').click(function(){
		var jsonData = {};
		var bankCard = $("#js_enter_bankCode").val();
		if( flagbankId ){
			jsonData = {
					"bankCard": bankCard
			}
			
		}else{
			 alertDialog("对不起，您还有未填写的信息！");
			 return false;
		}
		
		//符合要求执行提交
		stepThree(function(){
			 reqEnterpriseFirst(jsonData, function(result, text){
				 if(result){
					 $("#js_enterForm_three").hide();
					 $("#js_enterForm_four").html(
							 	'<p class="step-final-title">提交成功！</p>'
				    			+'<p class="mt10">我们将在1-2个工作日完成审核，并将结果以短信或邮件的方式通知到您。</p>'
				    			+'<p class="mt30"><a class="return-link-css" href="'
				    			+gfALLDATA("alipayHref")
				    			+'/index.html">返回我的账户</a></p>'
							 ).show();
				 }else{
					 alertDialog("对不起，信息有误，请重新填写提交");
					 return false;
				 }
			 });
		 });
	});
}

//提交企业实名认证数据
function reqEnterpriseFirst(jsonData, callback){	
    var user_id=$("#USER_ID").val();
    var user_role=$("#USER_ROLE").val();
	requestServer({
        requestURL: gfALLDATA("userInfoHref")+'/auth',
        requestType: 'POST',
        requestData: JSON.stringify( jsonData ),
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