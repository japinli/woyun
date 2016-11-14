$(document).ready(function(){

	//弹出框设置
	$(".buyInfoDialog").dialog({ 
	    autoOpen: false,
	    width: 400,
	    modal: true,
	});

	getComboList(function( data ){
		showComboInfo( data );
	});
	
});

//获取套餐
function getComboList(callback){
	 requestServer({
	        requestURL: gfALLDATA("payHref")+'/combos',
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
	            	return;
	            } 
	            hideLoading();
	        }
	    });
}

//展示用户信息
function showPayInfo( data ){
	if( data ){
		$("#js_account").text( data.realName );
		$("#js_accBalance").text( data.balanceMoney );
	}
}

//展示套餐
function showComboInfo( data ){
	if ((typeof(data)=="undefined")) {
		confirmInfo("暂无可选套餐！");
	}else if(data.length > 0 ){
		/*$("#js_combo_list").empty();*/
		for(var i = 0; i < data.length; i++){
			var id = data[i].id;
			var name = data[i].name;
			var descInfo = data[i].description;
			var frequency = data[i].frequency;	
			var fee = data[i].fee;		
			//是否有证书
			if (data[i].withCert) {
				var hasCert="x.509v3数字证书";
			}else{
				var hasCert="无";
			};
			//签名次数
			if (data[i].withFrequency) { 
				var periodType=data[i].periodType;
				var frequency=data[i].frequency+'份';
			  }else{
			  	var frequency="不限";
			  }
			//是否周期性资费
			if (data[i].withPeriod==true) {
				var vaildPeriod=data[i].period+data[i].periodType;
			}else{
				var vaildPeriod=data[i].validPeriod+'天';
			}
			var base=$("base").attr("href");
			$("#js_combo_list").append(
					'<li name="'+ id +'">'
					    +'<div class="sinplePay">'
						    +'<div class="needpayUnderline">'
							    +'<div class="pay-title needpayUnderline ">'
								    +'<img class="payment-logo-pic" src="'
								    +base
								    +'/static/system/images/payment-title.png"/>'
								    +'<a style="font-size: 18px;">'+name+'</a>'
									/*+'<p>适合初级创业者、个体创业者</p>'*/
								+'</div>'
								+'<table>'
									+'<tr><td class="combo-table-td1" ><p>文档数量</p></td>'
										+'<td class="combo-table-td2">'+frequency+'</td></tr>'
									+'<tr><td class="combo-table-td1"><p>有效期限</p></td>'
										+'<td class="combo-table-td2">'+vaildPeriod+'</td></tr>'
									+'<tr><td class="combo-table-td1"><p>用&nbsp;&nbsp;户&nbsp;数</p></td>'
										+'<td class="combo-table-td2">1个</td></tr>'
									+'<tr><td class="combo-table-td1"><p>证&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;书</p></td>'
									+'<td class="combo-table-td2">'+hasCert+'</td></tr>'
									+'<tr><td class="combo-table-td1"><p>证据凭证</p></td>'
										+'<td class="combo-table-td2">有</td></tr>'
									+'<tr><td class="combo-table-td1"><p>签名类型</p></td>'
										+'<td class="combo-table-td2">手写签名/图章</td></tr>'
									/*+'<tr><td class="combo-table-td1"><p>签名场景</p></td>'
										+'<td class="combo-table-td2">自己签/我与他人签/他人签</td></tr>'*/
									+'<tr><td class="combo-table-td1"><p>签名提醒</p></td>'
										+'<td class="combo-table-td2">EMAIL</td></tr>'
								+'</table>'
							+'</div>'
							+'<p class="sign-price"><a class="price-num">'+fee+'</a>元</p>'
							+'<span name="'+ id +'" class="submitCombo sinpleBut btn-primary">立即购买</span>'
						+'</div>'
			      	+'</li>'
				);
		}
		$("#js_combo_list").append(
			'<li name="">'
				+'<div class="sinplePay">'
    				+'<div class="needpayUnderline">'
	    				+'<div class="pay-title needpayUnderline ">'
			   				+'<img class="payment-logo-pic" src="'
								+base
							+'/static/system/images/payment-title.png"/>'
			    			+'<a style="font-size: 18px;">企业版</a>'
						+'</div>'
		    			+'<div class="customMade">'
		    				+'<p>没有适合您的套餐？<br><br>请联系我们为您定制！</p>'
		    			+'</div>'
					+'</div>'
					+'<div class="contactUs"><label>联系我们：400-168-8973</label></div>'
				+'</div>'
			+'</li>'
		);
		$("#js_combo_list .submitCombo").unbind('click').click(function(){
			var _this = this;
			isCanBuy(this,function(sysComboId){
				confirmCombo( sysComboId );
			});
		});
	}
}

//查询购买权限
function isCanBuy(_this,callback){
	var sysComboId=$(_this).attr("name");
	var jsonData={
		"sysComboId":sysComboId
	}
	requestServer({
        requestURL: gfALLDATA("payHref")+'/combos/check/match',
        requestType: 'GET',
        requestData: jsonData,
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
          
            if(code == 0){
            	callback(sysComboId);
            }else{
            	confirmInfo(desc);
            } 
            hideLoading();
        }
    });
}

//确认购买套餐
function confirmCombo( comboId ){
	 	$(".buyInfoDialog").dialog({autoOpen: true});
	    $('.confirmContent').html(
	    		'<div>您确定要购买此套餐吗？</div>' +
	    		'<div>套餐生效将不能被修改！</div>'
	    		);
	    $("#js_btn_group").html(
	    		'<span class="confirmNo btn-border-default-m mr25">取消</span>'+
	            '<span class="confirmYes btn-primary-m">确认</span>'
	    		);
	    $('.confirmNo').unbind('click').click(function(){ //取消
	        $(".ui-dialog-titlebar-close").click();
	    });

	    $('.confirmYes').unbind('click').click(function(){ //确认
	    	gotoPayMoney(comboId);
	        $(".ui-dialog-titlebar-close").click();
	    });
}

//选择套餐后页面跳转
function gotoPayMoney(comboId){
	window.location.href=gfALLDATA("payHref")+"/pay-combo.html?id="+comboId; 
}

//转义签章个数
function transeSign( size ){
	if( size >= 9999999 ){
		return "不限";
	}else{
		return size + " 个";
	}
}

//转义容量大小
function storeSize( size ){
	return parseFloat((size/1024).toFixed(2))+'MB';
}

//提示信息
function confirmInfo(html){
 	$(".buyInfoDialog").dialog({autoOpen: true});
    $('.confirmContent').html(html);
    $("#js_btn_group").html(
    	'<span class="confirmYes choosebtn">实名认证</span>'
    );
    $('.confirmYes').unbind('click').click(function(){ //实名认证入口
    	 window.location.href=gfALLDATA("alipayHref")+"/person-auth.html";;
        $(".ui-dialog-titlebar-close").click();
    });
}