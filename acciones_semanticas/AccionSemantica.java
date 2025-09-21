package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public interface AccionSemantica {

    public abstract Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura);
}                                   //  char q lei,     creo yo      ,        inicioIns fijate de ponerlo en la clase AnalizadorLexico      
