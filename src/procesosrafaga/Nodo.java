
package procesosrafaga;

public class Nodo {
    
    private String llave;
    private int rafaga;
    private int llegada;
    private int comienzo;
    private int retorno;
    private int finalizacion;
    private int indice;
    private Nodo siguiente;
    
    Nodo(String llave, int rafaga, int llegada, int indice){
    
        this.llave = llave;
        this.rafaga = rafaga;
        this.llegada = llegada;
        this.indice = indice;
        this.siguiente = null;
        
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public int getRafaga() {
        return rafaga;
    }

    public void setRafaga(int rafaga) {
        this.rafaga = rafaga;
    }

    public int getLlegada() {
        return llegada;
    }

    public void setLlegada(int llegada) {
        this.llegada = llegada;
    }

    public int getComienzo() {
        return comienzo;
    }

    public void setComienzo(int comienzo) {
        this.comienzo = comienzo;
    }

    public int getRetorno() {
        return retorno;
    }

    public void setRetorno(int retorno) {
        this.retorno = retorno;
    }

    public int getFinalizacion() {
        return finalizacion;
    }

    public void setFinalizacion(int finalizacion) {
        this.finalizacion = finalizacion;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }
    
}
