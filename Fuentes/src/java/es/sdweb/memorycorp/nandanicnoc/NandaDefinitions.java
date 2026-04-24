package es.sdweb.memorycorp.nandanicnoc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import es.sdweb.application.componentes.util.FileUtil;
import es.sdweb.application.componentes.util.StringUtil;

/**
 *
 * @author Antonio Carro Mariño
 */
public class NandaDefinitions {

    public static String TIPO_CARACTERISTICA_01="Característica definitoria";
    public static String TIPO_CARACTERISTICA_02="Factor relacionado";
    public static String TIPO_CARACTERISTICA_03="Condición asociada";

    //*** Array List de dominios pe "DOMINIO 1: Promoción de la salud", ...
    private static ArrayList<Dominio> dominiosNanda=new ArrayList();
    private static ArrayList<Dominio> dominiosNic=new ArrayList();
    private static ArrayList<Dominio> dominiosNoc=new ArrayList();

    //*** Hastable de NANDAs, NICs, y NOCs (para localizarlos rápidamente). Solo se menten en ella aquellos que tengan archivo dedicado (pe "[000123].txt")
    private static Hashtable nandaTable=new Hashtable();
    private static Hashtable nicTable=new Hashtable();
    private static Hashtable nocTable=new Hashtable();

    //Path base donde se ubica el vocabulario NANDA-NIC-NOC
    private final static String PATH_NANDANICNOC="/opt/desarrollo/memorycorp/";
    private final static String PATH_NANDAS="nandas.txt";
    private final static String PATH_NICS="nics.txt";
    private final static String PATH_NOCS="nocs.txt";

    //***** INICIALIZA Y CARGA EL ESTÁNDAR/VOCABULARIO NANDA-NIC-NOC
    static{
        inicializaNandaNicNoc();
    }



   private static String getValue(String linea){
       String result="";
       if (!StringUtil.isEmpty(linea)){
           String valor=linea.substring(linea.indexOf(":")).trim(); //Nos quedamos con la subcadena de los dos puntos en adelante
           result=valor;
       }
       return result;
   }

   private static boolean cargaDetalleNanda(IItem itemNanda){
       NandaItem nanda=(NandaItem)itemNanda;
       //Ruta del archivo que contiene el detalle del Nanda
       String ruta=NandaDefinitions.PATH_NANDANICNOC+"nandas/"+nanda.getCode()+".txt";

       InputStream is=FileUtil.openFile(ruta,false);
       if (is==null) return false; //Si no se encontró el fichero, no hacemos nada
       Scanner fich=new Scanner(is);

       Dominio dom=null; //Dominio actual
       Clase clas=null; //Clase actual
       int lista=1; //Lista actual 1=CAR; 2=NOC; 3=NIC
       while (fich.hasNextLine()){
           //Localizacion
           String linea=fich.nextLine().trim(); //Leemos una línea del fichero con una palabra

           String word[]=linea.split(" "); //Troceamos la cadena
           String w=word[0];
           if (w.equals("NANDA")||w.equals("CÓDIGO:")||w.equals("DOMINIO:")||w.equals("CLASE:")||
               w.equals("NOTAS:")){
               //No hacemos nada ya que estos datos ya los tenemos
           }else if (w.equals("EDICIÓN:")){
               nanda.setEdition(getValue(linea));
           }else if (w.equals("DIAGNÓSTICO:")){
               nanda.setDiagnostico(getValue(linea));
           }else if (w.equals("DEFINICIÓN:")){
               nanda.setDefinicion(getValue(linea));
           }else if (w.equals("NECESIDAD:")){
               nanda.setNecesidad(getValue(linea));
           }else if (w.equals("PATRÓN:")){
               nanda.setPatron(getValue(linea));
           }else if (w.equals("CARACTERÍSTICAS")){ //Características y factores
               lista=1;
           }else if (w.equals("NOCS")){ //Nocs relacionados
               lista=2;
           }else if (w.equals("NICS")){ //Nics relacionados
               lista=3;
           }else{ //En otro caso estamos ante un dato a guardar en una lista
               switch (lista){
                   case 1://Caracteristica o factor
                        String caracteristica=linea.substring(0,linea.indexOf("("));
                        String tipo=linea.substring(linea.indexOf("(")+1,linea.indexOf(")"));
                        SubItem sub=new SubItem(caracteristica,tipo);
                        nanda.addCaracteristica(sub);
                       break;
                   case 2: //Nocs relacionados
                        NocItem noc=new NocItem(w,linea);
                        if (!StringUtil.isEmpty(linea)){
                           nanda.addNocsRelacionados(noc);
                        }
                       break;
                   case 3: //Nics relacionados
                        NicItem nic=new NicItem(w,linea);
                        if (!StringUtil.isEmpty(linea)){
                           nanda.addNicsRelacionados(nic);
                        }
                       break;
               }
           }

       }//while

       nandaTable.put(nanda.getCode(), nanda); //Por último metemos en la tabla el NANDA
       FileUtil.closeFile(is); //Cerramos el fichero
       return true;
   }

   private static boolean cargaDetalleNic(IItem itemNic){
       NicItem item=(NicItem)itemNic;
       //Ruta del archivo que contiene el detalle del Nic
       String ruta=NandaDefinitions.PATH_NANDANICNOC+"nics/"+item.getCode()+".txt";

       InputStream is=FileUtil.openFile(ruta,false);
       if (is==null) return false; //Si no se encontró el fichero, no hacemos nada
       Scanner fich=new Scanner(is);

       Dominio dom=null; //Dominio actual
       Clase clas=null; //Clase actual
       int lista=1; //Lista actual 1=ACT; 2=NANDA; 3=NOC
       while (fich.hasNextLine()){
           //Localizacion
           String linea=fich.nextLine().trim(); //Leemos una línea del fichero con una palabra

           String word[]=linea.split(" "); //Troceamos la cadena
           String w=word[0];
           if (w.equals("NIC")||w.equals("CÓDIGO:")||w.equals("DOMINIO:")||w.equals("CLASE:")||
               w.equals("NOTAS:")){
               //No hacemos nada ya que estos datos ya los tenemos (o los ignoramos)
           }else if (w.equals("EDICIÓN:")){
               item.setEdition(getValue(linea));
           }else if (w.equals("INTERVENCIÓN:")){
               item.setIntervencion(getValue(linea));
           }else if (w.equals("DEFINICIÓN:")){
               item.setDefinicion(getValue(linea));
           }else if (w.equals("ESPECIALIDADES")){
               item.setEspecialidades(getValue(linea));
           }else if (w.equals("ACTIVIDADES")){ //Características y factores
               lista=1;
           }else if (w.equals("NANDAS")){ //Nandas relacionados
               lista=2;
           }else if (w.equals("NOCS")){ //Nocs relacionados
               lista=3;
           }else{ //En otro caso estamos ante un dato a guardar en una lista
               switch (lista){
                   case 1://Actividades
                        String caracteristica=linea;
                        String tipo="ACTIVIDAD";
                        SubItem sub=new SubItem(caracteristica,tipo);
                        item.addActividad(sub);
                       break;
                   case 2: //NANDAS relacionados
                        NandaItem nanda=new NandaItem(w,linea);
                        if (!StringUtil.isEmpty(linea)){
                           item.addNandasRelacionados(nanda);
                        }
                       break;
                   case 3: //NOCS relacionados
                        NocItem noc=new NocItem(w,linea);
                        if (!StringUtil.isEmpty(linea)){
                           item.addNocsRelacionados(noc);
                        }
                       break;
               }
           }

       }//while

       nicTable.put(item.getCode(), item); //Por último metemos en la tabla el NIC
       FileUtil.closeFile(is); //Cerramos el fichero
       return true;
   }

   private static boolean cargaDetalleNoc(IItem itemNoc){
       NocItem item=(NocItem)itemNoc;
       //Ruta del archivo que contiene el detalle del Noc
       String ruta=NandaDefinitions.PATH_NANDANICNOC+"nocs/"+item.getCode()+".txt";

       InputStream is=FileUtil.openFile(ruta,false);
       if (is==null) return false; //Si no se encontró el fichero, no hacemos nada
       Scanner fich=new Scanner(is);

       Dominio dom=null; //Dominio actual
       Clase clas=null; //Clase actual
       int lista=1; //Lista actual 1=IND; 2=NANDA; 3=NIC
       while (fich.hasNextLine()){
           //Localizacion
           String linea=fich.nextLine().trim(); //Leemos una línea del fichero con una palabra

           String word[]=linea.split(" "); //Troceamos la cadena
           String w=word[0];
           if (w.equals("NOC")||w.equals("CÓDIGO:")||w.equals("DOMINIO:")||w.equals("CLASE:")||
               w.equals("NOTAS:")){
               //No hacemos nada ya que estos datos ya los tenemos
           }else if (w.equals("EDICIÓN:")){
               item.setEdition(getValue(linea));
           }else if (w.equals("RESULTADO:")){
               item.setResultado(getValue(linea));
           }else if (w.equals("DEFINICIÓN:")){
               item.setDefinicion(getValue(linea));
           }else if (w.equals("ESPECIALIDADES")){
               item.setEspecialidades(getValue(linea));
           }else if (w.equals("INDICADORES")){ //Indicadores
               lista=1;
           }else if (w.equals("NANDAS")){ //Nandas relacionados
               lista=2;
           }else if (w.equals("NICS")){ //Nics relacionados
               lista=3;
           }else{ //En otro caso estamos ante un dato a guardar en una lista
               switch (lista){
                   case 1://Indicadores
                        int ini=(linea.contains("]")?linea.indexOf("]")+1:0);
                        int fin=(linea.contains("(")?linea.indexOf("(")-1:0);
                        String caracteristica=(fin==0?linea.substring(ini):linea.substring(ini,fin)).trim();
                        String tipo="INDICADOR";
                        SubItem sub=new SubItem(caracteristica,tipo);
                        item.addIndicador(sub);
                       break;
                   case 2: //Nandas relacionados
                        NandaItem nanda=new NandaItem(w,linea);
                        if (!StringUtil.isEmpty(linea)){
                           item.addNandasRelacionados(nanda);
                        }
                       break;
                   case 3: //Nics relacionados
                        NicItem nic=new NicItem(w,linea);
                        if (!StringUtil.isEmpty(linea)){
                           item.addNicsRelacionados(nic);
                        }
                       break;
               }
           }

       }//while

       nocTable.put(item.getCode(), item); //Por último metemos en la tabla el NOC
       FileUtil.closeFile(is); //Cerramos el fichero
       return true;
   }

   /**
    * Esta función carga el listado completo de Dominios, Clases y Nandas que pertenecen a esas clases.
    * No carga el detalle de cada Nanda, que debe ser cargado por separado, de un archivo independiente.
    * Cada nanda se almacena en un archivo del tipo "[000123].txt".
    */
   private static void cargaItems(String nombreArchivo, ArrayList<Dominio> dominiosList){

       InputStream is=FileUtil.openFile(NandaDefinitions.PATH_NANDANICNOC+nombreArchivo);
       if (is==null) return; //Si no se encontró el fichero, no hacemos nada
       Scanner fich=new Scanner(is);

       Dominio dom=null; //Dominio actual
       Clase clas=null; //Clase actual
       while (fich.hasNextLine()){
           //Localizacion
           String linea=fich.nextLine().trim(); //Leemos una línea del fichero con una palabra

           String word[]=linea.split(" "); //Troceamos la cadena
           String w=word[0]; //Primera palabra
           if (w.equals("DOMINIO")){
               dom=new Dominio(linea); //Creamos un nuevo dominio
               dominiosList.add(dom); //Lo añadimos a la lista
           }else if (w.equals("CLASE")){
               clas=new Clase(linea);
               dom.addClase(clas); //añadimos la clase al dominio actual
               String code=word[1].substring(0,word[1].length()-1); //Obtenemos el código de la Clase
               clas.setCode(code);
           }else if (w.equals("DESCRIPCION:")){
               clas.setDescription(getValue(linea));
           }else{ //En otro caso estamos ante un Item, p.e. el Item Nanda "[00097] Disminución de la implicación en actividades recreativas"
               IItem item=null;
               String code=word[0]; //Código del estilo [00097]
               String description=linea.substring(linea.indexOf("]")+1,linea.length()).trim(); //texto, pe "Tendencia a adoptar conductas de riesgo para la salud"
               boolean existeDetalle=false;
               if (nombreArchivo.equals(NandaDefinitions.PATH_NANDAS)){
                    item=new NandaItem(code, description);
                    existeDetalle=cargaDetalleNanda(item); //Carga el detalle si existe el fichero de detalle (pe "[00097].txt")
               }else if (nombreArchivo.equals(NandaDefinitions.PATH_NICS)){
                    item=new NicItem(code, description);
                    existeDetalle=cargaDetalleNic(item);  //Carga el detalle si existe el fichero de detalle (pe "[00132].txt")
               }else if (nombreArchivo.equals(NandaDefinitions.PATH_NOCS)){
                    item=new NocItem(code, description);
                    existeDetalle=cargaDetalleNoc(item);  //Carga el detalle si existe el fichero de detalle (pe "[00153].txt")
               }
               item.setDominio(dom.getName());
               item.setClase(clas.getName());
               clas.addItem(item); //Añadimos el item exista o no en disco. Si no existe será un Item vacío (item.hasDetail()==false)

           }//else

       }//while
       FileUtil.closeFile(is); //Cerramos el fichero
    }//Carga desde fichero


    /**
     * Carga el estándar Nanda-Nic-Noc en memoria.
     */
    public static void inicializaNandaNicNoc(){

        // Cargamos el estándar en tres partes: NANDA, NIC y NOC.
        cargaItems(PATH_NANDAS,dominiosNanda);
        cargaItems(PATH_NICS,dominiosNic);
        cargaItems(PATH_NOCS,dominiosNoc);

    }

    /**
     * @return Devuelve los dominios NANDA
     */
    public static ArrayList<Dominio> getDominiosNanda() {
        return dominiosNanda;
    }

    /**
     * @param domsNanda Establece los dominios NANDA
     */
    public static void setDominiosNanda(ArrayList<Dominio> domsNanda) {
        dominiosNanda = domsNanda;
    }

    /**
     * @return Devuelve los dominios NIC
     */
    public static ArrayList<Dominio> getDominiosNic() {
        return dominiosNic;
    }

    /**
     * @param domsNic Establece los dominios NIC
     */
    public static void setDominiosNic(ArrayList<Dominio> domsNic) {
        dominiosNic = domsNic;
    }

    /**
     * @return Devuelve los dominios NOC
     */
    public static ArrayList<Dominio> getDominiosNoc() {
        return dominiosNoc;
    }

    /**
     * @param domsNoc Establece los dominios NOC
     */
    public static void setDominiosNoc(ArrayList<Dominio> domsNoc) {
        dominiosNoc = domsNoc;
    }

    /**
     * Obtiene una cadena de texto con todos los Items (Nanda, Nic, Noc) y su organización en clases y dominios.
     * @return Cadena de texto con todos los Items.
     */
    public static String getItemStringTree(ArrayList<Dominio> dominios){
        String result="";
        for (Dominio d:dominios){
            result+=d.getName()+"\n";
            for (Clase cl:d.getClases()){
              result+="    "+cl.getName()+"\n";
              for (IItem item:cl.getItemList()){
                  String detail=(item.hasDetail()?"[OK]":"[--]");
                  result+="        "+detail+item.getCode()+" "+item.getDescription()+"\n";
              }//for
            }//for
        }//for
        return result;
    }

    /**
     * Obtiene una cadena de texto con todos los NANDA y su organización en clases y dominios.
     * @return Cadena de texto con todos los NANDAs.
     */
    public static String getNandasString(){
        String result=getItemStringTree(dominiosNanda);
        return result;
    }

    /**
     * Obtiene una cadena de texto con todos los NIC y su organización en clases y dominios.
     * @return Cadena de texto con todos los NICs.
     */
    public static String getNicsString(){
        String result=getItemStringTree(dominiosNic);
        return result;
    }

    /**
     * Obtiene una cadena de texto con todos los NOC y su organización en clases y dominios.
     * @return Cadena de texto con todos los NOCs.
     */
    public static String getNocsString(){
        String result=getItemStringTree(dominiosNoc);
        return result;
    }

    public static String getNandaString(String code){
        IItem item=(IItem)nandaTable.get(code);
        String result=StringUtil.toString(item);
        return result;
    }

    public static String getNicString(String code){
        IItem item=(IItem)nicTable.get(code);
        String result=StringUtil.toString(item);
        return result;
    }

    public static String getNocString(String code){
        IItem item=(IItem)nocTable.get(code);
        String result=StringUtil.toString(item);
        return result;
    }


}//class
