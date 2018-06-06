<#list table as t>
COMMENT ON TABLE ${t.name} IS '${t.remarks}';
    <#list t.COLUMNS as c>
COMMENT ON COLUMN ${t.name}.${c.name} IS '${c.remarks}';
    </#list>


</#list>

