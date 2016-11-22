var MAX_PAGESIZE = 15;
$(document).ready(function(){
    var totalPages = 1;
    var pageSize = 10;
    setCurDate();
    var mustClose = false;
    var _this;
    //点击选择框显示
    $(".dropdown").unbind("click").click(function(){
        if (mustClose == true && this != _this) {
            $(".options").hide();
            $(this).find(".options").show();
            mustClose = false;
        }else if (mustClose == false) {
            $(this).find(".options").show();
        }
        _this = this;
    });
    //点击取消选择框
    $(".container").unbind("click").click(function(){
        if (mustClose == true){
            $(".options").hide();
            mustClose = false;
        }else if($(".options").is(":visible")&&mustClose == false){
            mustClose = true;
        }
    });
    //确定并更改选择项
    $(".option-li").unbind("click").click(function(){
        var selectedValue = $(this).text();
        $(this).parents(".options").prevAll(".selected").text(selectedValue);
        searchRange = "part";
        loadDate(1,searchRange);
        if ($("#js_selectAll").text()=="返回搜索") {   
            $("#js_selectAll").text("查看全部");
        }
    });
    //查看全部
    $("#js_selectAll").unbind("click").click(function(){
    	if ($(this).text()=="查看全部") {   
            $(this).text("返回搜索"); 
            searchRange = "all";
        }else{
            $(this).text("查看全部"); 
            searchRange = "part";
        }
        loadDate(1,searchRange);
    });

    docPagination(totalPages,pageSize);
    loadDate(1);
});

//设置当前日期
function setCurDate() {
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    $("#js_year").text(year+"年");
    $("#js_month").text(month+"月");
    var yearStart = 2015;
    var yearEnd = year;
    for (var i = yearEnd; i >= yearStart; i--) { 
        $("#js_date_year").append(
            "<li class='option-li'>"+i+"年</li>"
        );
    }
    for (var i = month; i >= 1; i--) { 
        $("#js_date_month").append(
            "<li class='option-li'>"+i+"月</li>"
        );
    }
}

//分页
function docPagination(totalPages, pageSize){
    var totalPages = totalPages;    //总页数
    var pageSize = pageSize;    //每页条数
    $('.js_pagination').pagination({
        items: totalPages,
        itemsOnPage: pageSize,
        displayedPages:3,
        prevText:"< 上一页",
        nextText:"下一页 >",
        onPageClick: function(pageNumber,event){
            loadDate(pageNumber);
        }
    });
}

//展示订单详情
function bliudCombo(_this){
	var comboId = $(_this).attr("comboId");
    if (_this.data != undefined) {
        $("#js_comboDetail").html(_this.data);
        return;
    }
	comboDetail(comboId,function(data){
		//文档数量
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
				var vaildPeriod=data.validPeriod+'天';
		}
		//是否有证书
		if (data.withCert) {
			var hasCert="x.509v3数字证书";
		}else{
			var hasCert="无";
		};
        _this.data = "<p>文档数量：<span id='js_docMount'>"+frequency+"</span></p>"
            +"<p>有效期：<span id='js_docDays'>"+vaildPeriod+"</span></p>"
            +"<p>用户数：<span id='js_docUsers'>1个</span></p>"
            +"<p>证书：<span id='js_docCert'>"+hasCert+"</span></p>"
            +"<p>证据凭证：<span id='js_docLaw'>有</span></p>"
            +"<p>签名类型：<span id='js_docType'>手写签名/图章</span></p>"
            +"<p>签名提醒：<span id='js_docRemain'>EMAIL</span></p>";
		$("#js_comboDetail").html(_this.data);
	});
	//$("#js_comboDetail").show();
}

function showCombo(e) {	
    var mouseX = getMousePos(e).x-200;
    var mouseY = getMousePos(e).y;
    if ($(document).height()-$(".bottom-oparate").height()-mouseY-25
        <$("#js_comboDetail").height()) {
        mouseY = mouseY+$(".rmain-view-container").scrollTop()-110
        -$("#js_comboDetail").height()-30;
    }else{
        mouseY = mouseY+$(".rmain-view-container").scrollTop()-110;
    }
	$("#js_comboDetail").css("left",mouseX+"px");
	$("#js_comboDetail").css("top",mouseY+"px");
	$("#js_comboDetail").show();
}

//隐藏订单详情
function hideCombo(){
	$("#js_comboDetail").hide();
}

//获取购买记录
function buyRecord(jsonData,callback) {
	requestServer({
        requestURL: gfALLDATA("payHref")+'/bills/pay',
        requestType: 'GET',
        requestData: jsonData,
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            var pageNum = data.page;
            var pageSize = data.total;
            var costs = data.costs;
            if(code == 0){
            	callback(code,data.resultData,data.page,data.total,costs);
            	hideLoading();
            	return;
            }else{
            	callback(code);
            	hideLoading();
            	return;
            } 
        }
	});
}

//动态加载购买数据
function loadDate(page) {
	if (arguments[1] == "part") {
        var year = Number($("#js_year").text().slice(0,4));
        var month = $("#js_month").text();
        if(month.length>2){
            month = Number(month.slice(0,2));
        }else{
            month = Number(month.slice(0,1));
        }
        var date = new Date(year,month,0);
        var day = date.getDate();
        if (month<10) {
            month = "0"+month;
        }
        var fromDate = year+"-"+month+"-"+"01 "+"00:00:00";
        var toDate = year+"-"+month+"-"+day+" "+"23:59:59";
        var jsonData = {
            fromDate:fromDate,
            toDate:toDate,
            page:page,
            pagePerNum:MAX_PAGESIZE
        };
    }else{
        var jsonData = {
            page:page,
            pagePerNum:MAX_PAGESIZE
        }
    }
	buyRecord(jsonData,function(code,data,pageNum,pageSize,costs){
		$("#records-tot").children().remove();
		$("#records-tbd").children().remove();
		if (code == 1 || data == undefined) {
			$("#records-tbd").append(
				"<tr class='records-btr'>"
					+"<td colspan='9' class='records-btd'>暂无记录</td>"
				+"</tr>"
			);
			$("#records-tot").append(
                "<tr class='records-btr'>"
                    +"<td colspan='2' class='records-btd'>暂无记录</td>"
                +"</tr>"
            );
            docPagination(1,MAX_PAGESIZE);
			return;
		}
		$("#records-tot").append(
            "<tr class='records-btr'>"
                +"<td class='records-btd td-per5'>套餐购买</td>"
                +"<td class='records-btd td-per5'>"+costs+"</td>"
            +"</tr>"
        );
		for (var i = 0; i < data.length; i++) {
			if (data[i].payType == "signit") {
				var successPay = "赠送成功";
				var buyagain = "";
				var payType = "系统赠送"
			}else{
				var successPay = "交易成功";
				var buyagain = "<a class='primaryClass' href='"
					+gfALLDATA("alipayHref")+"/payments/pay-combo.html?id="+data[i].sysComboId+"'>再次购买</a>";
				if (data[i].payType == "wxpay") {
					var payType = "微信支付";
				}else{
					var payType = "支付宝支付";
				}
			}
			$("#records-tbd").append(
				"<tr class='records-btr'>"
					+"<td class='records-btd td-per2'>"+data[i].orderId+"</td>"
					+"<td class='records-btd td-per1' onmouseenter='bliudCombo(this)'comboId='"
                        +data[i].sysComboId+"'>"
						+"<span onmousemove='showCombo(event)' onmouseout='hideCombo()'>"
						+data[i].sysComboName+"套餐</span>"
					+"</td>"
					+"<td class='records-btd td-per1'>"+data[i].perCost+"</td>"
					+"<td class='records-btd td-per1'>"+data[i].period+"</td>"
					+"<td class='records-btd td-per1'>"+data[i].cost+"</td>"
					+"<td class='records-btd td-per1'>"+timeToDate(data[i].builtDate)+"</td>"
					+"<td class='records-btd td-per1 successClass'>"+successPay+"</td>"
					+"<td class='records-btd td-per1'>"+buyagain+"</td>"
					+"<td class='records-btd td-per1'>"+payType+"</td>"
				+"</tr>"
			);
		}
		if (pageNum == 1) {
            docPagination(pageSize,MAX_PAGESIZE);
        }
	});
}

//请求套餐详情
function comboDetail(comboId,callback) {
	requestServer({
        requestURL: gfALLDATA("payHref")+'/combos/'+comboId,
        requestType: 'GET',
/*        beforeCallback: function(){
           showLoading();
        },*/
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
            	callback(data.resultData);
            }else{
            	return;
            }  
            //hideLoading();
        }
    });
}

//时间戳转日期
function timeToDate(time) {
    var timestamp = time;
    var date = new Date(timestamp);
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    month = month<9?"0"+month:month;
    var day = date.getDate()<10?"0"+date.getDate():date.getDate();
    var hour = date.getHours()<10?"0"+date.getHours():date.getHours();
    var minute = date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes();
    var finalDate = year+"-"+month+"-"+day+" "+hour+":"+minute;
    return finalDate;
}

//兼容ie、火狐浏览器的鼠标event事件
function getMousePos(event) { 
    var e = event || window.event; 
    var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft; 
    var scrollY = document.documentElement.scrollTop || document.body.scrollTop; 
    var x = e.pageX || e.clientX + scrollX; 
    var y = e.pageY || e.clientY + scrollY; 
    return { 'x': x, 'y': y }; 
} 