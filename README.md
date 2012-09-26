Guía de consumo de servicios Web
================================

Contexto
--------

Se desea construir una aplicación Android que permita listar un conjunto de
hoteles y crear nuevos.

Lista de hoteles

<img src=""></img>

Creación de nuevo hotel

<img src=""></img>


Consideraciones iniciales
-------------------------

El servicio de la aplicación está implementado en el PaaS [PHP Fog][pf] con el 
framework CodeIgniter y el repositorio de su código fuente se encuentra en
[ConnectionExampleServer][ces].

La aplicación Android se encuentra en [ConnectionExample][ce] y esta
implementada para la version 2.2 (API 8).


Desarrollo del lado del servidor
--------------------------------

* Se creo un [controlador][hotels-controller] que va atender las peticiones. 
Representa el WS de consultar hoteles (`all`) y de registrar uno nuevo
(`insertHotel`).

* Se creo un [modelo][model] encargado de interactuar con la DB para consultar
los hoteles y registrar uno nuevo.

* El método `all` del [controlador][hotels-controller] consulta los hoteles
existentes y los codifica como JSON por medio de la instrucción 
`json_encode($hotels)`.

Tabla de hoteles

<img src=""></img>

* El método `insertHotel` del [controlador][hotels-controller] obtiene los
parámetros `name` y `valueReservation` enviados en la petición POST y 
trata de crear un nuevo hotel en la DB. De crearlo exitosamente responde un JSON
con la propiedad `success` en `true`, de lo contrario retorna en ésta misma 
propiedad `false`.


Desarrollo móvil
----------------

Se deben declarar los permisos para verificar la conectividad del dispositivo
e interactuar con Internet.

[AndroidManifest.xml][manifest]

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

Se usa la clase [`Connector`][connector] para realizar la interacción con los
WS. Allí está implementada una petición básica por el método GET y otra por el
método POST. Por medio de éste último método de petición se pueden enviar
parámetros.

### Lista de hoteles

En la actividad [HotelList][hl] es donde se desea mostrar el listado de hoteles.

<img src=""></img>

Para tal propósito se implementa un tarea asíncrona que hace una petición GET
al WS [`all`][all]. Cuando la respuesta es obtenida, se procede a ser
interpretada como JSON.

```java
JSONArray hotelsArray = new JSONArray(hotelsJsonString);
```

Se recorre cada uno de los objetos que representan un hotel y se obtienen sus 
propiedades.

```java
JSONObject jsonHotel = hotelsArray.getJSONObject(i);
Hotel hotel = new Hotel();

hotel.setId(jsonHotel.getLong("id"));
hotel.setName(jsonHotel.getString("name"));
```

### Creación de hoteles

La actividad [HotelList][hl] tiene un botón que permite navegar a la actividad de
creación de hoteles: [NewHotel][nh]. Está actividad permite a un usuario crear
un hotel con su nombre y valor de reserva.

<img src=""></img>

Para su implementación se usa la tarea asíncrona `NewHotelTask` que realiza una
petición POST al WS [`insertHotel`][insert] con los valores ingresados por el
usuario. Al finalizar éste proceso, se finaliza la actividad y se devuelve a
la lista de hoteles.

<img src=""></img>

[insert]: http://androidexample.phpfogapp.com/index.php?/hotels/insertHotel
[nh]: https://github.com/sanrodari/ConnectionExample/blob/master/src/com/example/connectionexample/NewHotel.java
[all]: http://androidexample.phpfogapp.com/index.php?/hotels/all
[hl]: https://github.com/sanrodari/ConnectionExample/blob/master/src/com/example/connectionexample/HotelList.java
[connector]: https://github.com/sanrodari/ConnectionExample/blob/master/src/com/example/connectionexample/Connector.java
[pf]: https://phpfog.com/
[ces]: https://github.com/sanrodari/ConnectionExampleServer
[ce]: https://github.com/sanrodari/ConnectionExample
[manifest]: https://github.com/sanrodari/ConnectionExample/blob/master/AndroidManifest.xml
[hotels-controller]: https://github.com/sanrodari/ConnectionExampleServer/blob/master/application/controllers/hotels.php
[model]: https://github.com/sanrodari/ConnectionExampleServer/blob/master/application/models/hotel.php
