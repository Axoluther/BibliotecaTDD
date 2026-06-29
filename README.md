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
Se está usando VSCode. Se instaló la extensión "Extension Pack for Java", El cual incluye "Test Runner For Java". Luego se Intaló el JDK de java desde powershell (aunque luego me fijé que la misma extensión tenía un setup)
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
```shell
mvn clean test
```
Esto crea una carpeta `target` con binarios, reportes de los test. Usar el parámetro `clean` borra la carpeta `target` en caso de que exista. Si no se incluye `clean` solo cambiará los archivos binarios de las clases modificadas volviendolas a compilar
```shell
mvn -q test
```
(`-q` para que no imprima tanto output)
- TDD: Solo conviene usar `test` sin `clean` o sería muy lento. Solo uasr `clean` si se notan comportamientos extraños

## Pluggins
Se usan los pluggins:
- [Apache Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/): Permite la ejecución de tests y hace reportes

- [Flatten Maven Plugin](https://www.mojohaus.org/flatten-maven-plugin/): Permite usar las variables en el `pom.xml` y reemplazarlas

- [Maven Compiler Pluging](https://maven.apache.org/plugins/maven-compiler-plugin/usage.html): Sirve para que se pueda compilar el proyecto, y fijar la versión de java

- [Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/): Sirve para generar documentación 


# GitFlow y Maven

## Flujo
La idea es conectar el versionamiento entre GitFlow y Maven. Para esto se configuran los `CI` y `CD`, en donde en `CI` ejecuta tests y notifica, y la versión va pero no es muy relevante todavía, y `CD` empaqueta y versiona, se asigna una ``versión`` con ``git``, ``Maven`` recibe la version y construye el ``artefacto versionado`` por medio del `pom.xml`, y luego se realiza la ``realise`` con `action-gh-release v2`

Luego de que se termien las etapas de CI|CD se pasa a la etapa de documentación continua. Se crea documentación del código usando `Maven JavaDoc Plugin`, el cual se añade a la pipeline `docs.yaml`. Solo realiza documentación en producción y usa github pages para mostrarla.

## Versiones
Con el uso de la extensión `Flatten Maven Plugin`, se crea la variable `${revision}` dentro de `pom.xml`, el cual es un "CI Friendly Versions", para poder cambiar de manera dinámica la versión de la compilación. 
En propiedades del pom.xml, se define la versión incial en `<revision>`.

Se puede cambiar la variable `${revision}` de manera dinámica usando `-Drevision`:

```
mvn clean test -Drevision=1.0.0-feature-x-SNAPSHOT
```
Esto genera un archivo `.flattened-pom.xml` que reemplaza al `pom.xml`

# Github Actions workflows
Se configura los jobs `CI`, `CD` y `docs`. Estos se encuentran en:
- ``.github\workflows\ci.yaml``
- ``.github\workflows\cd.yaml``
- ``.github\workflows\docs.yaml``

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

# Docs
Esta etapa corresponde a la documentación continua.

Solo se ejecuta cuando:
- Se pasan cambios a código en producción
- Cuando la etapa CD ha sido completada

Los pasos que sigue son:
- Se clona repo en MV con `checkout v4`
- Se configura java con `setup-java v4`
- Se genera documentación con `JavaDoc`
- Se configura Github Pages con `configure-pages v4`
- Se sube el artefacto a la documentación con `upload-pages-artifact@v3`. Notar que este artefacto no es el .jar, si no el ``reporte HTML``
- Se despliega la págjna con `deploy-pages@v4`