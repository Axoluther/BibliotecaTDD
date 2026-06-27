

# Configuración Java y JUnit
Se está usando VSCode. Se instaló la extensión "Extension Pack for Java", El cual incluye "Test Runner For Java". Luego se Intaló el JDK de java desde powershell (aunque luego me fijé que la misma extensión tenía uns setup)
```shell
winget install EclipseAdoptium.Temurin.21.JDK
```
Obteniendo así la versión 21 JDK.
Luego desde vscode, en el ícono "Testing" que está cerca de las extensiones, presioné la opción "Enable Java Tests" y seleccioné la opción recomendada JUnit Jupiter 6.

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

# Maven
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

# GitFlow y Maven

## Flujo
Este flujo debería indicar cómo funciona la creación de ``features`` y en qué momento llegan a producción
```
-> New feature/blabla local -> commits -> New branch remote -> Request pull remote to main -> git pull local
```
Cuando se realiza el `Pull Request` en remoto a main, se ejecuta `Github Actions`