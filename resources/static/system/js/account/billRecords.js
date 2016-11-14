var MAX_PAGESIZE = 15;
var searchRange = "part";
$(document).ready(function(){
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
        conDetails(1,searchRange);
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
        conDetails(1,searchRange);
    });
    conDetails(1,searchRange);
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
            conDetails(pageNumber,searchRange);
        }
    });
}

//展示消费明细
function conDetails(page) {
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
    conDetailsData(jsonData,function(hasRecord,data,pageNum,pageSize){
        $("#records-det").children().remove();
        $("#records-tot").children().remove();
        if (hasRecord == 1 || data == undefined) {
            $("#records-det").append(
                "<tr class='records-btr'>"
                    +"<td colspan='5' class='records-btd'>暂无记录</td>"
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
                +"<td class='records-btd td-per5'>文档签署</td>"
                +"<td class='records-btd td-per5'>"+pageSize+"</td>"
            +"</tr>"
        );
        for (var i = 0; i < data.length; i++) {
            var time = timeToDate(data[i].builtDate);
            //处理签署者
            $("#records-det").append(
                "<tr class='records-btr'>"
                    +"<td class='records-btd td-per2'>"+data[i].docName+"</td>"
                    +"<td class='records-btd td-per2'>"+data[i].signUserAccount+"</td>"
                    +"<td class='records-btd td-per2'>"
                    +"<span onmousemove='showSigner(event,this)' onmouseout='hideSigner(this)'>"
                    +changeString(data[i].signUserName,",",3)
                    +"<span class='hidden signerName'>"+data[i].signUserName+"</span></span></td>"
                    +"<td class='records-btd td-per2'>"+time+"</td>"
                    +"<td class='records-btd td-per2'>"+data[i].cost+"</td>"
               +"</tr>"
            );
        } 
        if (pageNum == 1) {
            docPagination(pageSize,MAX_PAGESIZE);
        }
    });
} 

//获取消费数据
function conDetailsData(jsonData,callback){
    requestServer({
        requestURL: gfALLDATA("payHref")+"/bills/sign",
        requestType: 'GET',
        requestData:jsonData,
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
                callback(data.resultCode,data.resultData,data.page,data.total);
                hideLoading();
                return;
            }else{
                callback(data.resultCode);
                hideLoading();
                return;
            }  
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

function showSigner(e,_this) {
    var signer = $(_this).children(".signerName").text();
    $("#js_signerDetail").html(signer);
    var mouseX = getMousePos(e).x-200;
    var mouseY = getMousePos(e).y+$(".rmain-view-container").scrollTop()-110;;
    $("#js_signerDetail").css({
        "left":mouseX+"px",
        "top":mouseY+"px"
    });
    $("#js_signerDetail").show();
}

function hideSigner(_this){
    $("#js_signerDetail").hide();
}

//数据转换
function changeString(data,iden,mount) {
    var string = "";
    for (var i = 0; i < data.length; i++) {
        if (data[i] == iden) {
            if (--mount <= 0) {
                break;
            }else{
                string = string+"、";
                continue;
            }
        }else{
            string = string+data[i];
        }
    }
    if (data[i]!=undefined) {
        return string+" …";
    }else{
        return string;
    }
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