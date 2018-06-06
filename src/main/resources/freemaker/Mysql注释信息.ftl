<#list table as t>
${t.name}   '${t.remarks}';
    <#list t.COLUMNS as c>
${c.name}   '${c.remarks}';
    </#list>


</#list>