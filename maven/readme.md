### 1. Add arguments of remote maven repository 

* Dynamically passed from command line（priority=3）：

  ```
  ./gradlew :xxx:publishReleasePublicationToMavenRepository -PREPO_URL=http://xxx/ -PREPO_USERNAME=xxx -PREPO_PASSWORD=xxxxxxxx
  ```

* Statically defined in the **`gradle.properties`** file of module（priority=2）：

  ```
  REPO_URL=http://xxx/
  REPO_USERNAME=xxx
  REPO_PASSWORD=xxxxxxxx
  ```

* Statically defined in the **`gradle.properties`** file of project（priority=1）：

  ```
  REPO_URL=http://xxx/
  REPO_USERNAME=xxx
  REPO_PASSWORD=xxxxxxxx
  ```
> 优先级高的会覆盖优先级低的

### 2. Add pom arguments of each Modules

* Dynamically passed from command line（priority=3）：

  ```
  ./gradlew :xxx:publishReleasePublicationToMavenRepository -PPOM_GROUP_ID=xxx.xxx.xxx -PPOM_ARTIFACT_ID=xxx -PPOM_VERSION=x.x.x -PPOM_PACKAGING=aar -PPOM_DESCRIPTION=xxx
  ```
  
* Statically defined in the **`gradle.properties`** file of each modules（priority=2）：

  ```
  1. define arguments
  POM_GROUP_ID=xxx.xxx.xxx
  POM_ARTIFACT_ID=xxx
  POM_VERSION=x.x.x
  POM_PACKAGING=aar //not need when gradle version >= 4.0
  POM_DESCRIPTION="xxx"
  
  2. apply script
      if (rootProject.name == "xxx") {
      	apply from: 'publish-maven.gradle'
      }
  ```
  
* Merge and configured together in the **`config.gradle`** file of root project（priority=1）：

  ```
  1. create config.gradle file in the root path of project
      ext {
          //wrapper module
          POM_GROUP_ID = 'xxx.xxx.xxx'
          POM_ARTIFACT_ID = 'xxx'
          POM_VERSION = 'x.x.x'
          POM_PACKAGING = 'aar'  //aar or jar, not need when gradle version >= 4.0
          POM_DESCRIPTION = "xxxxx"
  
          module1 = [
                  POM_GROUP_ID   : 'com.xxx.xxx',
                  POM_ARTIFACT_ID: 'xxx',
                  POM_VERSION    : '1.0.0',
                  POM_PACKAGING  : 'aar',  //not need when gradle version >= 4.0
                  POM_DESCRIPTION: "xxxxx"
          ]
  
          module2 = [
                  POM_GROUP_ID   : 'com.xxx.xxx',
                  POM_ARTIFACT_ID: 'xxx',
                  POM_VERSION    : '1.0.0',
                  POM_PACKAGING  : 'jar',  //not need when gradle version >= 4.0
                  POM_DESCRIPTION: "xxxxx"
          ]
      }
      
  2. apply to the top of the root project's build.gradle file
     apply from: 'config.gradle'
     
  3. apply script and assign config params in the build.gradle of each modules
      if (rootProject.name == "xxx") {
      	apply from: 'publish-maven.gradle'
  
          tasks.withType(PublishToMavenRepository) {
              if (publishing.publications.hasProperty("release")) {
                  publishing.publications.release.groupId = rootProject.ext.module1['POM_GROUP_ID']
                  publishing.publications.release.artifactId = rootProject.ext.module1['POM_ARTIFACT_ID']
                  publishing.publications.release.version = rootProject.ext.module1['POM_VERSION']
              }
          }
      }
  ```

### 3. Publish to remote Repository

   * maven（gradle version < 4.0）

     ``` bash
     ./gradlew :${moduleName}:publish:publishing
     ```

   * maven-publish（gradle version >= 4.0）

     ``` bash
     ./gradlew :${moduleName}:publishReleasePublicationToMavenRepository
     ```

