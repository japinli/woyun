/***
 * 	文档渲染
 ***/
/*
 * 一、去除JQ库的使用
 *
 * 1.用window.onload 代替 $(document).ready()  页面加载完成时间延后100ms左右
 * 2.翻页按钮取消unbind,如果之前绑定有事件可能有问题
 *
 * 二、加载算法优化
 *
 * 1.先请求所有图片链接(删除)--> 每次请求一张图片链接
 * 2.改同步请求为异步请求
 * 3.图片加载失败，疑似多重加载同一张图片的问题，提前标定以加载
 *
 * 三、展示
 *
 * 1.拉动时也显示当前页数
*/
window.onload=function(){
    var openDoc=new OpenDoc();
    openDoc.init();
};
var GIMGHREF = gfALLDATA( "docHref" )+ "/" + getQueryString("doc-id") + '/images';
//打开文档-新版本 2016/9/8
function OpenDoc(obj){
	//自定义数据
	obj = obj||{constant:{},elementName:{}};
	var config={
			constant:{
				initloadnum:obj.constant.initloadnum||3,//初始请求，加载图片数
				delay:obj.constant.delay||500,//滑轮停止事件判断
				stopLImgnum:obj.constant.stopLImgnum||1,//额外加载图片数 前后
				scrollDLImgnum:obj.constant.scrollDLImgnum||1,//滑动方向额外加载图片数
				nowPageProp:obj.constant.nowPageProp||3/4,//判定下一页所需页面距离视图顶部高度比例
				jumpPageProp:obj.constant.jumpPageProp || 0,//跳转页面，页面距离视图顶部高度比例
				cutLinksnum:obj.constant.cutLinknums||5//链接请求一次请求多少，
			},
			elementName:{
				previewPageCName:obj.elementName.previewPageCName||"preview-page",//图片列表标签
				docViewerID:obj.elementName.docViewerID||"docViewer",//包含图片列表，在scroll内的一个容器
				previewScrollCName:obj.elementName.previewScrollCName||"preview-container-scroll",//下拉框标签
				numCountID:obj.elementName.numCountID||"numCount",//页总数标签ID   默认为span
				showPageNumID:obj.elementName.showPageNumID||"showPageNum",//当前页数 默认为input
				prePageID:obj.elementName.prePageID||"pre-page",//前一页功能ID
				backPageID:obj.elementName.backPageID||"back-page"//后一页功能ID
			},
			extentFun:{
				numCount:false,
				showPageNum:false,
				prePage:false,
				backPage:false
			},
      getImgLinks:obj.getImgLinks||fggetImgLinks//获取图片链接方法   要求：传入{startPage:开始页数,endPage:结束页数,callback:回调函数}  向回调函数传链接数组
	  };

	//检测必要功能
	if(!document.getElementsByClassName(config.elementName.previewPageCName)[0]){
		alert("没有设置正确的标签CLASS名");
		console.error("没有设置正确的标签CLASS名");
		return;
	}
	if(!document.getElementById(config.elementName.docViewerID)){
		alert("没有设置正确的docViewer的标签ID名");
		console.error("没有设置正确的docViewer的标签ID名");
		return;
	}
	if(!document.getElementsByClassName(config.elementName.previewScrollCName)[0]){
		alert("没有设置正确的Scroll的标签CLASS名");
		console.error("没有设置正确的Scroll的标签CLASS名");
		return;
	}

	//检测额外功能
	if(document.getElementById(config.elementName.numCountID))  config.extentFun.numCount=true;
  else console.warn("无法激活页面总数功能");
	if(document.getElementById(config.elementName.showPageNumID)) config.extentFun.showPageNum=true;
  else console.warn("无法激活当前页面数功能");
	if(document.getElementById(config.elementName.prePageID)) config.extentFun.prePage=true;
  else console.warn("无法激活前翻页功能");
	if(document.getElementById(config.elementName.backPageID)) config.extentFun.backPage=true;
  else console.warn("无法激活后翻页功能");


	var nOldPage=1;//记录滚动前的页数
	var imgLinks=[];//存储图片链接
	var flagImg=[];//存储是否已经加载
  var pagePlaceOffsetTops=[];//存储图片相对高度


	var oPagePlace=document.getElementsByClassName(config.elementName.previewPageCName);//获取所有图片页
    var nPageTotal = oPagePlace.length;//获取图片页数量
    var oWraper = document.getElementsByClassName(config.elementName.previewScrollCName)[0];


    this.init=function(){
		getImgLinks(1,config.constant.initloadnum,function(){
			for(var i = 1; i<=config.constant.initloadnum; i++){
				loadImg(i,true);
			}
			init();
		});
    };

    var init=function(){
    	for(var l=0;page=oPagePlace[l];l++){
    		pagePlaceOffsetTops[l]=page.offsetTop;
    	}
    	if(config.extentFun.numCount) document.getElementById(config.elementName.numCountID).innerHTML=nPageTotal;//设置页面总数

    	var pageW = oPagePlace[0].clientWidth;
    	var pageH = oPagePlace[0].clientHeight;
    	var docViewer = document.getElementById(config.elementName.docViewerID);
		  docViewer.style.height=pageH*nPageTotal+"px";
		  docViewer.style.width=pageW+"px";

    	oWraper.addEventListener('scroll',scrollExtend({
    		delay:config.constant.delay,
    		  scrolling:function(){
            if(config.extentFun.showPageNum){
              return function(){
                var nowPage=getShowingPageNum();
    	          document.getElementById(config.elementName.showPageNumID).value=nowPage;
              };
            }
            return function(){};
        	}(),
        	scrollend:function(){
        		var nowPage=getShowingPageNum();
        		var start=-config.constant.stopLImgnum;
        		var end=config.constant.stopLImgnum;
        		if(nowPage>nOldPage){//下滑
	            	end+=config.constant.scrollDLImgnum;
	            }else{//上滑
	            	start-=config.constant.scrollDLImgnum;
	            }
				//loadImg_Link(start+nowPage,end+nowPage);
        		start+=nowPage;
        		end+=nowPage;
        		if(start<1) start=1;
        		if(end>nPageTotal) end=nPageTotal;
      				getImgLinks(start,end,function(){
    					loadImg(nowPage,true,function(){
    						for(var i = start; i<=end; i++){
    							loadImg(i);
						    }
					  });
				});
	            nOldPage=nowPage;
        	}
    	}));

      if(config.extentFun.prePage){
        document.getElementById(config.elementName.prePageID).onclick=function(){
          	var inputnum = parseInt(document.getElementById(config.elementName.showPageNumID).value);
            selectPage(inputnum-1);
          };
      }

      if(config.extentFun.backPage){
    	document.getElementById(config.elementName.backPageID).onclick=function(){
        	var inputnum = parseInt(document.getElementById(config.elementName.showPageNumID).value);
            selectPage(inputnum+1);
        };
      }
      if(config.extentFun.showPageNum){
        document.getElementById('showPageNum').onchange=function(){
    			var inputnum = parseInt(document.getElementById(config.elementName.showPageNumID).value);
           		 selectPage(inputnum);
    		};
      }
  };

	function getImgLinks(start,end,callback){
		var x1,x2;
		if(imgLinks[start-1]&&imgLinks[end-1]){
			 callback();
			 return;
		}
		if(!imgLinks[start-1]&&imgLinks[end-1]) x1=x2=(start-1)/config.constant.cutLinksnum;
		else if(imgLinks[start-1]&&!imgLinks[end-1]) x1=x2=(end-1)/config.constant.cutLinksnum;
		else {
			x1=(start-1)/config.constant.cutLinksnum;
			x2=(end-1)/config.constant.cutLinksnum;
		}
		x1=parseInt(x1)*config.constant.cutLinksnum+1;
		x2=parseInt(x2+1)*config.constant.cutLinksnum;
		if(x2>nPageTotal) x2=nPageTotal;
		config.getImgLinks({
			startPage:x1,
			endPage:x2,
		  "callback":function(links){
			for(var i=x1;i<=x2;i++){
				imgLinks[i-1]=links[i-x1];
			}
			callback();
		}});
	}

	function loadImg(page,ifshowLoading,callback){
		if(flagImg[page-1]||page<1||page>nPageTotal) {
    		if(callback) {
        		callback();
        	}
    		return ;
    	}
		flagImg[page-1] = true;
		if(ifshowLoading) showLoading();
		oPagePlace[page-1].getElementsByTagName('img')[0].src=imgLinks[page-1].href+ '&mark=' + new Date().getTime();
		oPagePlace[page-1].getElementsByTagName('img')[0].onload=function(){//保证图像加载完成后
			if(ifshowLoading){
				hideLoading();
			}
			if(callback) {
				callback();
			}
		};
    }

    function getShowingPageNum(){
    	var T=oWraper.scrollTop;//展示页面下滑距离
    	var H=oWraper.offsetHeight*config.constant.nowPageProp;//计算上个页面最多所占高度
    	var pageT;
    	for(var l=1;pageT=pagePlaceOffsetTops[l];l++){
    		if(pageT>T+H){
    			return l;
    		}
    	}
    	return nPageTotal;
    };

    function selectPage(inputpage){ //页面跳转函数
    	if(inputpage>nPageTotal) {
    		inputpage=nPageTotal;
    	}
    	if(inputpage<1){
    		inputpage=1;
    	}
    	document.getElementById(config.elementName.showPageNumID).value=inputpage;

	    setHeight = pagePlaceOffsetTops[inputpage-1]-oWraper.offsetHeight*config.constant.jumpPageProp;
	    oWraper.scrollTop=setHeight;
    };
}

function getQueryString(name, url){
	if (!url) url = window.location.href;
    var name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
          results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

//扩展滑动事件
function scrollExtend(obj){
	var handler=null;
	var start=obj.scrollstart;
	var scroll=obj.scrolling;
	var end=obj.scrollend;
	var delay=obj.delay;
	return function(){
		if(!handler&&start) start();
		if(handler) clearTimeout(handler);
		if(scroll) scroll();
		if(end&&delay){
			handler=setTimeout(function(){
				handler=null;
				end();
			},delay);
		}
	};
}

function fggetImgLinks(obj){
  fgGetDocImg({startPage:obj.startPage,
    endPage:obj.endPage,loading:true},obj.callback);
}
//获取指定页数据，其他JS文件会引用
function fgGetDocImg(objData,callback){
	var startPage = objData.startPage;
	var endPage = objData.endPage;
	var isShowLoading = objData.loading;
	AJAX_GET({
		URL: GIMGHREF + '?pages=' + startPage + '-' + endPage ,
		async:true,
		before:function(){
				if(isShowLoading){
				showLoading();
				}
		},
		success:function(data){
			var code = data.resultCode;
			var imgArray = data.docImageLinks;
			if(code === 0){
				callback(imgArray);
			}else{
				showErrorInfo("对不起，出现异常...");
			}
			if(isShowLoading){
				hideLoading();
			}
		}
	});
}

function AJAX_GET(obj){
	var req=new XMLHttpRequest();
	req.onreadystatechange=function(){
		if (req.readyState==4 && req.status==200)
		{
			obj.success(JSON.parse(req.responseText));
		}
	};
	req.open("get",obj.URL,obj.async||true);
	obj.before();
	req.send();
}
