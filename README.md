# study-maven

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
