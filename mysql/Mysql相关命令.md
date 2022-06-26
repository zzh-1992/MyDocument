## 创建数据库

```sql
create database grapefruit;
```

## 使用数据库

```sql
use grapefruit;
```

# 创建表

```sql
create table fruit
(
    id      int primary key,
    name    varchar(20),
    num     bigint,
    content text,
    index   i( name)
);
```

# 修改表属性

```sql
alter table fruit modify num bigint signed;
ALTER TABLE attence CHANGE attence_name NAME  VARCHAR(20)
ALTER TABLE fruit MODIFY column name varchar (19) default 'orange' comment '名称';
```

# 插入数据

```sql
insert into fruit (id, name, num, content)
values (1, 'meizhou', 2147483649, 'just bigint test');
insert into fruit (id, name, num, content)
values (2, 'shenzhen', 2147483650, 'just bigint test 0521');
insert into fruit (id, name, num, content)
values (3, 'meizhou', 2147483651, 'just bigint test 0532');
insert into fruit (id, name, num, content)
values (4, 'meizhou', 2147483652, 'just bigint test 0555');
insert into fruit (id, name, num, content)
values (5, 'meizhou', 2147483653, 'just bigint test 0566');
```

## 查询表的所以列名

```sql
select *
from information_schema.columns
where table_schema = 'grapefruit';
```

# 查看字段属性

```sql
show
columns from fruit;
```

# ======================官网教程==================

# 官网链接 https://dev.mysql.com/doc/refman/8.0/en/alter-table.html

# 修改表到引擎

```sql
ALTER TABLE t1 ENGINE = InnoDB;
```

# 将InnoDB表更改为使用压缩行存储格式：

```sql
ALTER TABLE t1 ROW_FORMAT = COMPRESSED;
```

# 重置当前的自动增量值

```sql
ALTER TABLE t1 AUTO_INCREMENT = 13;
```

# 更改默认表字符集

```sql
ALTER TABLE t1 CHARACTER SET = utf8;
```

# 添加（或更改）表格注释：

```sql
ALTER TABLE t1 COMMENT = 'New table comment';
```

# 这个语句还不清楚

```sql
ALTER TABLE t1 COMMENT = "NDB_TABLE=READ_BACKUP=0,PARTITION_BALANCE=FOR_RA_BY_NODE";
```

# 更改列以更改其名称和定义

```sql
ALTER TABLE t1 CHANGE a b BIGINT NOT NULL;
```

# 更改列定义但不更改其名称

```sql
ALTER TABLE t1 CHANGE b b INT NOT NULL;
```

# MODIFY 在不更改名称的情况下更改定义更方便，因为它只需要列名一次：

```sql
ALTER TABLE t1 MODIFY b INT NOT NULL;
```

# 要更改列名称但不更改其定义，请使用 CHANGE或RENAME COLUMN。

# 使用 时CHANGE，语法需要列定义，因此要保持定义不变，您必须重新指定列当前具有的定义。

# 例如，要重命名INT NOT NULL的列 b到a，这样做：

```sql
ALTER TABLE t1 CHANGE b a INT NOT NULL;
```

# RENAME COLUMN 在不改变定义的情况下更方便地更改名称，因为它只需要旧名称和新名称：

```sql
ALTER TABLE t1 RENAME COLUMN b TO a;
```

##

-- 交换字段名 swap a and b

```sql
ALTER TABLE t1 RENAME COLUMN a TO b, RENAME COLUMN b TO a; -- 替换字段名 "rotate" a, b, c through a cycle
```

```sql
ALTER TABLE t1 RENAME COLUMN a TO b, RENAME COLUMN b TO c, RENAME COLUMN c TO a;
```

# 仅打算更改INT为 BIGINT：

```sql
ALTER TABLE t1 MODIFY col1 BIGINT;
```

# 这种说法从改变数据类型INT 来BIGINT，但它也下降了 UNSIGNED，DEFAULT和 COMMENT属性。要保留它们，语句必须明确包含它们：

```sql
ALTER TABLE t1 MODIFY col1 BIGINT UNSIGNED DEFAULT 1 COMMENT 'my column';
```

# 要将表默认字符集和所有字符列 ( CHAR, VARCHAR, TEXT) 更改为新字符集(慎用!!!)

```sql
ALTER TABLE tbl_name CONVERT TO CHARACTER SET charset_name;
```

# MODIFY更改单个列

```sql
ALTER TABLE t MODIFY latin1_text_col TEXT CHARACTER SET utf8;
ALTER TABLE t MODIFY latin1_varchar_col VARCHAR (M)
    CHARACTER SET utf8;
```

# 仅更改表的默认字符集

```sql
ALTER TABLE tbl_name DEFAULT CHARACTER SET charset_name;
```

# 创建索引

```sql
create
index indexName on tbl_name(b);
```

# 官方创建索引的语法

```sql
CREATE
[UNIQUE | FULLTEXT | SPATIAL] INDEX index_name
[index_type]
ON tbl_name (key_part,...)
[index_option]
[algorithm_option | lock_option] ...

key_part: {col_name [(length)] | (expr)} [ASC | DESC]

    index_option: {
    KEY_BLOCK_SIZE [=] value
    | index_type
    | WITH PARSER parser_name
    | COMMENT 'string'
    | {VISIBLE | INVISIBLE}
    | ENGINE_ATTRIBUTE [=] 'string'
    | SECONDARY_ENGINE_ATTRIBUTE [=] 'string'
    }

    index_type:
    USING {BTREE | HASH}

    algorithm_option:
    ALGORITHM [=] {DEFAULT | INPLACE | COPY}

    lock_option:

LOCK [=] {DEFAULT | NONE | SHARED | EXCLUSIVE}
```

# 官方删除索引的语法

```sql
    DROP
INDEX index_name ON tbl_name
        [algorithm_option | lock_option] ...

    algorithm_option:
        ALGORITHM [=] {DEFAULT | INPLACE | COPY}

    lock_option:
    LOCK [=] {DEFAULT | NONE | SHARED | EXCLUSIVE}
```
