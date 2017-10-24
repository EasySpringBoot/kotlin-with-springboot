# 第13章 Kotlin 集成 SpringBoot 服务端开发

本章介绍Kotlin服务端开发的相关内容。首先，我们简单介绍一下Spring Boot服务端开发框架，快速给出一个 Restful Hello World的示例。然后，我们讲下 Kotlin 集成 Spring Boot 进行服务端开发的步骤，最后给出一个完整的 Web 应用开发实例。

## 13.1 SpringBoot 快速开始 Restful Hello World

Spring Boot 大大简化了使用 Spring 框架过程中的各种繁琐的配置, 另外可以更加方便的整合常用的工具链 (比如 Redis, Email, kafka, ElasticSearch, MyBatis, JPA) 等, 而缺点是集成度较高（事物都是两面性的），使用过程中不太容易了解底层，遇到问题了解决曲线比较陡峭。本节我们介绍怎样快速开始SpringBoot服务端开发。


### 13.1.1 Spring Initializr

工欲善其事必先利其器。我们使用 https://start.spring.io/ 可以直接自动生成 SpringBoot项目脚手架。如下图


![start.spring.io](http://upload-images.jianshu.io/upload_images/1233356-84524d5e4bfb1c21.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


点击“Switch to the full version ” ， 可以看到脚手架支持的工具链。

如果 https://start.spring.io/ 网络连不上，我们也可以自己搭建本地的 Spring Initializr服务。步骤如下
1. Git clone 源码到本机 https://github.com/spring-io/initializr
2. 源码根目录执行 $ ./mvnw clean install
3. 到initializr-service子项目目录下 cd initializr-service， 执行 ../mvnw spring-boot:run

即可看到启动日志
```
......
s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
i.s.i.service.InitializrService          : Started InitializrService in 15.192 seconds (JVM running for 15.882)
```
此时，我们本机浏览器访问 http://127.0.0.1:8080/ ，即可看到脚手架initializr页面。


### 13.1.2 创建SpringBoot项目

我们使用本地搭建的脚手架initializr， 页面上表单选项如下


![使用spring initializr创建SpringBoot项目](http://upload-images.jianshu.io/upload_images/1233356-afc659b67e3a702d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

首先 ，我们选择生成的是一个使用Gradle 构建的Kotlin项目，SpringBoot的版本号我们选择2.0.0(SNAPSHOT) 。

在 Spring Boot Starters 和 dependencies 选项中，我们选择 Web starter， 这个启动器里面包含了基本够用的Spring Web开发需要的东西：Tomcat 和 Spring MVC。

其余的项目元数据（Project Metadata）的配置（Bill Of Materials），我们可以从上面的图中看到。然后，点击“Generate Project” ，会自动下载一个项目的zip压缩包。解压导入IDEA中


![导入IDEA](http://upload-images.jianshu.io/upload_images/1233356-461ea8d6c80af877.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

因为我们使用的是Gradle构建项目，所以需要配置一下Gradle环境，这里我们使用的是Local gradle distribution , 选择对应的本地的 gradle 软件包目录。


#### 工程文件目录树

我们将得到如下一个样板工程，工程文件目录树如下
```
kotlin-with-springboot$ tree
.
├── build
│   └── kotlin-build
│       └── version.txt
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── kotlin-with-springboot.iml
└── src
    ├── main
    │   ├── java
    │   ├── kotlin
    │   │   └── com
    │   │       └── easy
    │   │           └── kotlin
    │   │               └── kotlinwithspringboot
    │   │                   └── KotlinWithSpringbootApplication.kt
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        ├── java
        ├── kotlin
        │   └── com
        │       └── easy
        │           └── kotlin
        │               └── kotlinwithspringboot
        │                   └── KotlinWithSpringbootApplicationTests.kt
        └── resources

23 directories, 10 files

```

其中，src/main/kotlin 是Kotlin源码放置目录。src/main/resources目录下面放置工程资源文件。application.properties 是工程全局的配置文件，static文件夹下面放置静态资源文件，templates目录下面放置视图模板文件。

#### build.gradle 配置文件

我们使用 Gradle 来构建项目。其中 build.gradle 配置文件类似 Maven中的pom.xml 配置文件。我们使用 Spring Initializr 自动生成的样板项目的默认配置如下

```groovy
buildscript {
	ext {
		kotlinVersion = '1.1.51'
		springBootVersion = '2.0.0.BUILD-SNAPSHOT'
	}
	repositories {
		mavenCentral()
		maven { url "https://repo.spring.io/snapshot" }
		maven { url "https://repo.spring.io/milestone" }
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
		classpath("org.jetbrains.kotlin:kotlin-allopen:${kotlinVersion}")
	}
}

apply plugin: 'kotlin'
apply plugin: 'kotlin-spring'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

group = 'com.easy.kotlin'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.8
compileKotlin {
	kotlinOptions.jvmTarget = "1.8"
}
compileTestKotlin {
	kotlinOptions.jvmTarget = "1.8"
}

repositories {
	mavenCentral()
	maven { url "https://repo.spring.io/snapshot" }
	maven { url "https://repo.spring.io/milestone" }
}


dependencies {
	compile('org.springframework.boot:spring-boot-starter-web')
	compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:${kotlinVersion}")
	compile("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
	testCompile('org.springframework.boot:spring-boot-starter-test')
}

```
其中，

spring-boot-gradle-plugin 是SpringBoot 集成 Gradle 的插件；
kotlin-gradle-plugin 是 Kotlin 集成Gradle的插件；
kotlin-allopen 是 Kotlin 集成 Spring 框架，把类全部设置为 open 的插件。因为Kotlin 的所有类及其成员默认情况下都是 final 的，也就是说你想要继承一个类，就要不断得写各种 open。而使用Java写的 Spring 框架中大量使用了继承和覆写，这个时候使用 kotlin-allopen 插件结合 kotlin-spring 插件，可以自动把 Spring 相关的所有注解的类设置为 open 。

spring-boot-starter-web 就是SpringBoot中提供的使用Spring框架进行Web应用开发的启动器。

kotlin-stdlib-jre8 是Kotlin使用Java 8 的库，kotlin-reflect 是 Kotlin 的反射库。

项目的整体依赖如下图所示


![项目的整体依赖](http://upload-images.jianshu.io/upload_images/1233356-c4ec51dda25fadaf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们可以看出，spring-boot-starter-web 中已经引入了我们所需要的 json 、tomcat 、validator 、webmvc （其中引入了Spring框架的核心web、context、aop、beans、expressions、core）等框架。


#### SpringBoot项目的入口类 KotlinWithSpringbootApplication

自动生成的 SpringBoot项目的入口类 KotlinWithSpringbootApplication如下

```kotlin
package com.easy.kotlin.kotlinwithspringboot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class KotlinWithSpringbootApplication

fun main(args: Array<String>) {
    SpringApplication.run(KotlinWithSpringbootApplication::class.java, *args)
}
```
其中，@SpringBootApplication注解是3个注解的组合，分别是@SpringBootConfiguration (背后使用的又是 @Configuration ),@EnableAutoConfiguration,@ComponentScan。由于这些注解一般都是一起使用，Spring Boot提供了这个@SpringBootApplication 统一的注解。这个注解的定义源码如下
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication {
    ...
}
```

main 函数中的 KotlinWithSpringbootApplication::class.java 是一个使用反射获取KotlinWithSpringbootApplication类的Java Class引用。这也正是我们在依赖中引入 kotlin-reflect 包的用途所在。

#### 写 Hello World 控制器

下面我们来实现一个简单的Hello World 控制器 。 首先新建 HelloWorldController Kotlin 类，代码实现如下

```kotlin
package com.easy.kotlin.kotlinwithspringboot

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class HelloWorldController {

    @RequestMapping("/")
    @ResponseBody
    fun home(): String {
        return "Hello World!"
    }

}

```


#### 启动运行

系统默认端口号是8080，我们在application.properties 中添加一行服务端口号的配置
```
server.port=8000
```
然后，直接启动入口类 KotlinWithSpringbootApplication , 可以看到启动日志
```
...o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8000 (http)
.e.k.k.KotlinWithSpringbootApplicationKt : Started KotlinWithSpringbootApplicationKt in 7.944 seconds (JVM running for 9.049)

```

也可以点击IDEA的Gradle工具栏里面的 Tasks - application - bootRun 执行


![Gradle工具栏 Tasks - application - bootRun ](http://upload-images.jianshu.io/upload_images/1233356-bfeb853ee7476bef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


启动完毕后，我们直接在浏览器中打开 http://127.0.0.1:8000/ ， 可以看到输出了 Hello World! 


![Hello World! ](http://upload-images.jianshu.io/upload_images/1233356-3f618c909a1872ef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)




## 13.2  综合实战：一个图片爬虫的Web应用实例





