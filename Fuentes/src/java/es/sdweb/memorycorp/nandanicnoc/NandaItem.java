package es.sdweb.memorycorp.nandanicnoc;

import java.util.ArrayList;

/**
 * Item (pe "NANDA [00031]: Limpieza ineficaz de las vías aéreas"),
 * perteneciente a una clase (pe "Clase 2: Lesión física"), perteneciente a un dominio de NANDA (pe "Dominio 11: Seguridad/Protección")
 * @author Antonio Carro Mariño
 */
public class NandaItem implements IItem{
    
    private String code="";
    private String edition="";
    private String dominio="";
    private String clase="";
    private String description="";
    private String diagnostico="";
    private String definicion="";
    private String necesidad="";
    private String patron="";
    
    private ArrayList<NocItem> nocsRelacionados=new ArrayList(); //De NocItem
    private ArrayList<NicItem> nicsRelacionados=new ArrayList(); //De NicItem
    
    private ArrayList<SubItem> caracteristicas=new ArrayList(); //De SubItem
    
    public NandaItem(String code, String description){
        this.code=code;
        this.description=description;
    }
    
    /**
     * Indica si este componente NANDA está detallado (es decir, se cargó su detalle desde un fichero específico).
     * Si es así, tendrá toda la información disponible, en otro caso, solo se dispondrá de la información básica.
     * @return True si se dispone de toda la información de este NANDA. False en caso contrario.
     */
    @Override
    public boolean hasDetail(){
        boolean result=!this.caracteristicas.isEmpty();
        return result;
    }
    
    /**
     * @return the code
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the diagnostico
     */
    public String getDiagnostico() {
        return diagnostico;
    }

    /**
     * @param diagnostico the diagnostico to set
     */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    /**
     * @return the definicion
     */
    public String getDefinicion() {
        return definicion;
    }

    /**
     * @param definicion the definicion to set
     */
    public void setDefinicion(String definicion) {
        this.definicion = definicion;
    }

    /**
     * @return the necesidad
     */
    public String getNecesidad() {
        return necesidad;
    }

    /**
     * @param necesidad the necesidad to set
     */
    public void setNecesidad(String necesidad) {
        this.necesidad = necesidad;
    }

    /**
     * @return the patron
     */
    public String getPatron() {
        return patron;
    }

    /**
     * @param patron the patron to set
     */
    public void setPatron(String patron) {
        this.patron = patron;
    }

    /**
     * @return the nocsRelacionados
     */
    public ArrayList<NocItem> getNocsRelacionados() {
        return nocsRelacionados;
    }

    /**
     * @param nocsRelacionados the nocsRelacionados to set
     */
    public void setNocsRelacionados(ArrayList<NocItem> nocsRelacionados) {
        this.nocsRelacionados = nocsRelacionados;
    }
    
    public void addNocsRelacionados(NocItem noc){
        this.nocsRelacionados.add(noc);
    }

    /**
     * @return the nicsRelacionados
     */
    public ArrayList<NicItem> getNicsRelacionados() {
        return nicsRelacionados;
    }

    /**
     * @param nicsRelacionados the nicsRelacionados to set
     */
    public void setNicsRelacionados(ArrayList<NicItem> nicsRelacionados) {
        this.nicsRelacionados = nicsRelacionados;
    }
    
    public void addNicsRelacionados(NicItem nic){
        this.nicsRelacionados.add(nic);
    }

    /**
     * @return the caracteristicas
     */
    public ArrayList<SubItem> getCaracteristicas() {
        return caracteristicas;
    }

    /**
     * @param caracteristicas the caracteristicas to set
     */
    public void setCaracteristicas(ArrayList<SubItem> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
    
    public void addCaracteristica(SubItem subItem){
        this.caracteristicas.add(subItem);
    }

    /**
     * @return the dominio
     */
    public String getDominio() {
        return dominio;
    }

    /**
     * @param dominio the dominio to set
     */
    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    /**
     * @return the clase
     */
    public String getClase() {
        return clase;
    }

    /**
     * @param clase the clase to set
     */
    public void setClase(String clase) {
        this.clase = clase;
    }

    /**
     * @return the edition
     */
    public String getEdition() {
        return edition;
    }

    /**
     * @param edition the edition to set
     */
    public void setEdition(String edition) {
        this.edition = edition;
    }
    
    
    @Override
    public String toString(){
        String result=this.getCode()+" "+this.getDescription()+"\n";
        result+="CÓDIGO: "+this.getCode()+"\n";
        result+="EDICIÓN: "+this.getEdition()+"\n";
        result+="DIAGNÓSTICO: "+this.getDiagnostico()+"\n";
        result+="DEFINICIÓN: "+this.getDefinicion()+"\n";
        result+="DOMINIO: "+this.getDominio()+"\n";
        result+="CLASE: "+this.getClase()+"\n";
        result+="NECESIDAD: "+this.getNecesidad()+"\n";
        result+="PATRÓN: "+this.getPatron()+"\n";
        result+="CARACTERÍSTICAS Y FACTORES\n";
        
        for (SubItem sub:this.caracteristicas){
            result+="   "+sub.getDescription()+"\n";
        }//for
        
        result+="NICS RELACIONADOS\n";
        
        for (NicItem nic:this.nicsRelacionados){
            result+="   "+nic.getCode()+" "+nic.getDescription()+"\n";
        }//for

        result+="NOCS RELACIONADOS\n";
        
        for (NocItem noc:this.nocsRelacionados){
            result+="   "+noc.getCode()+" "+noc.getDescription()+"\n";
        }//for
        
        return result;
    }
    
}//Class
