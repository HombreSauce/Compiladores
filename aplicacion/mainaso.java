package aplicacion;
import acciones_semanticas.AccionSemantica;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import aplicacion.funcionalidades.MiniTokenizador;
import datos.*;

public class mainaso {        /////////////////// ESTO NO VA ACA, VA EN EL Constructor del AnalizadorLexico /////////////////
     public static void main(String[] args){
        TablaIdentificadorToken tablaIdentificadorToken = TablaIdentificadorToken.getInstancia();
        TablaPalabraReservada tablaPalabraReservada = TablaPalabraReservada.getInstancia();
        TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();

         //Verifica que el usuario pase un archivo como argumento
        if (args.length < 1) {
           System.err.println("Uso: java AnalizadorLexico <archivo>");
           System.exit(1);
        }

        try {
             //Carga lo que hay en el archivo a un string
            String contenido = Files.readString(Paths.get(args[0]));

            AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico(contenido);
            if (analizadorSintactico.yyparse() == 0) 
            {
                System.out.println("Análisis exitoso");
            }       
            else 
            {
                System.out.println("Errores en el análisis sintáctico");
            }
            tablaSimbolos.mostrarTabla();
            System.out.println("Fin del análisis lexico y sintacc.");
        } catch (IOException e) {
          System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
