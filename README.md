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
```powershell
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

# Github Actions workflows
Se configura los jobs `CI` y `CD`. Estos se encuentran en:
- ``.github\workflows\ci.yaml``
- ``.github\workflows\cd.yaml``

## CI
Los steps constan de :
- descargar código en máquina virtual, usando `checkout v4` de actions de github
- Configuración del ``JDK`` 
- Crear ``artefacto`` usando Maven
- Ejecución de `Tests` y seteando un número de versión `revision`
- ``Notificación`` a `Discord` del resultado de los steps anteriores (build y tests)

Esto solo requiere de una sola máquina virtual para ejecutar todos los pasos.
Estos se ejecutan para:
- En `push` todas las ramas `feature/x` , `develop`, `main` y la rama de configuración `conf`
-  Y en `pull request` solo `main` y `develop`

Esto asegura que se testeen todos los cambios subidos en las ramas, y se testeen los cambios cuando ingresan al código base presente a main y develop, para asegurarnos de que no se rompe nada.

## CD
Los steps constan de:
- Descarga de código en máquina virtual ubuntu, usando `checkout v4`
- Se configura JDK versión 21 usando `setp-java v4`
- Se crea artefacto con la versión indicada en ``-Drevision`` y ya no hay necesidad de tests por el CI
- Se publica el `release` en el repositorio junto a su archivo `.jar` usando `action-gh-release v2` junto con su versión

Esto se ejecuta en:

Para versionar ``main`` al integrar `develop` en ella:
```shell
# nuevos cambios a produccion
git checkout main
git merge develop
# se tagea y se sube
git tag -a v1.2.0 -m "Release versión 1.2.0"
git push origin main --tags
```
El tag creado quedará en la variable o en el ``contexto`` de github `github.ref_name` el cual se usará para versionar el artefacto