# study-maven
http://books.sonatype.com/mvnex-book/reference/index.html

1创建一个简单工程
mvn archetype:generate -DgroupId=org.sonatype.mavenbook -DartifactId=simple -DpackageName=org.sonatype.mavenbook -Dversion=1.0-SNAPSHOT

安装
cd到工程目录simple
mvn install

运行
cd 编译发布目录simple\target
java -cp simple-1.0-SNAPSHOT.jar org.sonatype.mavenbook.App

2每个maven工程中的pom有道父工程，maven配置，用户配置，以及子集(profile)的影响。通过
mvn help:effective-pom
可以看到实际的pom

3 Maven的生命周期由多个插件完成，Maven插件包含多个目标。目标引用的格式一般如下：
  pluginId:goalId -DparamName=paramValue

4 Maven生命周期
Maven有三套相互独立的生命周期，请注意这里说的是“三套”，而且“相互独立”，初学者容易将Maven的生命周期看成一个整体，其实不然。这三套生命周期分别是：
Clean Lifecycle 在进行真正的构建之前进行一些清理工作。
Default Lifecycle 构建的核心部分，编译，测试，打包，部署等等。
Site Lifecycle 生成项目报告，站点，发布站点。

可以直接运行 mvn clean install site 运行所有这三套生命周期。


Clean生命周期一共包含了三个阶段：
pre-clean  执行一些需要在clean之前完成的工作
clean  移除所有上一次构建生成的文件
post-clean  执行一些需要在clean之后立刻完成的工作

在一个生命周期中，运行某个阶段的时候，它之前的所有阶段都会被运行，也就是说，mvn clean 等同于 mvn pre-clean clean ，如果我们运行 mvn post-clean ，那么 pre-clean，clean 都会被运行。

Site生命周期的各个阶段：
pre-site     执行一些需要在生成站点文档之前完成的工作
site    生成项目的站点文档
post-site     执行一些需要在生成站点文档之后完成的工作，并且为部署做准备
site-deploy     将生成的站点文档部署到特定的服务器上

Maven的最重要的Default生命周期，绝大部分工作都发生在这个生命周期中，这里，我只解释一些比较重要和常用的阶段：
validate
generate-sources
process-sources
generate-resources
process-resources     复制并处理资源文件，至目标目录，准备打包。
compile     编译项目的源代码。
process-classes
generate-test-sources 
process-test-sources 
generate-test-resources
process-test-resources     复制并处理资源文件，至目标测试目录。
test-compile     编译测试源代码。
process-test-classes
test     使用合适的单元测试框架运行测试。这些测试代码不会被打包或部署。
prepare-package
package     接受编译好的代码，打包成可发布的格式，如 JAR 。
pre-integration-test
integration-test
post-integration-test
verify
install     将包安装至本地仓库，以让其它项目依赖。
deploy     将最终的包复制到远程的仓库，以让其它开发人员与项目共享。

5 工程的唯一坐标为groupId:artifactId:version:packaging

6 Maven仓库即是一个外部仓库的本地缓存，又是本地工程能够相互依赖的机制。

7依赖管理。
Maven支持传递依赖(transitive dependency)是Maven最强大的特征之一。
当安装你的的工程到本地仓库时，Maven发布了一个稍微修改了的工程POM文件到jar包所在目录。存储一个POM文件再仓库中可以给别的工程提供关于次工程的信息，最重要的信息有它所依赖的其他工程。
Maven的依赖不仅是一个jar文件，它是一个POM文件，其申明了对其他工程的依赖。
Maven提供了不同的依赖范围（dependency scopes)。当一个依赖声明为test范围，表示它不能用于Complier插件的compile目标，它将被添加到compiler:testCompiler和surefire:test目标的类路径中。使用provided范围可以把某种依赖排除再WAR文件之外。

8 创建一个用户化的工程
mvn archetype:generate -DgroupId=org.sonatype.mavenbook.custom -DartifactId=simple-weather -Dversion=1.0

9 获得某个插件的描述
mvn help:describe -Dplugin=exec -Dfull

10 exec插件可以方面的执行程序，而不用声明类路径
mvn exec:java -Dexec.mainClass=org.sonatype.mavenbook.weather.Main

11 查看依赖关系
mvn dependency:resolve

12 可通过配置忽略测试失败
一般情况，Maven遇到一个失败时，构建就会停止。可配置testFailureIgnore属性为true来配置SurefirePlugin，使得即使遇到测试失败，也能继续进行构建
<project>
  <build>  
     <plugins>
	    <plugin>
		   <groupId>org.apache.maven.plugins</groupId>
		   <artifactId>maven-surefire-plugin</artifactId>
		   <configuration>
		      <testFailureIgnore>true</testFailureIgnore>
		   </configuration>
		</plugin>
	 </plugins>
  </build>
</project>
同样可通过命令行设置
mvn test -Dmaven.test.failure.ignore=true

跳过测试的命令行设置：
mvn install -Dmaven.test.skip=true
跳过测试POM配置:
<project>
  <build>  
     <plugins>
	    <plugin>
		   <groupId>org.apache.maven.plugins</groupId>
		   <artifactId>maven-surefire-plugin</artifactId>
		   <configuration>
		      <skip>true</skip>
		   </configuration>
		</plugin>
	 </plugins>
  </build>
</project>

13 通过Maven Assembly 插件自定义应用的发布
需要再POM中添加Maven Assembly描述
<project>
  [...]
  <build>  
     <plugins>
	    <plugin>
		   <artifactId>maven-assembly-plugin</artifactId>
		   <configuration>
		     <descriptorRefs>
		      <descriptorRef>jar-with-dependencies</descriptorRef>
			 </descriptorRefs>
		   </configuration>
		</plugin>
	 </plugins>
  </build>
  [...]
</project>

执行组装命令如下：
mvn install assembly:assembly
maven执行到install生命周期(lifecycle)后，又执行了assembly:assembly目标(goal)

通过上面的配置生成单一的可执行的jar包

通过把assembly:assembly目标绑定到Maven的package生命周期做法更常规，配置如下：
<project>
  [...]
  <build>  
     <plugins>
	    <plugin>
		   <artifactId>maven-assembly-plugin</artifactId>
		   <configuration>
		     <descriptorRefs>
		      <descriptorRef>jar-with-dependencies</descriptorRef>
			 </descriptorRefs>
		   </configuration>
		   <executions>
		     <execution>
			   <id>simple-command</id>
			   <phase>package</phase>
			   <goals> 
			     <goal>attached</goal>
			   </goals>
			 </execution>
		   </executions>
		</plugin>
	 </plugins>
  </build>
  [...]
</project>

13 创建一个简单的web工程
mvn archetype:generate -DgroupId=org.sonatype.mavenbook.simpleweb -DartifactId=simple-webapp -DpackageName=org.sonatype.mavenbook -Dversion=1.0-SNAPSHOT
选择maven-archetype-webapp，可能1010
修改编译时依赖的java版本为1.8
  <build>
    <finalName>simple-webapp</finalName>
	<plugins>
	  <plugin>
	    <artifactId>maven-compiler-plugin</artifactId>
		<version>3.3</version>
		<configuration>
		  <soure>1.8<soure>
		  <target>1.8<target>
		</configuration>
	  </plugin>
	</plugins>
  </build>

通过jetty Servle容器运行web应用
mvn jetty:run
注意在window操作系统中，用jetty运行web引用，本地仓库的路径中不能包含空格。工程运行后的url为http://localhost:8080/simple-webapp

添加对Servlet Api的依赖
<project>
  [...]
  <dependencies>
	<dependency>
	  <groupId>javax.servlet</groupId>
	  <artifactId>servlet-api</artifactId>
	  <version>2.4</version>
	  <scope>provided</scope>
	</dependency>
  </dependencies>
  [...]
</project>

privided范围声明，表示jar包不能包含再WAR内

14 生成获取天气的客户端
生成前先删除WeatherWebService.xml中的ws:import,否则无法生成客户端
wsimport -s src/main/java -p com.bj.znd.weather src/main/resources/WeatherWebService.xml
运行：
mvn exec:java -Dexec.mainClass=com.bj.znd.App -Dexec.args="上海" 

15 父子模块
父工程只是提供一个pom文件，在pom文件中包含子模块，父工程的一些配置将会被所有子模块所继承。父工程pom文件类似如下：
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.sonatype.mavenbook.multi</groupId>
  <version>1.0</version>
  <artifactId>simple-parent</artifactId>
  <packaging>pom</packaging>
  <name>Multi Chapter Simple Parent Project</name>
 
  <modules>
    <module>simple-weather</module>
    <module>simple-webapp</module>
  </modules>

  <build>
    <pluginManagement>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.5</source>
          <target>1.5</target>
        </configuration>
      </plugin>
    </plugins>
    </pluginManagement>
  </build>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>

子模块通Maven坐标引用父工程的
<project>
  [...]
  <parent>
    <groupId>org.sonatype.mavenbook.multi</groupId>
    <artifactId>simple-parent</artifactId>
    <version>1.0</version>
  </parent>
  [...]
</project>

16 创建模板
mvn archetype:generate -DgroupId=com.znd.plugins \
-DartifactId=statis-maven-plugin \
-DpackageName=com.znd.plugins \
DarchetypeArtifactId=maven-archetype-plugin \
-Dversion=1.0 

缩短命令行
有几种方式缩短输入：
1,如果运行本地仓库安装的最新版插件，可以省略版本号。使用”mvn sample.plugin:hello-Maven-plugin:sayhi”运行。
2,赋予插件一个短前缀，如mvn hello:sayhi。如果按照${prefix}-maven-plugin的命名方式（如果插件是Apache Maven的官方插件用maven-${prefix}-plugin）这是自动完成的。也可以通过额外的配置设置前缀，更多信息参见Introduction to Plugin Prefix Mapping。
3,最后，可以把插件的组id加入默认搜索的组id列表。这种方式需要在${user.home}/.m2/settings.xml中增加如下配置：
<pluginGroups>
  <pluginGroup>sample.plugin</pluginGroup>
</pluginGroups>
此时，可以用”mvn hello:sayhi”运行Mojo了。

将Mojo关联到构建生命周期
你也可以配置插件关联到构建生命周期某个特定阶段的指定目标，示例如下：
<build>
   <plugins>
     <plugin>
       <groupId>sample.plugin</groupId>
       <artifactId>hello-maven-plugin</artifactId>
       <version>1.0-SNAPSHOT</version>
       <executions>
         <execution>
           <phase>compile</phase>
           <goals>
             <goal>sayhi</goal>
           </goals>
         </execution>
       </executions>
     </plugin>
   </plugins>
 </build>
这样Java代码编译时这个简单Mojo就会执行。更多绑定Mojo到生命周期阶段的信息

17 优化依赖
1）把所有的共同依赖添加到依赖管理中(dependencyManagement)
2) 用内建的工程version和groupId用于引用兄弟工程
通过dependencyManager声明可以避免依赖复制和子模块依赖不匹配。
在父工程中申明依赖管理
<project>
	...
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework</groupId>
				<artifactId>spring</artifactId>
				<version>2.0.7</version>
			</dependency>
			<dependency>
				<groupId>org.apache.velocity</groupId>
				<artifactId>velocity</artifactId>
				<version>1.5</version>
			</dependency>
			<dependency>
				<groupId>org.hibernate</groupId>
				<artifactId>hibernate-annotations</artifactId>
				<version>3.3.0.ga</version>
			</dependency>
			<dependency>
				<groupId>org.hibernate</groupId>
				<artifactId>hibernate-commons-annotations</artifactId>
				<version>3.3.0.ga</version>
			</dependency>
			<dependency>
				<groupId>org.hibernate</groupId>
				<artifactId>hibernate</artifactId>
				<version>3.2.5.ga</version>
				<exclusions>
					<exclusion>
						<groupId>javax.transaction</groupId>
						<artifactId>jta</artifactId>
					</exclusion>
				</exclusions>
			</dependency>
		</dependencies>
	</dependencyManagement>
	...
</project>
简化后的子工程配置如下。区别为子工程不用申明版本号了
<project>
...
<dependencies>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-annotations</artifactId>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate</artifactId>
		</dependency>
	</dependencies>
...
</project>
在父工程中修复hibernate-annotations和hibernate-commons-annotations的版本重复声明
<project>
	...
	<properties>
		<hibernate.annotations.version>3.3.0.ga</hibernate.annotations.version>
	</properties>
	<dependencyManagement>
		...
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-annotations</artifactId>
			<version>${hibernate.annotations.version}</version>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-commons-annotations</artifactId>
			<version>${hibernate.annotations.version}</version>
		</dependency>
		...
	</dependencyManagement>
	...
</project>

在父工程定义子工程的版本，或者用内建属性保证子版本号一致性：
<project>
	...
	<dependencies>
		...
		<dependency>
			<groupId>${project.groupId}</groupId>
			<artifactId>simple-weather</artifactId>
			<version>${project.version}</version>
		</dependency>
		<dependency>
			<groupId>${project.groupId}</groupId>
			<artifactId>simple-persist</artifactId>
			<version>${project.version}</version>
		</dependency>
		...
	</dependencies>
	...
</project>

18 插件管理
类似于依赖管理，可在父工程中声明插件管理，用来简化字工程的插件声明
<project>
...
<build>
		<pluginManagement>
			<plugins>
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-compiler-plugin</artifactId>
					<configuration>
						<source>1.5</source>
						<target>1.5</target>
					</configuration>
				</plugin>
				<plugin>
					<groupId>org.codehaus.mojo</groupId>
					<artifactId>hibernate3-maven-plugin</artifactId>
					<version>2.1</version>
					<configuration>
						<components>
							<component>
								<name>hbm2ddl</name>
								<implementation>annotationconfiguration</implementation>
							</component>
						</components>
					</configuration>
					<dependencies>
						<dependency>
							<groupId>hsqldb</groupId>
							<artifactId>hsqldb</artifactId>
							<version>${hsqldb.version}</version>
						</dependency>
					</dependencies>
				</plugin>
			</plugins>
		</pluginManagement>
	</build>
...
</project>


19 由于Maven支持传递依赖，因此最好显示声明工程代码的所有依赖，依赖升级后，传递依赖失效。
通过dependency插件可以分析使用了的依赖，但未声明的依赖。
dependency:analyze  分析引用了的未直接声明的依赖
dependency:tree     列出所有直接和传递依赖


20,Maven增加外部jar
使用springboot插件的条件下
时system域下声明的jar包能添加到最终的单一jar包内
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<includeSystemScope>true</includeSystemScope>
				</configuration>
			</plugin>
		</plugins>
	</build>
	
	依赖生命
	<dependency>
		<groupId>com.znd</groupId>
		<artifactId>com.znd.memdb</artifactId>
		<version>1.0</version>
		<scope>system</scope>
		<systemPath>${project.basedir}/lib/ZhongNDMemDB.jar</systemPath>
	</dependency>	
	
	发布jar到一个远程仓库中
   http://maven.apache.org/guides/mini/guide-3rd-party-jars-remote.html
   mvn deploy:deploy-file -DgroupId=<group-id> \
  -DartifactId=<artifact-id> \
  -Dversion=<version> \
  -Dpackaging=<type-of-packaging> \
  -Dfile=<path-to-file> \
  -DrepositoryId=<id-to-map-on-server-section-of-settings.xml> \
  -Durl=<url-of-the-repository-to-deploy>
   
