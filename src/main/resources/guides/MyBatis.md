## 1.简介

### 1.1

* JDBC
* Dbutils(QueryRunner)
* Spring JdbcTemplate
* Mybatis整体解决方案



整体解决方案：

* 事务控制

* 查询缓存

* 部分映射
* ...



###  1.2 JDBC VS Hibernate VS MyBatis

  

![image-20210305104255866](/Users/ian/Library/Application Support/typora-user-images/image-20210305104255866.png)

#### 1.2.1 总结

* JDBC 会导致 Java 代码 和 SQL 脚本高耦合。

* Hibernate 是全自动的，但是内部自动生成的 SQL 会给优化带来困难。

* Hibernate HQL 解决全映射的问题。全映射会导致数据库性能下降

* Hibernate 使用成本很高。 
* MySQL 是半自动化的，SQL 和 Java 分离。
* MySQL 是轻量级的

## 2.HelloWorld

![image-20210305110406383](/Users/ian/Library/Application Support/typora-user-images/image-20210305110406383.png)

### 2.1 步骤

1. 创建全局配置文件
2. 创建映射文件，并将映射文件注册到全局配置文件中
3. 创建 SqlSessionFactory
4. 创建 SqlSession
   1. SqlSession 代表与数据库的一次会话
   2. **非线程安全**，如果以多线程的方式使用，可能会产生资源竞争。
5. 执行查询：注意入参，SQL 的唯一标识为命名空间+语句ID、参数对象

![image-20210305115153420](/Users/ian/Library/Application Support/typora-user-images/image-20210305115153420.png) 

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305114928253.png" alt="image-20210305114928253" style="zoom:55%;" />

### 2.2 总结

* 字段名与数据库字段名不匹配的时候如何解决。
  * SQL 语句中给字段起别名
  * settings 配置开启驼峰命名规则

* **为了能让 MyBatis 在控制台打印 SQL 语句，需要依赖 log4j.jar，进一步地，如果要使用 log4j.jar，那么在 classpath 路径下必须有 log4j.xml 配置文件。**
* 方法结束需要关闭 SqlSession
* 全局配置文件可以没有，然后通过 Configuration Class 的方式获取；但是映射文件必须要有。

### 2.3 官方文档

> [参考官方文档](https://mybatis.org/mybatis-3/getting-started.html)

Every MyBatis application centers around an instance of SqlSessionFactory. A SqlSessionFactory instance can be acquired by using the SqlSessionFactoryBuilder. SqlSessionFactoryBuilder can build a SqlSessionFactory instance from an XML configuration file, or from a custom prepared instance of the Configuration class.

---

### 2.4 其他

* IntelliJ MyBatis 的开发插件

## 3.HelloWorld 改进 - 面向接口编程

* **接口和映射文件进行动态绑定**
  * 接口的全类名设置为配置文件的命名空间
  * 接口中方法的名字设置为配置文件中 `<select/>` 标签的 id
* **MyBatis 会为 Mapper 接口创建代理对象**
* 面向接口编程的好处
  * 相对于配置文件，类型检查
  * 解耦，不限制实现

## 4.全局配置文件详解

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305124140784.png" alt="image-20210305124140784" style="zoom:55%;" />

### 4.1 引入外部 properties

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305132239685.png" alt="image-20210305132239685" style="zoom:55%;" />

### 4.2 settings

> 非常重要的一个配置。

#### 开启驼峰命名规则

开启驼峰命名规则 `mapUnderscoreToCamelCase`，默认值为`false`。

### 4.3 typeAliases - 别名管理

* 目标是：**为 Java 类型起别名，减少全类名的输入工作。**
* 进一步减少配置工作：使用 package 进行批量起别名的工作
* 批量别名的时候可能会用冲突（包嵌套），这个时候可以使用注解 `@Alias` 对目标 Entity 指定新的别名，也可以使用 `<typeAlias>`给冲突的情况指定新的别名。
* 别名不区分大小写
* **MyBatis 为 JDK 内置的类提供了一套别名。**

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305133624116.png" alt="image-20210305133624116" style="zoom:50%;" />

### 4.4 typeHandlers - 类型处理器

* **数据库类型与 Java 类型之间映射的桥梁**
* 默认的类型处理器
* 需要额外注册的类型处理器：如 JSR 310 规范？？
* JSR 310 规范（Date and Time API）

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305134649694.png" alt="image-20210305134649694" style="zoom:50%;" />

### 4.5 plugins - 插件

* MyBatis 允许在一个 SQL 执行过程中，使用插件进行拦截。[官方文档  ](https://mybatis.org/mybatis-3/configuration.html#plugins)
* 该部分的学习需要了解 MyBatis 的总体运行流程和 MyBatis 的源码。
* 拦截的原理是使用动态代理。
* **一定要记住以下 4 种对象。**

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305135259644.png" alt="image-20210305135259644" style="zoom:100%;" />

### 4.6 environments

* MyBatis 可以配置多种环境，每个环境必须有 transactionManager 和 dataSource。
  * MyBatis 内置 2 种事务管理器：见下图(见 Configuration.java)
    * JDBC 和 MANAGED 都只是别名而已。
  * MyBatis 内置 3 种数据源：见下图
  * 与 Spring 整合时，事务管理器和数据源都使用 Spring 来处理，因此这部分配置仅需了解。
* `<environment id/>` 表示环境的唯一标识。使用 `<environments default/>`进行切换。
  * 这种环境的切换只是针对于数据库环境，但是一个应用往往有不同层次的不同环境。因此这个功能目前看来显得有些鸡肋。反而，Spring Boot 中提供的 yml 环境切换更加实用。因为 Spring 整合了许多技术。

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305140411137.png" alt="image-20210305140411137" style="zoom:50%;" />

### 4.7 databaseIdProvider

* MyBatis 根据不同的数据产商执行不同的 SQL 脚本。这是 MyBatis 对一致性的支持。

使用步骤如下:

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305141200624.png" alt="image-20210305141200624" style="zoom:50%;" />![image-20210305141633009](/Users/ian/Library/Application Support/typora-user-images/image-20210305141633009.png)

![image-20210305141655418](/Users/ian/Library/Application Support/typora-user-images/image-20210305141655418.png)

### 4.8 mapper - 注册映射

#### 第一种方法：注册文件

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305141935772.png" alt="image-20210305141935772" style="zoom:50%;" />![image-20210305142132350](/Users/ian/Library/Application Support/typora-user-images/image-20210305142132350.png)

#### 第二种方法：注册接口

* 映射文件和接口类必须放在一起，并且文件名和类名相同。

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305142400187.png" alt="image-20210305142400187" style="zoom:50%;" />

#### 第三种方法：注册接口，不建议使用。

* 没有映射文件，所有的 SQL 通过注解的方式写在接口方法上。
* 不建议使用：SQL 和 Java 耦合

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305142735092.png" alt="image-20210305142735092" style="zoom:50%;" />



<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305142735092.png" alt="image-20210305142735092" style="zoom:50%;" />



<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305142800215.png"  style="zoom:90%;" />



<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305143201336.png" alt="image-20210305143201336" style="zoom:50%;" />

#### 第四种方法：批量注册

* 映射文件和接口类需要放在一起。

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305143335048.png" alt="image-20210305143335048" style="zoom:50%;" />



<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305143521970.png" alt="image-20210305143521970" style="zoom:80%;" />

#### 小结

* 注册文件：不要求 XML 和类文件放在同一目录下
* 注册接口：要求 XML 和类文件放在同一目录下
* 批量注册：要求 XML 和类文件放在同一目录下

### 4.9 ObjectFactory 对象工厂

MyBatis 查询数据之后，使用对象工厂封装成 Java 对象。

## 5.映射文件详解

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305155610594.png" alt="image-20210305155610594" style="zoom:50%;" />

[官方文档](https://mybatis.org/mybatis-3/sqlmap-xml.html)

### 5.1 CRUD

* 映射文件中 parameterType：参数类型，可以省略。
* sqlSessionFactory.openSession()：获取的 SqlSession 不会自动提交事务。需要手动提交。
  * sqlSessionFactory.openSession(true)：自动提交。
*  MyBatis 允许增删改操作定义以下类型的返回值，MyBatis 会自动封装结果返回。
  * Integer/Long：返回影响的行数
  * Boolean：返回操作是否成功
  * void
  * 这种情况在 XML 中不需要定义返回值 returnType
* 为什么可以忽略 parameterType ? MyBatis 根据 TypeHandler 进行推断。

### 5.2 插入时获取自增的主键

#### MySQL 支持自增字段

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305162302880.png" alt="image-20210305162302880" />



<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305162302880.png" alt="image-20210305162302880" style="zoom:50%;" />

#### Oracle 使用序列来实现自增

* 先从序列中取出主键值
* 然后在插入记录时，使用该主键值

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210305163553993.png" alt="image-20210305163553993" style="zoom:50%;" />

## 6. MyBatis 的参数处理

### 6.1 参数处理规则

* 单个参数：MyBatis 不会做特殊处理。
* 多个参数
  * MyBatis 会将多个参数封装到 Map 对象中，Key 取 param1...paramN，Key 也可以取从 0 开始的值。
* 命名参数
  * 命名参数明确指示 MyBatis 将多个参数封装到 Map 实例中时，所使用的 Key。
* POJO
  * 直接使用 #{属性名} 取值
* Map
  * 多个参数没有对应的 POJO
* TO：transfer object
  * POJO 的一种其他情况，如 `class page {int index, int size}`
* Collection/List/Array
  * 仍然封装到 Map 实例中，但 Key 分别是 collection/list/array

### 6.2 源码分析

* MapperMethod.java
* ParamNameResovler.java

### 6.3 ${} 和 #{}

比较

* ${}：取的值直接拼接成 SQL，会有安全问题。
  * **原生 JDBC 不支持占位符的情况，我们可以使用 ${} 进行取值。如表名、排序不支持预编译。**
  * 案例
    * 按照年份进行分表查询：`select * from ${year}_salary`
    * 排序：`select * from emp order by ${f_name} ${order}`
* #{}：以预编译的形式来设置参数，即 PreparedStatement。
  * 支持更丰富的用法。
    * 使用 `javaType`指定参数的 Java 类型
    * 使用`jdbcType`指定参数的数据库类型
    * 其他的一些用法：mode(存储过程相关)，numericScale，resultMap，typeHandler，jdbcTypeName(同jdbcType)
  * **默认情况下，MyBatis 将 Java 的 null 值映射为原生 JDBC 的 OTHER 类型，但是 Oracle 不支持该类型。**
    * 解决方式1： `#{email,jdbcType=NULL};`
    * 解决方式2：进行全局配置`<setting name="jdbcTypeForNull" value="NULL"/>`

## 7. select 元素

### 7.1 重要说明

* parameterType：参数类型，可以不传。MyBatis 会根据 TypeHandler 自动推断。
* resultType：返回值类型
  * 不能与 resultMap 同时使用。

### 7.2 resultType

* 如果返回的是 List，那么定义集合中元素的类型。MyBatis 会将多条记录映射成 Java 对象，并封装成一个 List 返回。
* 需求：查询一条记录，并将该记录封装成 Map 返回。此时，resultType=map
  * key：列名
  * value：列名对应的值
* 需求：封装多条记录成 Map 返回
  * key：列名
  * value：记录对应的 Java 对象。
  * 需要使用 @MapKey("id") 来实现

### 7.3 resultMap 自定义结果集映射规则

* 不能与 resultType 同时使用。
* 支持级联。
* 支持关联对象。
  * 关联单个对象使用 association
  * 关联集合使用 collection
* 关联的对象支持分步查询。
  * 分步查询支持延迟加载。
  * 分步查询支持传多列的值：`{deptId=id}`

#### 分步查询

* 分步查询要支持延迟加载，需要在全局配置中加入以下配置。

```xml
<!-- 开启延迟加载和属性按需加载 -->
<setting name="lazyLoadingEnabled" value="true"/>
<setting name="aggressiveLazyLoading" value="false"/>
```

* 即使开启了全局延迟加载，但单条语句设置fetchType="eager"时，仍然会立即加载关联的对象。

## 第8节没有进行练习。

## 8. 动态SQL

**MySQL 的动态 SQL 特性基于 OGNL 表达式。**

* `if` 标签：判断
  * where 1=1
  * 使用 `where`标签，会去掉多余的 and，但 and 必须写在前面
    * `where` 封装查询条件
  * `set` 封装修改条件，会去掉多余的 `,`
  * 使用 `trim` 标签：自定义首部或尾部的添加、截取规则。
* `choose`，`when`，`otherwise`：分支选择，相当于 switch
* `foreach`
  * collection：指定要遍历的集合
  * item：变量
  * seperator：分隔符
  * open：头部拼接
  * close：尾部拼接
  * <img src="/Users/ian/Library/Application Support/typora-user-images/image-20210329014608919.png" alt="image-20210329014608919" style="zoom:50%;" />

### 批量保存

使用 `foreach` 实现。

* Oracle 有两种方式

#### Oracle 的第一种方式

Oracle 不支持语法 `values(),(),()`

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210329020407984.png" alt="image-20210329020407984" style="zoom:67%;" />



<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210329020945807.png" alt="image-20210329020945807" style="zoom:67%;" />

#### Oracle 的第二种方式-利用中间表

![image-20210329020749931](/Users/ian/Library/Application Support/typora-user-images/image-20210329020749931.png)

#### MySQL 的第一种做法

MySQL 支持语法`values(),(),()`。

![image-20210329015134341](/Users/ian/Library/Application Support/typora-user-images/image-20210329015134341.png)



![image-20210329015318486](/Users/ian/Library/Application Support/typora-user-images/image-20210329015318486.png)



#### MySQL 的第二种做法

![image-20210329015906614](/Users/ian/Library/Application Support/typora-user-images/image-20210329015906614.png)



![image-20210329015923704](/Users/ian/Library/Application Support/typora-user-images/image-20210329015923704.png)



<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210329015958314.png" alt="image-20210329015958314" style="zoom:67%;" />

### 内置参数

* `_parameter`
* `_databaseId`

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210329021514464.png" alt="image-20210329021514464" style="zoom:67%;" />

### bind

![image-20210329022115137](/Users/ian/Library/Application Support/typora-user-images/image-20210329022115137.png)



### sql-抽取可重用的 SQL 片段

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210329022332235.png" alt="image-20210329022332235" style="zoom:67%;" />



<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210329022352827.png" alt="image-20210329022352827" style="zoom:67%;" />

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210329022806814.png" alt="image-20210329022806814" style="zoom:67%;" />

## 9. 缓存



​    /**

​     \* 两级缓存：

​     \* 一级缓存：（本地缓存）：sqlSession级别的缓存。一级缓存是一直开启的；SqlSession级别的一个Map

​     \*      与数据库同一次会话期间查询到的数据会放在本地缓存中。

​     \*      以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查询数据库；

​     \* 

​     \*      一级缓存失效情况（没有使用到当前一级缓存的情况，效果就是，还需要再向数据库发出查询）：

​     \*      1、sqlSession不同。

​     \*      2、sqlSession相同，查询条件不同.(当前一级缓存中还没有这个数据)

​     \*      3、sqlSession相同，两次查询之间执行了增删改操作(这次增删改可能对当前数据有影响)

​     \*      4、sqlSession相同，手动清除了一级缓存（缓存清空）

​     \* 

​     \* 二级缓存：（全局缓存）：基于namespace级别的缓存：一个namespace对应一个二级缓存：

​     \*      工作机制：

​     \*      1、一个会话，查询一条数据，这个数据就会被放在当前会话的一级缓存中；

​     \*      2、如果会话关闭；一级缓存中的数据会被保存到二级缓存中；新的会话查询信息，就可以参照二级缓存中的内容；

​     \*      3、sqlSession===EmployeeMapper==>Employee

​     \*                      DepartmentMapper===>Department

​     \*          不同namespace查出的数据会放在自己对应的缓存中（map）

​     \*          效果：数据会从二级缓存中获取

​     \*              查出的数据都会被默认先放在一级缓存中。

​     \*              只有会话提交或者关闭以后，一级缓存中的数据才会转移到二级缓存中

​     \*      使用：

​     \*          1）、开启全局二级缓存配置：<setting name="cacheEnabled" value="true"/>

​     \*          2）、去mapper.xml中配置使用二级缓存：

​     \*              <cache></cache>

​     \*          3）、我们的POJO需要实现序列化接口

​     \*  

​     \* 和缓存有关的设置/属性：

​     \*          1）、cacheEnabled=true：false：关闭缓存（二级缓存关闭）(一级缓存一直可用的)

​     \*          2）、每个select标签都有useCache="true"：

​     \*                  false：不使用缓存（一级缓存依然使用，二级缓存不使用）

​     \*          3）、【每个增删改标签的：flushCache="true"：（一级二级都会清除）】

​     \*                  增删改执行完成后就会清楚缓存；

​     \*                  测试：flushCache="true"：一级缓存就清空了；二级也会被清除；

​     \*                  查询标签：flushCache="false"：

​     \*                      如果flushCache=true;每次查询之后都会清空缓存；缓存是没有被使用的；

​     \*          4）、sqlSession.clearCache();只是清楚当前session的一级缓存；

​     \*          5）、localCacheScope：本地缓存作用域：（一级缓存SESSION）；当前会话的所有数据保存在会话缓存中；

​     \*                              STATEMENT：可以禁用一级缓存；     

​     \*              

​     *第三方缓存整合：

​     \*      1）、导入第三方缓存包即可；

​     \*      2）、导入与第三方缓存整合的适配包；官方有；

​     \*      3）、mapper.xml中使用自定义缓存

​     \*      <cache type="org.mybatis.caches.ehcache.EhcacheCache"></cache>

​     *

<img src="/Users/ian/Library/Application Support/typora-user-images/image-20210329023904346.png" alt="image-20210329023904346" style="zoom:50%;" />

### 官方文档

[官方文档](https://mybatis.org/mybatis-3/configuration.html#)