REM install these jars to the local .m2 repository
REM Source: https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html
call mvn install:install-file -Dfile=jim3dsModelImporterJFX.jar -DgroupId=com.modelimporter -DartifactId=jim3d -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=jimColModelImporterJFX.jar -DgroupId=com.modelimporter -DartifactId=jimcol -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=jimFxmlModelImporterJFX.jar -DgroupId=com.modelimporter -DartifactId=jimfxml -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=jimObjModelImporterJFX.jar -DgroupId=com.modelimporter -DartifactId=jimobj -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=jimStlMeshImporterJFX.jar -DgroupId=com.modelimporter -DartifactId=jimstlmesh -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=jimX3dModelImporterJFX.jar -DgroupId=com.modelimporter -DartifactId=jimx3d -Dversion=1.0 -Dpackaging=jar

