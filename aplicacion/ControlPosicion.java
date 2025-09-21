package aplicacion;

public class ControlPosicion {
    private int posicion;

    public ControlPosicion() {
        posicion = 1;
    }

    public void incrementar() {
        posicion++;
    }

    public void decrementar() {
        posicion--;
    }

    public int getPosicion() {
        return posicion;
    }
}
