package aplicacion;

public class ControlPosicion {
    private int posicion;

    public ControlPosicion() {
        posicion = 0;
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

    public void setPosicion (int posicion) {
        this.posicion = posicion;
    }
}
