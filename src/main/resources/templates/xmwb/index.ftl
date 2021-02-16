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
<#include "./xmwb/inc/head.ftl">
<#assign chapterVoList = chapterVoList />
<#assign queryDate = queryDate />
<#assign startNo = 1 />
<#assign detailAction = "/xmwb/articleList" />

<div class="div1">
    <form action="/xmwb/index" method="post">
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
            <th class="f-w430">收藏</th>
            <th class="f-w430">目录</th>
            <th class="f-w430">Url</th>
            <th class="f-w430">操作</th>
        </tr>
        </thead>
        <tbody>
        <#list chapterVoList as d>
            <tr>
                <td>${startNo?c}</td>
                <td><input type="button" value="收藏"  onclick="collect('${d.chapterUrl!''}' , '${d.desc!''}')" /></td>
                <td>${d.desc!''}</td>
                <td>${d.objectUrl!''}</td>
                <td>

                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${detailAction}?chapterUrl=${d.chapterUrl}&chapterDesc=${d.desc!''}&queryDate=${queryDate!''}" target="_blank"> 文章列表 </a>
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
        startDate: '2020-01-01',
        // endDate: '2014-4-15',
        chosenDate: '2021-01-01'
    })



    function collect(el ,e2) {
        $.ajax({
            type: "get",
            url: "/xmwb/collectChapter",
            timeout : 10000,
            data: {
                chapterUrl: el,
                chapterDesc : e2
            },
            dataType: "json",
            beforeSend:function(XMLHttpRequest){
                $("#loading").html("<img src='/img/loading.gif' />");
            },
            success: function (data) {
                if (data.retCode == 200) {
                    alert("添加成功！！" + el);
                    window.location.href = "/xmwb/index?queryDate=${queryDate}";
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
