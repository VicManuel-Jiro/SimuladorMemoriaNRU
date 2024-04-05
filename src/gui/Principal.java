/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import datos.Datos;
import java.awt.Component;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Jiro
 */
public class Principal extends javax.swing.JFrame implements ActionListener {
    /**
     * Creates new form Principal
     */
    private Datos d;
    private Abrir i;
    private int[][] memory,original;
    private String[][] operaciones;
    private HashMap componentMap;
    private int indicePaso=0,direccion,inicio,anterior,ms,indice;
    private Timer timer,timer2;
    private int[] memoryF;
    private int[] orden;
    public final ActionListener global = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            ms++;
            for(int y=0;y<memory.length;y++){
                if(memory[y][1]>=0){
                    memory[y][4]++;
                }
            }
            lblTiempo.setText(ms+"ms");
            
            //System.out.println(ms);
        }
        
    };
    public final ActionListener ejecutar = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            if(operaciones.length>indicePaso){
                inicio=ms;
                //System.out.println(inicio);
                paso();
            }else{
                lblStatus.setText("STOP");
                //btnPaso.setEnabled(true);
                btnReset.setEnabled(true);
                //btnEjecutar.setEnabled(true);
                btnSalir.setEnabled(true);
                timer.stop();
                timer2.stop();
                try {
                    //Files.write(Paths.get(d.getRuta().replaceAll("\\\\[a-zA-Z]+.conf$", "")+"\\tracefile.txt"), "".getBytes(), StandardOpenOption.CREATE);
                    Files.write(Paths.get(d.getRuta().replaceAll("\\\\[a-zA-Z]+.conf$", "")+"\\tracefile.txt"), txtSalida.getText().getBytes(),StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    txtSalida.append("Se guardo en: "+d.getRuta().replaceAll("\\\\[a-zA-Z]+.conf$", "")+"\\tracefile.txt");
                }catch (IOException ex) {
                    //exception handling left as an exercise for the reader
                    ex.printStackTrace();
                    System.out.println("error");
                }
            }
            //*/
        }
        
    };
    public Principal() {
        initComponents();
        createComponentMap();
        this.setLocationRelativeTo(null);
        for (int k=0;k<64;k++){
            JLabel label= (JLabel) getComponentByName(k+"");
            JButton boton= (JButton) getComponentByName("btn"+k);
            label.setText("");
            boton.setActionCommand("pag"+k);
            boton.addActionListener(this);
            boton.setEnabled(false);
        }
        this.lblBM.setText(null);
        this.lblBR.setText(null);
        this.lblDireccion.setText(null);
        this.lblFalla.setText(null);
        this.lblInstruccion.setText(null);
        this.lblPfisica.setText(null);
        this.lblPvirtual.setText(null);
        this.lblStatus.setText(null);
        this.lblTiempo.setText(null);
        this.lblTiempoMemoria.setText(null);
        this.lblUltMod.setText(null);
        this.lblfin.setText(null);
        this.lblinicio.setText(null);
        this.txtSalida.setText(null);
        timer= new Timer(10,global);
        timer2= new Timer(1000,ejecutar);
        
    }
    public void setDatos(Datos a){
        this.d=a;
        memory=null;
        memory=new int[d.getMemory().length][d.getMemory()[0].length];
        for(int i=0;i<memory.length;i++){
            for(int j=0;j<memory[0].length;j++){
                memory[i][j]=d.getMemory()[i][j];
            }
        }
        operaciones=d.getOperaciones().clone();
        ms=d.getTi();
        orden=null;
        orden=new int[d.getOrder().length];
        for(int i=0;i<orden.length;i++){
            orden[i]=d.getOrder()[i];
        }
        memoryF=null;
        memoryF=new int[d.getMemoryF().length];
        for(int i=0;i<memoryF.length;i++){
            memoryF[i]=d.getMemoryF()[i];
        }
        /*System.out.println(operaciones.length);
        for(int n=0;n<operaciones.length;n++){
            System.out.println(operaciones[n][0]);
        }*/
        this.txtSalida.setText(null);
        for(int o=0;o<memory.length;o++){
            JLabel l= (JLabel) getComponentByName(o+"");
            JButton b= (JButton) getComponentByName("btn"+o);
            if (l!=null && b!=null){
                if(memory[o][1]<0 ){
                    l.setForeground(new java.awt.Color(255, 0, 0));
                    l.setText("x");
                    //b.setEnabled(true);
                    b.setEnabled(true);
                }else{
                    l.setForeground(new java.awt.Color(0, 0, 0));
                    l.setText(""+memory[o][1]);
                    b.setEnabled(true);
                }
            }
        }
    }
    private void createComponentMap() {
        componentMap = new HashMap<String,Component>();
        Component[] components = this.jPanel1.getComponents();//this.getContentPane().getComponents();
        for (int i=0; i < components.length; i++) {
                componentMap.put(components[i].getName(), components[i]);
        }
}
private Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
                return (Component) componentMap.get(name);
        }
        else return null;
}
public void reemplazo(){
    //checa si hay algun espacio en memoria fisica
    int e=0,candidato;
    //int[] candidatos=new int[this.memory.length];
    List cand=new List();
    //Arrays.fill(candidatos, 0);
    for(int i=0;i<this.memoryF.length;i++){
        if (this.memoryF[i]==0){
            this.memory[this.indice][1]=i;
            this.memory[this.indice][2]=0;
            this.memory[this.indice][3]=0;
            this.memory[this.indice][4]=0;
            this.memory[this.indice][5]=0;
            JLabel label= (JLabel) getComponentByName(i+"");
            JButton boton= (JButton) getComponentByName("btn"+i);
            //boton.setEnabled(true);
            label.setForeground(new java.awt.Color(0, 0, 0));
            label.setText(""+memory[i][1]);
            //System.out.println("error aqui 1");
            e=0;
            this.memoryF[i]=1;
            break;
        }else{e=1;}
    }
    //NRU
    if(e==1){
        cand.removeAll();
        candidato=-1;
        for(int p=0;p<this.memory.length;p++){
            //r=0 m=0
            if (this.memory[p][1]>=0&&this.memory[p][2]==0 && this.memory[p][3]==0){
                cand.add(p+"");
            }
        }
        if (cand.getItemCount()==0){
            cand.removeAll();
            for(int s=0;s<this.memory.length;s++){
                //r=0 m=1
                if (this.memory[s][1]>=0&&this.memory[s][2]==0 && this.memory[s][3]==1){
                    cand.add(s+"");
                }
            }
            if(cand.getItemCount()==0){
                cand.removeAll();
                for(int t=0;t<this.memory.length;t++){
                    //r=1 m=0
                    if (this.memory[t][1]>=0&&this.memory[t][2]==1 && this.memory[t][3]==0){
                        cand.add(t+"");
                    }
                }
                if(cand.getItemCount()==0){
                    //cualquiera r=1 m=1
                    while(true){
                        candidato=(int)(Math.random() * this.d.getPvirtual());
                        if (this.memory[candidato][1]>=0){e=0;break;}
                    }
                    //System.out.println("r1 m1");
                }else{e=0;candidato=Integer.parseInt(cand.getItem((int)(Math.random() * cand.getItemCount())));}
            }else{e=0;candidato=Integer.parseInt(cand.getItem((int)(Math.random() * cand.getItemCount())));}
        }else{e=0;candidato=Integer.parseInt(cand.getItem((int)(Math.random() * cand.getItemCount())));}
        //System.out.println(candidato);
        //System.out.println(e);
        if (e==0){
            //hay candidatos
            this.memory[this.indice][1]=this.memory[candidato][1];
            this.memory[this.indice][2]=0;
            this.memory[this.indice][3]=0;
            this.memory[this.indice][4]=0;
            this.memory[this.indice][5]=0;
            
            this.memory[candidato][1]=-1;
            for(int f=2;f<6;f++){
                this.memory[candidato][f]=0;
            }
            JLabel l= (JLabel) getComponentByName(candidato+"");
            JButton b= (JButton) getComponentByName("btn"+candidato);
            l.setForeground(new java.awt.Color(255, 0, 0));
            l.setText("x");
            //b.setEnabled(true);
            //b.setEnabled(false);
            l= (JLabel) getComponentByName(this.indice+"");
            b= (JButton) getComponentByName("btn"+this.indice);
            l.setForeground(new java.awt.Color(0, 0, 0));
            l.setText(""+memory[this.indice][1]);
            //System.out.println("error aqui 2");
            //b.setEnabled(true);
            cand.removeAll();
            candidato=-1;
        }
        
    }
}
public void rellena(int opcion){
        switch (opcion) {
            case 1:
                this.lblPvirtual.setText(this.memory[indice][0]+"");
                this.lblPfisica.setText(this.memory[indice][1]+"");
                memory[indice][2]=1;
                this.lblBR.setText(memory[indice][2]+"");
                this.lblBM.setText(memory[indice][3]+"");
                this.lblTiempoMemoria.setText(memory[indice][4]+"ms");
                //this.memory[indice][5]=inicio;
                this.lblUltMod.setText(memory[indice][5]+"ms");
                this.lblinicio.setText(indice*this.d.getTamanoP()+"");
                this.lblfin.setText((indice*(this.d.getTamanoP()))+this.d.getTamanoP()-1+"");
                this.txtSalida.append(this.operaciones[this.indicePaso][0]+" "+this.operaciones[this.indicePaso][2]+" ...ok, Virtual: "+this.memory[indice][0]+" Fisica: "+this.memory[indice][1]+"\n");
                break;
            case 2:
                this.lblPvirtual.setText(this.memory[indice][0]+"");
                this.lblPfisica.setText(this.memory[indice][1]+"");
                this.lblBR.setText(memory[indice][2]+"");
                memory[indice][3]=1;
                this.lblBM.setText(memory[indice][3]+"");
                this.lblTiempoMemoria.setText(memory[indice][4]+"ms");
                memory[indice][5]=inicio;
                this.lblUltMod.setText(memory[indice][5]+"ms");
                this.lblinicio.setText(indice*this.d.getTamanoP()+"");
                this.lblfin.setText((indice*(this.d.getTamanoP()))+this.d.getTamanoP()-1+"");
                this.txtSalida.append(this.operaciones[this.indicePaso][0]+" "+this.operaciones[this.indicePaso][2]+" ...ok, Virtual: "+this.memory[indice][0]+" Fisica: "+this.memory[indice][1]+"\n");
                break;
            case 3:
                this.lblPvirtual.setText(this.memory[indice][0]+"");
                this.lblPfisica.setText(this.memory[indice][1]+"");
                memory[indice][2]=1;
                this.lblBR.setText(memory[indice][2]+"");
                this.lblBM.setText(memory[indice][3]+"");
                this.lblTiempoMemoria.setText(memory[indice][4]+"ms");
                //this.memory[indice][5]=inicio;
                this.lblUltMod.setText(memory[indice][5]+"ms");
                this.lblinicio.setText(indice*this.d.getTamanoP()+"");
                this.lblfin.setText((indice*(this.d.getTamanoP()))+this.d.getTamanoP()-1+"");
                this.txtSalida.append(this.operaciones[this.indicePaso][0]+" "+this.operaciones[this.indicePaso][2]+" ...Fallo de pagina,"+" Virtual: "+this.memory[indice][0]+" Fisica asignada: "+this.memory[indice][1]+"\n");
                break;
            case 4:
                this.lblPvirtual.setText(this.memory[indice][0]+"");
                this.lblPfisica.setText(this.memory[indice][1]+"");
                this.lblBR.setText(memory[indice][2]+"");
                memory[indice][3]=1;
                this.lblBM.setText(memory[indice][3]+"");
                this.lblTiempoMemoria.setText(memory[indice][4]+"ms");
                memory[indice][5]=inicio;
                this.lblUltMod.setText(memory[indice][5]+"ms");
                this.lblinicio.setText(indice*this.d.getTamanoP()+"");
                this.lblfin.setText((indice*(this.d.getTamanoP()))+this.d.getTamanoP()-1+"");
                this.txtSalida.append(this.operaciones[this.indicePaso][0]+" "+this.operaciones[this.indicePaso][2]+" ...Fallo de pagina,"+" Virtual: "+this.memory[indice][0]+" Fisica asignada: "+this.memory[indice][1]+"\n");
                break;
            default:
                break;
        }
}
    public void setInicio(Abrir a){
        this.i=a;
    }
    public void Inicializa(){
        indicePaso=0;
        ms=d.getTi();
    }
    private void paso(){
        if(this.operaciones.length>this.indicePaso){
            while(this.operaciones.length>this.indicePaso){
                if(!this.operaciones[this.indicePaso][0].equals("error") ){
                    if(this.operaciones[this.indicePaso][0].toUpperCase().equals("READ")){
                        direccion=d.aBaseDiez(this.operaciones[this.indicePaso][2], this.operaciones[this.indicePaso][1]);
                        indice=(int)Math.floor(direccion/this.d.getTamanoP());
                        if(direccion<(d.getTamanoP()*d.getPvirtual())){
                            this.lblInstruccion.setText(this.operaciones[this.indicePaso][0].toUpperCase());
                            this.lblDireccion.setText(direccion+"");
                            if(this.memory[indice][1]>=0){
                                this.lblFalla.setForeground(new java.awt.Color(0, 0, 0));
                                this.lblFalla.setText("NO");
                                rellena(1);
                            }else{
                                lblFalla.setForeground(new java.awt.Color(255, 0, 0));
                                this.lblFalla.setText("SI");
                                limpia(3);
                                reemplazo();
                                rellena(3);
                            }
                        }
                    }else if(this.operaciones[this.indicePaso][0].toUpperCase().equals("WRITE")){
                        direccion=d.aBaseDiez(this.operaciones[this.indicePaso][2], this.operaciones[this.indicePaso][1]);
                        indice=(int)Math.floor(direccion/16384);
                        if(direccion<(d.getTamanoP()*d.getPvirtual())){
                            this.lblInstruccion.setText(this.operaciones[this.indicePaso][0].toUpperCase());
                            this.lblDireccion.setText(direccion+"");
                            if(this.memory[indice][1]>=0){
                                this.lblFalla.setForeground(new java.awt.Color(0, 0, 0));
                                this.lblFalla.setText("NO");
                                rellena(2);
                            }else{
                                this.lblFalla.setForeground(new java.awt.Color(255, 0, 0));
                                this.lblFalla.setText("SI");
                                limpia(3);
                                reemplazo();
                                rellena(4);
                            }
                        }
                    }
                    break;
                }
                this.indicePaso++;
            }
            this.indicePaso++;
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.WARNING: Do NOT modify this code.
     * The content of this method is always
 regenerated by the Form Editor.
     * @param a
     */
    public void limpia(int a){
        switch (a) {
            case 1:
                this.lblInstruccion.setText(null);
                this.lblDireccion.setText(null);
                this.lblFalla.setText(null);
                this.lblBM.setText(null);
                this.lblBR.setText(null);
                this.lblPfisica.setText(null);
                this.lblPvirtual.setText(null);
                this.lblStatus.setText(null);
                this.lblTiempoMemoria.setText(null);
                this.lblUltMod.setText(null);
                this.lblfin.setText(null);
                this.lblinicio.setText(null);
                break;
            case 2:
                this.lblInstruccion.setText(null);
                this.lblDireccion.setText(null);
                this.lblFalla.setText(null);
                break;
            case 3:
                this.lblBM.setText(null);
                this.lblBR.setText(null);
                this.lblPfisica.setText(null);
                this.lblPvirtual.setText(null);
                this.lblStatus.setText(null);
                this.lblTiempoMemoria.setText(null);
                this.lblUltMod.setText(null);
                this.lblfin.setText(null);
                this.lblinicio.setText(null);
                break;
            case 4:
                this.lblTiempo.setText(null);
                this.lblInstruccion.setText(null);
                this.lblDireccion.setText(null);
                this.lblFalla.setText(null);
                this.lblBM.setText(null);
                this.lblBR.setText(null);
                this.lblPfisica.setText(null);
                this.lblPvirtual.setText(null);
                this.lblStatus.setText(null);
                this.lblTiempoMemoria.setText(null);
                this.lblUltMod.setText(null);
                this.lblfin.setText(null);
                this.lblinicio.setText(null);
                this.lblStatus.setText(null);
                this.btnEjecutar.setEnabled(true);
                this.btnPaso.setEnabled(true);
                this.txtSalida.setText(null);
                //this.lblTiempo.setText("00:00:00:00");
                this.memory=null;
                memory=new int[d.getMemory().length][d.getMemory()[0].length];
                for(int i=0;i<memory.length;i++){
                    for(int j=0;j<memory[0].length;j++){
                        memory[i][j]=d.getMemory()[i][j];
                    }
                }
                break;
            default:
                break;
        }
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnEjecutar = new javax.swing.JButton();
        btnPaso = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btn34 = new javax.swing.JButton();
        btn56 = new javax.swing.JButton();
        btn37 = new javax.swing.JButton();
        lbl54 = new javax.swing.JLabel();
        lbl41 = new javax.swing.JLabel();
        btn48 = new javax.swing.JButton();
        lbl44 = new javax.swing.JLabel();
        lbl48 = new javax.swing.JLabel();
        lbl43 = new javax.swing.JLabel();
        lbl34 = new javax.swing.JLabel();
        btn45 = new javax.swing.JButton();
        lbl50 = new javax.swing.JLabel();
        btn42 = new javax.swing.JButton();
        btn35 = new javax.swing.JButton();
        lbl42 = new javax.swing.JLabel();
        lbl56 = new javax.swing.JLabel();
        lbl37 = new javax.swing.JLabel();
        lbl39 = new javax.swing.JLabel();
        btn38 = new javax.swing.JButton();
        lbl38 = new javax.swing.JLabel();
        btn1 = new javax.swing.JButton();
        btn32 = new javax.swing.JButton();
        lbl1 = new javax.swing.JLabel();
        lbl32 = new javax.swing.JLabel();
        lbl3 = new javax.swing.JLabel();
        btn2 = new javax.swing.JButton();
        lbl2 = new javax.swing.JLabel();
        btn3 = new javax.swing.JButton();
        lbl5 = new javax.swing.JLabel();
        lbl7 = new javax.swing.JLabel();
        btn6 = new javax.swing.JButton();
        lbl6 = new javax.swing.JLabel();
        btn55 = new javax.swing.JButton();
        btn52 = new javax.swing.JButton();
        btn43 = new javax.swing.JButton();
        lbl45 = new javax.swing.JLabel();
        lbl47 = new javax.swing.JLabel();
        btn46 = new javax.swing.JButton();
        lbl46 = new javax.swing.JLabel();
        btn40 = new javax.swing.JButton();
        lbl40 = new javax.swing.JLabel();
        btn0 = new javax.swing.JButton();
        btn47 = new javax.swing.JButton();
        lbl0 = new javax.swing.JLabel();
        btn7 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        lbl4 = new javax.swing.JLabel();
        btn5 = new javax.swing.JButton();
        lbl9 = new javax.swing.JLabel();
        lbl12 = new javax.swing.JLabel();
        lbl11 = new javax.swing.JLabel();
        btn13 = new javax.swing.JButton();
        btn10 = new javax.swing.JButton();
        lbl10 = new javax.swing.JLabel();
        btn41 = new javax.swing.JButton();
        btn44 = new javax.swing.JButton();
        lbl52 = new javax.swing.JLabel();
        btn53 = new javax.swing.JButton();
        lbl57 = new javax.swing.JLabel();
        lbl60 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        btn11 = new javax.swing.JButton();
        lbl13 = new javax.swing.JLabel();
        lbl15 = new javax.swing.JLabel();
        btn14 = new javax.swing.JButton();
        lbl14 = new javax.swing.JLabel();
        btn8 = new javax.swing.JButton();
        lbl8 = new javax.swing.JLabel();
        btn15 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btn12 = new javax.swing.JButton();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        lbl20 = new javax.swing.JLabel();
        btn21 = new javax.swing.JButton();
        lbl25 = new javax.swing.JLabel();
        lbl28 = new javax.swing.JLabel();
        lbl27 = new javax.swing.JLabel();
        btn29 = new javax.swing.JButton();
        btn26 = new javax.swing.JButton();
        lbl26 = new javax.swing.JLabel();
        btn27 = new javax.swing.JButton();
        lbl29 = new javax.swing.JLabel();
        btn17 = new javax.swing.JButton();
        lbl31 = new javax.swing.JLabel();
        lbl17 = new javax.swing.JLabel();
        btn30 = new javax.swing.JButton();
        lbl19 = new javax.swing.JLabel();
        lbl30 = new javax.swing.JLabel();
        btn18 = new javax.swing.JButton();
        btn24 = new javax.swing.JButton();
        lbl18 = new javax.swing.JLabel();
        lbl24 = new javax.swing.JLabel();
        btn19 = new javax.swing.JButton();
        btn31 = new javax.swing.JButton();
        lbl21 = new javax.swing.JLabel();
        btn25 = new javax.swing.JButton();
        lbl23 = new javax.swing.JLabel();
        btn28 = new javax.swing.JButton();
        btn22 = new javax.swing.JButton();
        lbl22 = new javax.swing.JLabel();
        btn16 = new javax.swing.JButton();
        lbl16 = new javax.swing.JLabel();
        btn23 = new javax.swing.JButton();
        btn20 = new javax.swing.JButton();
        lbl59 = new javax.swing.JLabel();
        btn61 = new javax.swing.JButton();
        btn58 = new javax.swing.JButton();
        lbl58 = new javax.swing.JLabel();
        btn59 = new javax.swing.JButton();
        lbl61 = new javax.swing.JLabel();
        btn49 = new javax.swing.JButton();
        lbl63 = new javax.swing.JLabel();
        btn51 = new javax.swing.JButton();
        btn63 = new javax.swing.JButton();
        lbl53 = new javax.swing.JLabel();
        btn57 = new javax.swing.JButton();
        btn39 = new javax.swing.JButton();
        lbl55 = new javax.swing.JLabel();
        btn36 = new javax.swing.JButton();
        lbl49 = new javax.swing.JLabel();
        btn60 = new javax.swing.JButton();
        btn62 = new javax.swing.JButton();
        lbl36 = new javax.swing.JLabel();
        btn33 = new javax.swing.JButton();
        btn54 = new javax.swing.JButton();
        lbl51 = new javax.swing.JLabel();
        lbl33 = new javax.swing.JLabel();
        lbl62 = new javax.swing.JLabel();
        lbl35 = new javax.swing.JLabel();
        btn50 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        lblInstruccion = new javax.swing.JLabel();
        lblDireccion = new javax.swing.JLabel();
        lblFalla = new javax.swing.JLabel();
        lblPvirtual = new javax.swing.JLabel();
        lblPfisica = new javax.swing.JLabel();
        lblBR = new javax.swing.JLabel();
        lblBM = new javax.swing.JLabel();
        lblTiempoMemoria = new javax.swing.JLabel();
        lblUltMod = new javax.swing.JLabel();
        lblinicio = new javax.swing.JLabel();
        lblfin = new javax.swing.JLabel();
        lblTiempo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel97 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSalida = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulador de memoria -NRU-");
        setResizable(false);

        btnEjecutar.setText("Ejecutar");
        btnEjecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEjecutarActionPerformed(evt);
            }
        });

        btnPaso.setText("Paso a paso");
        btnPaso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasoActionPerformed(evt);
            }
        });

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn34.setText("Pagina 34");
        btn34.setName("btn34"); // NOI18N

        btn56.setText("Pagina 56");
        btn56.setName("btn56"); // NOI18N

        btn37.setText("Pagina 37");
        btn37.setName("btn37"); // NOI18N

        lbl54.setText("0");
        lbl54.setName("54"); // NOI18N

        lbl41.setText("0");
        lbl41.setName("41"); // NOI18N

        btn48.setText("Pagina 48");
        btn48.setName("btn48"); // NOI18N

        lbl44.setText("0");
        lbl44.setName("44"); // NOI18N

        lbl48.setText("99");
        lbl48.setName("48"); // NOI18N

        lbl43.setText("0");
        lbl43.setName("43"); // NOI18N

        lbl34.setText("0");
        lbl34.setName("34"); // NOI18N

        btn45.setText("Pagina 45");
        btn45.setName("btn45"); // NOI18N

        lbl50.setText("0");
        lbl50.setName("50"); // NOI18N

        btn42.setText("Pagina 42");
        btn42.setName("btn42"); // NOI18N

        btn35.setText("Pagina 35");
        btn35.setName("btn35"); // NOI18N

        lbl42.setText("0");
        lbl42.setName("42"); // NOI18N

        lbl56.setText("0");
        lbl56.setName("56"); // NOI18N

        lbl37.setText("0");
        lbl37.setName("37"); // NOI18N

        lbl39.setText("0");
        lbl39.setName("39"); // NOI18N

        btn38.setText("Pagina 38");
        btn38.setName("btn38"); // NOI18N

        lbl38.setText("0");
        lbl38.setName("38"); // NOI18N

        btn1.setText("Pagina 1");
        btn1.setName("btn1"); // NOI18N

        btn32.setText("Pagina 32");
        btn32.setName("btn32"); // NOI18N

        lbl1.setText("0");
        lbl1.setName("1"); // NOI18N

        lbl32.setText("99");
        lbl32.setName("32"); // NOI18N

        lbl3.setText("0");
        lbl3.setName("3"); // NOI18N

        btn2.setText("Pagina 2");
        btn2.setName("btn2"); // NOI18N

        lbl2.setText("0");
        lbl2.setName("2"); // NOI18N

        btn3.setText("Pagina 3");
        btn3.setName("btn3"); // NOI18N

        lbl5.setText("0");
        lbl5.setName("5"); // NOI18N

        lbl7.setText("0");
        lbl7.setName("7"); // NOI18N

        btn6.setText("Pagina 6");
        btn6.setName("btn6"); // NOI18N

        lbl6.setText("0");
        lbl6.setName("6"); // NOI18N

        btn55.setText("Pagina 55");
        btn55.setName("btn55"); // NOI18N

        btn52.setText("Pagina 52");
        btn52.setName("btn52"); // NOI18N

        btn43.setText("Pagina 43");
        btn43.setName("btn43"); // NOI18N

        lbl45.setText("0");
        lbl45.setName("45"); // NOI18N

        lbl47.setText("0");
        lbl47.setName("47"); // NOI18N

        btn46.setText("Pagina 46");
        btn46.setName("btn46"); // NOI18N

        lbl46.setText("0");
        lbl46.setName("46"); // NOI18N

        btn40.setText("Pagina 40");
        btn40.setName("btn40"); // NOI18N

        lbl40.setText("0");
        lbl40.setName("40"); // NOI18N

        btn0.setText("Pagina 0");
        btn0.setName("btn0"); // NOI18N

        btn47.setText("Pagina 47");
        btn47.setName("btn47"); // NOI18N

        lbl0.setText("99");
        lbl0.setName("0"); // NOI18N

        btn7.setText("Pagina 7");
        btn7.setName("btn7"); // NOI18N

        btn4.setText("Pagina 4");
        btn4.setName("btn4"); // NOI18N

        lbl4.setText("0");
        lbl4.setName("4"); // NOI18N

        btn5.setText("Pagina 5");
        btn5.setName("btn5"); // NOI18N

        lbl9.setText("0");
        lbl9.setName("9"); // NOI18N

        lbl12.setText("0");
        lbl12.setName("12"); // NOI18N

        lbl11.setText("0");
        lbl11.setName("11"); // NOI18N

        btn13.setText("Pagina 13");
        btn13.setName("btn13"); // NOI18N

        btn10.setText("Pagina 10");
        btn10.setName("btn10"); // NOI18N

        lbl10.setText("0");
        lbl10.setName("10"); // NOI18N

        btn41.setText("Pagina 41");
        btn41.setName("btn41"); // NOI18N

        btn44.setText("Pagina 44");
        btn44.setName("btn44"); // NOI18N

        lbl52.setText("0");
        lbl52.setName("52"); // NOI18N

        btn53.setText("Pagina 53");
        btn53.setName("btn53"); // NOI18N

        lbl57.setText("0");
        lbl57.setName("57"); // NOI18N

        lbl60.setText("0");
        lbl60.setName("60"); // NOI18N

        jLabel65.setText("Virtual");

        jLabel66.setText("Fisica");

        jLabel67.setText("Virtual");

        jLabel68.setText("Fisica");

        btn11.setText("Pagina 11");
        btn11.setName("btn11"); // NOI18N

        lbl13.setText("0");
        lbl13.setName("13"); // NOI18N

        lbl15.setText("0");
        lbl15.setName("15"); // NOI18N

        btn14.setText("Pagina 14");
        btn14.setName("btn14"); // NOI18N

        lbl14.setText("0");
        lbl14.setName("14"); // NOI18N

        btn8.setText("Pagina 8");
        btn8.setName("btn8"); // NOI18N

        lbl8.setText("0");
        lbl8.setName("8"); // NOI18N

        btn15.setText("Pagina 15");
        btn15.setName("btn15"); // NOI18N

        btn9.setText("Pagina 9");
        btn9.setName("btn9"); // NOI18N

        btn12.setText("Pagina 12");
        btn12.setName("btn12"); // NOI18N

        jLabel69.setText("Virtual");

        jLabel70.setText("Fisica");

        jLabel71.setText("Virtual");

        jLabel72.setText("Fisica");

        lbl20.setText("0");
        lbl20.setName("20"); // NOI18N

        btn21.setText("Pagina 21");
        btn21.setName("btn21"); // NOI18N

        lbl25.setText("0");
        lbl25.setName("25"); // NOI18N

        lbl28.setText("0");
        lbl28.setName("28"); // NOI18N

        lbl27.setText("0");
        lbl27.setName("27"); // NOI18N

        btn29.setText("Pagina 29");
        btn29.setName("btn29"); // NOI18N

        btn26.setText("Pagina 26");
        btn26.setName("btn26"); // NOI18N

        lbl26.setText("0");
        lbl26.setName("26"); // NOI18N

        btn27.setText("Pagina 27");
        btn27.setName("btn27"); // NOI18N

        lbl29.setText("0");
        lbl29.setName("29"); // NOI18N

        btn17.setText("Pagina 17");
        btn17.setName("btn17"); // NOI18N

        lbl31.setText("0");
        lbl31.setName("31"); // NOI18N

        lbl17.setText("0");
        lbl17.setName("17"); // NOI18N

        btn30.setText("Pagina 30");
        btn30.setName("btn30"); // NOI18N

        lbl19.setText("0");
        lbl19.setName("19"); // NOI18N

        lbl30.setText("0");
        lbl30.setName("30"); // NOI18N

        btn18.setText("Pagina 18");
        btn18.setName("btn18"); // NOI18N

        btn24.setText("Pagina 24");
        btn24.setName("btn24"); // NOI18N

        lbl18.setText("0");
        lbl18.setName("18"); // NOI18N

        lbl24.setText("0");
        lbl24.setName("24"); // NOI18N

        btn19.setText("Pagina 19");
        btn19.setName("btn19"); // NOI18N

        btn31.setText("Pagina 31");
        btn31.setName("btn31"); // NOI18N

        lbl21.setText("0");
        lbl21.setName("21"); // NOI18N

        btn25.setText("Pagina 25");
        btn25.setName("btn25"); // NOI18N

        lbl23.setText("0");
        lbl23.setName("23"); // NOI18N

        btn28.setText("Pagina 28");
        btn28.setName("btn28"); // NOI18N

        btn22.setText("Pagina 22");
        btn22.setName("btn22"); // NOI18N

        lbl22.setText("0");
        lbl22.setName("22"); // NOI18N

        btn16.setText("Pagina 16");
        btn16.setName("btn16"); // NOI18N

        lbl16.setText("99");
        lbl16.setName("16"); // NOI18N

        btn23.setText("Pagina 23");
        btn23.setName("btn23"); // NOI18N

        btn20.setText("Pagina 20");
        btn20.setName("btn20"); // NOI18N

        lbl59.setText("0");
        lbl59.setName("59"); // NOI18N

        btn61.setText("Pagina 61");
        btn61.setName("btn61"); // NOI18N

        btn58.setText("Pagina 58");
        btn58.setName("btn58"); // NOI18N

        lbl58.setText("0");
        lbl58.setName("58"); // NOI18N

        btn59.setText("Pagina 59");
        btn59.setName("btn59"); // NOI18N

        lbl61.setText("0");
        lbl61.setName("61"); // NOI18N

        btn49.setText("Pagina 49");
        btn49.setName("btn49"); // NOI18N

        lbl63.setText("0");
        lbl63.setName("63"); // NOI18N

        btn51.setText("Pagina 51");
        btn51.setName("btn51"); // NOI18N

        btn63.setText("Pagina 63");
        btn63.setName("btn63"); // NOI18N

        lbl53.setText("0");
        lbl53.setName("53"); // NOI18N

        btn57.setText("Pagina 57");
        btn57.setName("btn57"); // NOI18N

        btn39.setText("Pagina 39");
        btn39.setName("btn39"); // NOI18N

        lbl55.setText("0");
        lbl55.setName("55"); // NOI18N

        btn36.setText("Pagina 36");
        btn36.setName("btn36"); // NOI18N

        lbl49.setText("0");
        lbl49.setName("49"); // NOI18N

        btn60.setText("Pagina 60");
        btn60.setName("btn60"); // NOI18N

        btn62.setText("Pagina 62");
        btn62.setName("btn62"); // NOI18N

        lbl36.setText("0");
        lbl36.setName("36"); // NOI18N

        btn33.setText("Pagina 33");
        btn33.setName("btn33"); // NOI18N

        btn54.setText("Pagina 54");
        btn54.setName("btn54"); // NOI18N

        lbl51.setText("0");
        lbl51.setName("51"); // NOI18N

        lbl33.setText("0");
        lbl33.setName("33"); // NOI18N

        lbl62.setText("0");
        lbl62.setName("62"); // NOI18N

        lbl35.setText("0");
        lbl35.setName("35"); // NOI18N

        btn50.setText("Pagina 50");
        btn50.setName("btn50"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl15))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl14))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl13))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl12))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl11))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl10))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btn9, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                    .addComponent(btn7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl8)
                                            .addComponent(lbl7)
                                            .addComponent(lbl6)
                                            .addComponent(lbl5)
                                            .addComponent(lbl4)
                                            .addComponent(lbl3)
                                            .addComponent(lbl2)
                                            .addComponent(lbl1)
                                            .addComponent(lbl0)
                                            .addComponent(jLabel66)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl9)))))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn31)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl31))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn30)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl30))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn29)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl29))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn28)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl28))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl27))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl26))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn25)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl25))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn24)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl24))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl23))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn22)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl22))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl21))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl20))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl19))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl18))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl17))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl16)))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn47)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl47))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn46)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl46))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn45)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl45))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn44)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl44))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn43)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl43))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn42)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl42))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn41)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl41))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn40)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl40))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn39)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl39))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn38)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl38))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn37)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl37))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn36)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl36))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn35)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl35))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn34)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl34))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn33)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl33))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn32)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl32)))
                                        .addGap(43, 43, 43)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn63)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl63))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn62)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl62))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn61)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl61))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn60)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl60))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn59)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl59))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn58)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl58))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn57)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl57))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn56)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl56))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn55)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl55))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn54)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl54))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn53)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl53))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn52)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl52))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn51)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl51))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn50)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl50))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn49)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl49))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn48)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel72)
                                                    .addComponent(lbl48)))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(101, 101, 101)
                                        .addComponent(jLabel70)
                                        .addGap(40, 40, 40)
                                        .addComponent(jLabel71))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(101, 101, 101)
                                .addComponent(jLabel68)
                                .addGap(38, 38, 38)
                                .addComponent(jLabel69)))
                        .addContainerGap(28, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel67)
                        .addGap(398, 398, 398))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel65)
                        .addComponent(jLabel66))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel67, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel68))
                    .addComponent(jLabel69, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel71)
                        .addComponent(jLabel72)
                        .addComponent(jLabel70)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn48)
                                .addComponent(lbl48))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn49)
                                .addComponent(lbl49))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn50)
                                .addComponent(lbl50))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn51)
                                .addComponent(lbl51))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn52)
                                .addComponent(lbl52))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn53)
                                .addComponent(lbl53))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn54)
                                .addComponent(lbl54))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn55)
                                .addComponent(lbl55))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn56)
                                .addComponent(lbl56))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn57)
                                .addComponent(lbl57))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn58)
                                .addComponent(lbl58))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn59)
                                .addComponent(lbl59))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn60)
                                .addComponent(lbl60))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn61)
                                .addComponent(lbl61))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn62)
                                .addComponent(lbl62))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn63)
                                .addComponent(lbl63)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn32)
                                .addComponent(lbl32))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn33)
                                .addComponent(lbl33))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn34)
                                .addComponent(lbl34))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn35)
                                .addComponent(lbl35))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn36)
                                .addComponent(lbl36))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn37)
                                .addComponent(lbl37))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn38)
                                .addComponent(lbl38))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn39)
                                .addComponent(lbl39))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn40)
                                .addComponent(lbl40))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn41)
                                .addComponent(lbl41))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn42)
                                .addComponent(lbl42))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn43)
                                .addComponent(lbl43))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn44)
                                .addComponent(lbl44))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn45)
                                .addComponent(lbl45))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn46)
                                .addComponent(lbl46))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn47)
                                .addComponent(lbl47))))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn16)
                                .addComponent(lbl16))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn17)
                                .addComponent(lbl17))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn18)
                                .addComponent(lbl18))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn19)
                                .addComponent(lbl19))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn20)
                                .addComponent(lbl20))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn21)
                                .addComponent(lbl21))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn22)
                                .addComponent(lbl22))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn23)
                                .addComponent(lbl23))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn24)
                                .addComponent(lbl24))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn25)
                                .addComponent(lbl25))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn26)
                                .addComponent(lbl26))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn27)
                                .addComponent(lbl27))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn28)
                                .addComponent(lbl28))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn29)
                                .addComponent(lbl29))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn30)
                                .addComponent(lbl30))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn31)
                                .addComponent(lbl31)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn0)
                                .addComponent(lbl0))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn1)
                                .addComponent(lbl1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn2)
                                .addComponent(lbl2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn3)
                                .addComponent(lbl3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn4)
                                .addComponent(lbl4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn5)
                                .addComponent(lbl5))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn6)
                                .addComponent(lbl6))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn7)
                                .addComponent(lbl7))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn8)
                                .addComponent(lbl8))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn9)
                                .addComponent(lbl9))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn10)
                                .addComponent(lbl10))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn11)
                                .addComponent(lbl11))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn12)
                                .addComponent(lbl12))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn13)
                                .addComponent(lbl13))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn14)
                                .addComponent(lbl14))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn15)
                                .addComponent(lbl15)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel73.setText("Tiempo: ");

        jLabel74.setText("Instruccion:");

        jLabel75.setText("Direccion:");

        jLabel76.setText("Fallo de pagina:");

        jLabel77.setText("Pagina Virtual:");

        jLabel78.setText("Pagina Física:");

        jLabel79.setText("Bit R:");

        jLabel80.setText("Bit M:");

        jLabel81.setText("Tiempo en Memoria:");

        jLabel82.setText("Ultima Modificacion:");

        jLabel83.setText("Low:");

        jLabel84.setText("High:");

        lblInstruccion.setText("WRITE");

        lblDireccion.setText("8192");

        lblFalla.setForeground(new java.awt.Color(255, 0, 0));
        lblFalla.setText("SI");

        lblPvirtual.setText("64");

        lblPfisica.setText("32");

        lblBR.setText("SI");

        lblBM.setText("SI");

        lblTiempoMemoria.setText("10000000");

        lblUltMod.setText("12000000");

        lblinicio.setText("0");

        lblfin.setText("4096");

        lblTiempo.setText("0000");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel77)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPvirtual))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel78)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPfisica))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel79)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblBR))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel80)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblBM))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel83)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblinicio))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel84)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblfin))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel82)
                                    .addComponent(jLabel81))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTiempoMemoria)
                                    .addComponent(lblUltMod)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel73)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTiempo))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel74)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblInstruccion))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel75)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDireccion))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel76)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblFalla)))
                        .addGap(0, 188, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel73)
                    .addComponent(lblTiempo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74)
                    .addComponent(lblInstruccion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel75)
                    .addComponent(lblDireccion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76)
                    .addComponent(lblFalla))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPvirtual)
                    .addComponent(jLabel77, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(lblPfisica))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79)
                    .addComponent(lblBR))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(lblBM))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(lblTiempoMemoria))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(lblUltMod))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(lblinicio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel84)
                    .addComponent(lblfin))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel97.setText("STATUS:");

        lblStatus.setText("STOP");

        txtSalida.setColumns(20);
        txtSalida.setRows(5);
        jScrollPane1.setViewportView(txtSalida);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnPaso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEjecutar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnReset, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel97)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStatus))))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel97)
                                    .addComponent(lblStatus))
                                .addGap(14, 14, 14)
                                .addComponent(btnEjecutar)
                                .addGap(18, 18, 18)
                                .addComponent(btnPaso)
                                .addGap(18, 18, 18)
                                .addComponent(btnReset)
                                .addGap(18, 18, 18)
                                .addComponent(btnSalir)
                                .addGap(122, 122, 122))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        if (timer.isRunning()){timer.stop();}
        limpia(4);
        Inicializa();
        ms=0;
        memory=null;
        memoryF=null;
        orden=null;
        original=null;
        operaciones=null;
        indicePaso=0;
        direccion=0;
        inicio=0;
        anterior=0;
        indice=0;
        this.i.btn1.setEnabled(true);
        this.i.btn2.setEnabled(false);
        this.i.btn3.setEnabled(false);
        this.d.limpia();
        d=null;
        this.i.lblRutaConf.setText(null);
        this.i.lblRutaProcesos.setText(null);
        for (int k=0;k<64;k++){
            JLabel label= (JLabel) getComponentByName(k+"");
            JButton boton= (JButton) getComponentByName("btn"+k);
            label.setForeground(new java.awt.Color(0, 0, 0));
            label.setText("");
            boton.setEnabled(false);
        }
        this.setVisible(false);
        this.i.setVisible(true);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        if (timer.isRunning()){timer.stop();}
        Inicializa();
        limpia(4);
        ms=d.getTi();
        for(int o=0;o<memory.length;o++){
            JLabel l= (JLabel) getComponentByName(o+"");
            JButton b= (JButton) getComponentByName("btn"+o);
            if (l!=null && b!=null){
                if(memory[o][1]<0 ){
                    l.setForeground(new java.awt.Color(255, 0, 0));
                    l.setText("x");
                    //b.setEnabled(true);
                    //b.setEnabled(false);
                }else{
                    l.setForeground(new java.awt.Color(0, 0, 0));
                    l.setText(""+memory[o][1]);
                    //b.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnPasoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasoActionPerformed
        // TODO add your handling code here:
        if (!timer.isRunning()){timer.start();}
        if(this.operaciones.length>this.indicePaso){
            this.lblStatus.setText("STEP");
            inicio=new Integer(ms);
            paso();
        }else{
            this.lblStatus.setText("STOP");
            this.btnEjecutar.setEnabled(false);
            this.btnPaso.setEnabled(false);
            timer.stop();
            try {
                //Files.write(Paths.get(d.getRuta().replaceAll("\\\\[a-zA-Z]+.conf$", "")+"\\tracefile.txt"), "".getBytes(), StandardOpenOption.CREATE);
                Files.write(Paths.get(d.getRuta().replaceAll("\\\\[a-zA-Z]+.conf$", "")+"\\tracefile.txt"), txtSalida.getText().getBytes(),StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                txtSalida.append("Se guardo en: "+d.getRuta().replaceAll("\\\\[a-zA-Z]+.conf$", "")+"\\tracefile.txt");
            }catch (IOException ex) {
                //exception handling left as an exercise for the reader
                ex.printStackTrace();
                System.out.println("error");
            }
        }
    }//GEN-LAST:event_btnPasoActionPerformed

    private void btnEjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEjecutarActionPerformed
        // TODO add your handling code here:
        this.btnPaso.setEnabled(false);
        this.btnEjecutar.setEnabled(false);
        this.btnReset.setEnabled(false);
        this.btnSalir.setEnabled(false);
        this.lblStatus.setText("RUN");
        timer.start();
        timer2.start();
    }//GEN-LAST:event_btnEjecutarActionPerformed

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }*/

    @Override
    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        for (int i=0;i<64;i++){
            //System.out.println(d.getMemory()[i][1]+"");
            if(e.getActionCommand().equals("pag"+i)){
                this.lblPvirtual.setText(memory[i][0]+"");
                if(memory[i][1]<0){this.lblPfisica.setText("x");}else{this.lblPfisica.setText(memory[i][1]+"");}
                this.lblBR.setText(memory[i][2]+"");
                this.lblBM.setText(memory[i][3]+"");
                this.lblTiempoMemoria.setText(memory[i][4]+"ms");
                this.lblUltMod.setText(memory[i][5]+"ms");
                limpia(2);
                this.lblinicio.setText(i*this.d.getTamanoP()+"");
                this.lblfin.setText((i*(this.d.getTamanoP()))+this.d.getTamanoP()-1+"");
                break;
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn0;
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn10;
    private javax.swing.JButton btn11;
    private javax.swing.JButton btn12;
    private javax.swing.JButton btn13;
    private javax.swing.JButton btn14;
    private javax.swing.JButton btn15;
    private javax.swing.JButton btn16;
    private javax.swing.JButton btn17;
    private javax.swing.JButton btn18;
    private javax.swing.JButton btn19;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn20;
    private javax.swing.JButton btn21;
    private javax.swing.JButton btn22;
    private javax.swing.JButton btn23;
    private javax.swing.JButton btn24;
    private javax.swing.JButton btn25;
    private javax.swing.JButton btn26;
    private javax.swing.JButton btn27;
    private javax.swing.JButton btn28;
    private javax.swing.JButton btn29;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn30;
    private javax.swing.JButton btn31;
    private javax.swing.JButton btn32;
    private javax.swing.JButton btn33;
    private javax.swing.JButton btn34;
    private javax.swing.JButton btn35;
    private javax.swing.JButton btn36;
    private javax.swing.JButton btn37;
    private javax.swing.JButton btn38;
    private javax.swing.JButton btn39;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn40;
    private javax.swing.JButton btn41;
    private javax.swing.JButton btn42;
    private javax.swing.JButton btn43;
    private javax.swing.JButton btn44;
    private javax.swing.JButton btn45;
    private javax.swing.JButton btn46;
    private javax.swing.JButton btn47;
    private javax.swing.JButton btn48;
    private javax.swing.JButton btn49;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn50;
    private javax.swing.JButton btn51;
    private javax.swing.JButton btn52;
    private javax.swing.JButton btn53;
    private javax.swing.JButton btn54;
    private javax.swing.JButton btn55;
    private javax.swing.JButton btn56;
    private javax.swing.JButton btn57;
    private javax.swing.JButton btn58;
    private javax.swing.JButton btn59;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn60;
    private javax.swing.JButton btn61;
    private javax.swing.JButton btn62;
    private javax.swing.JButton btn63;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnEjecutar;
    private javax.swing.JButton btnPaso;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbl0;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl10;
    private javax.swing.JLabel lbl11;
    private javax.swing.JLabel lbl12;
    private javax.swing.JLabel lbl13;
    private javax.swing.JLabel lbl14;
    private javax.swing.JLabel lbl15;
    private javax.swing.JLabel lbl16;
    private javax.swing.JLabel lbl17;
    private javax.swing.JLabel lbl18;
    private javax.swing.JLabel lbl19;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lbl20;
    private javax.swing.JLabel lbl21;
    private javax.swing.JLabel lbl22;
    private javax.swing.JLabel lbl23;
    private javax.swing.JLabel lbl24;
    private javax.swing.JLabel lbl25;
    private javax.swing.JLabel lbl26;
    private javax.swing.JLabel lbl27;
    private javax.swing.JLabel lbl28;
    private javax.swing.JLabel lbl29;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lbl30;
    private javax.swing.JLabel lbl31;
    private javax.swing.JLabel lbl32;
    private javax.swing.JLabel lbl33;
    private javax.swing.JLabel lbl34;
    private javax.swing.JLabel lbl35;
    private javax.swing.JLabel lbl36;
    private javax.swing.JLabel lbl37;
    private javax.swing.JLabel lbl38;
    private javax.swing.JLabel lbl39;
    private javax.swing.JLabel lbl4;
    private javax.swing.JLabel lbl40;
    private javax.swing.JLabel lbl41;
    private javax.swing.JLabel lbl42;
    private javax.swing.JLabel lbl43;
    private javax.swing.JLabel lbl44;
    private javax.swing.JLabel lbl45;
    private javax.swing.JLabel lbl46;
    private javax.swing.JLabel lbl47;
    private javax.swing.JLabel lbl48;
    private javax.swing.JLabel lbl49;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lbl50;
    private javax.swing.JLabel lbl51;
    private javax.swing.JLabel lbl52;
    private javax.swing.JLabel lbl53;
    private javax.swing.JLabel lbl54;
    private javax.swing.JLabel lbl55;
    private javax.swing.JLabel lbl56;
    private javax.swing.JLabel lbl57;
    private javax.swing.JLabel lbl58;
    private javax.swing.JLabel lbl59;
    private javax.swing.JLabel lbl6;
    private javax.swing.JLabel lbl60;
    private javax.swing.JLabel lbl61;
    private javax.swing.JLabel lbl62;
    private javax.swing.JLabel lbl63;
    private javax.swing.JLabel lbl7;
    private javax.swing.JLabel lbl8;
    private javax.swing.JLabel lbl9;
    private javax.swing.JLabel lblBM;
    private javax.swing.JLabel lblBR;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblFalla;
    private javax.swing.JLabel lblInstruccion;
    private javax.swing.JLabel lblPfisica;
    private javax.swing.JLabel lblPvirtual;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTiempo;
    private javax.swing.JLabel lblTiempoMemoria;
    private javax.swing.JLabel lblUltMod;
    private javax.swing.JLabel lblfin;
    private javax.swing.JLabel lblinicio;
    private javax.swing.JTextArea txtSalida;
    // End of variables declaration//GEN-END:variables
}
/*
        this.btn0.setActionCommand("pag0");
        this.btn1.setActionCommand("pag1");
        this.btn2.setActionCommand("pag2");
        this.btn3.setActionCommand("pag3");
        this.btn4.setActionCommand("pag4");
        this.btn5.setActionCommand("pag5");
        this.btn6.setActionCommand("pag6");
        this.btn7.setActionCommand("pag7");
        this.btn8.setActionCommand("pag8");
        this.btn9.setActionCommand("pag9");
        this.btn10.setActionCommand("pag10");
        this.btn11.setActionCommand("pag11");
        this.btn12.setActionCommand("pag12");
        this.btn13.setActionCommand("pag13");
        this.btn14.setActionCommand("pag14");
        this.btn15.setActionCommand("pag15");
        this.btn16.setActionCommand("pag16");
        this.btn17.setActionCommand("pag17");
        this.btn18.setActionCommand("pag18");
        this.btn19.setActionCommand("pag19");
        this.btn20.setActionCommand("pag20");
        this.btn21.setActionCommand("pag21");
        this.btn22.setActionCommand("pag22");
        this.btn23.setActionCommand("pag23");
        this.btn24.setActionCommand("pag24");
        this.btn25.setActionCommand("pag25");
        this.btn26.setActionCommand("pag26");
        this.btn27.setActionCommand("pag27");
        this.btn28.setActionCommand("pag28");
        this.btn29.setActionCommand("pag29");
        this.btn30.setActionCommand("pag30");
        this.btn31.setActionCommand("pag31");
        this.btn32.setActionCommand("pag32");
        this.btn33.setActionCommand("pag33");
        this.btn34.setActionCommand("pag34");
        this.btn35.setActionCommand("pag35");
        this.btn36.setActionCommand("pag36");
        this.btn37.setActionCommand("pag37");
        this.btn38.setActionCommand("pag38");
        this.btn39.setActionCommand("pag39");
        this.btn40.setActionCommand("pag40");
        this.btn41.setActionCommand("pag41");
        this.btn42.setActionCommand("pag42");
        this.btn43.setActionCommand("pag43");
        this.btn44.setActionCommand("pag44");
        this.btn45.setActionCommand("pag45");
        this.btn46.setActionCommand("pag46");
        this.btn47.setActionCommand("pag47");
        this.btn48.setActionCommand("pag48");
        this.btn49.setActionCommand("pag49");
        this.btn50.setActionCommand("pag50");
        this.btn51.setActionCommand("pag51");
        this.btn52.setActionCommand("pag52");
        this.btn53.setActionCommand("pag53");
        this.btn54.setActionCommand("pag54");
        this.btn55.setActionCommand("pag55");
        this.btn56.setActionCommand("pag56");
        this.btn57.setActionCommand("pag57");
        this.btn58.setActionCommand("pag58");
        this.btn59.setActionCommand("pag59");
        this.btn60.setActionCommand("pag60");
        this.btn61.setActionCommand("pag61");
        this.btn62.setActionCommand("pag62");
        this.btn63.setActionCommand("pag63");
        this.btn0.addActionListener(this);
        this.btn1.addActionListener(this);
        this.btn2.addActionListener(this);
        this.btn3.addActionListener(this);
        this.btn4.addActionListener(this);
        this.btn5.addActionListener(this);
        this.btn6.addActionListener(this);
        this.btn7.addActionListener(this);
        this.btn8.addActionListener(this);
        this.btn9.addActionListener(this);
        this.btn10.addActionListener(this);
        this.btn11.addActionListener(this);
        this.btn12.addActionListener(this);
        this.btn13.addActionListener(this);
        this.btn14.addActionListener(this);
        this.btn15.addActionListener(this);
        this.btn16.addActionListener(this);
        this.btn17.addActionListener(this);
        this.btn18.addActionListener(this);
        this.btn19.addActionListener(this);
        this.btn20.addActionListener(this);
        this.btn21.addActionListener(this);
        this.btn22.addActionListener(this);
        this.btn23.addActionListener(this);
        this.btn24.addActionListener(this);
        this.btn25.addActionListener(this);
        this.btn26.addActionListener(this);
        this.btn27.addActionListener(this);
        this.btn28.addActionListener(this);
        this.btn29.addActionListener(this);
        this.btn30.addActionListener(this);
        this.btn31.addActionListener(this);
        this.btn32.addActionListener(this);
        this.btn33.addActionListener(this);
        this.btn34.addActionListener(this);
        this.btn35.addActionListener(this);
        this.btn36.addActionListener(this);
        this.btn37.addActionListener(this);
        this.btn38.addActionListener(this);
        this.btn39.addActionListener(this);
        this.btn40.addActionListener(this);
        this.btn41.addActionListener(this);
        this.btn42.addActionListener(this);
        this.btn43.addActionListener(this);
        this.btn44.addActionListener(this);
        this.btn45.addActionListener(this);
        this.btn46.addActionListener(this);
        this.btn47.addActionListener(this);
        this.btn48.addActionListener(this);
        this.btn49.addActionListener(this);
        this.btn50.addActionListener(this);
        this.btn51.addActionListener(this);
        this.btn52.addActionListener(this);
        this.btn53.addActionListener(this);
        this.btn54.addActionListener(this);
        this.btn55.addActionListener(this);
        this.btn56.addActionListener(this);
        this.btn57.addActionListener(this);
        this.btn58.addActionListener(this);
        this.btn59.addActionListener(this);
        this.btn60.addActionListener(this);
        this.btn61.addActionListener(this);
        this.btn62.addActionListener(this);
        this.btn63.addActionListener(this);*/