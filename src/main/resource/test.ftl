<#list table as t>
表名：${t.NAME}
    <#list t.COLUMNS as c>
    列名：${c.NAME} 列属性：${c.TYPE}
    </#list>
</#list>