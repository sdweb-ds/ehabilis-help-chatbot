package es.sdweb.memorycorp.nandanicnoc;

/**
 *
 * @author Antonio Carro Mariño
 */
import java.util.ArrayList;

/**
 * Item (pe "NOC [0504]: Función renal"),
 * perteneciente a una clase (pe "Clase 7: Eliminación"), perteneciente a un dominio de NOC (pe "Dominio 2: Salud fisiológica")
 * @author Antonio Carro Mariño
 */
public class NocItem implements IItem{
    
    private String code="";
    private String edition="";
    private String dominio="";
    private String clase="";
    private String description=""; //El nombre
    private String definicion=""; //La definición extensa
    private String resultado="";
    private String especialidades="";
    
    private ArrayList<NandaItem> nandasRelacionados=new ArrayList(); //De NandaItem
    private ArrayList<NicItem> nicsRelacionados=new ArrayList(); //De NicItem
    
    private ArrayList<SubItem> indicadores=new ArrayList(); //De SubItem
    
    public NocItem(String code, String description){
        this.code=code;
        this.description=description;
    }
    
    
    /**
     * Indica si este componente NOC está detallado (es decir, se cargó su detalle desde un fichero específico).
     * Si es así, tendrá toda la información disponible, en otro caso, solo se dispondrá de la información básica.
     * @return True si se dispone de toda la información de este NOC. False en caso contrario.
     */
    @Override
    public boolean hasDetail(){
        boolean result=!this.indicadores.isEmpty();
        return result;
    }
    
    /**
     * @return the code
     */
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

    public void addIndicador(SubItem subItem){
        this.getIndicadores().add(subItem);
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
     * @return the resultado
     */
    public String getResultado() {
        return resultado;
    }

    /**
     * @param resultado the resultado to set
     */
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    /**
     * @return the indicadores
     */
    public ArrayList<SubItem> getIndicadores() {
        return indicadores;
    }

    /**
     * @param indicadores the indicadores to set
     */
    public void setIndicadores(ArrayList<SubItem> indicadores) {
        this.indicadores = indicadores;
    }

    @Override
    public String toString(){
        String result=this.getCode()+" "+this.getDescription()+"\n";
        result+="CÓDIGO: "+this.getCode()+"\n";
        result+="EDICIÓN: "+this.getEdition()+"\n";
        result+="RESULTADO: "+this.getResultado()+"\n";
        result+="DEFINICIÓN: "+this.getDefinicion()+"\n";
        result+="DOMINIO: "+this.getDominio()+"\n";
        result+="CLASE: "+this.getClase()+"\n";
        result+="ESPECIALIDADES DE ENFERMERÍA: "+this.getEspecialidades()+"\n";
        result+="INDICADORES\n";
        
        for (SubItem sub:this.indicadores){
            result+="   "+sub.getDescription()+"\n";
        }//for
        
        result+="NANDAS RELACIONADOS\n";
        
        for (NandaItem item:this.nandasRelacionados){
            result+="   "+item.getCode()+" "+item.getDescription()+"\n";
        }//for

        result+="NICS RELACIONADOS\n";
        
        for (NicItem nic:this.nicsRelacionados){
            result+="   "+nic.getCode()+" "+nic.getDescription()+"\n";
        }//for
        
        return result;
    }
        

}//Class
