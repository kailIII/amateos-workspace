SocialWind Application
======================

Socialwind app es una aplicación que permite la localización de surfistas y permite a estos 
encontrar el mejor lugar donde practicar su deporte.

Se ha creado como parte del trabajo realizado en el [Máster INFTEL] (http://masterinftel.uma.es/) 
con la idea de desarrollar una arquitectura multiplataforma bajo un mismo interfaz (API).

Los clientes para la aplicación son.

 * Escritorio, creado para JRE y con interfaz Swing
 * Web, creado en GWT
 * Móvil, creado en Android

La presentación del proyecto se puede ver [aquí] (http://ignacio.bacamt.com/slides/java)

Como compilar
----------------

Para compilar el proyecto debe instalarse previamente.

 * Maven 3.0.3, y configurar las variables de entorno PATH, M2_HOME, MAVEN_HOME, M2
 * git. También puede descargarse el zip desde github.
 * Java JDK 1.6, y configurar las variables de entorno JAVA_HOME
 * Android SDK, y configurar las variables de entorno ANDROID_HOME. Aunque es recomendable bajarse 
   todas las versiones android, el proyecto usa la versión 2.2.1

Descargar el proyecto `git clone git://github.com/socialwind/socialwind.git`

Instalar en el repositior local los paquetes c2dm-server y c2dm. Desde el directorio `./src/lib` 
ejecutar.

    mvn install:install-file -Dfile=c2dm-server.jar -Dsources=c2dm-server-src.jar -DgroupId=com.google.android.c2dm -DartifactId=c2dm-server -Dversion=1.5.5 -Dpackaging=jar
    mvn install:install-file -Dfile=c2dm.jar -Dsources=c2dm-src.jar -DgroupId=com.google.android.c2dm -DartifactId=c2dm -Dversion=1.5.5 -Dpackaging=jar

Compilar el proyecto `mvn clean install`

Como ejecutar
--

Los proyectos ejecutables son socialwind-desktop, socialwind-mobile y socialwind-server. Para 
lanzarlos es recomendable primero hacer un `mvn clean install` desde el proyecto padre. Luego entrar
 en el directorio especifico y ejecutar los siguientes comandos.

### Lanzar aplicación de Escritorio (desde socialwind-desktop)

    mvn package
    java -jar .\target\socialwind-desktop-0.0.1-SNAPSHOT-jar-with-dependencies.jar

### Lanzar aplicación Web (desde socialwind-server)

    mvn gwt:run

### Lanzar aplicación Móvil (desde socialwind-server)

    mvn android:deploy

Deberá estar arrancado algún emulador o conectado algún teléfono android al ordenador.
