package es.sdweb.memorycorp.nandanicnoc;

/**
 *
 * @author Antonio Carro Mariño
 */
public interface IItem {
    
    public String getCode();
    public void setCode(String code);
    
    public String getDescription();
    public void setDescription(String description);
    
    public String getDominio();
    public void setDominio(String dominio);
    
    public String getClase();
    public void setClase(String clase);
    
    public boolean hasDetail();
    
    @Override
    public String toString();
    
}//Interface
