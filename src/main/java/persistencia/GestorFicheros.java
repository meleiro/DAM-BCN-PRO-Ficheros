package persistencia;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import modelo.Cliente;
import modelo.Producto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


public class GestorFicheros {





    // ============================================================
    // TXT
    // ============================================================


    /**
     * Exporta una lista de clientes a un fichero TXT.
     */
    public static void exportarClientesTxt(
            Path ruta,
            List<Cliente> clientes
    ) throws IOException {



    }


    /**
     * Importa clientes desde un fichero TXT.
     */
    public static List<Cliente> importarClientesTxt(
            Path ruta
    ) throws IOException {



        return new ArrayList<>();
    }


    /**
     * Exporta una lista de productos a un fichero TXT.
     */
    public static void exportarProductosTxt(
            Path ruta,
            List<Producto> productos
    ) throws IOException {



    }


    /**
     * Importa productos desde un fichero TXT.
     */
    public static List<Producto> importarProductosTxt(
            Path ruta
    ) throws IOException {



        return new ArrayList<>();
    }



    // ============================================================
    // CSV
    // ============================================================


    /**
     * Prepara un String para poder escribirlo correctamente
     * como campo de un fichero CSV.
     */
    private static String csv(String valor) {



        return "";
    }


    /**
     * Divide una línea CSV teniendo en cuenta
     * las comillas y los separadores.
     */
    private static List<String> parseCsv(String linea) {



        return new ArrayList<>();
    }


    /**
     * Exporta clientes a CSV.
     */
    public static void exportarClientesCsv(
            Path ruta,
            List<Cliente> clientes
    ) throws IOException {



    }


    /**
     * Importa clientes desde CSV.
     */
    public static List<Cliente> importarClientesCsv(
            Path ruta
    ) throws IOException {



        return new ArrayList<>();
    }


    /**
     * Exporta productos a CSV.
     */
    public static void exportarProductosCsv(
            Path ruta,
            List<Producto> productos
    ) throws IOException {



    }


    /**
     * Importa productos desde CSV.
     */
    public static List<Producto> importarProductosCsv(
            Path ruta
    ) throws IOException {



        return new ArrayList<>();
    }



    // ============================================================
    // XML
    // ============================================================


    /**
     * Exporta clientes a XML.
     */
    public static void exportarClientesXml(
            Path ruta,
            List<Cliente> clientes
    ) throws IOException {



    }


    /**
     * Importa clientes desde XML.
     */
    public static List<Cliente> importarClientesXml(
            Path ruta
    ) throws IOException {



        return new ArrayList<>();
    }


    /**
     * Exporta productos a XML.
     */
    public static void exportarProductosXml(
            Path ruta,
            List<Producto> productos
    ) throws IOException {



    }


    /**
     * Importa productos desde XML.
     */
    public static List<Producto> importarProductosXml(
            Path ruta
    ) throws IOException {



        return new ArrayList<>();
    }



    // ============================================================
    // CLASES CONTENEDORAS PARA XML
    // ============================================================


    /**
     * Clase contenedora utilizada para representar
     * una lista de clientes en XML.
     */
    public static class ClientesXml {

        private List<Cliente> clientes = new ArrayList<>();


        public ClientesXml() {

        }


        public ClientesXml(List<Cliente> clientes) {

            this.clientes = clientes;

        }


        public List<Cliente> getClientes() {

            return clientes;

        }


        public void setClientes(List<Cliente> clientes) {

            this.clientes = clientes;

        }
    }



    /**
     * Clase contenedora utilizada para representar
     * una lista de productos en XML.
     */
    public static class ProductosXml {

        private List<Producto> productos = new ArrayList<>();


        public ProductosXml() {

        }


        public ProductosXml(List<Producto> productos) {

            this.productos = productos;

        }


        public List<Producto> getProductos() {

            return productos;

        }


        public void setProductos(List<Producto> productos) {

            this.productos = productos;

        }
    }



    // ============================================================
    // JSON
    // ============================================================


    /**
     * Exporta clientes a JSON.
     */
    public static void exportarClientesJson(
            Path ruta,
            List<Cliente> clientes
    ) throws IOException {



    }


    /**
     * Importa clientes desde JSON.
     */
    public static List<Cliente> importarClientesJson(
            Path ruta
    ) throws IOException {



        return new ArrayList<>();
    }


    /**
     * Exporta productos a JSON.
     */
    public static void exportarProductosJson(
            Path ruta,
            List<Producto> productos
    ) throws IOException {



    }


    /**
     * Importa productos desde JSON.
     */
    public static List<Producto> importarProductosJson(
            Path ruta
    ) throws IOException {



        return new ArrayList<>();
    }

}