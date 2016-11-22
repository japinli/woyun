var MAX_PAGESIZE = 20;
var submitSingleton = true;
$(document).ready(function(){
    //弹出框设置
    var initDialog = $(".buyInfoDialog").html();
    $(".buyInfoDialog").dialog({ 
        autoOpen: false,
        top:70,
        width: 810,
        height: $(".container").height()>843?843:$(".container").height()-70,
        modal: true,
        close:function(){        
                $(".buyInfoDialog").html(initDialog);  
                initForm();
           } 
    });
    //申请发票弹出对话框
    $("#askInvoice").unbind("click").click(function() {
        $(".buyInfoDialog").dialog({autoOpen: true});
    });
    //弹出框随窗口大小自适应
    /*window.onresize=function(e){  
        var _thisDialog = $(".buyInfoDialog")[0];
        if ($(".buyInfoDialog").height()>=793) {
            return;
        }
        $(".buyInfoDialog").dialog({ 
            height: $(".container").height()-70,
        });
    };*/

    initForm();
    invDetails(1);
});

//初始化发票表单
function initForm(){
    //切换发票类型-普通发票
    $("#nor-invoiceType").unbind('click').click(function(){
        $("#specailInvo").hide();
    });
    //切换发票类型-专用发票
    $("#spec-invoiceType").unbind('click').click(function(){
        $("#specailInvo").show();
    });

    //动态获取消费记录
    showBuyRecord();
    //发票信息自动填入
    invoiceContent(function(data){
        if (typeof(data)=="undefined") {
            return;
        }
        //发票抬头
        $("#invoiceName").val(data.applyInvoice.header);
        //收件人姓名
        $("#reciveName").val(data.applyRecipient.recipientName);
        //收件人手机
        $("#invoicePhone").val(data.applyRecipient.recipientPhone);
        //寄件地址
        $("#invoiceAddr").val(data.applyRecipient.recipientAddress);
        //备注
        $("#invoiceSth").val(data.applyRecipient.remarks);
        //增值税发票信息
        if (data.applyInvoice.type!="普通发票") {
            //纳税人识别号
            $("#taxpayerId").val(data.applyVatInfo.taxpayerSerialCode);
            //纳税人名称
            $("#taxpayerName").val(data.applyVatInfo.taxpayerName);
            //注册电话
            $("#registerPhone").val(data.applyVatInfo.taxpayerPhone);
            //注册地址
            $("#registerAddr").val(data.applyVatInfo.taxpayerAddress);
            //银行名称
            $("#bankName").val(data.applyVatInfo.bank);
            //银行账户
            $("#bankAccount").val(data.applyVatInfo.bankAccount);
        }
    });
    //处理表单
    dealForm();
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
            invDetails(pageNumber);
        }
    });
}

//处理表单
function dealForm(){
    var validator = new Validator();
    var invoiceFlag = {
        /*"flag":false,
        "flagSpec":false,*/
        "invoiceName":false,
        "reciveName":false,
        "invoicePhone":false,
        "invoiceAddr":false,
        "invoiceSth":false,
        "taxpayerId":false,
        "taxpayerName":false,
        "registerPhone":false,
        "registerAddr":false,
        "bankName":false,
        "bankAccount":false
    };
    //发票抬头
    $("#invoiceName").focusout(function(){
        invoiceFlag.invoiceName = validator.invName( $(this) );
    });
    //收件人姓名
    $("#reciveName").focusout(function(){
        invoiceFlag.reciveName = validator.validName( $(this) );
    });
    //收件人手机
    $("#invoicePhone").focusout(function(){
        invoiceFlag.invoicePhone = validator.validPhone( $(this) );
    });
    //寄件地址
    $("#invoiceAddr").focusout(function(){
        invoiceFlag.invoiceAddr = validator.address( $(this) );
    });
    //备注
    $("#invoiceSth").focusout(function(){
        invoiceFlag.invoiceSth = validator.otherth( $(this) ) ;
    });
    //纳税人识别号
    $("#taxpayerId").focusout(function(){
        invoiceFlag.taxpayerId = validator.taxId( $(this) ) ;
    });
    //纳税人名称
    $("#taxpayerName").focusout(function(){
        invoiceFlag.taxpayerName = validator.taxName( $(this) ) ;
    });
    //注册电话
    $("#registerPhone").focusout(function(){
        invoiceFlag.registerPhone = validator.registNum( $(this) ) ;
    });
    //注册地址
    $("#registerAddr").focusout(function(){
        invoiceFlag.registerAddr = validator.address( $(this) ) ;
    });
    //银行名称
    $("#bankName").focusout(function(){
        invoiceFlag.bankName = validator.bankName( $(this) ) ;
    });
    //银行账户
    $("#bankAccount").focusout(function(){
        invoiceFlag.bankAccount = validator.bankCode( $(this) ) ;
    });

    $('.confirmNo').unbind('click').click(function(){ //取消
        $(".ui-dialog-titlebar-close").click();
    });
    //确认或取消申请
    $('.confirmYes').unbind('click').click(function(){ //确认
        var money = Number($("#js_price").text().slice(0,-1));
        if (money<500||!submitSingleton) {
            return;
        }
        $("input[class=inputField]").trigger("focusout");
        var type = $("input:radio:checked").val();
        if (type == "nor-invoice") {
            if (invoiceFlag.invoiceName&&invoiceFlag.reciveName&&invoiceFlag.invoicePhone&&invoiceFlag.invoiceAddr) {
                var jsonData = {
                    "applyInvoice": {
                        "header": $("#invoiceName").val(),
                        "type": "普通发票"
                    },
                    "applyRecipient": {
                        "recipientAddress": $("#invoiceAddr").val(),
                        "recipientName": $("#reciveName").val(),
                        "recipientPhone": $("#invoicePhone").val(),
                        "remarks": $("#invoiceSth").val()
                    },
                    "payBills":$("#js_price").attr("purchase").split(",")
                }
                submitForm(jsonData);
            }
        }else{
            if (invoiceFlag.invoiceName&&invoiceFlag.reciveName&&invoiceFlag.invoicePhone&&invoiceFlag.invoiceAddr&&
                invoiceFlag.taxpayerId&&invoiceFlag.taxpayerName&&invoiceFlag.registerPhone&&invoiceFlag.registerAddr&&
                invoiceFlag.bankName&&invoiceFlag.bankAccount) {
                var money = Number($("#js_price").text().slice(0,-1));
                var jsonData = {
                    "applyInvoice": {
                        "header": $("#invoiceName").val(),
                        "type": "增值税专用发票"
                    },
                    "applyRecipient": {
                        "recipientAddress": $("#invoiceAddr").val(),
                        "recipientName": $("#reciveName").val(),
                        "recipientPhone": $("#invoicePhone").val(),
                        "remarks": $("#invoiceSth").val()
                    },
                    "applyVatInfo": {
                        "bank": $("#bankName").val(),
                        "bankAccount": $("#bankAccount").val(),
                        "taxpayerAddress": $("#registerAddr").val(),
                        "taxpayerName": $("#taxpayerName").val(),
                        "taxpayerPhone": $("#registerPhone").val(),
                        "taxpayerSerialCode": $("#taxpayerId").val()
                    },
                    "payBills":$("#js_price").attr("purchase").split(",")
                }
                submitForm(jsonData);
            }
        }
        //$(".ui-dialog-titlebar-close").click();
    });
}

//校验
function Validator(){
    var _this = this;
    
    //校验结果的显示处理——展示给用户的界面效果
    appendHtml = function(elem, flag, text){
        if(elem.next().hasClass("errorClass")){
            if(flag){
                elem.next().html('<p class="errorClass"></p>');
            }else{
                elem.next().html( '<p class="errorClass">'+ text +'</p>');
            }      
        }else{
            elem.after('<p class="errorClass ml112"></p>');
            if(flag){
                elem.next().html('<p class="errorClass"></p>');
            }else{
                elem.next().html( '<p class="errorClass">'+ text +'</p>');
            }
        }
    }
    
    _this.invName = function($obj){ //发票抬头
        var str1 = /^[\u4e00-\u9fa5]{1,50}$/;  
        var str2 = /^[A-Za-z]{1,50}$/;
        var pass = false;
        var tip = "";
        var value = $obj.val();
        
        if( str1.test(value) ){
            pass = true;
            
        }else if( str2.test(value) ){
            pass = true;
            
        }else if(!value){
             tip = "请输入发票抬头" ;
             pass = false;
        }else{
             tip = "（1）发票抬头为1~50位中文 （2）发票抬头为1~50位字母" ;
             pass = false;
        }
        
        appendHtml($obj, pass, tip);
        return pass;
    }    

    _this.validName =function($obj){    //收件人姓名
        var str1 = /^[\u4e00-\u9fa5]{1,10}$/;  
        var str2 = /^[A-Za-z]{1,20}$/;
        var pass = false;
        var tip = "";
        var value = $obj.val();
        
        if( str1.test(value) ){
            pass = true;
            
        }else if( str2.test(value) ){
            pass = true;
            
        }else if(!value){
             tip = "请输入收件人姓名" ;
             pass = false;
        }else{
             tip = "（1）收件人姓名为1~10位中文 （2）收件人姓名为1~20位字母" ;
             pass = false;
        }
        
        appendHtml($obj, pass, tip);
        return pass;
    }
    
    _this.validPhone = function($obj){  //手机号码
        var reg =/^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
        var value = $obj.val();
        var pass = false;
        var tip = "";
        
        if( value == "" ){
            tip = "请输入收件人手机号码";
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

    _this.address = function($obj){   //邮寄地址
        var minMaxLength = /^[\S]{6,50}$/,
                number = /^[0-9]+$/;   
                special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;    
        var value = $obj.val();
        var pass = false;
        var tip = "";
        
        if( value == ""){
            tip = "请输入地址";
            pass = false;
        }else if(!minMaxLength.test(value)){
            if(value.length > 50){
                tip = "地址长度不能超过50位" ;
            }else if(value.length < 6){
                tip = "地址长度不能少于6位" ;
            }else{
                tip = "邮件地址不合法"
            }
            pass = false;    
        }else if(number.test(value)){
            tip = "邮寄地址不能全为数字" ;
            pass = false;
        }else if(special.test(value)){
            tip = "邮寄地址不能全为特殊字符";
            pass = false;
        }else{
            pass = true;
        } 
        appendHtml($obj, pass, tip);
        return pass;
    }

    _this.otherth =function($obj){    //备注
        var minMaxLength = /^[\S]{0,50}$/,
            number = /^[0-9]+$/,
            special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;
    
        var value = $obj.val();
        var pass = false;
        var tip = "";
        if(!minMaxLength.test(value)){
            if(value.length > 50){
                tip = "备注长度不能超过50位" ;
            }
            pass = false;
        }else if(number.test(value)){
            tip = "备注不能全为数字" ;
            pass = false;            
        }else if(special.test(value)){
            tip = "备注不能全为特殊字符";
            pass = false;
        }else{
            pass = true;
        }
        appendHtml($obj, pass, tip);
        return pass;
    }

    _this.taxName = function($obj){ //纳税人名称
        var str1 = /^[\u4e00-\u9fa5]{1,50}$/;  
        var pass = false;
        var tip = "";
        var value = $obj.val();
        
        if( str1.test(value) ){
            pass = true;    
        }else if(!value){
             tip = "请输入纳税人名称" ;
             pass = false;
        }else{
             tip = "纳税人名称为1~50位中文";
             pass = false;
        }
        appendHtml($obj, pass, tip);
        return pass;
    } 

    _this.bankName = function($obj){ //银行名称
        var str1 = /^[\u4e00-\u9fa5]{1,30}$/;  
        var pass = false;
        var tip = "";
        var value = $obj.val();
        
        if( str1.test(value) ){
            pass = true;
            
        }else if(!value){
             tip = "请输入银行名称" ;
             pass = false;
        }else{
             tip = "银行名称为1~50位中文";
             pass = false;
        }
        appendHtml($obj, pass, tip);
        return pass;
    } 

    _this.bankCode = function($obj){ //银行卡校验
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
           
        }else if(bankno == ""){
            tip = "请输入银行账户";
            pass = false;
        }else{   
           //不符合Luhm校验
           tip = "银行账户不正确";
           pass = false;  
        }    
        appendHtml($obj, pass, tip);
        return pass;
    }

    _this.taxId =function($obj){    //纳税人识别号
        var minMaxLength = /^[\S]{15}|[\S]{18}|[\S]{20}$/;
        var value = $obj.val();
        var pass = false;
        var tip = "";
        if(value == ""){
            tip = "请输入纳税人识别号";
            pass = false;
        }else if(!minMaxLength.test(value)){
            tip = "纳税人识别号为15、18或20位字符" ;
            pass = false;
        }else{
            pass = true;
        }
        appendHtml($obj, pass, tip);
        return pass;
    }

    _this.registNum = function($obj){  //注册号码
        var reg =/^((13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8})|(\d{7,8})|(\d{3}-\d{7,8}|\d{4}-\{7,8})$/;
        var value = $obj.val();
        var pass = false;
        var tip = "";
        
        if( value == "" ){
            tip = "请输入注册号码";
            pass = false;
            
        }else if( reg.test(value) ){
            pass = true;
            
        }else{
            tip = "请输入合法的注册号码";
            pass = false;
        }
        
        appendHtml($obj, pass, tip);
        return pass;
    }
}

//获取套餐消费记录
function buyRecord(callback) {
    requestServer({
        requestURL: gfALLDATA("payHref")+'/bills/pay',
        requestType: 'GET',
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
                callback(code,data.resultData);
                hideLoading();
                return;
            }else{
                callback(code,data.resultData);
                hideLoading();
                return;
            } 
        }
    });
}

//动态展示消费记录
function showBuyRecord(){
    buyRecord(function(code,data){
        var $con_record = $("#js_con_record");
        $con_record.children().remove();
        var costType = "套餐购买";
        if (data == undefined||code != 0) {
            $con_record.append(
                "<p class='m10'>暂无消费记录</p>"
            );
            return;
        }
        for (var i = 0,j = 0; i < data.length; i++) {
            if (data[i].cost<=0||data[i].checkState!=0) {
                continue;
            } 
            $con_record.append(
                "<p class='m10'><input type='checkbox' name='conRecord' value='"
                +transeTwo(data[i].cost)+"' purchaseId='"+data[i].id+"'/>"
                    +"<span class='ml35'>"+timeToDate(data[i].builtDate)+"</span>"
                    +"<span class='ml35'>"+data[i].cost+"元"+"</span>"
                    +"<span class='ml35'>"+costType+"</span>"
                +"</p>"
            );
            j++;
        }
        if (j<=0) {
             $con_record.append(
                "<p class='m10'>暂无消费记录</p>"
            );
            return;
        }else{
            $con_record.prepend(
                "<p class='m10'><input id='js_select_all' type='checkbox' value='all'/>"
                    +"<span class='ml35' id='js_select_text'>全选</span>"
                +"</p>"
            );
        }
        //绑定全选事件
        $("#js_select_all").unbind("click").click(function(){
            if ($("#js_select_all").is(":checked")==true) {
                $("#js_select_text").text("取消全选");
                $("input[name=conRecord]").each(function(){
                    $(this).prop("checked",true);
                });
            }else{
                $("#js_select_text").text("全选");
                $("input[name=conRecord]").each(function(){
                    $(this).prop("checked",false);
                });
            }
        });

        //监听消费记录总价
        changeFee();
    });
}

//获取发票记录
function invoiceRecords(jsonData,callback){
    requestServer({
        requestURL: gfALLDATA("alipayHref")+"/invoices",
        requestType: 'GET',
        requestData:jsonData,
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data,state,resObj){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
                callback(data.resultCode,data.resultData,resObj.getResponseHeader("X-Total-Count"));
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

//展示发票记录
function invDetails(page) {
    var jsonData = {
        offset:(page-1)*MAX_PAGESIZE,
        limit:MAX_PAGESIZE,
        sortings:"-apply_date"
    }
    invoiceRecords(jsonData,function(hasRecord,data,pageSize){
        $("#records-det").children().remove();
        if (hasRecord == 1 || data == undefined) {
            $("#records-det").append(
                "<tr class='records-btr'>"
                    +"<td colspan='6' class='records-btd'>暂无记录</td>"
                +"</tr>"
            );
            docPagination(1,MAX_PAGESIZE);
            return;
        }
        for (var i = 0; i < data.length; i++) {
            var completedDate = (data[i].completedDate==undefined)?"":timeToDate(data[i].completedDate);
            var remarks = (data[i].remarks==undefined)?"":data[i].remarks;
            if (data[i].state == "正在审核") {
                var state = "<span class='primaryClass'>"+data[i].state+"</span>";
            }else if (data[i].state == "审核通过" || data[i].state == "发票已邮寄") {
                var state = "<span class='successClass'>"+data[i].state+"</span>";
            }else{
                var state = "<span class='errorClass'>"+data[i].state+"</span>";
                //completedDate = "";
            }
            //处理签署者
            $("#records-det").append(
                "<tr class='records-btr'>"
                    +"<td class='records-btd td-per1'>"+data[i].header+"</td>"
                    +"<td class='records-btd td-per1'>"+data[i].sum+"</td>"
                    +"<td class='records-btd td-per1'>"+state+"</td>"
                    +"<td class='records-btd td-per1'>"+timeToDate(data[i].applyDate)+"</td>"
                    +"<td class='records-btd td-per1'>"+completedDate+"</td>"
                    +"<td class='records-btd td-per1'>"+remarks+"</td>"
               +"</tr>"
            );
        } 
        if (page == 1) {
            docPagination(pageSize,MAX_PAGESIZE);
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

//提交表单
function submitForm(jsonData){
    requestServer({
        requestURL: gfALLDATA("alipayHref")+"/invoices",
        requestType: 'POST',
        requestData:JSON.stringify(jsonData),
        beforeCallback: function(){
           submitSingleton = false;
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
                window.location.reload();
                hideLoading();
                return;
            }else{
                submitSingleton = true;
                alert(desc);
                hideLoading();
                return;
            }  
        }
    });
}

//实时监听 选择消费记录，改变价格,记录消费记录
function changeFee(){
    var sum = 0;
    var purchase = new Array();
    $('input:checkbox').on('change',function(){
        if ($(this).prop("checked")) {
            var $this = $(this);
            if ($this.val() == "all") {
                sum = transeTwo(0);
                purchase = [];
                $("input[name=conRecord]").each(function(){
                    sum = sum + transeTwo($(this).val());
                    purchase.push(Number($(this).attr("purchaseId")));
                });
            }else if ($this.prop('checked')) {
                sum = sum + transeTwo($this.val());
                purchase.push(Number($this.attr("purchaseId")));
            }
        }else{
            var $this = $(this);
            if ($this.val() == "all"){
                sum = 0;
                purchase = [];
            }else{
                sum = sum - transeTwo($this.val());
                purchase.splice($.inArray(Number($this.attr("purchaseId")),purchase),1);
            }
        }
        $("#js_price").text(transeTwo(sum)+"元").attr("purchase",purchase);
    })
}

//转换数值
function transeTwo(num){
    return parseFloat(Number(num).toFixed(2));
}

//发票内容自动生成
function invoiceContent(callback){
    requestServer({
        requestURL: gfALLDATA("alipayHref")+"/invoices/recent",
        requestType: 'GET',
        //requestData: jsonData,
        beforeCallback: function(){
           showLoading();
        },
        successCallback: function(data){
            var code = data.resultCode;
            var desc = data.resultDesc;
            if(code == 0){
                callback(data.resultData);
                hideLoading();
                return;
            }else{
                hideLoading();
                return;
            }  
        }
    });
}