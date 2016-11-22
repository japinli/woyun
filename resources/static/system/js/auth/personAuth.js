$(document).ready(function(){
 /* var roleCode = $("#_user-role-code").val();*/
  var $objType = $(".vertify-type");
  autoAuthen( $objType, "set", "1");
   //检测是否已认证
   userState();
   
   //银行卡验证
   $("#js_bankCard_vertify").unbind('click').click(function(){
     var _this = this;
     if( $(_this).hasClass("disabled") ){
       $(_this).removeClass("disabled").siblings(".formItemBtn").addClass("disabled");
       $(".js_step_twoItem").text("银行卡验证");
       
       //设置验证类型为自动 yes
       autoAuthen( $objType, "set", "1");
       //更换表单
      $("#js_step_one .formItem:lt(2)").show();
     }
   });
   
   //人工审核
   $("#js_artificial_vertify").unbind('click').click(function(){
     var _this = this;
     if( $(_this).hasClass("disabled") ){
       $(_this).removeClass("disabled").siblings(".formItemBtn").addClass("disabled");
       $(".js_step_twoItem").text("上传证件照");
       
       //设置验证类型为人工 no
       autoAuthen( $objType, "set", "2");
       //更换表单
      $("#js_step_one .formItem:lt(2)").show();
       
     }
   });
   
   //支付宝验证
   $("#js_alipay_vertify").unbind('click').click(function(){
    var _this = this;
    if( $(_this).hasClass("disabled") ){
       $(_this).removeClass("disabled").siblings(".formItemBtn").addClass("disabled");
       $(".js_step_twoItem").text("支付宝验证");
       
      //设置验证类型为自动 yes
      autoAuthen( $objType, "set", "3");

      //更换表单
      $("#js_step_one .formItem:lt(2)").hide();
    }
   });
   
});

// 设置获取验证类型处理函数
function autoAuthen( $obj, action, value){
  /*value==1:银行卡验证  value==2：人工审核  value==3：支付宝验证 */
  if( action === "set" ){ //设置值
    $obj.attr("name", value);
    
  }else if( action === "get" ){ //获取值
    return $obj.attr("name");
  }
}

//根据认证状态展示
function userState(){
  checkState(function(state){
    if (state==2||state==1) {
      //进度条100%
      $("#js_step_listtwo").addClass("active-step");
      $("#js_step_linetwo").addClass("active-line");
      $("#js_step_linethree").addClass("active-line");
      $("#js_step_listthree").addClass("active-step");
      $("#js_step_linefour").addClass("active-line");

      $("#js_chooseType").hide(); //隐藏个人认证方式选择
      $("#js_person_prov").show();//显示个人认证
      $("#js_person_prov_tab").hide();//隐藏认证选择
      $("#js_route_name").text("实名认证");
      $("#js_route_desc").html(
        '<span id="js_route_desc" class="fs12 plr10">*认证通过，<span class="free">免费</span>颁发专有证书</span>'
        );
      $(".js_step_twoItem").text("详细信息验证");
      $("#js_step_one").hide();
      if (state==2) {
        $("#js_step_three").html(
          '<p class="step-final-title">资料正在审核中，请您耐心等待！</p>'
          +'<p>我们将在1-2个工作日完成审核,并将结果以短信或邮件的方式通知到您。</p>'
          +'<p class="mt60"><a class="return-link-css formItemBtn nextBtn" href="'
          +gfALLDATA("alipayHref")
          +'/my-info.html">返回我的账户</a></p>'
        ).show();
      }else{
        $("#js_step_three").html(
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
      checkPersonAuthState();
    }else{
      checkPersonAuthState();
    }
  })
}

//查询用户是否已认证
function checkState(callback){
  var personInfo={
    "fields":"state"
  };
  requestServer({
    requestURL:gfALLDATA("userInfoHref")+"/auth",
    requestType:'GET',
    resultData: personInfo,
    beforeCallback:function(){
      showLoading();
    },
    successCallback:function(data){
      var code = data.resultCode;
      var resultData = data.resultData;
      if (code==0) {
        hideLoading();
        callback(resultData.state);
      }else{
        hideLoading();
        callback(resultData.state);
        return false;
      }
    }
  })
}

//初始化用户信息
function initPersonAuthForm(){
  requestServer({
    requestURL:gfALLDATA("userInfoHref"),
    requestType: 'GET',
    beforeCallback: function(){
      showLoading();
    },
    successCallback: function(data){
      var code = data.resultCode;
      var desc = data.resultDesc;
      if(code == 0){
        hideLoading();
        //获取用户信息
        if(data.resultData){
          checkPersonAuthState(data.resultData);
          return;
        }else{
          hideLoading();
          return;
        }
      } 
    }
  });
}

//未认证成功时认证表单
function checkPersonAuthState(){
    $("#js_step_one").html(
        '<div class="formItem"><label for="js_person_name">请输入姓名:</label><br><input id="js_person_name" name="" value="" type="text" /></div>'+
          '<div class="formItem"><label for="js_person_idcard">请输入身份证号码:</label><br><input id="js_person_idcard" name="" value="" type="text" /></div>'+
          '<div class="formItem"><label for="js_person_phone">请输入手机号码:</label><br><input id="js_person_phone" name="" value="" type="text" />'+
          '<span id="js_phoneCheck" class="errorClass"></span></div>'+
          '<div id="js_show_code" class="formItem">'+
            '<label for="js_person_code">短信验证码:</label>'+'<br>'+
            '<input id="js_person_code" class="telcode-css" name="" value="" type="text" />'+
            '<input id="js_personcode_hidden" type="hidden"/>'+
            '<span id="js_person_getCodebtn"class="telcode-btn js_telcode_btn" type="text">获取验证码</span>'+
            '<span class="js_code_desc errorClass"></span>'+
            '<p class="js_telcode_text errorClass mt10"></p>'+
          '</div>'+
          '<div class="formItem"><span id="js_step_onebtn" class="formItemBtn mt20 nextBtn">下一步</span></div>'
    );
    personAuthFirst( false );
}

//个人认证第一步
function personAuthFirst( isCode ){
  var validator = new Validator();
  var codeFlag = isCode;
  var personFlag = {
      "name": false,
      "idcard": false,
      "phone": isCode,
      "code": false,
      "codeResult": false,
      "numberResult":false
  };
  if (codeFlag) {
    personFlag.phone=true;
    personFlag.numberResult=true;
  }
  //姓名
  $("#js_person_name").focusout(function(){
    personFlag.name = validator.realName( $(this) );
  });
  
  //身份证号
  $("#js_person_idcard").focusout(function(){
    personFlag.idcard =  validator.identityCard($(this));
  });
  
  //手机号
  $("#js_person_phone").focusout(function(){
    var valValue = $("#js_person_phone").val();
    personFlag.phone = validator.validPhone($(this));
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
    var phoneNumber = $("#js_person_phone").val();
    var token = $("#js_person_code").data("data");
    var $tip = $(".js_code_desc");
    personFlag.code = validator.code($(this), $tip);
    var jsonData={
      "data":activateCode,
      "type":"code",
      "sendDest":phoneNumber
    }
    if( personFlag.code ){
      if(personFlag.codeResult){
        return;
      }else{
        verifyPhoneCode("person",activateCode, jsonData, token, $tip, function(dataResult){
          personFlag.codeResult = dataResult;
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
        if(personFlag.phone){
          //清除验证码输入框中的信息与错误提示信息
		  $("#js_person_code").val("");
		  $(".js_code_desc").text("");
		  personFlag.codeResult=false;
			
          var phoneNumber = $("#js_person_phone").val();
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
  
  $("#js_step_onebtn").unbind('click').click(function(){    //个人认证第一步——提交
    setTimeout(function(){
      var hasClass = $("#js_step_onebtn").hasClass("disabled");
      var isBankCard = autoAuthen( $(".vertify-type"), "get");
      if( isBankCard === "1" ){ //使用银行卡验证
        if( !hasClass && personFlag.name && personFlag.idcard && personFlag.phone && personFlag.codeResult){ 
          submitFirstdataBank();
        }else{
          return false;
        } 
      }else if( isBankCard === "2" ){ //使用人工验证
        if( !hasClass && personFlag.name && personFlag.idcard && personFlag.phone && personFlag.codeResult){ 
          submitFirstdata();   
        }else{
          return false;
        }
      }else if( isBankCard === "3" ){   //使用支付宝
        if (!hasClass && personFlag.phone && personFlag.codeResult) {   
          submitFirstdataAlipay();
        }else{
          return false;
        }
      }else{
        return;
      }   
    },700);
  });  
}

//第一步提交数据处理-银行卡验证
function submitFirstdataBank(){
  var phone = $("#js_person_phone").val();
  /*updateUserInfo(phone,function(result){
    if (result) {*/
      stepOne(function(){
        $("#js_step_one").hide();
        $(".vertify-type-container").hide();
        $("#js_step_two").html(
          '<div class="formItem"><label for="js_person_bank">请输入银行卡号:</label><input id="js_person_bank" name="" value="" type="text" /></div>'+
            '<div class="formItem"><label for="js_person_bankPhone">请输入预留手机号:</label><input id="js_person_bankPhone" name="" value="" type="text" /></div>'+
            '<div><span id="js_step_twobtn" class="formItemBtn mt20 nextBtn">确认提交</span></div>'
        ).show();
        $("#js_step_listtwo").addClass("active-step");
        $("#js_step_linetwo").addClass("active-line");
        $("#js_step_linethree").addClass("active-line");
        bankAuthenUi();
      });
    /*}else{
      alertDialog("对不起，信息有误，请重新填写提交");
      return false;
    }
  })*/
}

//银行卡验证前台操作
function bankAuthenUi(){
  var name = $("#js_person_name").val();
  var idCardCode = $("#js_person_idcard").val(); 
  var phone = $("#js_person_phone").val();
  var bankValidator = new Validator();
  var flagBank = {
      "bankId": false,
      "bankPhone": false
  };
   //银行卡
  $("#js_person_bank").focusout(function(){
     flagBank.bankId = bankValidator.bankCode($(this));
  });
   //手机号
  $("#js_person_bankPhone").focusout(function(){
    flagBank.bankPhone = bankValidator.validPhone($(this));
  });   
  $("#js_step_twobtn").unbind("click").click(function(){
    var bankCard = $("#js_person_bank").val();
    var bankPhone = $("#js_person_bankPhone").val();
    var jsonData = {
      "name": name,
      "number": idCardCode,
      "phone": bankPhone,
      "type" : "person-prove",
    };
    if( flagBank.bankId && flagBank.bankPhone ){
      requestBankAuthen(jsonData, function(result, des){
        if( result ){
        }else{
          alertDialog("数据验证失败。");
          return false;
        }
        $("#js_step_two").hide();
        $("#js_step_three").html(
          '<p class="step-final-title">认证成功！</p>'
          +'<p>易企签已为您颁发专有证书，<a class="successClass" href="'
          +gfALLDATA("certHref")
          +'/details.html">点击查看</a></p>'
          +'<div class="mt60 finalStep-css">'
          +'<a href="'
            +gfALLDATA("alipayHref")
            +'/my-info.html" class="formItemBtn nextBtn">返回我的账户</a>'
          +'</div>' 
        ).show();
        $("#js_step_listthree").addClass("active-step");
        $("#js_step_linefour").addClass("active-line");
      });   
    }else{
      //alertDialog("您还未完成信息的填写，请先填写再提交！");
      return false;
    } 
  });
}

//请求银行卡验证
function requestBankAuthen(jsonData, callback){
  var bankCard = $("#js_person_bank").val();
  requestServer({
    requestURL:gfALLDATA("userInfoHref")+'/bankcard-auth?cardno='+bankCard,
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

//第一步提交数据处理
function submitFirstdata(){
  var name = $("#js_person_name").val();
  var idCardCode = $("#js_person_idcard").val(); 
  var phone = $("#js_person_phone").val();
  var jsonData = {
    "name": name,
    "number": idCardCode,
    "phone": phone,
    "type":"person-prove"
  };
  stepOne(function(){
    reqPersonFirst(jsonData, function(result, text){
      if(result){ 
            $("#js_step_one").hide();
            $(".vertify-type-container").hide();
            $("#js_step_two").html(
              '<div class="formItem2 cardradio">'
              +'<label>身份证类型：</label>'
              +'<input id="js_card_second" class="person" name="identity" value="second" checked="checked" type="radio" />二代身份证 '
              +'</div>'
              +'<div class="license-container">'
                +'<div id="js_person_id_card_1" name="person1" class="formItem sm-formItem">'
                  +'<label class="mt30">请上传身份证正面：(png/gif/jpeg)</label>'
                  +'<div class="set-upload-container">'
                    +'<input class="set-upload-btn" type="file" name="files[]"/>'
                    +'<p class="set-upload-title">选择文件或拖拽图片到本框</p>'
                    +'<div class="js_uploadview set-upload-view"> </div>'
                  +'</div>'
                +'</div>'
                +'<div class="formItem sm-formItem">'
					+'<label for="">示例：</label>'
					+'<div class="set-upload-container set-upload-sfzfont">'
					+'</div>'
				+'</div>'
                +'<div id="js_person_id_card_2" name="person2" class="formItem sm-formItem">'
                  +'<label class="floatL">请上传身份证背面：(png/gif/jpeg)</label>'
                  +'<div class="set-upload-container">'
                    +'<input class="set-upload-btn" type="file" name="files[]"/>'
                    +'<p class="set-upload-title">选择文件或拖拽图片到本框</p>'
                    +'<div class="js_uploadview set-upload-view"> </div>'
                  +'</div>'
                +'</div>'
                +'<div class="formItem sm-formItem">'
					+'<label for="">示例：</label>'
					+'<div class="set-upload-container set-upload-sfzback">'
					+'</div>'
				+'</div>'
              +'</div>'
              +'<div><span id="js_step_twobtn" class="formItemBtn mt20 nextBtn">确认提交</span></div>'
            ).show();
            $("#js_step_listtwo").addClass("active-step");
            $("#js_step_linetwo").addClass("active-line");
            $("#js_step_linethree").addClass("active-line");
            personAuthTwo();
      }else{
        alertDialog("对不起，信息有误，请重新填写提交");
        return false;
      }
    });
  });
}

//个人认证第二步
function personAuthTwo(){
  var user_id=gfALLDATA("uId");
  var user_role=gfALLDATA("uRole");
  var personFlag2 = {
    "idCardType" : 0,
    "idCardValid": false,
    "idCardValidDate": false
  };
   
   //二代身份证
  $("#js_card_second").unbind('click').click(function(){
    $("#js_person_id_card_2").show();
    personFlag2.idCardType = 0; 
  });
   
   //临时
   //ajax异步上传初始化
  person1Data={
    "type":"person-prove",
    "imgCode":"1000"
  };
  person2Data={
    "type":"person-prove",
    "imgCode":"1001"
  };
   //身份证正面
  uploadPicAndDisplay($("#js_person_id_card_1"), gfALLDATA("userInfoHref")+"/auth-pics?type=person-prove&&imgCode=1000",person1Data);
   //身份证反面
  uploadPicAndDisplay($("#js_person_id_card_2"), gfALLDATA("userInfoHref")+"/auth-pics?type=person-prove&&imgCode=1001",person2Data);
   //提交个人认证第二步
   $("#js_step_twobtn").unbind('click').click(function(){
     var  jsonData = {};
     var flagidCardPic1 = $( '#fileList' + $("#js_person_id_card_1").attr("name") ).data("flag");
     var flagidCardPic2 = $( '#fileList' + $("#js_person_id_card_2").attr("name") ).data("flag");
     var hasFlagidCardPic1 = $( '#fileList' + $("#js_person_id_card_1").attr("name") ).find('img').length;
     var hasFlagidCardPic2 = $( '#fileList' + $("#js_person_id_card_2").attr("name") ).find('img').length;
     if( personFlag2.idCardType == 0 && flagidCardPic1 == true && flagidCardPic2 == true &&hasFlagidCardPic1&&hasFlagidCardPic2/*&& personFlag2.idCardValidDate*/){
        jsonData = {
            "idCardType" : 0,
        }; 
     }else{
       return false;
     }
     
     stepTwo(function(){
      // reqPersonFirst(jsonData, function(result, text){
        // if(result){
        //检查表单项
          checkForm( "person-prove", function(checkResult, checkDesc){
             if(checkResult){
               $("#js_step_two").hide();
               $("#js_step_three").html(
                  '<p class="step-final-title">提交成功！</p>'
                    +'<p>我们正在审核,并通过短信或邮件的方式将结果通知到您。</p>'
                    +'<p class="mt60"><a class="return-link-css formItemBtn nextBtn" href="'
                      +gfALLDATA("alipayHref")
                      +'/my-info.html">返回我的账户</a></p>'
                   ).show();
               $("#js_step_listthree").addClass("active-step");
               $("#js_step_linefour").addClass("active-line");
             }else{
               alertDialog("对不起，提交失败，请重新提交");
             }
           });
/*         }else{
           alertDialog("对不起，信息有误，请重新填写提交");
           return false;
         }*/
     //  });
     });
   });
   $("#js_restep_twobtn").unbind('click').click(function(){   //个人认证返回第一步
      reStepOne();
   });
}

//提交个人实名认证数据
function reqPersonFirst(jsonData, callback){
  requestServer({
    requestURL:gfALLDATA("userInfoHref")+'/auth',
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

//第一步提交数据处理——支付宝
function submitFirstdataAlipay(){
  stepOne(function(){
        $("#js_step_one").hide();
        $(".vertify-type-container").hide();
        $("#js_step_two").html(
          '<div class="formItem"><label for="js_person_realName">请输入真实姓名:</label><input id="js_person_realName" name="" value="" type="text" /></div>'+
          '<div><span id="js_step_twobtn" class="formItemBtn mt20 nextBtn">确认提交</span></div>'
        ).show();
        $("#js_step_listtwo").addClass("active-step");
        $("#js_step_linetwo").addClass("active-line");
        $("#js_step_linethree").addClass("active-line");
        alipayAuthTwo();
  });
}

//验证个人数据——支付宝
function reqAlipayFirst(jsonData,callback){
  requestServer({
    requestURL: gfALLDATA("alipayHref")+'/person-auth/auto-mode/alipay',
    requestType: 'POST',
    requesDatatType: 'text',
    requestData: JSON.stringify(jsonData),
    beforeCallback: function(){
       showLoading();
    },
    successCallback: function(data){
      if (data) {
        hideLoading();
        callback(true,data);
      }else{
        hideLoading();
        callback(false,data);
      }
    }
  });
}

//支付宝认证第二步
function alipayAuthTwo(){
  var alipayValidator = new Validator();
  var flagAlipay = {
      "name": false,
  };
  //姓名
  $("#js_person_realName").focusout(function(){
    flagAlipay.name = alipayValidator.realName( $(this) );
  });
  $("#js_step_twobtn").unbind('click').click(function(){
    var phone = $("#js_person_phone").val();
    var personName = $("#js_person_realName").val();   //获取真实姓名
    if (flagAlipay.name) {
    var jsonData={
        "phone":phone,
        "name":personName
      }
    }else{
      return false;
    }
    stepTwo(function(){
      reqAlipayFirst(jsonData, function(result, text){
        if(result){
          $("#js_step_two").html(text);//跳转支付宝登录页面
         }else{
           alertDialog("对不起，信息有误，请重新填写提交");
           return false;
         }
      });
    });
 });
}

//Unicode编码转换
function unicode(text){
  return escape(text).replace(/%u/gi, '\\u');
}

/*************************/
//请求检查手机号是否已占用
function requestValidTel( phone , callback){
  var jsonData={
    "type":1,
    "param":"null"
  }
    requestServer({
        requestURL: gfALLDATA("baseHref")+'/wesign/'+gfALLDATA("uRole")+
        '/users/'+phone+'/check',
        requestType: 'get',
        requestData:jsonData,
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            if(code){
              callback( false );
            }else{
              callback( true );
            }
            hideLoading();
        }
    });
}

//绑定用户手机号
function updateUserInfo( phone, callback ){
  if (!$("#js_person_phone").prop("disabled")) {  //判断是否绑定手机
    var jsonData={
      "authMsg":"null",
      "newData":phone,
      "oldData":"null",
      "type":"3"
    }
    requestServer({
      requestURL: gfALLDATA("userInfoHref"),
      requestType: 'put',
      beforeCallback: function(){
         showLoading();
      },
      requestData: JSON.stringify( jsonData ),
      successCallback: function(data){
        var code = data.resultCode;
        var desc = data.resultDesc;
        if(code == 0){
          callback(true);
        }else{
          callback(false);
        } 
        hideLoading();
      }
    });
  }else{
    callback(true);
  }
}