<html>
<head>
<link href="/css/pagination.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="/js/jquery.pagination.js"></script>
<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>


<style type="text/css">

body {TEXT-ALIGN: center;}
table.elist {
	margin:0 auto;
   	width:80%;
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table.elist th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
	text-align: center;
}
table.elist td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
	text-align: center;
}
table.elist td.num {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
	text-align: right;
}
.div2{
   	font-size:20px;
   	margin:5px auto;
   	width:80%;
   	text-align:center;
   	align:center;
}
.div1{
  	font-size:20px;
   	margin:5px auto;
   	width:80%;
}
.pagination{
   margin-top:10px;
   margin-bottom:10px;
}


table{
   margin:0 auto;
   width:95%
}

table.oddsList {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table.oddsList th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}
table.oddsList td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}

</style>
<title>${title}</title>
</head>
<#assign articleLists = xmwbArticleVoList />
<#assign startNo = 1 />
<#assign detailAction = "/xmwb/articlePage" />
<body>
<#include "./xmwb/inc/head.ftl">

 <div class="div2">
      <table>
        <tbody>
        <tr>
          <td>
            <div style="color:red">${errorMsg!''}</div>
          </td><td></td>
        </tr>
        <tr>
          <td align="left">
          <font color="red">--->>> </font>
           <input type="button" value="导出收藏Excel"  onclick="downLoad(this)"/>
           <font color="red"><<<--- </font>
            &nbsp;&nbsp;&nbsp;&nbsp;
           
          </td>
          <td align="right">
          <font color="blue"> --->>></font>
          <input type="button"  value="全部清空收藏列表"   onclick="delAllPringtList()"/>
          <font color="blue"> ---<<< </font>
          </td>
          
        </tr>
        </tbody>
      </table>
  </div>
<div id="loading"></div>
<div class="div1">
	<table class="oddsList u-tb u-tb-brd">
	  <thead>
	  <tr>
	    <th class="f-w50">编号</th>
	    <th class="f-w430">URL</th>
	    <th class="f-w100">文章日期</th>
		  <th class="f-w430">版面</th>
	    <th class="f-w430">文章题目</th>
	    <th class="f-w430">作者</th>
	    <th class="f-w430">操作</th>
	  </tr>
	  </thead>
	  <tbody>
		  <#list articleLists as d>
		  <tr>
		    <td>${startNo?c}</td>
		    <td>${d.objectUrl!''}</td>
			  <td>${d.pubDate!''}</td>
			  <td>${d.chapterDesc!''}</td>
		    <td>${d.desc!''}</td>
		    <td>${d.artAhthor!''}</td>
		    <td>
				<a href="${detailAction}?articleUrl=${d.articleUrl!''}&chapterDesc=${d.chapterDesc!''}" target="_blank">文章内容 </a>
				&nbsp;&nbsp;&nbsp;
		     &nbsp;&nbsp;&nbsp;&nbsp;
		     <input type="button" value="删除"   onclick="delfromPrintList('${d.articleUrl}')" /></td>
		  </tr>
		    <#assign startNo = startNo+1 />
		  </#list>
	  </tbody>
	</table>
</div>

</body>


<script type="text/javascript">

  function delAllPringtList(el) {
    $.ajax({
      type: "post",
      url: "/xmwb/delAllPringtList",
      timeout : 10000,
      data: {
      },
      dataType: "json",
      success: function (data) {
        if (data.retCode == 200) {
          alert("删除收藏！！");
          window.location.href = "/xmwb/queryCollectList";
        }
        else {
          alert("错误！！");
          return;
        }
      }
    });
  }
  
  
   function delfromPrintList(el) {
    $.ajax({
      type: "post",
      url: "/xmwb/delPrintList",
      timeout : 10000,
      data: {
      	artId: el
      },
      dataType: "json",
      success: function (data) {
        if (data.retCode == 200) {
          alert("删除收藏！！");
          window.location.href = "/xmwb/queryCollectList";
        }
        else {
          alert("错误！！");
          return;
        }
      }
    });
  }
  
	function downLoad(el) {
    $.ajax({
      type: "post",
      url: "/xmwb/startTaskForPrintWord",
      timeout : 30000,
      data: {
      },
      dataType: "json",
      beforeSend:function(XMLHttpRequest){ 
		$("#loading").html("<img src='/img/loading.gif' />");
	},
      success: function (data) {
      	$("#loading").empty();
        if (data.retCode == 200) {
          alert("申请打印完成，请去下列路径查询结果！！\r\n" + data.retDetail);
          window.location.href = "/xmwb/queryCollectList";
        }
        else {
          alert("错误！！");
          return;
        }
      }
    });
  }

</script>
</html>
