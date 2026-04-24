package es.sdweb.memorycorp.core;

import es.sdweb.memorycorp.nlpengine.DWord;

/**
 *
 * @author Antonio Carro Mariño
 */
public class RealityNode extends Node{
    
    private String pos=""; //Part of Speech: ADJ, ADV, ADP, ... Clasificacion lingüística del elemento.
    private String ud=""; //Universal Dependencies: ACL, ADVCL, ADVMOD, ... Clasificacion lingüística especializada del elemento.
    private String referencia=""; //Texto adicional al que se vincula este termino. P.e. un modificador como la conjuncion "y", "pero", etc.
    
    public RealityNode(DWord word, String tipo){
        super(word,tipo);
    }
    
}//class 
