package es.sdweb.memorycorp.nandanicnoc;

import es.sdweb.application.componentes.util.StringUtil;
import java.util.ArrayList;

/**
 * Esta clase contiene el resultado del procesamiento y clasificación de un texto de enfermería.
 * @author Antonio Carro Mariño
 */
public class TextResult {
    private ArrayList<ResultItem> nanda=new ArrayList();
    private ArrayList<ResultItem> nic=new ArrayList();
    private ArrayList<ResultItem> noc=new ArrayList();
    
    @Override
    public String toString(){
        String result="NANDA:\n";
        for (ResultItem item:nanda){
            result+=item.getItem().getCode()+" "+item.getItem().getDescription()+"\n";
            for (ResultSubItem sub:item.getSubItemList()){
                String tipo=sub.getSubItem().getTipo();
                result+=(StringUtil.isEmpty(tipo)?"":"   *"+sub.getSubItem().getTipo()+":\n");
                result+="      ->"+sub.getSubItem().getDescription()+": "+sub.getOracion()+"\n";
            }//for
        }//for
        
        result+="\n----------------------------------------------------------------------------\n";
        result+="NIC:\n";
        for (ResultItem item:nic){
            result+=item.getItem().getCode()+" "+item.getItem().getDescription()+"\n";
            for (ResultSubItem sub:item.getSubItemList()){
                String tipo=sub.getSubItem().getTipo();
                result+=(StringUtil.isEmpty(tipo)?"":"   *"+sub.getSubItem().getTipo()+":\n");
                result+="      ->"+sub.getSubItem().getDescription()+": "+sub.getOracion()+"\n";
            }//for
        }//for
        
        result+="\n----------------------------------------------------------------------------\n";
        result+="NOC:\n";
        for (ResultItem item:noc){
            result+=item.getItem().getCode()+" "+item.getItem().getDescription()+"\n";
            for (ResultSubItem sub:item.getSubItemList()){
                String tipo=sub.getSubItem().getTipo();
                result+=(StringUtil.isEmpty(tipo)?"":"   *"+sub.getSubItem().getTipo()+":\n");
                result+="      ->"+sub.getSubItem().getDescription()+": "+sub.getOracion()+"\n";
            }//for
        }//for

        return result;
    }

    /**
     * @return the nanda
     */
    public ArrayList<ResultItem> getNanda() {
        return nanda;
    }
    
    /**
     * Añade un elemento, si no existe, a la lista de resultados NANDA, NIC o NOC.
     * @param item Item (NANDA, NIC o NOC) a añadir a la lista.
     * @param lista Lista a la que se añadirá.
     */
    private void addResultItem(ResultItem item, ArrayList<ResultItem> lista){
        boolean hallado=false;
        for (ResultItem ri:lista){ //Vemos si está presente
            if (ri.getItem().getCode().equals(item.getItem().getCode())){
                hallado=true;
            }
        }//for
        if (!hallado){ //Si no está presente, lo añadimos
           lista.add(item); 
        }
    }
    
    /**
     * 
     * @param nanda 
     */
    public void addNanda(ResultItem nanda){
        addResultItem(nanda,this.nanda);
    }
    
    public void addNic(ResultItem nic){
       addResultItem(nic,this.nic);
    }
    
    public void addNoc(ResultItem noc){
        addResultItem(noc, this.noc);
    }
    

    /**
     * @param nanda the nanda to set
     */
    public void setNanda(ArrayList<ResultItem> nanda) {
        this.nanda = nanda;
    }

    /**
     * @return the nic
     */
    public ArrayList<ResultItem> getNic() {
        return nic;
    }

    /**
     * @param nic the nic to set
     */
    public void setNic(ArrayList<ResultItem> nic) {
        this.nic = nic;
    }

    /**
     * @return the noc
     */
    public ArrayList<ResultItem> getNoc() {
        return noc;
    }

    /**
     * @param noc the noc to set
     */
    public void setNoc(ArrayList<ResultItem> noc) {
        this.noc = noc;
    }
    
    
}//class
