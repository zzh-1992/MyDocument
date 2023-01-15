```sql
create table t_project
(
    id   int primary key auto_increment not null,
    p_id int,
    name varchar(20)
);
```

```sql
insert into t_project(id, p_id, name) value
    (1, 0, '一级项目 1'),
    (2, 0, '一级项目 2'),
    (3, 0, '一级项目 3'),
    (4, 1, '二级项目 4'),
    (5, 1, '二级项目 5'),
    (6, 1, '二级项目 6'),
    (7, 4, '三级项目 7'),
    (8, 5, '三级项目 8'),
    (9, 8, '四级项目 9');
```

```sql
# 递归-查询所有:从顶层到底层(附带层级路径p1,p2,p3...)
with recursive p_path (id, name, path) as (select id, name, cast(id as char(200))
                                           from t_project
                                           where id = 4

                                           union all

                                           select c.id, c.name, concat(pp.path, ',', c.id)
                                           from p_path pp
                                                    join t_project as c on c.p_id = pp.id)
select *
from p_path;
```

```sql
# 递归-查询所有:从顶层到底层
with RECURSIVE cte as
                   (select id, name,p_id
                    from t_project
                    where id = 1 # 顶层id
                    union all

                    select p.id, p.name,p.p_id
                    from t_project p
                             inner join cte c on c.id = p.p_id)
select id, name,p_id
from cte;
```

```sql
# 递归-查询所有:从底层到顶层(9,四级项目 9,"1,5,8,9")
with RECURSIVE cte as
                   (select id, name, p_id
                    from t_project
                    where id = 9

                    union all

                    select p.id, p.name, p.p_id
                    from t_project p
                             inner join cte c on c.p_id = p.id
                    where c.p_id != 0  #递归终止条件
                    )
select id, name, p_id
from cte;
```
