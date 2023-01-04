1. add config of remote maven repository in the **`gradle.properties`** of project

   ```
   REPO_URL=http://192.168.10.162:8081/repository/irtek-and/
   REPO_USERNAME=admin
   REPO_PASSWORD=qq123456
   ```

2. add module config in the **`gradle.properties`** of module

   ```
   POM_GROUP_ID=com.fotric.ir
   POM_ARTIFACT_ID=irinfra
   POM_VERSION=1.0.0
   POM_PACKAGING=aar
   POM_DESCRIPTION="initial publish"
   ```

3. apply gradle script，add below in the end of module's **`build.gradle`**

   ```
   apply from: 'publish.gradle'
   ```