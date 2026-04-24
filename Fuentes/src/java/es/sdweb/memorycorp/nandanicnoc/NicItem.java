package es.sdweb.memorycorp.nandanicnoc;

/**
 *
 * @author Antonio Carro Mariño
 */
import java.util.ArrayList;

/**
 * Item (pe "NIC [1630]: Vestir"),
 * perteneciente a una clase (pe "Clase F: Facilitación del autocuidado"), perteneciente a un dominio de NIC (pe "Dominio 1: Fisiológico: Básico")
 * @author Antonio Carro Mariño
 */
public class NicItem implements IItem{
    
    private String code=""; //Código
    private String edition="";
    private String dominio="";
    private String clase="";
    private String description=""; //Nombre del NIC
    private String intervencion="";
    private String definicion="";
    private String especialidades="";
    
    private ArrayList<NandaItem> nandasRelacionados=new ArrayList(); //De NandaItem
    private ArrayList<NocItem> nocsRelacionados=new ArrayList(); //De NocItem
    
    private ArrayList<SubItem> actividades=new ArrayList(); //De SubItem
    
    public NicItem(String code, String description){
        this.code=code;
        this.description=description;
    }
    
    
    /**
     * Indica si este componente NIC está detallado (es decir, se cargó su detalle desde un fichero específico).
     * Si es así, tendrá toda la información disponible, en otro caso, solo se dispondrá de la información básica.
     * @return True si se dispone de toda la información de este NIC. False en caso contrario.
     */
    @Override
    public boolean hasDetail(){
        boolean result=!this.actividades.isEmpty();
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
     * @return the nandasRelacionados
     */
    public ArrayList<NandaItem> getNandasRelacionados() {
        return nandasRelacionados;
    }

    /**
     * @param nandasRelacionados the nandasRelacionados to set
     */
    public void setNandasRelacionados(ArrayList<NandaItem> nandasRelacionados) {
        this.nandasRelacionados = nandasRelacionados;
    }
    
    public void addNandasRelacionados(NandaItem nanda){
        this.nandasRelacionados.add(nanda);
    }

    public void addActividad(SubItem subItem){
        this.getActividades().add(subItem);
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

    /**
     * @return the especialidades
     */
    public String getEspecialidades() {
        return especialidades;
    }

    /**
     * @param especialidades the especialidades to set
     */
    public void setEspecialidades(String especialidades) {
        this.especialidades = especialidades;
    }

    /**
     * @return the actividades
     */
    public ArrayList<SubItem> getActividades() {
        return actividades;
    }

    /**
     * @param actividades the actividades to set
     */
    public void setActividades(ArrayList<SubItem> actividades) {
        this.actividades = actividades;
    }

    /**
     * @return the intervencion
     */
    public String getIntervencion() {
        return intervencion;
    }

    /**
     * @param intervencion the intervencion to set
     */
    public void setIntervencion(String intervencion) {
        this.intervencion = intervencion;
    }

    @Override
    public String toString(){
        String result=this.getCode()+" "+this.getDescription()+"\n";
        result+="CÓDIGO: "+this.getCode()+"\n";
        result+="EDICIÓN: "+this.getEdition()+"\n";
        result+="INTERVENCIÓN: "+this.getIntervencion()+"\n";
        result+="DEFINICIÓN: "+this.getDefinicion()+"\n";
        result+="DOMINIO: "+this.getDominio()+"\n";
        result+="CLASE: "+this.getClase()+"\n";
        result+="ESPECIALIDADES DE ENFERMERÍA: "+this.getEspecialidades()+"\n";
        result+="ACTIVIDADES\n";
        
        for (SubItem sub:this.actividades){
            result+="   "+sub.getDescription()+"\n";
        }//for
        
        result+="NANDAS RELACIONADOS\n";
        
        for (NandaItem item:this.nandasRelacionados){
            result+="   "+item.getCode()+" "+item.getDescription()+"\n";
        }//for

        result+="NOCS RELACIONADOS\n";
        
        for (NocItem noc:this.nocsRelacionados){
            result+="   "+noc.getCode()+" "+noc.getDescription()+"\n";
        }//for
        
        return result;
    }
    
}//Class
