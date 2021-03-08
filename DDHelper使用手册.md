# DDHelper使用手册

DDHepler是基于Eric Evans提出的领域驱动设计（Domain- Driven Design）结合近几年领域驱动设计在工业领域的落地实践，自主设计并研发的Java技术中台（框架）。它基于领域驱动设计理论，将开发过程中繁琐、重复的工作纳入框架。遵循约定优于配置（Convention over Configuration）的原则，助力于开发人员将经历集中在领域驱动建模而非繁琐的软件开发步骤与规范，实现快速、高效、按照既地进行领域驱动设计及编程。

## 1 领域服务

框架使用领域服务作为微服务间或与前端间调用的接口层。

前端接口可以调用领域服务来实现业务操作，各限界上下文之间或微服务之间也可使用领域服务进行相互的调用，目前微服务之间仅支持Http协议的互相调用，暂时不支持RPC协议的调用。

### 1.1 标注领域服务

可以在类上使用注解@DomainService来标注一个服务是一个领域服务。领域服务的命名规范为{DomainObject}+Service，其中Service为常量，DomainObject为领域对象的名称。可参考如下样例：

```java
@DomainService
public class StudentService {
    public Person get(String id){
        System.out.println("Getting Student id = ".concat(id));
      	return new Person("1", "张三");
    }
}
```

Student为领域对象，Service为常量。调用时，框架会自动解析StudentService的名称，除去固定的Service子串，将Student认定为领域对象的名称。

### 1.2 实现领域服务方法

领域服务类中的方法名即为领域服务的操作名称，其中的参数名称即为领域服务操作的参数名，对应了前端通过Http协议调用领域服务时需要传递的参数。它能够接受两种类型参数，一种是Java内的基本数据类型（目前支持了String、Integer、Long、LocalDateTime等），另一种是用户定义的领域对象或值对象。

另外需要注意的是，领域服务不能添加重载的方法，如果领域服务添加了名称相同的重载方法，框架将会在接收前端请求时随机调用其中一个，这可能会带来不必要的混乱。

如下介绍了不同参数方法的不同实现样例。

#### 1.2.1 参数全为基本数据类型的服务

参数全为基本数据类型的服务实现方式样例如下：

```java
@DomainService
public class StudentService {
    public Student get(String id){
        System.out.println("Getting Student id = ".concat(id));
      	return new Person("1", "张三");
    }
}
```

基本数据类型为参数的服务，形参可以有无穷多个，返回值可以是任意对象，返回值会使用json进行序列化后返回给前端。

参数全为数据类型的方法仅支持Get方法的调用。使用如下规范进行接口调用，params

> /{DomainObject}/{Operation}?param1=:value1&param2=:value2

param1的名称与param2的名称应与领域服务的方法中的形参名称一一对应，即可实现名称的自动装入。

针对上例可以通过Url进行调用：

> http://localhost:8080/demo/service/Student/get?id=1

返回值为固定的模版：

```json
{
  "retCode":0,
  "data":{
    "id":1,
    "name":"张三"
  }
}
```

其中retCode为0表示返回成功，data为领域服务方法的返回值经过json自动序列化后的结果。

#### 1.2.2 参数包含领域对象或值对象的服务

参数包含领域对象或值对象时，也可接收其他基本数据类型的形参，基本数据类型的形参传递方法可见1.2.1节。但方法仅能传递一个领域对象或值对象，这是Http协议中只能传递一次json的限制，也是方法单一职责原则的保证。

参数包含领域对象或值对象的服务方法实现方式样例如下：

```java
@DomainService
public class StudentService {
    public String add(Person person){
        System.out.println("Adding Student ".concat(person.toString()));
      	return "Success to add student";
    }
}
```

包含领域对象服务的调用仅能使用post方法调用。post的body参数格式须遵循application/json格式，且json值的键值与领域对象的属性值一一对应，调用样例如下所示：

> http://localhost:8080/demo/service/Student/add

post的body值：

```json
{
  "id":1,
  "name":"李四"
}
```

获取到的返回值样例：

```json
{
  "retCode":0,
  "msg":"Success to add student"
}
```

### 1.3 异常处理

### 1.4 权限拦截器

### 1.5 入参空值校验



## 2 CQRS支持

## 3 EventSource支持

## 4 领域对象的数据库定义

在领域驱动设计中，数据库已不再是程序运行的核心。在内存足够大、永远不会停机的计算机中，所有的数据存储在内存中即可，无需数据库操作。但现在没有这样强大的计算机，因此，在领域驱动设计中，数据库仅仅作为领域模型对象数据持久化的媒介，而非核心。

### 4.1 数据对象配置

在领域驱动编程中，区分了值对象和领域对象两种。但为了表示该对象为需要数据持久化存储或获取的对象，我们依然需要对它进行标注。为便于编码实现，统一使用@DataObject注解对需要持久化的数据对象进行标注，其中table为必填项，表明了该对象对应了数据库的哪张表。

```java
@DataObject(table = "student")
public class Student {
}
```

### 4.2 数据列配置

框架支持领域对象中的属性与数据库列的一一对应关系，开发者仅需遵循相应的编码规范：java属性使用驼峰命名法，数据库列使用下划线命名法。即可实现属性与数据列的直接对应。

例如：类属性中的cardNumber字段，对应数据库列名card_number或CARD_NUMBER列。

这在一定程度上规范了开发者要以一定开发规范起名，而非随意起名。

如果需要自定义类属性与数据库列名的对应关系，可以使用@Column({columnName})注解进行定义，如下所示：

```java
@DataObject(table = "student")
public class Student {
    private String id;
    private String name;
    @Column("CARD_NUMBER")
    private String cardNo;
}
```

使用该定义方法，类属性中的cardNo将会与数据库中的CARD_NUMBER字段一一对应。

### 4.3 主键配置

如需支持根据主键对数据进行查找，则需要对类的领域对象在数据库中的主键进行配置。主键可以使用@PrimaryKey注解进行配置。如下为注解配置的样例：

```java
@DataObject(table = "student")
public class Student {
    @PrimaryKey
    private String id;
    private String name;
    private String cardNumber;
}
```

针对有多个主键的情况，可以在多个列中配置@PrimaryKey注解，以标识主键。为区分它们的顺序，如存在多个主键时，需要设置其index的值，以保证框架能够区分主键顺序。

```java
public class Student {
    @PrimaryKey(index = 1)
    private String id;
    private String name;
    private String cardNumber;
}
```

## 5 领域对象的数据库操作

框架不建议所有数据在数据库层进行随意的组装，而应以领域模型为基础进行组装。这样既能使得程序逻辑与业务领域一致，又能反过来指导业务需求，使得业务需求更加合理。因此框架假定每次对数据的增、删、改的操作可以由几种一下几种对单一领域对象的基本操作组合而成：insert、delete、update和find。

框架提供了BasicDao类，基于ORM的思想，使得用户无需编写任何SQL，通过直接调用就可以完成对领域对象的几种基本操作。

### 5.1 BasicDao实例化

由于不同DataObject的数据库操作不同，因此需要在BasicDao实例化时的构造方法中指定其对应的DataObject。

```java
BasicDao studentDao = new BasicDao(Student.class);
```

### 5.2 数据查询

BasicDao为用户提供了多种数据查询方法，包括通过id查询、通过模版查询，通过id的列表批量查询。具体调用方式：

1. 通过id查询

   通过id查询，即通过在领域模型中通过@PrimaryKey设置的主键进行数据的查询。针对多个主键的情况，需要根据index设置的主键顺序，依次填入相应的筛选条件，多主键的情况下，有些id可填可不填，因此可能会导致查询出现多个数据的情况。所以设计返回值为List。如下为使用basicDao查询数据的方式：

   ```java
   BasicDao studentDao = new BasicDao(Student.class);
   List<Student> student = studentDao.find("1");
   ```

2. 通过模版查询

   通过模版查询，即利用领域对象自身作为模版对数据库实例进行匹配查询，由于根据模版查询到的数据不一定为一条，因此会返回一个领域对象的List。

   ```java
   BasicDao studentDao = new BasicDao(Student.class);
   Student student = new Student();
   student.setName("张三");
   List<Student> student = studentDao.findByTemplate(student);
   ```

3. 通过id列表批量查询

   通过id列表查询主要解决的是多次单id查询需求，带来的不必要的数据库性能开销，传入一个id组成的List即可。该方法无法适配多主键的表。

   ```java
   BasicDao studentDao = new BasicDao(Student.class);
   List<String> idList = Arrays.asList("1","2","3");
   List<Student> students = studentDao.findList(idList);
   ```

### 5.3 数据更新

数据更新仅支持根据模版更新，通过basicDao中的update方法实现

模版中的主键用来定位需要更新的数据，其他非空属性表示需更新的字段。对于为空的字段是会更新的。在领域模型中通过@PrimaryKey字段被定义为主键的属性（以下简称主属性）至少要有一个不为空。领域模型的更新操作样例如下：

```java
BasicDao studentDao = new BasicDao(Student.class);
Student student = new Student();
student.setName("李四");
List<Student> student = studentDao.update(student);
```

### 5.4 数据插入

### 5.5 数据删除