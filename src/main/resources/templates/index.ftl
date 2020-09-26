<html>
<head>
<link href="/css/pagination.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/css/calendar.css">
<script type="text/javascript" src="/js/z.src.js"></script>
<script type="text/javascript" src="/js/ui.js"></script>
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
.div2{
  font-size:20px;
   margin:5px auto;
   width:80%;
   text-align:center;
}
</style>
<title>${title}</title>
</head>
<body>
<#assign articleLists = articleLists />
<#assign queryDate = queryDate />
<#assign startNo = 1 />
<#assign addPrintAction = "/jwb/addPrintList" />
<#include "./inc/head.ftl">

 <div class="div1">
    <form action="/jwb/lists" method="post">
      <table>
        <tbody>
        <tr>
          <td>
            <div style="color:red">${errorMsg!''}</div>
          </td>
        </tr>
        <tr>
          <td>
            &nbsp;&nbsp;&nbsp;&nbsp;
            查询日期 <input type="text" id="queryDate" name="queryDate" value="${queryDate!''}"  onclick="laydate()" />
            &nbsp;&nbsp;&nbsp;&nbsp;
<font color="red">查询日期：${queryDate!''}&nbsp;</font>
          </td>
        </tr>
            <tr>
          <td>
            <div style="color:red">&nbsp;</div>
          </td>
        </tr>
 <tr>
          <td>
            &nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" id="queryList" value="查询" />
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="重新抓取文章列表"  onclick="refreshSpide(this)"  />
          </td>
        </tr>
        </tr>
        </tbody>
      </table>
    </form>
  </div>
<div id="loading"></div>
<div class="div1">
	<table class="oddsList u-tb u-tb-brd">
	  <thead>
	  <tr>
	    <th class="f-w50">编号</th>
          <th class="f-w430">id</th>
	    <th class="f-w430">副刊</th>
	    <th class="f-w100">文章日期</th>
	    <th class="f-w430">文章题目</th>
	    <th class="f-w430">文章作者</th>
	    <th class="f-w430">是否添加收藏</th>
	    <th class="f-w430">操作</th>
	  </tr>
	  </thead>
	  <tbody>
	  <#list articleLists as d>
	  <tr>
	    <td>${startNo?c}</td>
          <td>${d.artId!''}</td>
          <td>${d.fkDesc!''}</td>
	    <td>${d.artDate!''}</td>
	    <td>${d.artTitle!''}</td>
	    <td>${d.artAhthor!''}</td>
	    <td><#if d.collect==1> <font color='red'>已收藏</font><#else>未收藏</#if></td>
	    <td>
	    <#if d.collect==1>
	     <input type="button" value="取消收藏"  onclick="delfromPrintList(${d.artId!''})" />
	    <#else>
	     <input type="button" value="收藏"  onclick="addPrint(${d.artId!''})" />
	    </#if>
	    &nbsp;&nbsp;&nbsp;&nbsp;
	     
	      <input type="button" value="获取作者"  onclick="spiderArticleDetal(${d.artId})" />
            &nbsp;&nbsp;&nbsp;
            <a href="${detailAction}?artId=${d.artId}" target="_blank">详情</a>
	     </td>
	  </tr>
	    <#assign startNo = startNo+1 />
	  </#list>
	  </tbody>
	</table>
</div>
</body>


<script type="text/javascript">

        var calendar2 = Z.ui.CalendarTwo('#queryDate', {
            startDate: '2008-01-19', 
            // endDate: '2014-4-15',
            chosenDate: '2008-01-26'
        })


  function refreshSpide(el) {
    $.ajax({
      type: "post",
      url: "/query/startTask",
      timeout : 10000,
      data: {
        startDate: $("#queryDate").val(),
        endDate: $("#queryDate").val()
      },
      dataType: "json",
		beforeSend:function(XMLHttpRequest){ 
		$("#loading").html("<img src='/img/loading.gif' />");
	},
      success: function (data) {
      	$("#loading").empty();
        if (data.retCode == 200) {
          window.location.href = "/jwb/lists?queryDate=${queryDate}";
        }
        else {
          alert("错误！！");
          return;
        }
      }
    });
  }



  function addPrint(el) {
    $.ajax({
      type: "get",
      url: "/jwb/addPrintList",
      timeout : 10000,
      data: {
        artId: el
      },
      dataType: "json",
      success: function (data) {
        if (data.retCode == 200) {
         	alert("添加成功！！" + el);
          window.location.href = "/jwb/lists?queryDate=${queryDate}";
        }
        else {
          alert(data);
          return;
        }
      }
    });
  }
  
   function delfromPrintList(el) {
    $.ajax({
      type: "post",
      url: "/jwb/delPrintList",
      timeout : 10000,
      data: {
      	artId: el
      },
      dataType: "json",
      success: function (data) {
        if (data.retCode == 200) {
          alert("删除收藏！！");
          window.location.href = "/jwb/lists?queryDate=${queryDate}";
        }
        else {
          alert("错误！！");
          return;
        }
      }
    });
  }
  
  function spiderArticleDetal(el) {
    $.ajax({
      type: "post",
      url: "/query/startSpiderActicleDetailByArtIds",
      timeout : 10000,
      data: {
        artIds: el
      },
      dataType: "json",
      success: function (data) {
        if (data.retCode == 200) {
        	alert("获取成功！！");
          window.location.href = "/jwb/lists?queryDate=${queryDate}";
        }
        else {
          alert(data);
          return;
        }
      }
    });
  }
  
</script>

</html>
