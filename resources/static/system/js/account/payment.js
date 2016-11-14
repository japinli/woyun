$(document).ready(function(){
	confirmSelect();//获取当前用户选择的套餐信息
	showPayInfo();//展示用户信息
	payChoose();//支付方式选择事件
	$("#js_submit_needpay").unbind("click").click(function(){
		confirmInfo(false);//加载提示信息
		var payPath=$(this).attr("name");
		if (!payPath) {
			alertDialog("请先选择支付方式！");
			return false;
		}
		var money = $("#js_actuallypay").text();
		var comboId =window.location.search.substring(window.location.pathname.lastIndexOf("?")+5);
		getPayMode(payPath,money,comboId,getPayType);//选择支付方式
	});
});

//获取支付的二维码/页面
function getPayType(form){
	//如果微信的二维码已存在，删除
	if ($(".buyInfoDialog").find("img").length>0) {
		$(".buyInfoDialog").find("img").remove();
	}
	if (form.payType=="qrcode") {
		var baseUrl=$("base").attr("href");

		confirmInfo(true,'<img src="'+baseUrl+form.url+'" width="200px" />');
		$('.confirmContent').addClass("qrcodeDialog");

		/*$("#js_qrcode").removeClass("hidden").prepend('<img src="'+baseUrl+form.url+'" width="200px" />');*/
	}else if (form.payType=="page") {
		$('.confirmContent').removeClass("qrcodeDialog");
		window.open(form.url);
/*		var id=$("#js_submit_pay");	
		fOpenWind(form.url,id);*/
		returnBuyInfo(true);
	}
}

//用户付款
function getPayMode(payPath,money,comboId,callback){
	if (payPath==("alipay"||"alipay-sm")) {
		payPath="alipay";
	}
	if (payPath==("wxpay"||"alipay-sm")) {
		var payType="qrcode";
	}else{
		var payType="page";
	}
	var comboMount=$("#js_combo_month").val();
	var payComboData={
		"sysComboId":comboId,
		"period":comboMount,
		"createType":"ontime",
		"payFrom":payPath,
		"payType":payType,
		//"cost":"0"
	};	
	$.ajax({
        url: gfALLDATA("payHref")+'/combos/buy',
        async: false,
        type: 'get',
        contentType: 'application/json',
        data: payComboData,
        beforeSend: function(xhr){
            showLoading();
        },
        success: function(data, textStatus, resObj){
            if(data == 'No Access'){
                showInfo('连接超时，请重新登录');
            }
            else{
                var code = data.resultCode;
	            var desc = data.resultDesc;
	            var form = data.resultData;
	            if(code == 1){
	            	alertDialog(desc);
	            }else{
	          		$('#js_orderId').text(form.orderId);
	        		callback(form);
	            }
	         	hideLoading();
            }
        },
        error: function(jqXHR, textStatus, errorThrown){
            hideLoading();
            alertDialog("操作失败，请刷新后重新操作！");
        }
    });
}

//获取套餐详情
function getComboInfo(comboId,callback){
	requestServer({
        requestURL: gfALLDATA("payHref")+'/combos/'+comboId,
        requestType: 'GET',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
            	callback(true,data.resultData);
            }else{
            	return;
            }  
            hideLoading();
        }
    });
}

//获取当前用户选择的套餐信息
function confirmSelect(){
	var currentUser = $("#user-header-name").text();
	var comboId =window.location.search.substring(window.location.pathname.lastIndexOf("?")+5);
	getComboInfo(comboId,function(code,data){
	if(code){
		//套餐名
		var name = data.name;
		//签名次数
		if (data.withFrequency) { 
			var periodType=data.periodType;
			var frequency=data.frequency+'份';
		  }else{
		  	var frequency="不限";
		  }
		//是否周期性资费
		if (data.withPeriod==true) {
			var vaildPeriod=data.period+data.periodType;
		}else{
			var vaildPeriod=data.validPeriod;
		}
		//是否有证书
		if (data.withCert) {
			var hasCert="x.509v3数字证书";
		}else{
			var hasCert="无";
		};
		var fee=transeTwo(data.fee);
		//	var storeMemory = "存储大小："+ storeSize( storeMemoryOri); 
		    //$("#js_comboInfo > lable").eq(2).text(storeMemory);
	    $("#js_comboName").text(name);
	    $("#js_docMount").text(frequency);
	    /*$("#js_vaildPeriod").text(vaildPeriod);*/
	    $("#js_vaildTime").text(vaildPeriod);
	    $("#js_cert").text(hasCert);
	    //初始化费用信息
	    var initMount=1;
	    $("#js_actuallypay").text(fee*initMount+"元");
	    $("#js_combo_month").val(initMount);
		changeFee(fee);
		comboMount(fee);	//绑定选择套餐数量事件
	}else{
		alertDialog("不存在该套餐");
	}
});
}

//支付方式选择事件
function payChoose(){
	//默认选中支付宝支付
	$("#alipay").addClass('payChoosed');	//当前节点添加选中事件
	var payPath=$("#alipay")[0].id;	//获取支付方式名称
	$('#js_submit_needpay').attr("name",payPath);
	//绑定支付方式点击事件
	$('.payZFB').unbind('click').click(function(){
		$(this).siblings('span').removeClass('payChoosed');	//兄弟节点移除选中事件
		$(this).addClass('payChoosed');	//当前节点添加选中事件
		var payPath=$(this).context.id;	//获取支付方式名称
		$('#js_submit_needpay').attr("name",payPath);
	})
}

//获取用户信息
function getUserPayInfo(callback){
	requestServer({
        requestURL: gfALLDATA("userInfoHref"),
        requestType: 'GET',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
          
            if(code == 0){
            	callback(data.resultData);
            }else{
            	alertDialog("操作失败！");
            } 
     	hideLoading();
        }
	});
}

//展示用户信息
function showPayInfo(){
	getUserPayInfo(function(data){
		if( data ){
			var user = '';
			if( data.username ){
				user = data.username;
			}else if( data.email ){
				user = data.email;
			}else if( data.phone ){
				user = data.phone;
			}
			$("#js_account").text( user );
		}else{
			alertDialog("操作失败！");
		}
	})
}

//弹出框设置
$(".buyInfoDialog").dialog({ 
    autoOpen: false,
    width: 400,
    modal: true,
});

//确认信息
function confirmInfo(boo,contentInfo){
    $(".buyInfoDialog").dialog({autoOpen: boo});
    $('.confirmContent').html(contentInfo);

    $('.confirmNo').unbind('click').click(function(){ //取消
        $(".ui-dialog-titlebar-close").click();
    });

    $('.confirmYes').unbind('click').click(function(){ //确认
    	var orderId=$('#js_orderId').text();	//获取暂存的订单号
    	var comboId =window.location.search.substring(window.location.pathname.lastIndexOf("?")+5);
    	window.location.href=gfALLDATA("payHref")+"/pay-result.html?order-id="+orderId
    	+"&comboId="+comboId; 
        $(".ui-dialog-titlebar-close").click(); 
    });
    returnBuyInfo(false);
}

//转换数值
function transeTwo(num){
	return parseFloat(num.toFixed(2));
}

//返回购买信息页面
function returnBuyInfo(boo){
	if (boo) {
		window.onblur=function(){	//页面重新获得焦点
			var buyInfo="请在新打开的页面完成支付，<br>付款完成后再关闭此窗口。";
			confirmInfo(true,buyInfo);
		}
	}else{
		window.onblur=function(){
			return false;
		}
	}
}

//检查订单结果
function checkOrder(callback){
	var orderId=$('#js_orderId').text();	//获取暂存的订单号
	var jsonData={
		"order-id":orderId
	}
	requestServer({
		requestURL:gfALLDATA("payHref")+'/pay-result.html',
		requestType:'get',
		requestData:jsonData,
		beforeCallback:function(){
			showLoading();
		},
		successCallback:function(data){
			var code = data.resultCode;
			var result = data.hasPay;
			if (code==0) {
				callback(true,result);
			}else{
				callback(false,desc);
			}
			hideLoading();
		}
	});
}

//该状态下无法解决拦截问题
function fOpenWind(href,id){
	 //在很多情况下，弹出的窗口会被浏览器阻止，但若是使用a链接target='_blank'，则不会
   	id.attr("href", href);
    id.attr("target", "_blank");
    id[0].click();	//模拟用户点击事件
    //防止反复添加
    $("#js_submit_pay").removeAttr("href");
    $("#js_submit_pay").removeAttr("target");
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

//选择套餐数量
function comboMount(price){
	var fee=price;
	var initMount=1;
	$("#js_add_month").unbind("click").click(function(){
		var comboMount=$("#js_combo_month").val();
		if (isLegal(comboMount)) {
			$("#js_combo_month").val(++comboMount);
			var sum=fee*comboMount;
			$("#js_actuallypay").text(transeTwo(sum)+"元");
		}else{
			alertDialog("请输入合法的套餐数量！");
		}
	});
	$("#js_reduce_month").unbind("click").click(function(){
		var comboMount=$("#js_combo_month").val();
		if (isLegal(comboMount)) {
			if (comboMount>1) {
				$("#js_combo_month").val(--comboMount);
				var sum=fee*comboMount;
				$("#js_actuallypay").text(transeTwo(sum)+"元");
			}
		}else{
			alertDialog("请输入合法的套餐数量！");
		}
	});
	$("#js_combo_month").focusout(function(){
		var comboMount=$("#js_combo_month").val();
		if (!isLegal(comboMount)||isLegal(comboMount)=="") {
			$("#js_combo_month").val(initMount);
			$("#js_actuallypay").text(fee*initMount+"元");
		}
	});
}

//判断数字是否为正整数
function isLegal(text){
	var re = /^[0-9]*[1-9][0-9]*$/ ; 
	return re.test(text) 
}

//实时监听 用户输入套餐份数，改变总价
function changeFee(price){
	var fee=price;
	$("#js_combo_month").bind('input propertychange',function(){
		var comboMount=$("#js_combo_month").val();
		//修改总价
		if (isLegal(comboMount)) {
			var sum=fee*comboMount;
			$("#js_actuallypay").text(transeTwo(sum)+"元");
		}else{
			$("#js_actuallypay").text(fee+"元");
		}
	})
}