package datos;
import acciones_semanticas.*;

public class MatrizAccionSemantica {
    //Instancias de las tablas anteriores
    private static TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();
    private static TablaIdentificadorToken tablaIDToken = TablaIdentificadorToken.getInstancia();
    private static TablaPalabraReservada tablaPalabraReservada = TablaPalabraReservada.getInstancia();
    // Instancias únicas de acciones
    private static final AccionSemantica AS1 = new AccionSemantica1();
    private static final AccionSemantica AS2 = new AccionSemantica2();
    private static final AccionSemantica AS3 = new AccionSemantica3(tablaSimbolos, tablaIDToken);
    private static final AccionSemantica AS4 = new AccionSemantica4(tablaIDToken);
    private static final AccionSemantica AS5 = new AccionSemantica5(tablaIDToken);
    private static final AccionSemantica AS6 = new AccionSemantica6(tablaIDToken);
    private static final AccionSemantica AS7 = new AccionSemantica7(tablaPalabraReservada);
    private static final AccionSemantica AS8 = new AccionSemantica8(tablaSimbolos, tablaIDToken);
    private static final AccionSemantica AS9 = new AccionSemantica9(tablaSimbolos, tablaIDToken);
    private static final AccionSemantica AS10 = new AccionSemantica10();
    private static final AccionSemantica AS11 = new AccionSemantica11(tablaSimbolos, tablaIDToken);
    private static final AccionSemantica AS12 = new AccionSemantica12(AS4, AS9);
    private static final AccionSemantica ASErr1 = new AccionSemanticaError1();
    private static final AccionSemantica ASErr2_1 = new AccionSemanticaError2("=");
    private static final AccionSemantica ASErr3 = new AccionSemanticaError3(AS8);
    private static final AccionSemantica ASErr4 = new AccionSemanticaError4();
    private static final AccionSemantica ASErr2_2 = new AccionSemanticaError2("+' o '-");
    private static final AccionSemantica ASErr2_3 = new AccionSemanticaError2("&");
    private static final AccionSemantica ASnull = new AccionSemanticaNULL(); 

    
    public static final AccionSemantica[][] MATRIZ_AS = {
        //  L            num      %          +         *         /          -          :        =          <          >          l           .        @           &         Tab         \n       espacio          ,          ;          (        )          {          }        _         I          D         !        C       \EOF
        {  AS1,          AS1,  ASErr1,      AS5,      AS5,      AS5,       AS1,       AS1,     AS1,       AS1,       AS1,       AS1,       AS1,    ASnull,      AS10,     ASnull,     ASnull,     ASnull,        AS5,       AS5,      AS5,      AS5,       AS5,      AS5,      AS5,      AS1,       AS1,   ASErr1,   ASErr1,   ASnull }, // Estado 0
        {  AS2,          AS2,     AS2,      AS3,      AS3,      AS3,       AS3,       AS3,     AS3,       AS3,       AS3,       AS3,       AS3,       AS3,       AS3,        AS3,        AS3,        AS3,        AS3,       AS3,      AS3,      AS3,       AS3,      AS3,      AS3,      AS2,       AS2,      AS3,      AS3,      AS3 }, // Estado 1
        {  ASErr2_1,ASErr2_1,ASErr2_1, ASErr2_1, ASErr2_1, ASErr2_1,  ASErr2_1,  ASErr2_1,     AS6,  ASErr2_1,  ASErr2_1,  ASErr2_1,  ASErr2_1,  ASErr2_1,  ASErr2_1,   ASErr2_1,   ASErr2_1,   ASErr2_1,   ASErr2_1,  ASErr2_1, ASErr2_1, ASErr2_1,  ASErr2_1, ASErr2_1, ASErr2_1, ASErr2_1,  ASErr2_1, ASErr2_1, ASErr2_1, ASErr2_1 }, // Estado 2
        {  AS4,          AS4,     AS4,      AS4,      AS4,      AS4,       AS4,       AS4,     AS6,       AS4,       AS4,       AS4,       AS4,       AS4,       AS4,        AS4,        AS4,        AS4,        AS4,       AS4,      AS4,      AS4,       AS4,      AS4,      AS4,      AS4,       AS4,      AS4,      AS4,      AS4 }, // Estado 3
        {  AS4,          AS4,     AS4,      AS4,      AS4,      AS4,       AS4,       AS4,     AS6,       AS4,       AS4,       AS4,       AS4,       AS4,       AS4,        AS4,        AS4,        AS4,        AS4,       AS4,      AS4,      AS4,       AS4,      AS4,      AS4,      AS4,       AS4,      AS6,      AS4,      AS4 }, // Estado 4
        {  AS4,          AS4,     AS4,      AS4,      AS4,      AS4,       AS4,       AS4,     AS4,       AS4,       AS6,       AS4,       AS4,       AS4,       AS4,        AS4,        AS4,        AS4,        AS4,       AS4,      AS4,      AS4,       AS4,      AS4,      AS4,      AS4,       AS4,      AS4,      AS4,      AS4 }, // Estado 5
        {  AS7,          AS7,     AS7,      AS7,      AS7,      AS7,       AS7,       AS7,     AS7,       AS7,       AS7,       AS2,       AS7,       AS7,       AS7,        AS7,        AS7,        AS7,        AS7,       AS7,      AS7,      AS7,       AS7,      AS7,      AS7,      AS7,       AS7,      AS7,      AS7,      AS7 }, // Estado 6
        {  ASErr3,       AS2,  ASErr3,   ASErr3,   ASErr3,   ASErr3,    ASErr3,    ASErr3,  ASErr3,    ASErr3,    ASErr3,    ASErr3,       AS2,    ASErr3,    ASErr3,     ASErr3,     ASErr3,     ASErr3,     ASErr3,    ASErr3,   ASErr3,   ASErr3,    ASErr3,   ASErr3,   ASErr3,      AS8,    ASErr4,   ASErr3,   ASErr3,   ASErr3 }, // Estado 7
        {  AS12,         AS2,     AS12,      AS12,      AS12,      AS12,       AS12,       AS12,     AS12,       AS12,       AS12,       AS12,       AS12,       AS12,       AS12,        AS12,        AS12,        AS12,        AS12,       AS12,      AS12,      AS12,       AS12,      AS12,      AS12,      AS12,       AS2,      AS12,      AS12,      AS12 }, // Estado 8
        {  ASErr2_2,ASErr2_2,ASErr2_2,      AS2, ASErr2_2, ASErr2_2,       AS2,  ASErr2_2,ASErr2_2,  ASErr2_2,  ASErr2_2,  ASErr2_2,  ASErr2_2,  ASErr2_2,  ASErr2_2,   ASErr2_2,   ASErr2_2,   ASErr2_2,   ASErr2_2,  ASErr2_2, ASErr2_2, ASErr2_2,  ASErr2_2, ASErr2_2, ASErr2_2, ASErr2_2,  ASErr2_2, ASErr2_2, ASErr2_2, ASErr2_2 }, // Estado 9
        {  AS9,          AS2,     AS9,      AS9,      AS9,      AS9,       AS9,       AS9,     AS9,       AS9,       AS9,       AS9,       AS9,       AS9,       AS9,        AS9,        AS9,        AS9,        AS9,       AS9,      AS9,      AS9,       AS9,      AS9,      AS9,      AS9,       AS2,      AS9,      AS9,      AS9 }, // Estado 10
        {  AS2,          AS2,     AS2,      AS2,      AS2,      AS2,       AS2,       AS2,     AS2,       AS2,       AS2,       AS2,       AS2,       AS2,      AS11,        AS2,        AS2,        AS2,        AS2,       AS2,      AS2,      AS2,       AS2,      AS2,      AS2,      AS2,       AS2,      AS2,      AS2, ASErr2_3 }, // Estado 11
        { ASnull,     ASnull,  ASnull,   ASnull,   ASnull,   ASnull,    ASnull,    ASnull,  ASnull,    ASnull,    ASnull,    ASnull,    ASnull,    ASnull,    ASnull,     ASnull,     ASnull,     ASnull,     ASnull,    ASnull,   ASnull,   ASnull,    ASnull,   ASnull,   ASnull,   ASnull,    ASnull,   ASnull,   ASnull,    ASnull}, // Estado 12
        {  AS9,          AS2,     AS9,      AS9,      AS9,      AS9,       AS9,       AS9,     AS9,       AS9,       AS9,       AS9,       AS9,       AS9,       AS9,        AS9,        AS9,        AS9,        AS9,       AS9,      AS9,      AS9,       AS9,      AS9,      AS9,      AS9,       AS9,      AS9,      AS9,      AS9 }, // Estado 13
        //El estado 14 no está porque una vez que nos dé estado actual 14 sabemos que es estado final
    };
}