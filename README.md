# 使用方法
## 首先创建一个SimpleDataSource:
```java
SimpleDataSource dataSource=new SimpleDataSource(
                Dialect.ORACLE,
                "jdbc:oracle:thin:@//localhost:1521/orcl",
                "scott",
                "scott"
        );
```
除了上面这种方式外，还可以使用SimpleDataSource(Dialect dialect, DataSource dataSource)这个构造方法，直接使用其他的DataSource。
然后就是用创建好的dataSource去创建DBMetadataUtils:
```java
DBMetadataUtils dbMetadataUtils = new DBMetadataUtils(dataSource);
```
创建一个DatabaseConfig,调用introspectTables(config)方法获取数据库表的元数据
```java
DatabaseConfig config = new DatabaseConfig("orcl","scott");
        List<IntrospectedTable> list = dbMetadataUtils.introspectTables(config);
```
这里需要注意DatabaseConfig，他有下面三个构造方法：
```java
DatabaseConfig()
DatabaseConfig(String catalog, String schemaPattern)
DatabaseConfig(String catalog, String schemaPattern, String tableNamePattern)
```
一般情况下我们需要设置catalog和schemaPatter，还可以设置tableNamePattern来限定要获取的表。

其中schemaPatter和tableNamePattern都支持sql的%和_匹配。

最后利用制作的模板生成文档。
```java
FtUtil ftUtil = new FtUtil();
ftUtil.generateFile("/", "demo.xml", list, "D:/", "demo.doc");
```
FtUtil类中有两个方法可供使用。使用后者，可以自己设计模板，但需要更改相应的代码。
```java
public void generateFile(String templatesDir, String templateName, List<IntrospectedTable> list, String outDir, String outFileName)
public void generateFile(String templatesDir, String templateName, Map root, String outDir, String outFileName) 
```
目前生成的文档有点大，另存之后会变小。