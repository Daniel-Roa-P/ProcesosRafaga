
package procesosrafaga;

public class ListaCircular {
    
    private Nodo cabeza;
    private Nodo ultimo;
    private int tamaño;
    private String cadenaLista;
    
    ListaCircular(){
        
       this.cabeza = null; 
       this.ultimo = null; 
       this.tamaño = 0;
       
    }
    
    void insertar(String s, int r, int l, int i){
        
        Nodo p = new Nodo(s, r, l, i);
        Nodo sig = null;
        
        if(cabeza == null){
            
            cabeza = p;
            ultimo = p;
            cabeza.setSiguiente(ultimo);
            
        } else {
            
            ultimo.setSiguiente(p);

            p.setSiguiente(cabeza);

            ultimo = p;
            
        }
        
        tamaño ++;
        
    }
    
    public void eliminar(Nodo nodoAtendido){
        
        Nodo actual = nodoAtendido;
                
        if(actual.equals(ultimo)){

            ultimo = null;
            this.cabeza = null; 
            actual = null;
            tamaño--;

        }else if(actual.equals(cabeza)) {

            cabeza = actual.getSiguiente();
            ultimo.setSiguiente(cabeza);
            actual = null;
            tamaño--;

        } 
        
    }

    public void intercambiar(Nodo nodoAtendido){
    
        cabeza = nodoAtendido.getSiguiente();
        ultimo.setSiguiente(nodoAtendido);
        
        ultimo = nodoAtendido;
        ultimo.setSiguiente(cabeza);
    
    }
    
    public int getTamaño() {
        return tamaño;
    }

    public Nodo getCabeza() {
        return cabeza;
    }

    public Nodo getUltimo() {
        return ultimo;
    }

    public void setUltimo(Nodo ultimo) {
        this.ultimo = ultimo;
    }
    
}
