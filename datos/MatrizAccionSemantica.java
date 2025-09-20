package datos;
import acciones_semanticas.*;

public class MatrizAccionSemantica {
    // Instancias únicas de acciones
    private static final AccionSemantica AS1 = new AccionSemantica1();
    private static final AccionSemantica AS2 = new AccionSemantica2();
    private static final AccionSemantica AS3 = new AccionSemantica1();
    private static final AccionSemantica AS4 = new AccionSemantica1();
    private static final AccionSemantica AS5 = new AccionSemantica1();
    private static final AccionSemantica AS6 = new AccionSemantica1();
    private static final AccionSemantica AS7 = new AccionSemantica1();
    private static final AccionSemantica AS8 = new AccionSemantica1();
    private static final AccionSemantica AS9 = new AccionSemantica1();
    private static final AccionSemantica AS10 = new AccionSemantica1();
    private static final AccionSemantica AS11 = new AccionSemantica1();
    private static final AccionSemantica err = new AccionSemantica1(); //ACCION SEMANTICA ERROR SERíA??????
    
    public static final AccionSemantica[][] MATRIZ_AS = {
        //  L     num    %    +    *    /     -     :     =     <     >     l     .    @     &     Tab    \n    espacio   ,     ;    (    )     {     }   _    I     D    !    C
        {  AS1,   AS1,  err, AS5, AS5, AS5,  AS1,  AS1,  AS1,  AS1,  AS1,  AS1,  AS1, null, AS10,  null,  null,  null,   AS5,  AS5, AS5, AS5,  AS5, AS5, AS5, AS1,  AS1, err, err }, // Estado 0
        {  AS2,   AS2,  AS2, AS3, AS3, AS3,  AS3,  AS3,  AS3,  AS3,  AS3,  AS3,  AS3,  AS3,  AS3,   AS3,   AS3,   AS3,   AS3,  AS3, AS3, AS3,  AS3, AS3, AS3, AS2,  AS2, AS3, AS3 }, // Estado 1
        {  err,   err,  err, err, err, err,  err,  err,  AS6,  err,  err,  err,  err,  err,  err,   err,   err,   err,   err,  err, err, err,  err, err, err, err,  err, err, err }, // Estado 2
        {  AS4,   AS4,  AS4, AS4, AS4, AS4,  AS4,  AS4,  AS6,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,   AS4,   AS4,   AS4,   AS4,  AS4, AS4, AS4,  AS4, AS4, AS4,  AS4, AS4, AS4, AS4 }, // Estado 3
        {  AS4,   AS4,  AS4, AS4, AS4, AS4,  AS4,  AS4,  AS6,  AS4,  AS4,  AS4,  AS4,  AS4,  AS4,   AS4,   AS4,   AS4,   AS4,  AS4, AS4, AS4,  AS4, AS4, AS4,  AS4, AS4, AS4, AS4 }, // Estado 4
        {  AS4,   AS4,  AS4, AS4, AS4, AS4,  AS4,  AS4,  AS4,  AS4,  AS6,  AS4,  AS4,  AS4,  AS4,   AS4,   AS4,   AS4,   AS4,  AS4, AS4, AS4,  AS4, AS4, AS4,  AS4, AS4, AS4, AS4 }, // Estado 5
        {  AS7,   AS7,  AS7, AS7, AS7, AS7,  AS7,  AS7,  AS7,  AS7,  AS7,  AS2,  AS7,  AS7,  AS7,   AS7,   AS7,   AS7,   AS7,  AS7, AS7, AS7,  AS7, AS7, AS7,  AS7, AS7, AS7, AS7 }, // Estado 6
        {  err,   AS2,  err, err, err, err,  err,  err,  err,  err,  err,  err,  AS2,  err,  err,   err,   err,   err,   err,  err, err, err,  err, err, err,  AS8, err, err, err }, // Estado 7
        {  AS9,   AS2,  err, AS9, AS9, AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,   AS9,   AS9,   AS9,   AS9,  AS9, AS9, AS9,  AS9, AS9, AS9,  AS9, AS2, AS9, AS9 }, // Estado 8
        {  err,   err,  err, AS2, err, err,  AS2,  err,  err,  err,  err,  err,  err,  err,  err,   err,   err,   err,   err,  err, err, err,  err, err, err,  err, err, err, err }, // Estado 9
        {  AS9,   AS2,  AS9, AS9, AS9, AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,  AS9,   AS9,   AS9,   AS9,   AS9,  AS9, AS9, AS9,  AS9, AS9, AS9,  AS9, AS9, AS9, AS9 }, // Estado 10
        {  AS2,   AS2,  AS2, AS2, AS2, AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2,  AS2, AS11,   AS2,   AS2,   AS2,   AS2,  AS2, AS2, AS2,  AS2, AS2, AS2,  AS2, AS2, AS2, AS2 }, // Estado 11
        { null,  null, null,null,null,null, null, null, null, null, null, null, null, null, null,  null,  null,  null,  null,  null,null,null, null,null,null, null,null,null,null }, // Estado 12
        //El estado 13 no está porque una vez que nos dé estado actual 13 sabemos que es estado final
    };
}