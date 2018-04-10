package com.csii.tzy.utils;

import com.csii.tzy.database.IndexInFo;
import com.csii.tzy.database.IntrospectedColumn;
import com.csii.tzy.database.IntrospectedTable;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FtUtil {

    /*
    public void generateFile(String templatesDir, String templateName, Map root, String outDir, String outFileName) {
        FileWriter out = null;
        try {
            //通过一个文件输出流，就可以写到相应的文件中
            out = new FileWriter(new File(outDir, outFileName));
            Template temp = getTemplate(templatesDir, templateName);
            temp.process(root, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
     */

    /**
     * Description: 根据模版生成文件 <br/>
     */
    public void generateFile(String templatesDir, String templateName, List<IntrospectedTable> list, String outDir, String outFileName) {
        Map root = getFtUtilMap(list);
        FileWriter out = null;
        try {
            //通过一个文件输出流，就可以写到相应的文件中
            out = new FileWriter(new File(outDir, outFileName));
            Template temp = this.getTemplate(templatesDir, templateName);
            temp.process(root, out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 获取模板
     * @param templatesDir 例如"/templates"
     * @return
     */
    private Template getTemplate(String templatesDir, String name) {
        try {
            //通过Freemaker的Configuration读取相应的ftl
            Configuration cfg = new Configuration();
            //设定去哪里读取相应的模板文件
            cfg.setClassForTemplateLoading(this.getClass(), templatesDir);
            //在模板文件目录中找到名称为name的文件
            Template temp = cfg.getTemplate(name);
            return temp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Map<String,Object> getFtUtilMap(List<IntrospectedTable> list){
        //表集合
        List<Map<String, Object>> tableList = new ArrayList<>();
        for (IntrospectedTable table : list) {
            Map<String, Object> tb = new HashMap<>();
            System.out.println("表名称："+table.getName()+"  表注释："+table.getRemarks()+"  表类型："+table.getType());
            if(table.getType()!=null){
                tb.put("type",table.getType());
            }
            else
                tb.put("type","没有？");
            //表名称
            tb.put("name", table.getName());
            //表备注
            if (table.getRemarks()!=null){
                tb.put("remarks",table.getRemarks()+"。");
            }else
                tb.put("remarks"," ");
            //表主键
            String primaryKey="";
            for (IntrospectedColumn column : table.getPrimaryKeyColumns()) {
                primaryKey+=column.getName()+" ";
            }
            tb.put("primaryKey",primaryKey);
            //表外键
            String foreignKey="";
            for (IntrospectedColumn column : table.getForeignKeyColumns()) {
                primaryKey+=column.getName()+" ";
            }
            tb.put("foreignKey",foreignKey);
            //表索引
            String inFo="";
            for (IndexInFo column : table.getInFoColumns()) {
                inFo+=column.getColumnName()+" ";
            }
            tb.put("inFo",inFo);

            //字段集合
            List<Map<String, Object>> columnList = new ArrayList<>();
            for (IntrospectedColumn column : table.getAllColumns()) {
                //字段
                Map<String, Object> field = new HashMap<>();
                field.put("name", column.getName());

                if (column.getRemarks()!=null){
                    field.put("remarks",column.getRemarks());
                }else
                    field.put("remarks","");
                field.put("jdbcTypeName", column.getJdbcTypeName());
                field.put("length",column.getLength());
                if (column.isNullable()){
                    field.put("nullable","Y");
                }else {
                    field.put("nullable","N");
                }
                if (column.getDefaultValue()!=null){
                    field.put("default",""+column.getDefaultValue());
                }else
                    field.put("default","");
                columnList.add(field);
            }
            tb.put("COLUMNS", columnList);
            tableList.add(tb);
        }
        Map<String,Object> map =new HashMap<>();
        map.put("table", tableList);
        return map;
    }
}