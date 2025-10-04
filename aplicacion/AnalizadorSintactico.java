package aplicacion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import datos.TablaIdentificadorToken;
import datos.TablaPalabraReservada;
import datos.TablaSimbolos;

public class AnalizadorSintactico extends Parser {  /// QSY ES PARA EL YYLVAL y añadi un coso al parser.java

    private AnalizadorLexico analizadorLexico;

    public int yylex() {  // Se puede eliminiar y llamar directamente al analizador lexico desde el parser con yylex ??????
        Token token = null;
        if ((token = analizadorLexico.getToken()) != null) { 
            yylval = new ParserVal(token.getIDToken()); // valor léxico o semántico del token 
            System.out.print("Token ID: " + token.getIDToken() + ". ");
            return token.getIDToken();
        } else {
            return 0; // Indica que no hay más tokens
        }
    }
    public void yyerror(String mensaje) {
    System.err.println("Error sintáctico en línea " + analizadorLexico.getLineaActual() + ": " + mensaje);
}


    public AnalizadorSintactico(String contenido) {
        System.out.println("Contenido del archivo:");
        System.out.println(contenido);
        this.analizadorLexico = new AnalizadorLexico(contenido);
    } 
}
