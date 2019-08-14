# Spring-Boot
数据库两种初始化方式

2 参考网址https://www.jianshu.com/p/743894b9e2fe?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation


1. 数据库初始化


Spring Boot提供两种方法来定义数据库的表结构以及添加数据。

使用Hibernate提供的工具来创建表结构，该机制会自动搜索@Entity实体对象并创建对应的表，然后使用import.sql文件导入测试数据；
利用旧的Spring JDBC，通过schema.sql文件定义数据库的表结构、通过data.sql导入测试数据。

JPA有个生成DDL的特性，并且可以设置为在数据库启动时运行，这可以通过两个外部属性进行控制：


spring.jpa.generate-ddl （ boolean ）控制该特性的关闭和开启，跟实现者没关系。

spring.jpa.hibernate.ddl-auto （ enum ）是一个Hibernate特性，用于更细力度的控制该行为。


2.1 利用Hibernate实现数据库初始化

spring.jpa.hibernate.ddl-auto 可以显式设置 spring.jpa.hibernate.ddl-auto ，标准的Hibernate属性值有 none ， validate ， update ， create ， create-drop。
Spring Boot 会根据数据库是否是内嵌类型，选择一个默认值。具体的关系见下图：



内嵌类型
数据库名称
默认值




内嵌
hsqldb, h2, derby
create-drop


非内嵌
余下的所有数据库
none



spring.jpa.hibernate.ddl-auto的四个属性的含义见下表：



属性值
作用




create
每次加载hibernate时都会删除上一次的生成的表，然后根据你的model类再重新来生成新表，哪怕两次没有任何改变也要这样执行，这就是导致数据库表数据丢失的一个重要原因。


create-drop
每次加载hibernate时根据model类生成表，但是sessionFactory一关闭，表就自动删除。


update
最常用的属性，第一次加载hibernate时根据model类会自动建立起表的结构（前提是先建立好数据库），以后加载hibernate时根据 model类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行。要注意的是当部署到服务器后，表结构是不会被马上建立起来的，是要等应用第一次运行起来后才会。


validate
每次加载hibernate时，验证创建数据库表结构，只会和数据库中的表进行比较，不会创建新表，但是会插入新值。



此外，启动时处于classpath根目录下的 import.sql文件会被执行(前提是ddl-auto属性被设置为 create 或create-drop)。这在demos或测试时很有用，但在生产环境中可能不期望这样。这是Hibernate的特性，和Spring没有一点关系。

2.2 利用Spring JDBC实现数据库初始化

Spring Boot可以自动创建DataSource的模式（DDL脚本）并初始化它（DML脚本），并从标准的位置 schema.sql 和 data.sql （位于classpath根目录）加载SQL，脚本的位置可以通过设置 spring.datasource.schema 和 spring.datasource.data 来改变。此外，Spring Boot将加载 schema-${platform}.sql和 data-${platform}.sql 文件（如果存在），在这里 platform 是 spring.datasource.platform 的值，比如，可以将它设置为数据库的供应商名称（ hsqldb , h2 , oracle , mysql , postgresql 等）。
Spring Boot默认启用Spring JDBC初始化快速失败特性，所以如果脚本导致异常产生，那应用程序将启动失败。能通过设置spring.datasource.continue-on-error的值来控制是否继续。一旦应用程序成熟并被部署了很多次，那该设置就很有用，例如，插入失败时意味着数据已经存在，也就没必要阻止应用继续运行。
如果想要在一个JPA应用中使用 schema.sql ，那如果Hibernate试图创建相同的表， ddl-auto=create-drop 将导致错误产生。为了避免那些错误，可以将 ddl-auto 设置为""或 none 。
最后要提的一点是，spring.datasource.initialize=false 可以阻止数据初始化。
