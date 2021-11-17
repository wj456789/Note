# Mybatis

## 概念

是一个持久层框架，或称为ORM框架 。用来访问数据库，做数据持久化操作 。本质上只是对JDBC进行封装，简化JDBC繁琐的操作。

### 持久层 DAO

Data Access Object 数据访问对象 。用来对数据进行持久化操作，如将数据存入数据库、硬盘等，可以永久保存 。

### ORM

Object Relaltional Mapping 对象关系映射，Java程序和数据库之间的映射关系： 

类 ————>表、对象 ————> 一条数据、属性 ————> 列 

## MyBatis应用

```java
public static void main(String[] args) { 
    //创建SqlSession，称为持久化管理器，是MyBatis的核心 
    // 1.创建SqlSessionFactoryBuilder 
    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder(); 
    // 2.创建SqlSessionFactory 
    SqlSessionFactory factory = builder.build(Test01.class.getClassLoader().getResourceAsStream("­config.xml")); 
    // 3.创建SqlSession 
    SqlSession session = factory.openSession(); 
    // Connection conn = session.getConnection(); 
    User user=new User(); 
    user.setUsername("tom"); 
    user.setPassword("111"); 
    
    //方式1：纯配置文件，没有接口,直接读取mapper该文件      
    session.insert("aaa.insertUser",user); //参数1表示当前调用的方法，值为namespace.id，参数2表示方法的实参 
    
    //方式2：配置文件+接口,通过代理自动生成DAO的实现类 
    UserDao userDao=session.getMapper(UserDao.class); // 参数为接口的Class对象 
    OtherDao otherDao=session.getMapper(OtherDao.class);
    //System.out.println(userDao.getClass()); //DAO的实现类即是代理类
    userDao.insertUser(user); 
    
    session.commit(); //提交事务 
}	
```

## 配置文件

- 主配置文件
- 映射配置文件

### 主配置文件config.xml

```xml
<configuration> 

    <!-- environments：配置所有数据库环境。
    	default：指定默认使用的环境，取值为某一个environment的id--> 
    <environments default="hello"> 
    
        <!-- environment：配置某一个数据库环境，可以有多个。
        		id：指定该环境的唯一标识符--> 
        <environment id="hello"> 
        
            <!-- transactionManager：配置事务管理器 
        		type：指定事务管理器的类型，取值有两种： 
                 		jdbc：使用简单的jdbc事务操作，如开启、提交、回滚，在mybatis中，默认是关闭自动提交事务的，conn.setAutoCommit(false)
            		managed：将事务交给其他框架/容器来处理--> 
            <transactionManager type="jdbc"></transactionManager> 
            
            <!-- dataSource：配置数据源 
            		type：配置数据源的类型，取值有三种： 
            			UNPOOLED：简单的JDBC配置，未使用连接池，相当于DriverManager.getConnection(url,username,password) 
            			POOLED：使用连接池技术 
            			JNDI：通过外部容器来获取连接--> 
            <dataSource type="POOLED"> 
            	<property name="driver" value="com.mysql.jdbc.Driver"/> 
            	<property name="url" value="jdbc:mysql://localhost:3306/mybatis?useUnicode=true&;characterEncoding=utf­8"/> 
            	<property name="username" value="root"/> 
            	<property name="password" value=""/> 
            </dataSource> 
            
            <!-- 也可以同时引用外部的properties文件--> 
            <properties resource="datasource.properties"/> 
            <!--	jdbc.driver=com.mysql.jdbc.Driver 
            	jdbc.url=jdbc:mysql://localhost:3306/mybatis?useUnicode=true&;characterEncoding=utf-8
            	jdbc.username=root 
            	jdbc.password=
             --> 
        </environment> 
    </environments> 
    
    <!-- 注册当前工程中使用的映射文件--> 
    <mappers> 
    
    	<!--mapper：注册某一个mapper文件，可以有多个 
    		resource属性：指定映射文件的路径--> 
    	<mapper resource="mapper/UserMapper.xml"/> 
    </mappers> 
    
    <!-- 配置别名，为当前工程中的某些类指定别名--> 
    <typeAliases> 
    
        <!-- typeAlias：为某个类配置别名 
        		type属性：指定类名 
        		alias属性：指定类的别名--> 
        <typeAlias type="entity.User" alias="User"/>
        
        <!-- package：为某个包下的所有类配置别名 
        		name属性：指定包名，该包下的所有类的别名就是其类名(别名不区分大小写，但建议与类名完全一致)--> 
        <package name="entity"/> 
    </typeAliases> 
</configuration>
```

### 映射配置文件XxxMapper.xml

> 每一个mapper文件相当于原来的DAO实现类，每个实体类对应一个映射文件

```xml
<!-- namespace属性：指定当前mapper配置文件的唯一标识符 
    	如果是纯配置文件的方式，没有接口，值可以随便写 
    	如果是配置文件+接口的方式，值必须是对应接口的全名--> 
<mapper namespace="aaa">
 
    <!-- insert：用来执行添加操作 
    	id：表示当前的方法名 
    		如果是纯配置文件的方式，没有接口，值可以随便写，但必须唯一 
    		如果是配置文件+接口的方式，值必须与接口中的方法名相同 
    	parameterType：表示方法的参数类型 
    		如果参数是对象，可以使用类的全名 
    		如果参数是普通数据，可以使用mybatis中的别名 
      	useGeneratedKeys属性：是否返回主键，取值有两个： 
    		false：表示不返回主键，默认值 
    		true：表示返回主键，会自动将返回的主键绑定到参数对象的主键属性中 
    	keyProperty属性：指定对象的哪个属性为主键属性，即主键所映射的属性，必须指定
    	标签体：编写sql语句 
    		使用#{xxx}表示占位符 
    			如果参数是对象，则xxx为对象的属性名 
    			如果参数是普通数据，则xxx为参数名--> 
    <insert id="insertUser" parameterType="entity.User" useGeneratedKeys="true" keyProperty="id"> 
    	insert into 
    		t_user 
    		(username,password,phone,address) 
    	values
    		(#{username},#{password},#{phone},#{address}) 
    </insert> 
    
    <!-- update：执行修改操作--> 
    <update id="updateUser" parameterType="User"> 
        update t_user 
        set username=#{username},password=#{password},phone=#{phone},address=#{address} 
        where id=#{id} 
    </update>
    
    <!-- delete：执行删除操作--> 
    <delete id="deleteById" parameterType="int"> 
        delete from t_user where id=#{id} 
    </delete>
</mapper>
```

#### select+返回值

```xml
<mapper namespace="aaa">
    <!-- select：执行查询操作 
    	resultType属性：表示返回的结果类型 
    	如果返回的是一个对象，会自动进行映射,前提条件：查询结果的字段名必须与对象的属性名相同
            对于引用数据类型，都是将大写字母转小写，
            比如java.lang.String对应的别名是'string',java.util.HashMap对应的别名是 'hashmap',User的别名是'user'
            基本数据类型考虑到重复的问题，会在其前面加上 '_'，比如java.lang.Byte对应的别名是 '_byte'
     --> 
    <select id="selectById" parameterType="int" resultType="User"> 
        select 
        	id,username,password,phone,address 
        from
        	t_user 
        where 
        	id=#{id} 
    </select> 
    
    <!-- 返回值封装：使用别名，为查询结果的每个字段指定别名，与对象的属性名相同，此时相当于自动映射--> 
    <select id="selectById2" parameterType="int" resultType="User"> 
        select 
        	user_id id,user_username username,user_password password,user_phone phone,user_address address 
        from
        	t_user2 
        where 
        	user_id=#{id} 
    </select> 
    
    <!-- 返回值封装：引用一个ResultMap属性，使用该ResultMap进行手动映射,其值为已存在的某一个ResultMap标签的id值--> 
    <select id="selectById3" parameterType="int" resultMap="userMap"> 
        select 
        	user_id,user_username,user_password,user_phone,user_address 
        from
        	t_user2 
        where 
        	user_id=#{id} 
    </select>
    <!-- resultMap：定义结果映射，将数据库的字段与对象的属性进行映射 
    	id属性：指定该resultMap的唯一标识符 
    	type属性：指定映射的对象类型--> 
    <resultMap id="userMap" type="User"> 
        <!-- id：配置主键映射，result：配置其他映射 
        		property属性：指定映射的属性名
          		column属性：指定映射的字段名，或者字段取的别名--> 
        <id property="id" column="user_id"/> 
        <result property="username" column="user_username"/> 
        <result property="password" column="user_password"/> 
        <result property="phone" column="user_phone"/> 
        <result property="address" column="user_address"/> 
    </resultMap>	
    
    
    <!-- 返回值封装：当方法返回值为对象集合时，resultType指定的是集合中元素的类型，而非集合本身--> 
    <select id="selectAll" resultType="User"> 
        <!-- include：用于引用sql代码段 
        		refid：指定要引用的sql代码段的id值--> 
        select 
        	<include refid="userColumn"/> 
        from
        	t_user 
    </select> 
    <!-- sql：定义sql代码段，便于复用 
    	id属性：指定该sql代码段的唯一标识符--> 
    <sql id="userColumn"> 
        id,username,password,phone,address
    </sql>
    
    <!-- 返回值封装：当方法返回值为List<Map<String,Object>>时，resultType="map"--> 
    <select id="getAllUsersList" resultType="map">
        select * from t_user
    </select>
    
    <!-- 返回值封装：当方法返回值为map时-->
    <!-- 如果查询的结果是一条，我们可以把查询的数据以{表字段名，对应的值}方式存入到Map中
    
         //根据 id 查询信息，并把结果信息封装成 Map 
         Map<String, Object> getUserAsMapById(Integer id);
         
         注意这里的 resultType 返回值类型是 'map'
         结果是{id=1,username=zhangsan,password=123456,phone=12345678912,address=diqiu}
    -->
    <select id="getUserAsMapById" resultType="map">
        select * from t_user where id = #{id}
    </select>
    
    <!-- 如果查询的结果是多条数据，我们也可以把查询的数据以{表中某一字段名, JavaBean}方式来封装成Map
    
        //@MapKey 中的值表示用数据库中的哪个字段名作 key
        @MapKey("id")
        Map<Integer, Employee> getAllUsersAsMap();
        
        注意 resultType 返回值类型，不再是 'map'，而是 Map 的 value 对应的 JavaBean 类型
        结果是{1={id=1,username=zhangsan,...},2={id=2,username=lisi,...},...}
    -->
    <select id="getAllUsersAsMap" resultType="user">
        select * from t_user
    </select>
</mapper>
```

#### select+请求参数

```xml
<mapper namespace="aaa">
    <!-- 请求参数封装：使用参数的索引，索引从0开始，如#{0}表示第一个参数，#{1}表示第二个参数--> 
    <select id="selectByUsernameAndPassword" resultType="User"> 
        select 
        	<include refid="userColumn"/> 
        from 
        	t_user 
        where 
        	username=#{0} and password=#{1} 
    </select> 
    
    <!-- 请求参数封装：使用@Param()定义的名称来引用指定的参数--> 
    <!-- 使用@Param()注解，标注在参数前，为参数指定占位符名称 
         public User selectByUsernameAndPassword2(@Param("username") String username,@Param("password") String password);--> 
    <select id="selectByUsernameAndPassword2" resultType="User"> 
        select 
        	<include refid="userColumn"/> 
        from 
        	t_user 
        where 
        	username=#{username} and password=#{password} 
    </select>
    
    <!-- 请求参数封装：将多个参数封装为一个对象，然后传递该对象--> 
    <select id="selectByUsernameAndPassword3" parameterType="User" resultType="User"> 
        select 
        	<include refid="userColumn"/> 
        from 
        	t_user 
        where 
        	username=#{username} and password=#{password} 
    </select> 
    
    <!-- 请求参数封装：将多个参数封装成一个Map集合，在#{}占位符中根据key获取value--> 
    <select id="selectByUsernameAndPassword4" resultType="User"> 
        select 
        	<include refid="userColumn"/> 
        from 
        	t_user 
        where 
        	username=#{username} and password=#{password} 
    </select> 
    <!-- Map map=new HashMap<String,Object>(); 
         map.put("username","alice"); 
         map.put("password","111"); 
         User user = userDao.selectByUsernameAndPassword4(map);-->
         
    <!-- 模糊查询：手动拼接模糊查询的字段-->
    <select id="selectByUsername" resultType="User"> 
        select 
        	<include refid="userColumn"/> 
        from 
        	t_user 
        where 
        	username like #{username} 
    </select> 
    <!-- String username="a"; 
         List<User> users = userDao.selectByUsername("%" + username + "%");-->
         
    <!-- 模糊查询：使用bind对参数进行额外的处理 -->
    <select id="selectByUsername2" resultType="User"> 
        select 
        	<include refid="userColumn"/> 
        from 
        	t_user 
        <!-- bind：创建一个变量，并为其绑定值 
    		name属性：指定变量名 
        		value属性：指定要绑定的值，可以使用_parameter获取当前方法的参数,也可以直接使用abc--> 
        <bind name="abc" value="'%'+_parameter+'%'"/> 
        where username like #{abc} 
    </select> 
    <!-- String username="a"; 
         List<User> users = userDao.selectByUsername2(username);-->
         
    <!-- 内置参数：_databaseId，配置多数据库中配置的数据库的别名-->
    <select id="getUsersByInnerParam" resultType="User"> 
        <if test="_databasesId=='mysql'">
            select * from sb_users
        </if>
        <if test="_databasesId=='oracle'">
            select * from sb_users
        </if>
    </select> 
    
    <!-- 内置参数：_parameter，接口方法传过来的参数的整体,
            一是如果接口方法传来的是单个参数，_parameter就是这个单参数的值，
            另一种情况是接口方法传来了多个参数的时候会被封装为一个map对象，_parameter就是这个map对象的引用变量-->
    <select id="getUsersByInnerParam" resultType="User"> 
        <if test="_databasesId=='mysql'">
            select * from sb_users
            <if test="_parameter!=null">
                where username=#{_parameter.username}
            </if>
        </if>
    </select> 
</mapper>
```

#### 多表关系映射

> 关联关系：
>
> - 多对一，多个员工都在同一个部门中
> - 一对多，一个部门中可以有多个员工
> - 一对一，一个员工只有一个身份证
> - 多对多，一个员工同时开发多个项目，一个项目也可以同时有多个员工开发

```mysql
#数据库设计
create table t_dept(
    id int primary key auto_increment,
    name varchar(50)
)charset utf8;

create table t_emp(
    id int primary key auto_increment,
    name varchar(30),
    salary double,
    dept_id int,
    foreign key (dept_id) references t_dept(id)
)charset utf8;
```

##### 多对一

> 在一个对象中定义另一个对象的属性

```java
//实体类
//多个员工对应一个部门,查询多个员工，每个员工对应一个部门
//员工类
public class Emp{
    private int id;
    private String name;
    private int salary,
    private Dept dept;
}
//部门类
public class Dept{
    private int id;
    private String name;
}
```

```xml
<mapper namespace="dao.EmpDao">

    <select id="selectAll" resultMap="empMapper">
        select 
            <include refid="empColumn"/>
        from 
            t_emp e
        left join 
            t_dept d on e.dept_id = d.id
    </select>

    <!-- 直接使用关联属性：association标签-->
    <resultMap id="empMapper" type="Emp">
        <id property="id" column="id"/>
        <result property="name" column="name"/>
        <result property="salary" column="salary"/>
        <!-- association：用于配置关联属性，多对一的关系
                property属性：当前需要映射的是对象中的哪个属性
                javaType属性：当前映射的属性的Java类型
                标签体：对当前映射的属性所在的表进行映射-->
        <association property="dept" javaType="Dept">
            <id property="id" column="deptId"/>
            <result property="name" column="deptName"/>
        </association>
    </resultMap>
    
    <!-- 使用关联的嵌套结果：使用assocition的resultMap属性，引用其他的resultMap-->
    <resultMap id="empMapper2" type="Emp">
        <id property="id" column="id"/>
        <result property="name" column="name"/>
        <result property="salary" column="salary"/>
        <!-- resultMap属性：引用其他的ResultMap映射配置
                      值为：resultMap所在Mapper文件的namespace.resultMap的id值-->
        <association property="dept" javaType="Dept" resultMap="dao.DeptDao.deptMapper"/>
    </resultMap>
    
    <!-- 使用关联的嵌套查询:使用assocition的select属性，引用其他select，通过多个单表查询来实现，效率低，会进行多次查询，存在N+1问题-->
    <resultMap id="empMapper3" type="Emp">
        <id property="id" column="id"/>
        <result property="name" column="name"/>
        <result property="salary" column="salary"/>
        <!--select属性：引用其他的select查询配置
                  值为：select所在Mapper文件的namespace.select的id值
                      column属性：当前查询的某列，作为查询条件，传递给引用的select查询配置的参数-->
        <association property="dept" javaType="Dept" select="dao.DeptDao.selectById" column="dept_id"/>
    </resultMap>
</mapper>

<mapper namespace="dao.DeptDao">
    <select id="selectById" parameterType="int" resultType="Dept">
        select
            id,name
        from 
            t_dept
        where 
            id=#{id}
    </select>
    
    <resultMap id="deptMapper" type="Dept">
        <id property="id" column="deptId"/>
        <result property="name" column="deptName"/>
    </resultMap>
</mapper>
```

##### 一对多

> 在一个对象中定义另一个对象的集合

```java
//实体类
//一个部门有多个员工,查询一个部门，每个部门有多个员工
//员工类
public class Emp{
    private int id;
    private String name;
    private int salary;
}
//部门类
public class Dept{
    private int id;
    private String name;
    private List<Emp> emps;
}
```

















