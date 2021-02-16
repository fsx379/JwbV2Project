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
<#assign xmwbArticleVoList = xmwbArticleVoList />
<#assign detailAction = "/xmwb/articlePage" />
<#assign queryDate = queryDate />
<#assign chapterUrl = chapterUrl />
<#assign chapterDesc = chapterDesc />
<#assign startNo = 1 />

<div id="loading"></div>
<div class="div1">
    &nbsp;&nbsp;&nbsp;&nbsp;${queryDate!''}&nbsp;&nbsp;&nbsp;&nbsp;${chapterDesc!''}
</div>
<div class="div1">
    <table class="oddsList u-tb u-tb-brd">
        <thead>
        <tr>
            <th class="f-w50">编号</th>
            <th class="f-w430">文章</th>
            <th class="f-w430">Url</th>
            <th class="f-w430">是否添加收藏</th>
            <th class="f-w430">操作</th>
        </tr>
        </thead>
        <tbody>
        <#list xmwbArticleVoList as d>
            <tr>
                <td>${startNo?c}</td>
                <td>${d.desc!''}</td>
                <td>${d.objectUrl!''}</td>
                <td><#if d.collect==1> <font color='red'>已收藏</font><#else>未收藏</#if></td>
                <td>
                    <input type="button" value="收藏"  onclick="collect('${d.articleUrl!''}')" />
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${detailAction}?articleUrl=${d.articleUrl!''}&chapterDesc=${d.chapterDesc!''}" target="_blank">文章内容 </a>
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
        startDate: '2020-12-01',
        // endDate: '2014-4-15',
        chosenDate: '2021-01-01'
    })



    function collect(el) {
        $.ajax({
            type: "get",
            url: "/xmwb/collectArticle",
            timeout : 10000,
            data: {
                articleUrl: el
            },
            dataType: "json",
            success: function (data) {
                if (data.retCode == 200) {
                    alert("添加成功！！" + el);
                    window.location.href = "/xmwb/articleList?queryDate=${queryDate}&chapterDesc=${chapterDesc}&chapterUrl=${chapterUrl}";
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
