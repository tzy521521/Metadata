<#list table as t>
alter table ${t.name} comment '${t.remarks}';
    <#list t.COLUMNS as c>
alter table ${t.name} modify column ${c.name} ${c.type}(${c.length}) comment '${c.remarks}';
    </#list>


</#list>