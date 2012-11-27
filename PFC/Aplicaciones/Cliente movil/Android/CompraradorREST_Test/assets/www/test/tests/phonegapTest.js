/*******************************************************************************
 * ACELERÓMETRO
 */
module('ACELEROMETRO');
test("debe existir", function() {
    expect(1);
    ok(navigator.accelerometer != null, "navigator.accelerometer no debe ser null.");
});
test("debe tener función getCurrentAcceleration", function() {
    expect(2);
    ok(typeof navigator.accelerometer.getCurrentAcceleration != 'undefined'
            && navigator.accelerometer.getCurrentAcceleration != null,
            "navigator.accelerometer.getCurrentAcceleration no debe ser null.");
    ok(typeof navigator.accelerometer.getCurrentAcceleration == 'function',
            "navigator.accelerometer.getCurrentAcceleration debe ser una función.");
});
test(
        "getCurrentAcceleration success callback debe ser llamada con un objeto Acceleration",
        function() {
            expect(7);
            QUnit.stop(Tests.TEST_TIMEOUT);
            var win = function(a) {
                ok(
                        typeof a == 'object',
                        "El objeto Acceleration devuelto en getCurrentAcceleration success callback debe ser de tipo 'object'.");
                ok(
                        a.x != null,
                        "El objeto Acceleration devuelto en getCurrentAcceleration success callback debe tener una propiedad 'x'.");
                ok(
                        typeof a.x == 'number',
                        "La propiedad 'x' del objeto Acceleration devuelto en getCurrentAcceleration success callback debe ser de tipo 'number'.");
                ok(
                        a.y != null,
                        "El objeto Acceleration devuelto en getCurrentAcceleration success callback debe tener una propiedad 'y'.");
                ok(
                        typeof a.y == 'number',
                        "La propiedad 'y' del objeto Acceleration devuelto en getCurrentAcceleration success callback debe ser de tipo 'number'.");
                ok(
                        a.z != null,
                        "El objeto Acceleration devuelto en getCurrentAcceleration success callback debe tener una propiedad 'z'.");
                ok(
                        typeof a.z == 'number',
                        "La propiedad 'z' del objeto Acceleration devuelto en getCurrentAcceleration success callback debe ser de tipo 'number'.");
                start();
            };
            var fail = function() {
                start();
            };
            navigator.accelerometer.getCurrentAcceleration(win, fail);
        });
test("debe contener la función watchAcceleration", function() {
    expect(2);
    ok(typeof navigator.accelerometer.watchAcceleration != 'undefined'
            && navigator.accelerometer.watchAcceleration != null,
            "navigator.accelerometer.watchAcceleration no debe ser null.");
    ok(typeof navigator.accelerometer.watchAcceleration == 'function',
            "navigator.accelerometer.watchAcceleration debe ser una función.");
});
test("debe contener la función clearWatch", function() {
    expect(2);
    ok(typeof navigator.accelerometer.clearWatch != 'undefined'
            && navigator.accelerometer.clearWatch != null,
            "navigator.accelerometer.clearWatch no debe ser nulo.");
    ok(typeof navigator.accelerometer.clearWatch == 'function',
            "navigator.accelerometer.clearWatch debe ser una función.");
});
test(
        "debe ser capaz de definir un nuevo objeto Acceleration con propiedades x,y,z y timestamp.",
        function() {
            expect(9);
            var x = 1;
            var y = 2;
            var z = 3;
            var a = new Acceleration(x, y, z);
            ok(a != null, "el nuevo objeto Acceleration no debe ser null.");
            ok(typeof a == 'object', "el nuevo objeto Acceleration debe ser de tipo 'object'.");
            ok(a.x != null, "el nuevo objeto Acceleration debe tener una propiedad 'x'.");
            equals(
                    a.x,
                    x,
                    "el nuevo objeto Acceleration debe tener una propiedad 'x' igual al primer parámetro pasado en el constructor de Acceleration.");
            ok(a.y != null, "el nuevo objeto Acceleration debe tener una propiedad 'y'.");
            equals(
                    a.y,
                    y,
                    "el nuevo objeto Acceleration debe tener una propiedad 'y' igual al segundo parámetro pasado en el constructor de Acceleration.");
            ok(a.z != null, "el nuevo objeto Acceleration debe tener una propiedad 'z'.");
            equals(
                    a.z,
                    z,
                    "el nuevo objeto Acceleration debe tener una propiedad 'z' igual al tercer parámetro pasado en el constructor de Acceleration.");
            ok(a.timestamp != null, "new Acceleration object should have a 'timestamp' property.");
        });


/*******************************************************************************
 * DISPOSITIVO
 */
module('DISPOSITIVO');
test("debe existir", function() {
    expect(1);
    ok(window.device != null, "window.device no debe ser null.");
});
test("debe contener un string que contiene información sobre las características del dispositivo", function() {
    expect(2);
    ok(typeof window.device.platform != 'undefined' && window.device.platform != null,
            "window.device.platform no debe ser null.")
    ok((new String(window.device.platform)).length > 0,
            "window.device.platform debe contener algún tipo de descripción.")
});
test("debe contener un string con la versión del dispositivo", function() {
    expect(2);
    ok(typeof window.device.version != 'undefined' && window.device.version != null,
            "window.device.version no debe ser null.")
    ok((new String(window.device.version)).length > 0,
            "window.device.version debe contener algún tipo de descripción.")
});
test("debe contener la especificación de nombre en formato string", function() {
    expect(2);
    ok(typeof window.device.name != 'undefined' && window.device.name != null,
            "window.device.name no debe ser null.");
    ok((new String(window.device.name)).length > 0,
            "window.device.name debe contener algún tipo de descripción.");
});
test("debe contener la especificación UUID en formato string o number", function() {
    expect(2);
    ok(typeof window.device.uuid != 'undefined' && window.device.uuid != null,
            "window.device.uuid no debe ser null.");
    if (typeof window.device.uuid == 'string' || typeof window.device.uuid == 'object') {
        ok((new String(window.device.uuid)).length > 0,
                "window.device.uuid, como string, debe tener al menos un carácter.");
    } else {
        ok(window.device.uuid > 0,
                "window.device.uuid, como number, debe ser mayor que 0.");
    }
});
test("debe contener la especificación de phonegap en formato string", function() {
    expect(2);
    ok(typeof window.device.cordova != 'undefined' && window.device.cordova != null,
            "window.device.cordova no debe ser null.");
    ok((new String(window.device.cordova)).length > 0,
            "window.device.cordova debe contener algún tipo de descripción.");
});

/*******************************************************************************
 * RED
 */
module('RED');

test("debe existir", function() {
    expect(1);
    ok(navigator.network != null, "navigator.network no debe ser null.");
});
test("connection debe existir", function() {
    expect(1);
    ok(navigator.network.connection != null, "navigator.network.connection no debe ser null.");
});
test("debe contener propiedades de la conexión", function() {
    expect(1);
    ok(typeof navigator.network.connection.type != 'undefined',
            "navigator.network.connection.type está definido.");
});
test("debe definir constantes para el estado de la conexión", function() {
    expect(7);
    equals(Connection.UNKNOWN, "unknown", "Connection.UNKNOWN es igual a 'unknown'.");
    equals(Connection.ETHERNET, "ethernet", "Connection.ETHERNET es igual a 'ethernet'.");
    equals(Connection.WIFI, "wifi", "Connection.WIFI es igual a 'wifi'.");
    equals(Connection.CELL_2G, "2g", "Connection.CELL_2G es igual a '2g'.");
    equals(Connection.CELL_3G, "3g", "Connection.CELL_3G es igual a '3g'.");
    equals(Connection.CELL_4G, "4g", "Connection.CELL_4G es igual a '4g'.");
    equals(Connection.NONE, "none", "Connection.NONE es igual a 'none'.");
});

/*******************************************************************************
 * NOTIFICACIONES
 */
module('NOTIFICACIONES');
test("debe existir", function() {
    expect(1);
    ok(navigator.notification != null, "navigator.notification no debe ser null.");
});
test("debe contener la función vibrate", function() {
    expect(2);
    ok(typeof navigator.notification.vibrate != 'undefined'
            && navigator.notification.vibrate != null,
            "navigator.notification.vibrate no debe ser null.");
    ok(typeof navigator.notification.vibrate == 'function',
            "navigator.notification.vibrate debe ser una función.");
});
test("debe contener la función beep", function() {
    expect(2);
    ok(typeof navigator.notification.beep != 'undefined' && navigator.notification.beep != null,
            "navigator.notification.beep no debe ser null.");
    ok(typeof navigator.notification.beep == 'function',
            "navigator.notification.beep debe ser una función.");
});
test("debe contener la función alert", function() {
    expect(2);
    ok(typeof navigator.notification.alert != 'undefined' && navigator.notification.alert != null,
            "navigator.notification.alert no debe ser null.");
    ok(typeof navigator.notification.alert == 'function',
            "navigator.notification.alert debe ser una función.");
});

/*******************************************************************************
 * BASE DE DATOS
 */
module('BASE DE DATOS');
test("debe existir", function() {
    expect(1);
    ok(typeof (window.openDatabase) == "function", "La base de datos ha sido definida");
});
test("Should open a database", function() {
    var db = openDatabase("Database", "1.0", "HTML5 Database API example", 200000);
    ok(db != null, "La base de datos debe poder ser abierta.");
});