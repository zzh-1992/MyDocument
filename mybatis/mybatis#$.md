MyBatis中的`#`和`$`是两种不同的参数注入方式，它们在使用时有一些重要区别：

1. **#（井号）**：
    - `#` 是<font color = green size=5 face="STCAIYUN">预编译</font>的方式，参数值会被替换成一个占位符 `?`。
    - 使用 `#` 时，MyBatis 会<font color = green size=5 face="STCAIYUN">自动为参数添加单引号</font>，因此适用于字符串和日期等需要加引号的情况。
    - `#` <font color = green size=5 face="STCAIYUN">避免 SQL 注入</font>，因为参数值不会直接嵌入SQL语句中。

   示例：
   ```xml
   SELECT * FROM users WHERE id = #{userId}
   ```

2. **$（美元符号）**：
    - `$` 是<font color = red size=5 face="STCAIYUN">直接替换参数的值</font>，不会生成占位符 `?`。
    - 使用 `$` 时，参数值会按原样替换到SQL语句中，适用于表名、列名、排序字段等不需要加引号的情况。
    - `$` <font color = red size=5 face="STCAIYUN">可能会导致SQL注入</font>问题，因为参数值会被直接嵌入SQL语句中，应谨慎使用。

   示例：
   ```xml
   SELECT * FROM ${tableName}
   ```

总之，`#`和`$`的选择取决于你的需求。如果你需要在SQL语句中使用参数值，并且希望避免SQL注入问题，通常建议使用`#`
。如果你需要动态构建SQL语句，例如表名、列名等，可以使用`$`，但要确保用户输入不会导致SQL注入问题。使用时要注意安全性和性能方面的考虑。
