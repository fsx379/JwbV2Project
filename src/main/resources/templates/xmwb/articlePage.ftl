<html>
<head>
<link href="/css/pagination.css" rel="stylesheet" type="text/css" />

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
.div4{
   	font-size:20px;
   	margin:5px auto;
   	width:80%;
   	text-align:right;
   	align:center;
}
.div3{
   	font-size:20px;
   	margin:5px auto;
   	width:80%;
   	text-align:left;
   	align:center;
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
<#assign articleInfo = articleInfo />
<body>
<#include "./xmwb/inc/head.ftl">
<div class="div1">
	&nbsp;&nbsp;&nbsp;&nbsp;${articleInfo.pubDate!''}&nbsp;&nbsp;&nbsp;&nbsp;${articleInfo.chapterDesc!''}
</div>
<div class="div1">
	<h1>${articleInfo.desc}</h1>
	<h2>${articleInfo.artAhthor}</h2>
</div>

<div class="div3">
	<#list articleInfo.artContentList as d>
		<p>${d !''}</p>
	</#list>
</div>
<div class="div4">
	<p>${articleInfo.pubDate}</p>
</div>
</body>

</html>
