package es.sdweb.application.model.dto;

import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author Antonio Carro Mariño
 *
 */
public class ElementoDTOList {

    private List elementoDTOs;
    private boolean existMoreElementoDTO;

    public ElementoDTOList(List elementoDTOs, boolean existMoreElementoDTO) {
        super();
        // TODO Auto-generated constructor stub
        this.elementoDTOs = elementoDTOs;
        this.existMoreElementoDTO = existMoreElementoDTO;
    }

    public List getElementoDTOs() {
        return elementoDTOs;
    }

    public boolean isExistMoreElementoDTO() {
        return existMoreElementoDTO;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public int compareTo(Object remoteObject) {
        return CompareToBuilder.reflectionCompare(this, remoteObject);
    }

    @Override
    public boolean equals(Object remoteObject) {
        return EqualsBuilder.reflectionEquals(this, remoteObject);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
