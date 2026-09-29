
package persistencia;


/*
 * ================================================================
 * IMPORTACIONES DE JACKSON
 * ================================================================
 *
 * Jackson es una biblioteca externa que utilizaremos más adelante
 * para trabajar principalmente con JSON y XML.
 *
 * IMPORTANTE:
 *
 * Estas clases NO forman parte del JDK de Java.
 *
 * Para poder utilizarlas necesitamos haber añadido las
 * correspondientes dependencias en el pom.xml.
 *
 * En este momento del desarrollo todavía no estamos utilizando
 * todas estas clases, pero las dejamos preparadas porque las
 * necesitaremos cuando implementemos JSON y XML.
 */


/*
 * TypeReference se utilizará más adelante cuando queramos
 * deserializar colecciones genéricas con Jackson.
 *
 * Por ejemplo:
 *
 *      List<Cliente>
 *
 * Debido al borrado de tipos (type erasure) de Java, Jackson
 * necesita información adicional para saber qué contiene
 * exactamente una lista.
 */
import com.fasterxml.jackson.core.type.TypeReference;


/*
 * ObjectMapper es una de las clases principales de Jackson.
 *
 * Nos permitirá realizar conversiones como:
 *
 *      objetos Java  ---> JSON
 *
 * y:
 *
 *      JSON ---> objetos Java
 */
import com.fasterxml.jackson.databind.ObjectMapper;


/*
 * Permite configurar determinadas características
 * de la serialización.
 *
 * Por ejemplo, posteriormente podremos utilizar:
 *
 *      SerializationFeature.INDENT_OUTPUT
 *
 * para generar JSON/XML con indentación y hacerlo
 * más legible.
 */
import com.fasterxml.jackson.databind.SerializationFeature;


/*
 * XmlMapper es una variante de ObjectMapper especializada
 * en trabajar con documentos XML.
 *
 * Permitirá realizar:
 *
 *      objetos Java ---> XML
 *
 *      XML ---> objetos Java
 */
import com.fasterxml.jackson.dataformat.xml.XmlMapper;



/*
 * ================================================================
 * CLASES DE NUESTRO PROYECTO
 * ================================================================
 */

/*
 * Importamos nuestras clases del paquete modelo.
 *
 * Estas clases representan los datos con los que trabaja
 * nuestra aplicación.
 */
import modelo.Cliente;
import modelo.Producto;



/*
 * ================================================================
 * CLASES DEL JDK
 * ================================================================
 */

/*
 * java.io contiene clases relacionadas con entrada/salida.
 *
 * En este código utilizaremos, entre otras:
 *
 *      BufferedWriter
 *      BufferedReader
 *      IOException
 */
import java.io.*;


/*
 * StandardCharsets contiene las codificaciones estándar
 * que podemos utilizar.
 *
 * Nosotros trabajaremos explícitamente con UTF-8.
 *
 * Esto permite representar correctamente caracteres como:
 *
 *      á
 *      é
 *      ñ
 *      €
 */
import java.nio.charset.StandardCharsets;


/*
 * java.nio.file contiene la API moderna de Java para
 * trabajar con ficheros y rutas.
 *
 * Entre otras clases contiene:
 *
 *      Path
 *      Files
 */
import java.nio.file.*;


/*
 * ArrayList es una implementación concreta de List.
 */
import java.util.ArrayList;


/*
 * List es una interfaz que representa una colección
 * ordenada de elementos.
 *
 * Por ejemplo:
 *
 *      List<Cliente>
 *
 * significa:
 *
 *      "una lista que contiene objetos Cliente"
 */
import java.util.List;



/*
 * ================================================================
 * CLASE GESTORFICHEROS
 * ================================================================
 *
 * Esta clase va a concentrar las operaciones relacionadas
 * con la persistencia en ficheros.
 *
 * Su responsabilidad será:
 *
 *      - exportar datos;
 *      - importar datos;
 *      - convertir objetos Java en representaciones persistentes;
 *      - reconstruir objetos Java a partir de esas representaciones.
 *
 *
 * La idea general es:
 *
 *
 *                    EXPORTAR
 *
 *      OBJETOS JAVA -------------> FICHERO
 *
 *
 *                    IMPORTAR
 *
 *      OBJETOS JAVA <------------- FICHERO
 *
 *
 * Posteriormente tendremos diferentes formatos:
 *
 *                  GestorFicheros
 *                        |
 *          +-------------+-------------+
 *          |             |             |
 *         TXT           CSV          XML/JSON
 *
 */
public class GestorFicheros {


    private static final XmlMapper XML_MAPPER =
            (XmlMapper) new  XmlMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private static final ObjectMapper MAPPER =
            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    // ============================================================
    // TXT
    // ============================================================


    /*
     * En esta primera fase vamos a trabajar con un formato
     * TXT diseñado por nosotros.
     *
     * Hemos decidido que:
     *
     *      - cada línea representa un objeto;
     *      - cada atributo se separa utilizando ;
     *
     *
     * Ejemplo:
     *
     *      3;Marta;marta@email.com;622345345
     *
     *
     * Podemos interpretarlo como:
     *
     *      3
     *      |
     *      +------> id
     *
     *      Marta
     *      |
     *      +------> nombre
     *
     *      marta@email.com
     *      |
     *      +------> email
     *
     *      622345345
     *      |
     *      +------> teléfono
     *
     *
     * Por tanto, ";" es nuestro DELIMITADOR.
     */



    /**
     * Exporta una lista de clientes a un fichero TXT.
     *
     * Recibe:
     *
     *      ruta      -> dónde queremos guardar el fichero
     *      clientes  -> qué clientes queremos guardar
     *
     * No devuelve ningún valor porque su objetivo es
     * escribir información en un fichero.
     */
    public static void exportarClientesTxt(
            Path ruta,
            List<Cliente> clientes
    ) throws IOException {


        /*
         * --------------------------------------------------------
         * ¿POR QUÉ static?
         * --------------------------------------------------------
         *
         * El método pertenece a la propia clase GestorFicheros.
         *
         * Eso significa que podremos llamarlo así:
         *
         *      GestorFicheros.exportarClientesTxt(...)
         *
         * sin necesidad de hacer:
         *
         *      GestorFicheros gestor = new GestorFicheros();
         *
         *
         * --------------------------------------------------------
         * ¿POR QUÉ throws IOException?
         * --------------------------------------------------------
         *
         * Trabajar con ficheros puede producir errores:
         *
         *      - el fichero no existe;
         *      - no tenemos permisos;
         *      - la ruta no es válida;
         *      - existe un problema de lectura/escritura;
         *      - etc.
         *
         * IOException es una CHECKED EXCEPTION.
         *
         * Java nos obliga a:
         *
         *      capturarla con try-catch
         *
         *              O
         *
         *      declararla con throws.
         *
         * Aquí hemos elegido declararla:
         *
         *      throws IOException
         */


        /*
         * --------------------------------------------------------
         * TRY-WITH-RESOURCES
         * --------------------------------------------------------
         *
         * Utilizamos:
         *
         *      try (recurso) {
         *
         *      }
         *
         * Esto se denomina try-with-resources.
         *
         * Java se encargará automáticamente de cerrar el
         * BufferedWriter cuando terminemos.
         *
         * Por tanto, no necesitamos hacer manualmente:
         *
         *      bw.close();
         */
        try (BufferedWriter bw =
                     Files.newBufferedWriter(
                             ruta,
                             StandardCharsets.UTF_8
                     )) {


            /*
             * Files.newBufferedWriter(...)
             *
             * abre el fichero para escritura de texto.
             *
             *
             * Tenemos:
             *
             *      ruta
             *
             * que indica DÓNDE escribir.
             *
             * Y:
             *
             *      StandardCharsets.UTF_8
             *
             * que indica qué codificación utilizar.
             *
             *
             * El resultado es un:
             *
             *      BufferedWriter
             *
             *
             * Simplificando:
             *
             *      nuestro programa
             *             |
             *             v
             *      BufferedWriter
             *             |
             *             v
             *          fichero
             */


            /*
             * Recorremos todos los clientes.
             *
             * Este for-each:
             *
             *      for (Cliente c : clientes)
             *
             * puede leerse como:
             *
             *      "Para cada Cliente c que exista
             *       dentro de la lista clientes..."
             */
            for (Cliente c : clientes) {


                /*
                 * Escribimos una representación textual
                 * del objeto Cliente.
                 *
                 * Por ejemplo, si tenemos:
                 *
                 *      id       = 3
                 *      nombre   = Marta
                 *      email    = marta@email.com
                 *      telefono = 622345345
                 *
                 * generaremos:
                 *
                 *      3;Marta;marta@email.com;622345345
                 *
                 *
                 * Estamos transformando:
                 *
                 *      OBJETO JAVA
                 *
                 *          |
                 *          v
                 *
                 *      REPRESENTACIÓN TEXTUAL
                 *
                 *
                 * Podemos considerar esto una forma sencilla
                 * de SERIALIZACIÓN MANUAL.
                 *
                 * Es manual porque nosotros decidimos:
                 *
                 *      - qué atributos escribir;
                 *      - en qué orden;
                 *      - qué separador utilizar.
                 */
                bw.write(
                        c.getId()
                                + ";"
                                + c.getNombre()
                                + ";"
                                + c.getEmail()
                                + ";"
                                + c.getTelefono()
                );


                /*
                 * Una vez escrito un cliente añadimos
                 * un salto de línea.
                 *
                 * Así conseguimos:
                 *
                 *      UN CLIENTE = UNA LÍNEA
                 *
                 *
                 * Ejemplo:
                 *
                 * 1;Ana;ana@email.com;611111111
                 * 2;Pedro;pedro@email.com;622222222
                 * 3;Marta;marta@email.com;633333333
                 */
                bw.newLine();

            }

        }

        /*
         * Aquí ya hemos salido del try.
         *
         * El BufferedWriter ha sido cerrado automáticamente
         * gracias al try-with-resources.
         */
    }



    /**
     * Importa clientes desde un fichero TXT.
     *
     * Ahora vamos a realizar el proceso contrario:
     *
     *      FICHERO TXT
     *           |
     *           v
     *        Strings
     *           |
     *           v
     *        Cliente
     *           |
     *           v
     *      List<Cliente>
     */
    public static List<Cliente> importarClientesTxt(
            Path ruta
    ) throws IOException {


        /*
         * Creamos una lista inicialmente vacía.
         *
         * Aquí iremos almacenando todos los clientes
         * que consigamos reconstruir correctamente.
         *
         *
         * Observa:
         *
         *      List<Cliente>
         *
         * es el tipo de la variable.
         *
         *      ArrayList<>()
         *
         * es la implementación concreta que estamos creando.
         */
        List<Cliente> resultado =
                new ArrayList<>();



        /*
         * Abrimos el fichero para LECTURA.
         *
         * Antes utilizamos:
         *
         *      BufferedWriter
         *
         * porque queríamos escribir.
         *
         * Ahora utilizamos:
         *
         *      BufferedReader
         *
         * porque queremos leer.
         */
        try (BufferedReader br =
                     Files.newBufferedReader(
                             ruta,
                             StandardCharsets.UTF_8
                     )) {


            /*
             * Esta variable almacenará temporalmente
             * cada línea que vayamos leyendo.
             */
            String linea;


            /*
             * ----------------------------------------------------
             * LECTURA LÍNEA A LÍNEA
             * ----------------------------------------------------
             *
             * readLine() devuelve la siguiente línea.
             *
             * Por ejemplo:
             *
             *      "3;Marta;marta@email.com;622345345"
             *
             *
             * Cuando ya no quedan más líneas devuelve:
             *
             *      null
             *
             *
             * Por tanto:
             *
             *      while ((linea = br.readLine()) != null)
             *
             * significa:
             *
             *      "Mientras pueda seguir leyendo líneas..."
             */
            while ((linea = br.readLine()) != null) {


                /*
                 * ------------------------------------------------
                 * DIVIDIMOS LA LÍNEA
                 * ------------------------------------------------
                 *
                 * Tenemos:
                 *
                 *      3;Marta;marta@email.com;622345345
                 *
                 * Queremos obtener:
                 *
                 *      c[0] = "3"
                 *      c[1] = "Marta"
                 *      c[2] = "marta@email.com"
                 *      c[3] = "622345345"
                 *
                 *
                 * Para ello utilizamos split().
                 *
                 * ";" indica que queremos separar utilizando
                 * el punto y coma como delimitador.
                 */
                String[] c =
                        linea.split(";", -1);


                /*
                 * ------------------------------------------------
                 * ¿POR QUÉ -1?
                 * ------------------------------------------------
                 *
                 * El -1 hace que split() conserve también
                 * los campos vacíos que aparecen al final.
                 *
                 * Por ejemplo:
                 *
                 *      3;Marta;marta@email.com;
                 *
                 * queremos obtener:
                 *
                 *      c[0] = "3"
                 *      c[1] = "Marta"
                 *      c[2] = "marta@email.com"
                 *      c[3] = ""
                 *
                 *
                 * Sin ese -1, el último campo vacío podría
                 * desaparecer del resultado.
                 */



                /*
                 * Nuestro formato exige exactamente cuatro campos:
                 *
                 *      id
                 *      nombre
                 *      email
                 *      teléfono
                 *
                 * Por tanto:
                 *
                 *      c.length debe ser 4
                 */
                if (c.length != 4) {


                    /*
                     * Si la línea no tiene cuatro campos,
                     * consideramos que no tiene la estructura
                     * esperada.
                     *
                     * continue significa:
                     *
                     *      "deja de procesar esta iteración
                     *       y pasa a la siguiente vuelta
                     *       del while".
                     *
                     * NO termina todo el método.
                     */
                    continue;
                }


                /*
                 * ------------------------------------------------
                 * RECONSTRUCCIÓN DEL OBJETO
                 * ------------------------------------------------
                 *
                 * Si hemos llegado hasta aquí sabemos que
                 * tenemos cuatro campos.
                 *
                 * Ahora tenemos que convertirlos a los tipos
                 * correspondientes y construir un Cliente.
                 */
                try {


                    /*
                     * Ejemplo de línea:
                     *
                     * 3;Marta;marta@email.com;622345345
                     */


                    /*
                     * c[0] es un String:
                     *
                     *      "3"
                     *
                     * pero el id de Cliente es un int.
                     *
                     * Por eso necesitamos convertir:
                     *
                     *      String ---> int
                     *
                     * mediante Integer.parseInt().
                     */
                    int id =
                            Integer.parseInt(c[0]);


                    /*
                     * El resto de campos ya son String,
                     * por lo que no necesitamos convertirlos.
                     */
                    String nombre =
                            c[1];

                    String email =
                            c[2];

                    String tel =
                            c[3];


                    /*
                     * Ya tenemos todos los datos con los tipos
                     * que necesita nuestro constructor.
                     *
                     * Construimos un nuevo objeto Cliente.
                     *
                     *
                     *      datos del fichero
                     *             |
                     *             v
                     *      variables Java
                     *             |
                     *             v
                     *       new Cliente(...)
                     *             |
                     *             v
                     *       objeto Cliente
                     */
                    Cliente cliente =
                            new Cliente(
                                    id,
                                    nombre,
                                    email,
                                    tel
                            );


                    /*
                     * Añadimos el objeto recién creado
                     * a nuestra lista de resultados.
                     */
                    resultado.add(cliente);


                } catch (NumberFormatException e) {


                    /*
                     * ------------------------------------------------
                     * NumberFormatException
                     * ------------------------------------------------
                     *
                     * ¿Qué sucedería con esta línea?
                     *
                     *      ABC;Marta;marta@email.com;622345345
                     *
                     * Al ejecutar:
                     *
                     *      Integer.parseInt("ABC")
                     *
                     * Java no puede convertir "ABC" en un entero.
                     *
                     * Se produce:
                     *
                     *      NumberFormatException
                     *
                     *
                     * Esta excepción es diferente de IOException.
                     *
                     * IOException:
                     *
                     *      CHECKED
                     *      Java obliga a gestionarla.
                     *
                     * NumberFormatException:
                     *
                     *      UNCHECKED
                     *      Java NO obliga a capturarla.
                     *
                     * Nosotros hemos decidido capturarla porque
                     * estamos leyendo datos externos y podrían
                     * estar mal formados.
                     */


                    /*
                     * Mostramos la línea problemática por
                     * la salida estándar de errores.
                     */
                    System.err.println(
                            "Cliente incorrecto " + linea
                    );
                }

            }

        }


        /*
         * Cuando terminamos de recorrer el fichero,
         * devolvemos todos los clientes que se hayan
         * podido reconstruir correctamente.
         */
        return resultado;
    }



    /**
     * Exporta una lista de productos a un fichero TXT.
     *
     * Este método todavía NO está implementado.
     *
     * La idea será aplicar exactamente el mismo proceso
     * que hemos utilizado con Cliente:
     *
     *      List<Producto>
     *             |
     *             v
     *         for-each
     *             |
     *             v
     *          Producto
     *             |
     *             v
     *       representación TXT
     */
    public static void exportarProductosTxt(
            Path ruta,
            List<Producto> productos
    ) throws IOException {


        // TODO: implementar posteriormente.

    }



    /**
     * Importa productos desde un fichero TXT.
     *
     * Todavía no está implementado.
     *
     * Posteriormente haremos:
     *
     *      fichero
     *         ↓
     *      readLine()
     *         ↓
     *      split()
     *         ↓
     *      conversiones
     *         ↓
     *      new Producto(...)
     *         ↓
     *      List<Producto>
     */
    public static List<Producto> importarProductosTxt(
            Path ruta
    ) throws IOException {


        // TODO: implementar posteriormente.


        /*
         * De momento devolvemos una lista vacía
         * para que el método compile.
         *
         * Este return es PROVISIONAL.
         */
        return new ArrayList<>();
    }



    // ============================================================
    // CSV
    // ============================================================


    /*
     * CSV parece inicialmente muy parecido a nuestro TXT:
     *
     *      TXT:
     *
     *      3;Marta;marta@email.com;622345345
     *
     *
     *      CSV:
     *
     *      3,Marta,marta@email.com,622345345
     *
     *
     * Sin embargo, aparece un problema:
     *
     * ¿qué ocurre si el propio dato contiene una coma?
     *
     * Por ejemplo:
     *
     *      Rodriguez, Pepe
     *
     *
     * No podemos escribir simplemente:
     *
     *      3,Rodriguez, Pepe,email,telefono
     *
     * porque la coma del nombre se confundiría con
     * una coma separadora.
     *
     * Necesitamos aplicar reglas de ESCAPE.
     */



    /**
     * Prepara un String para poder escribirlo correctamente
     * como campo de un fichero CSV.
     */
    private static String csv(String valor) {


        /*
         * --------------------------------------------------------
         * CASO 1: VALOR NULL
         * --------------------------------------------------------
         *
         * Si recibimos null decidimos representarlo como
         * una cadena vacía.
         *
         * Así evitamos problemas posteriores al llamar a:
         *
         *      valor.contains(...)
         *
         * porque llamar a un método sobre null provocaría
         * NullPointerException.
         */
        if (valor == null) {

            return "";
        }


        /*
         * Ejemplo:
         *
         *      Rodriguez, Pepe
         *
         * contiene una coma.
         *
         * En CSV tendremos que convertirlo en algo como:
         *
         *      "Rodriguez, Pepe"
         *
         * De esa forma, la coma queda dentro del campo.
         */


        /*
         * --------------------------------------------------------
         * ¿CUÁNDO NECESITAMOS COMILLAS?
         * --------------------------------------------------------
         *
         * Comprobamos si el texto contiene:
         *
         *      ,
         *
         * o:
         *
         *      salto de línea
         *
         * o:
         *
         *      "
         *
         *
         * || significa OR lógico:
         *
         *      condición 1
         *          O
         *      condición 2
         *          O
         *      condición 3
         */
        if (valor.contains(",")
                || valor.contains("\n")
                || valor.contains("\"")) {


            /*
             * Si el campo contiene caracteres especiales,
             * lo rodeamos con comillas.
             *
             *
             * Por ejemplo:
             *
             *      Rodriguez, Pepe
             *
             * pasa a:
             *
             *      "Rodriguez, Pepe"
             *
             *
             * Pero todavía tenemos otro problema:
             *
             * ¿qué pasa si el propio contenido tiene comillas?
             *
             * Por ejemplo:
             *
             *      Academia "Java"
             *
             *
             * En CSV una forma habitual de escapar una comilla
             * es duplicarla.
             *
             * Por tanto:
             *
             *      "
             *
             * se convierte en:
             *
             *      ""
             *
             *
             * Eso es precisamente lo que hacemos con:
             *
             *      valor.replace("\"", "\"\"")
             *
             *
             * Finalmente añadimos una comilla al principio
             * y otra al final.
             */
            return "\""
                    + valor.replace("\"", "\"\"")
                    + "\"";

        }


        /*
         * Si el texto no contiene ningún carácter que requiera
         * tratamiento especial, lo devolvemos tal cual.
         *
         * Por ejemplo:
         *
         *      Marta
         *
         * seguirá siendo:
         *
         *      Marta
         */
        return valor;
    }



    /**
     * Divide una línea CSV teniendo en cuenta
     * las comillas y los separadores.
     *
     * Este método será más complejo que hacer:
     *
     *      linea.split(",")
     *
     * porque una coma puede formar parte del propio dato.
     *
     * Ejemplo:
     *
     *      3,"Rodriguez, Pepe",email,600000000
     *
     * La coma entre Rodriguez y Pepe NO es un separador.
     *
     * Este método todavía está pendiente de implementación.
     */
    private static List<String> parseCsv(String linea) {


        // TODO: implementar el parser CSV.


        /*
         * Return provisional para que compile.
         */
        return new ArrayList<>();
    }



    /**
     * Exporta clientes a CSV.
     *
     * Posteriormente aquí:
     *
     *      - abriremos el fichero;
     *      - escribiremos la cabecera;
     *      - recorreremos los clientes;
     *      - utilizaremos csv() para escapar los campos;
     *      - escribiremos una línea por cliente.
     */
    public static void exportarClientesCsv(
            Path ruta,
            List<Cliente> clientes
    ) throws IOException {


        // TODO: implementar.

    }



    /**
     * Importa clientes desde CSV.
     *
     * Posteriormente utilizaremos parseCsv() para interpretar
     * correctamente cada línea.
     */
    public static List<Cliente> importarClientesCsv(
            Path ruta
    ) throws IOException {


        // TODO: implementar.


        return new ArrayList<>();
    }



    /**
     * Exporta productos a CSV.
     */
    public static void exportarProductosCsv(
            Path ruta,
            List<Producto> productos
    ) throws IOException {


        // TODO: implementar.

    }



    /**
     * Importa productos desde CSV.
     */
    public static List<Producto> importarProductosCsv(
            Path ruta
    ) throws IOException {


        // TODO: implementar.


        return new ArrayList<>();
    }



    // ============================================================
    // XML
    // ============================================================


    /*
     * Más adelante utilizaremos Jackson XML.
     *
     * La gran diferencia respecto a TXT será que ya no tendremos
     * que construir manualmente toda la representación.
     *
     *
     * TXT:
     *
     * Cliente
     *    |
     *    +-- getId()
     *    +-- getNombre()
     *    +-- getEmail()
     *    +-- getTelefono()
     *    |
     *    v
     * concatenamos manualmente
     *    |
     *    v
     * TXT
     *
     *
     * XML:
     *
     * Cliente
     *    |
     *    v
     * XmlMapper
     *    |
     *    v
     * XML
     */



    /**
     * Exporta clientes a XML.
     */
    public static void exportarClientesXml(
            Path ruta,
            List<Cliente> clientes
    ) throws IOException {

        /*
         * XML_MAPPER es el objeto de Jackson especializado en XML.
         *
         * writeValue() recibe:
         *
         *      1. el fichero de destino;
         *      2. el objeto que queremos serializar.
         *
         * ruta.toFile() convierte el Path en un File.
         *
         * Envolvemos la lista dentro de ClientesXml para que
         * Jackson tenga un elemento raíz que contenga los clientes.
         *
         * Conceptualmente:
         *
         *      List<Cliente>
         *            |
         *            v
         *      ClientesXml
         *            |
         *            v
         *       XML_MAPPER
         *            |
         *            v
         *          fichero XML
         */
        XML_MAPPER.writeValue(ruta.toFile(), new ClientesXml(clientes));

        /*
         * Esta segunda instrucción utiliza MAPPER, que es el
         * ObjectMapper configurado para JSON, y escribe sobre
         * la misma ruta.
         *
         * Por tanto, tal como está escrito el código original,
         * esta llamada vuelve a escribir el fichero después de
         * XML_MAPPER. Se conserva aquí porque forma parte del
         * código original, aunque conviene revisarla si el objetivo
         * del método es generar exclusivamente XML.
         */
        MAPPER.writeValue(ruta.toFile(), new ClientesXml(clientes));


    }



    /**
     * Importa clientes desde XML.
     */
    public static List<Cliente> importarClientesXml(
            Path ruta
    ) throws IOException {

        /*
         * readValue() realiza la DESERIALIZACIÓN:
         *
         *      fichero XML
         *           |
         *           v
         *      XML_MAPPER
         *           |
         *           v
         *      ClientesXml
         *
         * ruta.toFile() indica qué fichero queremos leer.
         *
         * ClientesXml.class indica a Jackson qué tipo de objeto
         * debe reconstruir a partir del contenido XML.
         */
        ClientesXml contenedor =
                XML_MAPPER.readValue(
                        ruta.toFile(),
                        ClientesXml.class
                );

        /*
         * Una vez reconstruido el contenedor obtenemos su lista
         * mediante getClientes().
         *
         * Utilizamos el operador ternario:
         *
         *      condicion ? valorSiTrue : valorSiFalse
         *
         * Si Jackson ha dejado la lista a null devolvemos una
         * ArrayList vacía. En caso contrario devolvemos la lista
         * de clientes recuperada del XML.
         */
        return contenedor.getClientes() == null
                ? new ArrayList<>()
                : contenedor.getClientes();


    }



    /**
     * Exporta productos a XML.
     */
    public static void exportarProductosXml(
            Path ruta,
            List<Producto> productos
    ) throws IOException {


        // TODO: implementar.

    }



    /**
     * Importa productos desde XML.
     */
    public static List<Producto> importarProductosXml(
            Path ruta
    ) throws IOException {


        // TODO: implementar.


        return new ArrayList<>();
    }



    // ============================================================
    // CLASES CONTENEDORAS PARA XML
    // ============================================================


    /*
     * Estas clases servirán posteriormente como "envoltorio"
     * de nuestras listas.
     *
     * Es decir, en lugar de entregar directamente a Jackson:
     *
     *      List<Cliente>
     *
     * podemos utilizar:
     *
     *      ClientesXml
     *           |
     *           +---- List<Cliente>
     *
     *
     * Esto nos permite tener una estructura XML raíz
     * más clara y controlada.
     */






    /**
     * Clase contenedora utilizada para representar
     * una lista de clientes en XML.
     */
    public static class ClientesXml {


        /*
         * Lista que realmente contiene los clientes.
         *
         * La inicializamos para evitar que inicialmente
         * tenga valor null.
         */
        private List<Cliente> clientes =
                new ArrayList<>();



        /*
         * Constructor vacío.
         *
         * Es especialmente importante para muchas herramientas
         * de serialización/deserialización.
         *
         * Jackson puede necesitar crear primero el objeto vacío
         * y después establecer sus propiedades.
         */
        public ClientesXml() {

        }



        /*
         * Constructor que permite crear directamente
         * el contenedor a partir de una lista.
         *
         * Ejemplo:
         *
         *      new ClientesXml(clientes)
         */
        public ClientesXml(List<Cliente> clientes) {

            this.clientes = clientes;

        }



        /*
         * Getter.
         *
         * Devuelve la lista de clientes.
         */
        public List<Cliente> getClientes() {

            return clientes;

        }



        /*
         * Setter.
         *
         * Permite sustituir la lista de clientes.
         */
        public void setClientes(List<Cliente> clientes) {

            this.clientes = clientes;

        }
    }



    /**
     * Clase equivalente para los productos.
     */
    public static class ProductosXml {


        private List<Producto> productos =
                new ArrayList<>();



        /*
         * Constructor vacío.
         */
        public ProductosXml() {

        }



        /*
         * Constructor con lista de productos.
         */
        public ProductosXml(List<Producto> productos) {

            this.productos = productos;

        }



        /*
         * Getter.
         */
        public List<Producto> getProductos() {

            return productos;

        }



        /*
         * Setter.
         */
        public void setProductos(List<Producto> productos) {

            this.productos = productos;

        }
    }



    // ============================================================
    // JSON
    // ============================================================


    /*
     * Finalmente trabajaremos con JSON mediante Jackson.
     *
     * Conceptualmente tendremos:
     *
     *      List<Cliente>
     *            |
     *            v
     *       ObjectMapper
     *            |
     *            v
     *          JSON
     *
     *
     * y para importar:
     *
     *          JSON
     *            |
     *            v
     *       ObjectMapper
     *            |
     *            v
     *      List<Cliente>
     */



    /**
     * Exporta clientes a JSON.
     */
    public static void exportarClientesJson(
            Path ruta,
            List<Cliente> clientes
    ) throws IOException {

        /*
         * writeValue() SERIALIZA la lista de clientes.
         *
         * Es decir, transforma:
         *
         *      List<Cliente>
         *            |
         *            v
         *       ObjectMapper
         *            |
         *            v
         *          JSON
         *
         * ruta.toFile() indica el fichero donde se guardará
         * el resultado y clientes es el objeto que queremos
         * convertir a JSON.
         *
         * Como MAPPER está configurado con INDENT_OUTPUT, el
         * JSON se escribirá indentado y será más fácil de leer.
         */
        MAPPER.writeValue(ruta.toFile(), clientes);

    }



    /**
     * Importa clientes desde JSON.
     */
    public static List<Cliente> importarClientesJson(
            Path ruta
    ) throws IOException {

        /*
         * readValue() realiza el proceso contrario: DESERIALIZA
         * el contenido del fichero JSON y reconstruye objetos Java.
         *
         * Queremos obtener exactamente:
         *
         *      List<Cliente>
         *
         * No podemos utilizar directamente List<Cliente>.class
         * debido al borrado de tipos genéricos (type erasure)
         * de Java.
         *
         * Por eso utilizamos TypeReference<List<Cliente>>.
         * De esta forma Jackson conserva la información necesaria
         * para saber que la lista contiene objetos Cliente.
         */
        return MAPPER.readValue(
                ruta.toFile(),
                new TypeReference<List<Cliente>>() {}
        );
    }



    /**
     * Exporta productos a JSON.
     */
    public static void exportarProductosJson(
            Path ruta,
            List<Producto> productos
    ) throws IOException {


        // TODO: implementar.

    }



    /**
     * Importa productos desde JSON.
     */
    public static List<Producto> importarProductosJson(
            Path ruta
    ) throws IOException {


        // TODO: implementar.


        return new ArrayList<>();
    }

}
