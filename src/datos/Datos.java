package datos;
import gui.Abrir;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Jiro
 */
public class Datos {
    Abrir a;
    private int tamanoP,paginasV,paginasF;
    private int[][] memoriaV;
    private String[][] operaciones;
    private String linea;
    private String[] arreglo;
    private Scanner scn;
    private int error,ti,io;
    private int[] memoriaF;
    private int[] order;
    private String ruta;
    
    public Datos(Abrir a){
    this.a=a;
    }
    public String getRuta(){
        return ruta;
    }
    public int[] getOrder(){
        return order;
    }
    public int[] getMemoryF(){
        return memoriaF;
    }
    public int[][] getMemory(){
        return memoriaV;
    }
    public String[][] getOperaciones(){
        return operaciones;
    }
    public int getTamanoP(){
        return tamanoP;
    }
    public int getPvirtual(){
        return paginasV;
    }
    public int getPfisica(){
        return paginasF;
    }
    public int getTi(){return ti;}
    public void configuracion(File archivo){
        io=0;
        
        // muestra error si es inválido
        if (((archivo == null) || (archivo.getName().equals("")))) {
        }else{
            if(archivo.getName().toLowerCase().endsWith(".conf")){
                a.lblRutaConf.setText(archivo.getAbsolutePath());
                ruta=archivo.getAbsolutePath();
                //JOptionPane.showMessageDialog(a, "1", "Error", JOptionPane.ERROR_MESSAGE);
                try {
                    error=0;
                    scn = new Scanner(archivo);
                    //JOptionPane.showMessageDialog(a, "2", "Error", JOptionPane.ERROR_MESSAGE);
                    //obtener configuracion
                    while (scn.hasNext()) {
                        //JOptionPane.showMessageDialog(a, "5", "Error", JOptionPane.ERROR_MESSAGE);
                        linea=scn.nextLine();
                        if(linea.length()>0 && !linea.startsWith("//")){
                            arreglo=linea.split(" ");
                            //JOptionPane.showMessageDialog(a, "4", "Error", JOptionPane.ERROR_MESSAGE);
                            if(arreglo[0].toLowerCase().equals("tamanopag") && arreglo.length>1){
                                //this.tamanoP=Integer.parseInt(arreglo[1]);
                                if (isNumeric(arreglo[1])){
                                    this.tamanoP=Integer.parseInt(arreglo[1]);
                                    //JOptionPane.showMessageDialog(a, "3", "Error", JOptionPane.ERROR_MESSAGE);
                                }else{
                                    error=1;
                                    //JOptionPane.showMessageDialog(a, "La configuracion del tamaño de pagina no es valida", "Error", JOptionPane.ERROR_MESSAGE);
                                    this.a.txtinformacion.append("La configuracion del tamaño de pagina no es valida\n");
                                }
                                //System.out.print("Tamaño de pagina: "+tamanoP+"\n");
                            }else if(arreglo[0].toLowerCase().equals("numpag") && arreglo.length>1){
                                //this.paginasV=Integer.parseInt(arreglo[1]);
                                if (isNumeric(arreglo[1])){this.paginasV=Integer.parseInt(arreglo[1]);}else{
                                    error=1;
                                    //JOptionPane.showMessageDialog(a, "La configuracion de las paginas virtuales no es valida", "Error", JOptionPane.ERROR_MESSAGE);
                                    this.a.txtinformacion.append("La configuracion de las paginas virtuales no es valida\n");
                                }
                                //System.out.print("Paginas virtuales: "+paginasV+"\n");
                            }else if(arreglo[0].toLowerCase().equals("marcpag") && arreglo.length>1){
                                if (isNumeric(arreglo[1])){this.paginasF=Integer.parseInt(arreglo[1]);}else{
                                    error=1;
                                    //JOptionPane.showMessageDialog(a, "La configuracion de los marcos de pagina no es valida", "Error", JOptionPane.ERROR_MESSAGE);
                                    this.a.txtinformacion.append("La configuracion de los marcos de pagina no es valida\n");
                                }
                                //System.out.print("Paginas fisicas: "+paginasF+"\n");
                                //JOptionPane.showMessageDialog(a, "Seleccione un archivo de configuracion (.conf) valido", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    /*if(this.tamanoP==0){
                        error=1;
                        //JOptionPane.showMessageDialog(a, "Error en el tamaño de pagina", "Error", JOptionPane.ERROR_MESSAGE);
                        this.a.txtinformacion.append("Error en el tamaño de pagina\n");
                    }*/
                    if(error==0){
                        scn = new Scanner(archivo);
                        this.memoriaV=new int[this.paginasV][6];
                        this.memoriaF=new int[this.paginasF];
                        this.order=new int[this.paginasV];
                        for(int j=0;j<this.paginasF;j++){
                            this.memoriaF[j]=0;
                        }
                        for (int i=0;i<this.memoriaV.length;i++){
                            this.memoriaV[i][0]=i;
                            this.memoriaV[i][1]=-1;
                            for (int j=2;j<6;j++){
                                this.memoriaV[i][j]=0;
                            }
                            //System.out.print("memoria virtual: "+i+" en "+this.memoriaV[i][1]+"\n");
                        }
                        while (scn.hasNext()) {
                            linea=scn.nextLine();
                            if(linea.length()>0 && !linea.startsWith("//")){
                                linea=linea.replaceAll("- 1", "-1");
                                arreglo=linea.split(" ");
                                if (arreglo[0].toLowerCase().equals("memset") && arreglo.length==7){
                                    //for (int i=0;i<6;i++){
                                        if (Integer.parseInt(arreglo[1])>=0&&Integer.parseInt(arreglo[1])<this.paginasV&&(Integer.parseInt(arreglo[2])>-2 && Integer.parseInt(arreglo[2])<this.paginasF) &&Integer.parseInt(arreglo[3])>=0&& Integer.parseInt(arreglo[4])>=0&&Integer.parseInt(arreglo[5])>=0&&Integer.parseInt(arreglo[6])>=0 ){
                                            for(int u=0;u<paginasV;u++){
                                                if(Integer.parseInt(arreglo[2])==memoriaV[u][1] || (memoriaV[u][0]==Integer.parseInt(arreglo[1])&&memoriaF[u]>0)){
                                                    error=1;
                                                    //JOptionPane.showMessageDialog(a, "Estas inicializano dos paginas virtuales a la misma fisica, se sobreescribiran los valores de la pagina anterior", "Atencion", JOptionPane.WARNING_MESSAGE);
                                                    if(Integer.parseInt(arreglo[2])==memoriaV[u][1]){
                                                        this.a.txtinformacion.append("Estas inicializando dos paginas virtuales a la misma fisica, se sobreescribiran los valores de la pagina anterior\n");
                                                    }else if ((memoriaV[u][0]==Integer.parseInt(arreglo[1])&&memoriaF[u]>0)){
                                                        this.a.txtinformacion.append("Estas inicializando dos veces la pagina virtual, se sobreescribiran los valores de la pagina anterior\n");
                                                    }
                                                    //break;
                                                    //this.memoriaV[u][0]=Integer.parseInt(arreglo[1]);
                                                    this.memoriaV[u][1]=Integer.parseInt(arreglo[2]);
                                                    this.memoriaV[u][2]=Integer.parseInt(arreglo[3]);
                                                    this.memoriaV[u][3]=Integer.parseInt(arreglo[4]);
                                                    this.memoriaV[u][4]=Integer.parseInt(arreglo[5]);
                                                    this.memoriaV[u][5]=Integer.parseInt(arreglo[6]);
                                                    this.a.txtinformacion.append("Se inicializó con exito la pagina "+this.memoriaV[u][0]+"\n");//*/
                                                    
                                                }
                                            }
                                            if (error==0){
                                                //System.out.println(Integer.parseInt(arreglo[2]));
                                                this.memoriaF[Integer.parseInt(arreglo[2])]=1;
                                                this.order[io]=Integer.parseInt(arreglo[1]);
                                                io++;
                                                //this.memoriaV[Integer.parseInt(arreglo[1])][0]=Integer.parseInt(arreglo[1]);
                                                this.memoriaV[Integer.parseInt(arreglo[1])][1]=Integer.parseInt(arreglo[2]);
                                                this.memoriaV[Integer.parseInt(arreglo[1])][2]=Integer.parseInt(arreglo[3]);
                                                this.memoriaV[Integer.parseInt(arreglo[1])][3]=Integer.parseInt(arreglo[4]);
                                                this.memoriaV[Integer.parseInt(arreglo[1])][4]=Integer.parseInt(arreglo[5]);
                                                this.memoriaV[Integer.parseInt(arreglo[1])][5]=Integer.parseInt(arreglo[6]);
                                                this.a.txtinformacion.append("Se inicializó con exito la pagina "+this.memoriaV[Integer.parseInt(arreglo[1])][0]+"\n");//*/
                                            }
                                            error=0;
                                            //System.out.print("memoria virtual: "+Integer.parseInt(arreglo[1])+" en "+Integer.parseInt(arreglo[2])+"\n");
                                        }else{
                                            //tiene valores raros
                                            if(Integer.parseInt(arreglo[1])>=0&&Integer.parseInt(arreglo[2])>-2 &&Integer.parseInt(arreglo[3])>=0&& Integer.parseInt(arreglo[4])>=0&&Integer.parseInt(arreglo[5])>=0&&Integer.parseInt(arreglo[6])>=0 ){
                                                if(Integer.parseInt(arreglo[1])>this.paginasV||Integer.parseInt(arreglo[2])>this.paginasF){
                                                    //JOptionPane.showMessageDialog(a, "Estas inicializando una pagina virtual o fisica mayor a las configuradas, esta configuracion se ignorará", "Atencion", JOptionPane.WARNING_MESSAGE);
                                                    if(Integer.parseInt(arreglo[1])>this.paginasV){
                                                        //JOptionPane.showMessageDialog(a, "Estas inicializando una pagina virtual mayor a la configurada, esta inicializacion se ignorará", "Atencion", JOptionPane.WARNING_MESSAGE);
                                                        this.a.txtinformacion.append("Estas inicializando una pagina virtual mayor a la configurada, esta inicializacion se ignorará\n");
                                                    }else{
                                                        this.a.txtinformacion.append("Estas inicializando una pagina fisica mayor a la configurada, esta inicializacion se ignorará\n");
                                                        //JOptionPane.showMessageDialog(a, "Estas inicializando una pagina fisica mayor a la configurada, esta inicializacion se ignorará", "Atencion", JOptionPane.WARNING_MESSAGE);
                                                    }
                                                }
                                            }else{
                                                this.a.txtinformacion.append("Hay un error en la inicializacion de una pagina, esta inicializacion se ignorará\n");
                                                //JOptionPane.showMessageDialog(a, "Hay un error en la inicializacion de una pagina, esta inicializacion se ignorará", "Atencion", JOptionPane.WARNING_MESSAGE);
                                            }
                                        }
                                   // }
                                }
                            }
                        }
                        if (error==0){
                            ti=0;
                            for(int p=0;p<memoriaV.length;p++){
                                if (this.memoriaV[p][4]>ti){ti=this.memoriaV[p][4];}
                            }

                            a.btn2.setEnabled(true);
                            a.btn1.setEnabled(false);
                        
                        }
                    
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Abrir.class.getName()).log(Level.SEVERE, null, ex);
                    //JOptionPane.showMessageDialog(a, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(a, "Seleccione un archivo de configuracion valido (Ejemplo: memory.conf)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void operaciones(File archivo){
        error=0;
        if ((archivo == null) || (archivo.getName().equals(""))) {
        }else{
            a.lblRutaProcesos.setText(archivo.getAbsolutePath());
            int contador=0,contador2=0;
            try {
                scn = new Scanner(archivo);
                while (scn.hasNext()) {
                    linea=scn.nextLine();
                    //System.out.println(linea);
                    if(linea.length()>0 && !linea.startsWith("//")){
                        arreglo=linea.split(" ");
                        if((arreglo[0].toLowerCase().equals("read") || arreglo[0].toLowerCase().equals("write")) && arreglo.length>1){
                            contador++;
                        }
                    }
                }
                //System.out.println(contador);
                this.operaciones=new String[contador][3];
                
                scn = new Scanner(archivo);
                
                while (scn.hasNext()) {
                    linea=scn.nextLine();
                    if(linea.length()>0 && !linea.startsWith("//")){
                        arreglo=linea.split(" ");
                        if((arreglo[0].toLowerCase().equals("read") || arreglo[0].toLowerCase().equals("write")) && arreglo.length>1){
                            if (arreglo[0].toLowerCase().equals("read")){
                                    this.operaciones[contador2][0]="read";
                            }else{
                                this.operaciones[contador2][0]="write";
                            }
                            if (arreglo.length>2){
                                if (arreglo[1].toLowerCase().equals("dec")||arreglo[1].toLowerCase().equals("bin")||arreglo[1].toLowerCase().equals("hex")||arreglo[1].toLowerCase().equals("oct")){
                                    switch (arreglo[1].toLowerCase()){
                                        case "bin":
                                            if (arreglo[2].matches("^+(1|0)+$")){
                                                this.operaciones[contador2][1]="bin";
                                                this.operaciones[contador2][2]=arreglo[2];
                                            }else{borrar(2,contador2);}
                                            break;
                                        case "hex":
                                            if (arreglo[2].matches("^[0-9A-F]+$")){
                                                this.operaciones[contador2][1]="hex";
                                                this.operaciones[contador2][2]=arreglo[2];
                                            }else{borrar(2,contador2);}
                                            break;
                                        case "oct":
                                            if (arreglo[2].matches("^[0-7]+$")){
                                                this.operaciones[contador2][1]="oct";
                                                this.operaciones[contador2][2]=arreglo[2];
                                            }else{borrar(2,contador2);}
                                            break;
                                        case "dec":
                                            if (isNumeric(arreglo[2])){
                                                this.operaciones[contador2][1]="dec";
                                                this.operaciones[contador2][2]=arreglo[2];
                                            }else{borrar(2,contador2);}
                                            break;
                                    }
                                    this.a.txtinformacion.append("Se agregó con exito el proceso "+this.operaciones[contador2][0]+" "+this.operaciones[contador2][1]+" "+this.operaciones[contador2][2]+" "+"\n");
                                }else{borrar(2,contador2);this.a.txtinformacion.append("Hubo un error al agregar el proceso "+this.arreglo[0]+" "+this.arreglo[1]+" "+this.arreglo[2]+" "+"\n");}
                            }else if(arreglo.length<3){
                                if(arreglo[1].toLowerCase().equals("aleatorio")){
                                    this.operaciones[contador2][1]="dec";
                                    this.operaciones[contador2][2]=""+(int)(Math.random() * (this.paginasV*this.tamanoP));
                                    this.a.txtinformacion.append("Se agregó con exito el proceso "+this.operaciones[contador2][0]+" "+this.operaciones[contador2][1]+" "+this.operaciones[contador2][2]+" "+"\n");
                                }else if (isNumeric(arreglo[1])&& Integer.parseInt(arreglo[1])<this.paginasV*this.tamanoP){
                                    this.operaciones[contador2][1]="dec";
                                    this.operaciones[contador2][2]=arreglo[1];
                                    this.a.txtinformacion.append("Se agregó con exito el proceso "+this.operaciones[contador2][0]+" "+this.operaciones[contador2][1]+" "+this.operaciones[contador2][2]+" "+"\n");
                                }else{borrar(2,contador2);this.a.txtinformacion.append("Hubo un error al agregar el proceso "+this.arreglo[0]+" "+this.arreglo[1]+"\n");}
                            }
                            
                        }
                        contador2++;
                    }
                }
                contador=0;
                for(int err=0;err<this.operaciones.length;err++){
                    if(!this.operaciones[0].equals("error")){contador++;}
                }
                if(contador>0){
                    a.btn3.setEnabled(true);
                    a.btn2.setEnabled(false);
                }else{this.a.lblRutaProcesos.setText(null);this.a.txtinformacion.append("No se han encontrado procesos o todos estan mal redactados\n");}
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Abrir.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    private void borrar(int o,int c){
        if(o==1){
            for (int i=0;i<6;i++){
                this.memoriaV[c][i]=-1;
            }
        }else{
            this.operaciones[c][0]="error";
            this.operaciones[c][1]="dec";
            this.operaciones[c][2]="0";
        }
    }
    private boolean isNumeric(String str){
       boolean isNumeric= str.chars().allMatch( Character::isDigit );
       return isNumeric;
    }
    public int aBaseDiez(String numero,String base){
        switch(base){
            case "bin":
                return Integer.parseInt(numero,2);
            case "hex":
                return Integer.parseInt(numero,16);
            case "oct":
                return Integer.parseInt(numero,8);
            default:
                return Integer.parseInt(numero,10);
        }
    }
    public void limpia(){
        tamanoP=0;
        paginasV=0;
        paginasF=0;
        memoriaV=null;
        operaciones=null;
        linea="";
        arreglo=null;
        scn=null;
        error=0;
        ti=0;
        io=0;
        memoriaF=null;
        order=null;
        ruta="";
        a.archivo=null;
        a.resultado=0;
        a.txtinformacion.setText(null);
        
    }
}
