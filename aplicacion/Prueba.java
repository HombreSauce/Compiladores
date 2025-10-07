// package aplicacion;

// import acciones_semanticas.AccionSemantica;
// import datos.MatrizAccionSemantica;

// public class Prueba {
//     private static AccionSemantica[][] matrizAS = MatrizAccionSemantica.MATRIZ_AS;
    
//     public static void main(String[] args) throws Exception {
//         StringBuilder lexema = new StringBuilder(20);
//         Token token;
//         ControlPosicion posicion = new ControlPosicion();
//         // System.out.println("lexema: " + lexema);        
//         // System.out.println("Token ID: " + token.getIDToken());
//         // System.out.println("Token ref: " + token.getEntradaTS());
//         // System.out.println("Posicion: " + posicion.getPosicion());
//         // token = matrizAS[0][1].ejecutar('3', lexema, posicion);
//         token = matrizAS[7][1].ejecutar('3', lexema, posicion);
//         token = matrizAS[7][12].ejecutar('.', lexema, posicion);
//         // token = matrizAS[8][26].ejecutar('D', lexema, posicion);
//         // token = matrizAS[9][3].ejecutar('+', lexema, posicion);
//         token = matrizAS[10][1].ejecutar('3', lexema, posicion);
//         token = matrizAS[10][0].ejecutar('L', lexema, posicion);
//         System.out.println("lexema: " + lexema);        
//         System.out.println("Token ID: " + token.getIDToken());
//         System.out.println("Token ref: " + token.getEntradaTS());
        
//     }
// }