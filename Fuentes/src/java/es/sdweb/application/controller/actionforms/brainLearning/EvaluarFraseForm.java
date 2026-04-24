package es.sdweb.application.controller.actionforms.brainLearning;


import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.struts.action.ActionForm;


/**
 *
 * @author Antonio Carro Mariño
 *
 */
public class EvaluarFraseForm extends ActionForm {

  private String textoEntrada;

  private String textoSalida;

  private String advertencia;


  public String getAdvertencia() {
    return advertencia;
  }
  public void setAdvertencia(String advertencia) {
    this.advertencia = advertencia;
  }

  public String getTextoEntrada() {
    return textoEntrada;
  }
  public void setTextoEntrada(String textoEntrada) {
    this.textoEntrada = textoEntrada;
  }

  public String getTextoSalida() {
    return textoSalida;
  }
  public void setTextoSalida(String textoSalida) {
    this.textoSalida = textoSalida;
  }


  @Override
public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

  public int compareTo(Object remoteObject){
    return CompareToBuilder.reflectionCompare(this,remoteObject);
  }

  @Override
public boolean equals(Object remoteObject){
    return EqualsBuilder.reflectionEquals(this, remoteObject);
  }

  @Override
public int hashCode(){
    return HashCodeBuilder.reflectionHashCode(this);
  }

}
