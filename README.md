# Blibioteca USM TDD | Testing
Este trabajo trata de probar TDD para desarrollar un software de una biblioteca

En este Readme encontrará:
- Instrucciones de ejecución
- Evidencia de ejecución de pruebas
- Casos de pruebas implementados
- Documentación
- Respuesta a las preguntas de reflexión realizadas
- Y configuración del repositorio y el proyecto

# Instrucciones de ejecución

## Requisitos mínimos
- Tener Java JDK 21 instalado.
- Tener Maven instalado.
- Si se usa VS Code, instalar la extensión `Extension Pack for Java`.

### Verificar requisitos previos
Antes de compilar o ejecutar el proyecto, revisar en una terminal:

```shell
java -version
javac -version
mvn -version
```

El proyecto está configurado para Java 21, por lo que `java` y `javac` deberían mostrar una versión 21 o compatible.

Si `java` funciona pero `javac` no, falta instalar el JDK completo. No basta con tener solo el runtime de Java.

En Windows se puede instalar JDK 21 con:

```powershell
winget install EclipseAdoptium.Temurin.21.JDK
```

En VS Code se recomienda instalar:

- `Extension Pack for Java`
- `Maven for Java` (viene incluido en el pack)
- `Debugger for Java` (viene incluido en el pack)
- `Test Runner for Java` (viene incluido en el pack)

Después de instalar Java o extensiones, cerrar y volver a abrir VS Code desde la carpeta raíz del proyecto.

## Ejecución

## Ejecución de la web mínima

La web mínima levanta un servidor Java local.

Se dispone de los scripts de ejecución `iniciarWeb.sh`(Linux) y `iniciarWeb.bat`(Windows) para ejecutar el `.jar`. Deben de estar en la misma carpeta.
Estos archivos se consiguen en las realise oficiales publicadas donde puede el `.jar` y los scripts

Para Windows:
```shell
.\iniciarWeb.bat
```
Para Linux:
```shell
./iniciarWeb.sh
```

También puede ejecutar el `.jar` directamente con el siguiente comando:

```shell
java -jar archivo.jar
```

Por ejemplo
```shell
java -jar "sistema-biblioteca-1.0.0-SNAPSHOT.jar"
```

Si no puede hallar el `.jar`, puede construirlo:
```shell
git clone git@github.com:Axoluther/BibliotecaTDD.git
cd BibliotecaTDD
mvn clean package
jar -jar ".\target\sistema-biblioteca-1.0.0-SNAPSHOT.jar"
```

Debería mostrar:

```text
Biblioteca web disponible en http://localhost:8080
```

Luego de haber logrado ejecutarlo, debe abrir en el navegador el enlace:

```text
http://localhost:8080
```

La web permite:

- Registrar libros.
- Buscar por ISBN.
- Buscar por título.
- Listar libros disponibles.
- Prestar libros.
- Devolver libros.

### Ejecución de pruebas
Para ejecutar las pruebas, que equivale a ver si funciona el proyecto, se usa maven de la siguiente manera:
```shell
mvn test
```
o 
```shell
maven clean test
```
Esto debería mostrar la ejecución de las pruebas y que funciona todo el CRUD.

## Evidencia
En log es mas grande pero se alcanza a ver que se pasaron 10 sets de pruebas (funciones), que cada uno tiene varias pruebas unitaraias o casos de pruebas

<img width="687" height="674" alt="image" src="https://github.com/user-attachments/assets/df9dc6af-d68c-4e40-aaaa-524139f082cb" />

También en dentro de `\target\surefire-reports` se encuentran los resultados de los tests. Por ejemplo :

<img width="678" height="291" alt="image" src="https://github.com/user-attachments/assets/7c3c1521-d14c-4386-932e-e23fe7201607" />

<img width="691" height="362" alt="image" src="https://github.com/user-attachments/assets/aa0e5261-77a6-4ab1-ab5a-770db6a322a6" />

Igualmente, la pipeline de CI ejecuta las pruebas cada vez que s esube algún cambio a las ramas. Todo lo que entra a producción `main` ya esstá testeado y se vuelve a testear para que se integre bien.

# Casos de pruebas implementados
## LibroTest.java
Aquí se realizaron 3 sets de pruebas que se componene de varias pruebas unitarias o casos de pruebas
- `testCrearLibroValido`
  - Se crea un libro y se consulta cada parámetro entregado para corroborar que se guardó bien

- `testCrearLibroInvalido`
  - Se crea un libro con el `isbn` nulo

- `testLibroDisponible`
  - Se crea un libro y se testea si inmediatamente tiene un estado disponible en true
 
## BibliotecaTest.java
Aquí se realizaron casos de pruebas más complejos, que requerían manejar una lista de libros para las diferentes operaciones de búsqueda y mantener consistencia en actualizaciones de estado
- Primero se realiza un `setUp` con `@BefroreEach` que prepara las instancias que se usarán para cada set de pruebas.
- `registrarLibroValido`
  - Se registra el `libro1` y se comprueba que efectivamente está registrado buscándolo por el `isbn`
  - Se maneja la excepción `LibroNoEncontradoException`
- `registrarLibroInvalido`
  - Se intenta registrar un libro nulo `null`
  - Se maneja con la excepción `NullPointerException` retornada por `Objects.requireNonNull`
- `testLibrosDuplicadosIsbn`
  - Se registra dos libros que comparten el mismo `isbn`
  - Se maneja con la excepción `LibroDuplicadoException`
- `testLibroNoEncontrado`
  - Se añaden dos libros: `libro1` y `libro3`
  - Se busca un libro en la biblioteca por `isbn` con el `isbn:"9999"`, ninguno de los libros anteriores tiene ese isbn
  - Se maneja con la excepción `LibroNoEncontradoException`
  - Se busca un libro por un título que no hay, y debe retornar que es una lista `empty`
- `prestarLibros`
  - Se añaden los libros `libro1` y `libro3`
  - Se presta el `libro3`
  - Se vuelve a solicitar el `libro3`, se maneja con la excepción `LibroNoDisponibleException`
  - Se devuelve el `libro3`, y se comprueba que queda en estado `disponible`
  - Se `pide` y `devuelve` libros que `no estan registrados` y no existen. Se maneja con la excepción `LibroNoEncontradoException`
- `listarLibros`
  - Se crea una lista de libros `libros_a_listar` que son los libros que deve devolver la lista de libros disponibles
  - Se añade a esta lista los libros: `libro1` y `libro4`
  - Luego se registra a la biblioteca todos los libros menos el libro2 (por tener el isbn duplicado)
  - Se pide prestado el libro `libro3`
  - Se obtiene la `lista de libros disponibles` (no ocupados)
  - Se comparan ambas listas
- buscarPorTitulo
  - Se añaden todos los libros menos el libro2
  - Se crea la `word1` que es una palabra para buscar entre los títulos
  - Se crea la `word2` que es una palabra que no está entre los títulos
  - Se crea una lista solo con le libro `libro3` que es la solución
  - Se buscan los libros por ambos words
  - Para el `word1` se compara con la lista
  - Para el `word2` se ve si está vacía la lista
 
## Aplicación del TDD
Se iban agregando las funciones on las pruebas unitarias:
- Se agrupaban por categoría por decirlo así, categorías personales, como: pruebas unitarias de entradas inválidas, pruebas unitarias de búsqueda, etc
- Se creaba una función con esas pruebas unitarias, este se comiteaba con el estado Rojo
- Los commits tienen un título descriptivo, y el estado se ponía debajo del título con alguna explicación
- Luego se implementaba en el código de la clase la función correspondiente para que se pasara el caso de prueba
- Luego de programar se documentaba el código co javadoc

## Documentación
La documentación del código se puede encontrar en el siguiente enlace:

```shell
https://axoluther.github.io/BibliotecaTDD/
```

## Repsuestas a preguntas
### Pregunta 1
#### Flujo
- Flujo 1
  - Fase rojo: en `testLibroNoEncontrado` se pide recibir una lista vacía para un título que no existe
  - Fase Verde: Se retorna una lista vacía
  - Fase Azul: No hay mucho que hacer
- Flujo 2:
  - Fase rojo: En `buscarPorTitulo` se realizan casos de prueba para buscar entre títulos con coincidencia parcial, se incluye el caso del flujo 1
  - Fase verde: Se reimplementa el código para que logre buscar entre los títulos esa coincidencia
  - Fase azul: Se corrige el código en donde se usaba `==` por `.contains`, funcionaba porque el título era completamente igual al otro, luego de corregir el caso de prueba, se arregló correctamente el código
- Flujo 3:
  - Fase Rojo: En `buscarPorTitulo` se modifican los words para que tengan variaciónes entre mayúsculas y minúsculas
  - Fase verde: Se pasa todo a lowercase para que se puedan encontrar los líbros por coincidencia parcial+case-insensitive
  - Fase azul: Se reescribe el códgo para que se vea más ordenado
### Pregunta 2
- Aveces requería hacer modificaciones en otras clases
- Es difícil ser dogmático, pues a veces es necesario cambiar los casos de prueba porque se cambia la idea del diseño de lo qeu se entiende de los requerimientos
- La fase azul no tiene mucho sentido muchas veces, cuando el código funciona, muchas veces ya está acabado, y puede ser contra producente con el tiempo invertido tratar de que se vea más profesional, quizás falta una definición aceptable de cuánto es suficientemente profesional, pues puede quedar al criterio de cada persona
- Encuentro difícil seguir el TDD con GitFlow, ya que inicialmente quería hacer varias ramas `feature/x`, pero al ir añadiendo casos de pruebas de manera incremental, terminaba programando diferentes features a la vez para que funcioe un solo caso de prueba. La verdad que termine todos los requerimientos en una sola feature
- Adaptarse a pensar en TDD cuesta un poco, igual a medida que voy a vanzando me surgen varias dudas de cómo se aplica correctamente en cada caso
- Encuentro que es muy útil para enfocarse en lo que uno tiene que hacer, es un poco `greedy` o `local`, en el sentido de que tienes que ir un paso a la vez, un paso a la vez añadiendo características, este paso sería el ciclo, y luego añadir más casos de prueba, y añadir la siguiente cacterística. Por lo general uno se abruma pensando en todas las posibilidades, pero así se siente más liviano
- Es más robusto contra fallas. A medida que completo todos los requerimientos para una función, luego puedo usar esa función en otra, y sé que está completamente bien, y rara vez requiere alguna modificación, solo requiriría una en caso de que te encontraste con una situación no prevista en los requerimentos o casos de pruebas. Pero complementaod bien con hacer buenas pruebas con técnicas de casja blanca y negra debería ser más robusto aún
- Siento que el desarrollo fue más rápido de lo esperado ,por el punto anterior, no tuve que hacer tanto retrabajo. Más me tarde en configurar el CI/CD (nos e pedía en la entrega, fue por cuenta propia)

### Pregunta 3
- Sí megustaría seguir usando TDD, con el fin de hacerlo mejor la siguiente vez (en aplicarlo), y porque siento que tiene varias ventajas:
  - El desarrollo es más rápido
  - Es más robusto a no tener una metodología, porque el retrabajo es menor ante errores
  - Hay menos errores
  - Usar funciones dentro de otra ya no da tanta carga mental, pero que no tengo que pensar si va a funcionar o no
  - Siento que mezcla el enfoque iterativo para los ciclos, pero es incremental entre ciclos. Uno va como construyendo el panorama, y se va viendo más claro, es más fácil esa persepctiva local de ir resolviendo casos de pruebas, y entender mejor el funcionamiento del proyecto

# Configuración
De aquí en adelante se explica cómo se configuró este repositorio. Esta parte simplemente fue para practicar más el CI y CD , fue decisión propia.
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
