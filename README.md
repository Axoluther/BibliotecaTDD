# Directorios
Se sigue la estrucutra `Maven Standard Directory Layout` explicado en [baeldung](https://www.baeldung.com/maven-directory-structure)
```
└───maven-project
    ├───pom.xml
    ├───README.txt
    ├───NOTICE.txt
    ├───LICENSE.txt
    └───src
        ├───main
        │   ├───java
        │   ├───resources
        │   ├───filters
        │   └───webapp
        ├───test
        │   ├───java
        │   ├───resources
        │   └───filters
        ├───it
        ├───site
        └───assembly
```

# Configuración Java y JUnit
Se está usando VSCode. Se instaló la extensión "Extension Pack for Java", El cual incluye "Test Runner For Java". Luego se Intaló el JDK de java desde powershell (aunque luego me fijé que la misma extensión tenía uns setup)
```shell
winget install EclipseAdoptium.Temurin.21.JDK
```
Obteniendo así la versión 21 JDK.
Luego desde vscode, en el ícono "Testing" que está cerca de las extensiones, presioné la opción "Enable Java Tests" y seleccioné la opción recomendada JUnit Jupiter 6.

# Maven
- [GetStarted](https://maven.apache.org/guides/getting-started/)
## Instalación
Se realiza la instalación con powershell por medio del comando 
```
choco install maven
```
Seguido de esto se crea un archivo `pom.xml` en la raíz del proyecto, para configurar maven, el nombre del proyecto y versión, versión de java, y declarar junit para pruebas.
Para ejecutarse las pruebas se usa el comando
```
mvn clean test
```
Esto crea una carpeta `target` con binarios, reportes de los test.

## Pluggins
Se usan los pluggins:
- [Apache Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/): Permite la ejecución de tests y hace reportes

- [Flatten Maven Plugin](https://www.mojohaus.org/flatten-maven-plugin/): Permite usar las variables en el `pom.xml` y reemplazarlas


# GitFlow y Maven

## Flujo
Este flujo debería indicar cómo funciona la creación de ``features`` y en qué momento llegan a producción
```
-> New feature/blabla local -> commits -> New branch remote -> Request pull remote to develop -> git pull local
```
Cuando se realiza el `Pull Request` en remoto a ``develop``, se ejecuta `Github Actions`. Aunque para efectos prácticos de este proyecto quizás podría pasar directo al ``main`` o producción

## Versiones
Con el usao de la extensión `Flatten Maven Plugin`, se crea la variable `${revision}` dentro de `pom.xml`, el cual es un "CI Friendly Versions", para poder cambiar de manera dinámica la versión de la compilación. 
En propiedades del pom.xml, se define la versión incial en `<revision>`.

Se puede cambiar la variable `${revision}` de manera dinámica usando `-Drevision`:

```
mvn clean test -Drevision=1.0.0-feature-x-SNAPSHOT
```
Esto genera un archivo `.flattened-pom.xml` que reemplaza al `pom.xml`
