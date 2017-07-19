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
