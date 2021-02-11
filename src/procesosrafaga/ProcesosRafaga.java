
package procesosrafaga;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class ProcesosRafaga extends JFrame implements Runnable ,ActionListener {

    JScrollPane scrollPane = new JScrollPane();
    JScrollPane scrollPane1 = new JScrollPane();
    
    JScrollPane scrollPane2 = new JScrollPane();
    JScrollPane scrollPane3 = new JScrollPane();
    
    JScrollPane scrollPane4 = new JScrollPane();
    JScrollPane scrollPane5 = new JScrollPane();
    
    JLabel semaforo = new JLabel();
    
    JLabel label1 = new JLabel("Nombre del proceso: ");
    JLabel label2 = new JLabel("Rafaga aleatoria de 1 - 12");
    JLabel label3 = new JLabel("Proceso en ejecucion: Ninguno");
    JLabel label4 = new JLabel("Tiempo: ");
    JLabel label5 = new JLabel("Tabla de procesos:");
    JLabel label6 = new JLabel("Diagrama de Gant:");
    JLabel label7 = new JLabel("Tabla de Bloqueados:");
    JLabel label8 = new JLabel("Rafaga restante del proceso: 0");
    
    JButton botonIngresar = new JButton("Ingresar proceso");
    JButton botonIniciar = new JButton("Iniciar ejecucion");
    JButton botonBloquear = new JButton("Bloquear proceso");
    
    JTextField tfNombre = new JTextField("P1");
    
    JTextField[][] tabla = new JTextField[100][7];
    JTextField[][] tablaBloqueados = new JTextField[100][4];
    JLabel[][] diagrama = new JLabel[40][100];  
    
    ListaCircular cola = new ListaCircular();
    
    Nodo nodoEjecutado;
    
    int filas = 0, rafagaTemporal;
    int tiempoGlobal = 0;
    int coorX = 0, coorY = 1;
    
    Thread procesos;
    public static void main(String[] args) {
        
        ProcesosRafaga pr = new ProcesosRafaga(); 
        pr.setBounds(0, 0, 1200, 730);
        pr.setTitle("Procesos con menor rafaga ");
        pr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pr.setVisible(true);
        
    }
    

    ProcesosRafaga(){
        
        Container c = getContentPane();
        c.setLayout(null);
        this.getContentPane().setBackground(Color.GRAY);
        
        c.add(label1);
        c.add(label2);
        c.add(label3);
        c.add(label4);
        c.add(label5);
        c.add(label6);
        c.add(label7);
        c.add(label8);
        c.add(semaforo);
        
        c.add(scrollPane1);
        c.add(scrollPane3);
        c.add(scrollPane5);
        
        c.add(botonIngresar);
        c.add(botonIniciar);
        c.add(botonBloquear);
        
        c.add(tfNombre);
        
        label1.setBounds(800, 40, 300, 20);
        label2.setBounds(800, 70, 300, 20);
        label3.setBounds(800, 250, 300, 20);
        label4.setBounds(1020, 250, 300, 20);
        label5.setBounds(50, 20, 300, 20);
        label6.setBounds(50, 280, 300, 20);
        label7.setBounds(800, 280, 300, 20);
        label8.setBounds(800, 265, 300, 20);
        
        scrollPane.setBounds(50, 40, 2500, 2500);
        scrollPane.setPreferredSize(new Dimension(2500, 2500));  
        scrollPane.setBackground(Color.WHITE);
        
        scrollPane1.setBounds(50, 40, 700, 230);
        scrollPane1.setPreferredSize(new Dimension(1150, 400)); 
        scrollPane1.setBackground(Color.WHITE);
        
        scrollPane2.setBounds(50, 300, 2500, 2500);
        scrollPane2.setPreferredSize(new Dimension(2500, 2500));  
        scrollPane2.setBackground(Color.WHITE);
        
        scrollPane3.setBounds(50, 300, 700, 350);
        scrollPane3.setPreferredSize(new Dimension(1150, 400)); 
        scrollPane3.setBackground(Color.WHITE);
        
        scrollPane2.setBounds(50, 300, 2500, 2500);
        scrollPane2.setPreferredSize(new Dimension(2500, 2500));  
        scrollPane2.setBackground(Color.WHITE);
        
        scrollPane3.setBounds(50, 300, 700, 350);
        scrollPane3.setPreferredSize(new Dimension(700, 350)); 
        scrollPane3.setBackground(Color.WHITE);
        
        scrollPane4.setBounds(800, 300, 500, 1000);
        scrollPane4.setPreferredSize(new Dimension(500, 1000));  
        scrollPane4.setBackground(Color.WHITE);
        
        scrollPane5.setBounds(800, 300, 350, 350);
        scrollPane5.setPreferredSize(new Dimension(350, 350)); 
        scrollPane5.setBackground(Color.WHITE);
        
        tfNombre.setBounds(930, 40, 70, 20);
        
        botonIngresar.addActionListener(this);
        botonIngresar.setBounds(800, 100, 200, 40);
        botonIngresar.setBackground(Color.CYAN);
        
        botonIniciar.addActionListener(this);
        botonIniciar.setBounds(800, 150, 200, 40);
        botonIniciar.setBackground(Color.GREEN);
        
        botonBloquear.addActionListener(this);
        botonBloquear.setBounds(800, 200, 200, 40);
        botonBloquear.setBackground(Color.RED);
        
        dibujarSemaforo("Verde.jpg");
        
    }
    
    public void dibujarSemaforo(String color){
        
        JLabel img = new JLabel();
        
        ImageIcon imgIcon = new ImageIcon(getClass().getResource(color));

        Image imgEscalada = imgIcon.getImage().getScaledInstance(130, 200, Image.SCALE_SMOOTH);
        Icon iconoEscalado = new ImageIcon(imgEscalada);
        semaforo.setBounds(1020 , 40, 130, 200);
        semaforo.setIcon(iconoEscalado);
     
    }
    
    public void dibujarTabla(String nombre, int rafaga, int tiempo){
        
        scrollPane.removeAll();
        
        JLabel texto1 = new JLabel("Proceso");
        JLabel texto2 = new JLabel("T. llegada");
        JLabel texto3 = new JLabel("Rafaga");
        JLabel texto4 = new JLabel("T. comienzo");
        JLabel texto5 = new JLabel("T. final");
        JLabel texto6 = new JLabel("T. retorno");
        JLabel texto7 = new JLabel("T. espera");
        
        texto1.setBounds(20, 20, 150, 20);
        texto2.setBounds(100, 20, 150, 20);
        texto3.setBounds(180, 20, 150, 20);
        texto4.setBounds(260, 20, 150, 20);
        texto5.setBounds(340, 20, 150, 20);
        texto6.setBounds(420, 20, 150, 20);
        texto7.setBounds(500, 20, 150, 20);
        
        scrollPane.add(texto1);
        scrollPane.add(texto2);
        scrollPane.add(texto3);
        scrollPane.add(texto4);
        scrollPane.add(texto5);
        scrollPane.add(texto6);
        scrollPane.add(texto7);
        
        for(int i = 0; i<filas; i++){
            
            for(int j = 0; j<7; j++){
            
                if(tabla[i][j] != null){
                    
                    scrollPane.add(tabla[i][j]);
                    
                } else {
                
                    tabla[i][j] = new JTextField("");
                    tabla[i][j].setBounds(20 + (j*80), 40 + (i*25), 70, 20);
                    
                    scrollPane.add(tabla[i][j]);
                    
                }

            }
        
        }
        
        tabla[filas-1][0].setText(nombre);
        tabla[filas-1][1].setText(Integer.toString(tiempo));
        tabla[filas-1][2].setText(Integer.toString(rafaga));

        scrollPane.repaint();
        scrollPane1.setViewportView(scrollPane);
            
    }
    
    public void llenarBloqueados(){
        
        scrollPane4.removeAll();
        
        JLabel texto1 = new JLabel("Proceso");
        JLabel texto2 = new JLabel("T. llegada");
        JLabel texto3 = new JLabel("Rafaga");
        
        texto1.setBounds(20, 20, 150, 20);
        texto2.setBounds(100, 20, 150, 20);
        texto3.setBounds(180, 20, 150, 20);
        
        scrollPane4.add(texto1);
        scrollPane4.add(texto2);
        scrollPane4.add(texto3);
        
        if(cola.getCabeza() != null){
        
        Nodo temp = cola.getCabeza().getSiguiente();
        
            for(int i = 0; i<cola.getTamaño()-1; i++){

                for(int j = 0; j<4; j++){

                        tablaBloqueados[i][j] = new JTextField("");
                        tablaBloqueados[i][j].setBounds(20 + (j*80), 40 + (i*25), 70, 20);

                        scrollPane4.add(tablaBloqueados[i][j]);

                }

                tablaBloqueados[i][0].setText(temp.getLlave());
                tablaBloqueados[i][1].setText(Integer.toString(temp.getLlegada()));
                tablaBloqueados[i][2].setText(Integer.toString(temp.getRafaga()));
                
                temp = temp.getSiguiente();

            }
        
        }
        
        scrollPane4.repaint();
        scrollPane5.setViewportView(scrollPane4);
        
    }
    
    public void llenarRestante(){
        
        tabla[nodoEjecutado.getIndice()-1][3].setText(Integer.toString(nodoEjecutado.getComienzo()));
        tabla[nodoEjecutado.getIndice()-1][4].setText(Integer.toString(nodoEjecutado.getFinalizacion()));
        tabla[nodoEjecutado.getIndice()-1][5].setText(Integer.toString(nodoEjecutado.getFinalizacion() - nodoEjecutado.getLlegada()));
        tabla[nodoEjecutado.getIndice()-1][6].setText(Integer.toString(nodoEjecutado.getComienzo() - nodoEjecutado.getLlegada()));
 
    }
    
    public void dibujarEsperas(){
        
        JLabel img2 = new JLabel();
        
        ImageIcon imgIcon2 = new ImageIcon(getClass().getResource("barraEspera.png"));

        Image imgEscalada2 = imgIcon2.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon iconoEscalado2 = new ImageIcon(imgEscalada2);
        
        for(int i = coorX-1; i >= nodoEjecutado.getLlegada(); i--){
        
            diagrama[coorY][i+1] = new JLabel();
            diagrama[coorY][i+1].setBounds(40 + (i*20), 20 + (coorY*20), 20, 20);
            diagrama[coorY][i+1].setIcon(iconoEscalado2);
            
            scrollPane2.add(diagrama[coorY][i+1]);
            
        }
        
    }
    
    public void dibujarDiagrama(String nombre, int coorX, int coorY){
        
        scrollPane2.removeAll();
        
        for(int i = 0; i<100; i++){
            
            diagrama[0][i] = new JLabel(Integer.toString(i));
            diagrama[0][i].setBounds(40 + (i*20), 20, 20, 20);

            scrollPane2.add(diagrama[0][i]);
            
        }
        
        diagrama[coorY][0] = new JLabel("  " + nombre);
        diagrama[coorY][0].setBounds(0, 20 + (coorY*20), 50, 20);
        
        scrollPane2.add(diagrama[coorY][0]);
        
        JLabel img = new JLabel();
        
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("barra.png"));

        Image imgEscalada = imgIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon iconoEscalado = new ImageIcon(imgEscalada);
        
        for(int i = 1; i < coorY+1; i++){
            
            for(int j = 0; j < coorX+1; j++){
                
                if(diagrama[i][j] != null){
                
                    scrollPane2.add(diagrama[i][j]);
                    
                }
                
            }
            
        }
        
        diagrama[coorY][coorX+1] = new JLabel();
        diagrama[coorY][coorX+1].setBounds(40 + (coorX*20), 20 + (coorY*20), 20, 20);
        diagrama[coorY][coorX+1].setIcon(iconoEscalado);
        
        scrollPane2.add(diagrama[coorY][coorX+1]);
        
        scrollPane2.repaint();
        scrollPane3.setViewportView(scrollPane2);
            
    }        
    
    public int calcularRafaga(){
        
        return 1 + ((int) (Math.random()*12));
        
    }
    
    public void ordenarRafagas(){
        
        int movimientos = 0;
        int contador = 0;
        
        Nodo temp = cola.getCabeza().getSiguiente();
        
        int menorRaf = cola.getCabeza().getRafaga();
        
        while(!(temp.equals(cola.getCabeza()))){
    
            contador++;
            
            if(temp.getRafaga() < menorRaf){
            
                menorRaf = temp.getRafaga();
                movimientos = contador;
                
            }
            
            temp = temp.getSiguiente();
            
        }
        
        for(int i = 0; i < movimientos; i++){
            
            cola.intercambiar(cola.getCabeza());
            
        }
        
    }
    
    public void ingresar(String nombre, int rafaga, int tiempo, int filas){
        
        cola.insertar(nombre, rafaga, tiempo, filas);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    
        if(e.getSource() == botonIngresar){
            
            filas++;
            
            String nombre = tfNombre.getText();
            rafagaTemporal = calcularRafaga();
            
            ingresar(nombre, rafagaTemporal, tiempoGlobal, filas);
            dibujarTabla(nombre, rafagaTemporal, tiempoGlobal);
            
            tfNombre.setText("P" + (filas + 1));
            
        } else if(e.getSource() == botonIniciar){
        
            procesos = new Thread( this );
            procesos.start();  
            
        } else if(e.getSource() == botonBloquear){
        
            if(nodoEjecutado.getRafaga() != 0){
            
                filas++;
                ingresar(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal, filas);
                dibujarTabla(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal);
                nodoEjecutado.setFinalizacion(tiempoGlobal);
                llenarRestante();
                cola.eliminar(cola.getCabeza());
                ordenarRafagas();
                llenarBloqueados();
                nodoEjecutado = cola.getCabeza();
                coorY++;
                nodoEjecutado.setComienzo(tiempoGlobal);
                dibujarEsperas();
            }
        }
    
    }
    
    @Override
    public void run() {
        
        try{

            while(cola.getTamaño() != 0){
                
                dibujarSemaforo("Rojo.jpg");
                
                ordenarRafagas();
                
                nodoEjecutado = cola.getCabeza();
                nodoEjecutado.setComienzo(tiempoGlobal);
                
                dibujarEsperas();
                
                while(nodoEjecutado.getRafaga() > 0){
                    
                    nodoEjecutado.setRafaga(nodoEjecutado.getRafaga()-1);
                    
                    label3.setText("Proceso en ejecucion: " + nodoEjecutado.getLlave());
                    label4.setText("Tiempo: " + String.valueOf(tiempoGlobal) + " Segundos.");
                    label8.setText("Rafaga restante del proceso: " + nodoEjecutado.getRafaga());
                    
                    dibujarDiagrama(nodoEjecutado.getLlave(), coorX, coorY);
                    llenarBloqueados();
                    
                    tiempoGlobal++;
                    coorX++;
                    
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ProcesosRafaga.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
                nodoEjecutado.setFinalizacion(tiempoGlobal);
                llenarRestante();
                cola.eliminar(cola.getCabeza());
                llenarBloqueados();
                coorY++;
                
            }

            dibujarSemaforo("Verde.jpg");
            label3.setText("Proceso en ejecucion: Ninguno");
            
        } catch(Exception e){
        
            System.out.print("No se que poner aca :D");
            
        } 

    }
    
}
