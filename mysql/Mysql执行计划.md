
# EXPLAAIN
## 1.0 id列
- 标识select所属的行
- mysql将select查询分为简单和复杂类型。复杂类型可以分为三大类：简单子查询、所谓的派生表（在from字句中的子查询）以及union查询

## 2.0 select_type
- 这一行显示列对应行是简单还是复杂select。SIMPLE值意味着查询不包括子查询和UNION。如果查询有任何复杂的子部分，则最外层
标记为PRIMARY，其他部分标记如下
  SUBQUERY：包含在select列表中的的子查询select（换句话说，不在from字句中）标记为SUBQUERY
  DERIVED：在FROM字句的子查询中的SELECT
  UNION：在UNION中的第二个和随后的SELECT被标记为UNION
  UNION RESULT：用来从UNION的匿名临时表检索结果的select被标记为UNINO RESULT

## 3.0 table列
- 显示对应行正在访问哪个表

## 4.0 type列
- ALL 全表扫描
- index 这个跟全表扫描一样，至少mysql扫描表时按索引次序进行而不是行。它的主要优点是避免列排序；最大缺氮是要承担按索引次序
  读取整个表的开销
- range 一个有限制的索引扫描，它开始与索引里的某一点，返回匹配这个值域的行
- ref 这是一种索引访问（有时也叫索引查询）返回所有匹配某个单值的行
- eq_ref mysql知道最多返回一条符合条件的记录（这种访问方法在mysql使用主键或者唯一性索引查找时可以看到）
- const，system mysql能对查询的某部分进行优化并将其转换成一个常量时，它就会使用这些访问类型
- null 这种访问方式意味着mysql能在优化阶段分解查询语句，在执行阶段甚至用不着再访问表或者索引

## 5.0 possible_key
- 显示列查询可以使用哪些索引

## 6.0 key
这一列显示列mysql决定采用哪个索引来优化对该表的访问。

## 7.0 key_len列
- 该列显示列mysql在索引里使用的字节数

## 8.0 ref列
- 显示列之前表在key列记录的索引中查找值所用的列或常量

## 9.0 rows
- 这一列是mysql估计为列找到所需行而需要读取的行数

## 10.0 filtered列
- 显示针对表里符合某个条件（where字句或联接条件）的记录数的百分比所做的一个悲观估算

## 11.0 Extra列
- Using index 使用覆盖索引
- Using where 这意味着mysql服务器将在存储引擎检索后再进行过滤
- Using temporary 这意味着mysql在对查询结果排序时会使用一个临时表
- Using filesorf 这意味着mysql会对结果使用一个外部索引排序
- Range checked for each record（index map：N）这个值意味着没有好用的索引，新的索引将在联接的每一行上重新估算。
N是显示在possible_keys列中索引的位图，并且是冗余的。













