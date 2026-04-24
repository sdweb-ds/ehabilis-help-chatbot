package es.sdweb.memorycorp.nandanicnoc;

import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.memorycorp.core.LearningEngine;
import es.sdweb.memorycorp.nlpengine.DVerb;
import es.sdweb.memorycorp.nlpengine.LOracion;
import es.sdweb.memorycorp.nlpengine.LParrafo;
import es.sdweb.memorycorp.nlpengine.NLPEngine;
import es.sdweb.memorycorp.nlpengine.NLPUtil;
import es.sdweb.memorycorp.nlpengine.SpanishDictionary;
import java.util.ArrayList;

/**
 * Esta clase dispone de las funciones para realizar un procesamiento semántico del texto de entrada de enfermería.
 * @author Antonio Carro Mariño
 */
public class TextAnalizer {
    
    private static final String NANDA="NANDA";
    private static final String NIC="NIC";
    private static final String NOC="NOC";
    
    private static final double UMBRAL=0.51; //Umbral de coincidencia aceptable de un NANDA/NIC/NOC respecto a una oración de entrada

    /**
     * En base a heurísticas, se establece el tipo de oración que es: NANDA, NIC, y/o NOC.
     * Puede pertenecer a dos tipos, por ejemplo: NIC y NOC. Pues la horación puede realizar indicaciones en ambos ámbitos.
     * @param or Oración a analizar.
     * @return Tipos a los que pertenece.
     */
    private static ArrayList<String> getTipoOracion(LOracion or){
        ArrayList<String> result=new ArrayList(); //Una frase puede implicar actuaciones de varios tipos.
        
        //Si no hay verbo => es un diagnóstico NANDA.
        if (or.getAccion()==null){
          result.add(NANDA);
          return result;
        }
        
        //** El análisis del verbo nos dará una indicación de si es un diagnóstico (NANDA), una intervención (NIC),
        //** o un resultado (NOC).
        String verbConj=or.getAccion().getTexto(); //Analizamos el verbo
        DVerb verb=SpanishDictionary.getDictionary().getVerbInfinitivo(verbConj); //Obtenemos su infinitivo
        String verbText=verb.getText();
        
        //** Diagnósticos
        if (verbText.equals("ser")||verbText.equals("tener")||verbText.equals("estar")||verbText.equals("presentar")||verbText.equals("mostrar")||
            verbText.equals("evidenciar")||verbText.equals("ir")||verbText.equals("consumir")||verbText.equals("desarrollar")||verbText.equals("referir")){
            result.add(NANDA);
        }
        
        //** Actividades
        if (verbText.equals("manejar")||verbText.equals("realizar")||verbText.equals("tomar")||verbText.equals("analizar")||
            verbText.equals("medir")||verbText.equals("estudiar")||verbText.equals("administrar")||verbText.equals("suministrar")||
            verbText.equals("dar")||verbText.equals("proporcionar")||verbText.equals("pautar")||verbText.equals("identificar")||
            verbText.equals("observar")||verbText.equals("vestir")||verbText.equals("fomentar")||verbText.equals("lavar")||
            verbText.equals("limpiar")||verbText.equals("usar")||verbText.equals("cambiar")||verbText.equals("elegir")||
            verbText.equals("responsabilizar")){
            result.add(NIC);
        }

        //** Indicadores
        if (verbText.equals("tomar")||verbText.equals("medir")||verbText.equals("indicar")||
            verbText.equals("dar")||verbText.equals("proporcionar")||verbText.equals("pautar")){
            result.add(NOC);
        }
        
        return result;
    }
    
    
    /**
     * Procesa un texto de entrada del dominio de enfermería y extrae los NANDA, NIC y NOC de su interior,
     * identificando qué frase pertenece a cada tipo, y a qué Item específico NANDA, NIC o NOC hace referencia.
     * @param inputText Texto de entrada del ámbito de enfermería que debe procesarse para extraer los NANDA, NIC y NOC.
     * @param learningEngine LearningEngine que se está empleando.
     * @return Texto procesado.
     */
    public static TextResult processText(String inputText, LearningEngine learningEngine){
        TextResult result=new TextResult();
        
        // this is the parse tree of the current sentence
        NLPEngine engineNLP=learningEngine.getEngine();
        LParrafo par=engineNLP.addParrafo(inputText);
                
        for (LOracion or:par.getOraciones()){ //Procesamos oración a oración
            LogUtil.logTraza("*Procesando: "+or.getOracion(),7);
            ArrayList tipos=getTipoOracion(or); //Oracion que contiene info de tipo: NANDA, NIC, NOC
            ArrayList<String> keyWordsDest=new ArrayList();
            NLPUtil.getKeyWords(or.getEstructura(), keyWordsDest); //obtenemos las key words de la oración.
            if (tipos.contains(NANDA)){ //Recorremos los NANDA para buscar coincidencias
                for (Dominio dom:NandaDefinitions.getDominiosNanda()){
                    for (Clase clase:dom.getClases()){
                        for (IItem itemNanda:clase.getItemList()){ //Para cada NANDA. Ojo, en distintos dominios puede haber nandas repetidos
                           NandaItem item=(NandaItem)itemNanda;
                           if (item.hasDetail()){ //Si el Item ha podido ser cargado lo analizamos (en otro caso no, porque está vacío)
                             ResultItem rItem=null;
                             for (SubItem sub:item.getCaracteristicas()){ //Revisamos cada una de las características del NANDA
                                String desc=sub.getDescription();
                                LParrafo parNanda=engineNLP.addParrafo(desc); //Analizamos el texto de la característica
                                LOracion orNanda=parNanda.getOraciones().get(0); //Cogemos la primera oración
                                ArrayList<String> keyWordsOr=new ArrayList();
                                NLPUtil.getKeyWords(orNanda.getEstructura(), keyWordsOr); //obtenemos las keyWords de la característica

                                //** Analizamos la coincidencia del NANDA con la oración leída del input
                                double ratio=NLPUtil.getCoincidenceRatio(keyWordsOr, keyWordsDest, SpanishDictionary.getDictionary());

                                if (ratio>UMBRAL){ //Si el ratio de coincidencia es significativo
                                    ResultSubItem rSubItem=new ResultSubItem(sub,or.getOracion()); //Guardaremos en el resultado esta oración y subItem asociados
                                    if (rItem==null){ //Si no tenemos ResultItem, lo creamos recogiendo el Item actual
                                       rItem=new ResultItem(item);
                                    }
                                    rItem.addResultSubItem(rSubItem); //Y añadimos el subItem cuya presencia se ha detectado
                                }
                             }//for subitem (caracteristicas, indicadores, etc)
                           
                             if (rItem!=null){ //Si se ha detectado la presencia de algún subItem de este Item
                               result.addNanda(rItem); //Añadimos el Item NANDA
                             }    
                           }//Si Item has detail
                        }//for Item NANDA
                    }//for
                }//for
              
            } //If tipo contiene NANDA
            
            if (tipos.contains(NIC)){ //Recorremos los NIC para buscar coincidencias
                for (Dominio dom:NandaDefinitions.getDominiosNic()){
                    for (Clase clase:dom.getClases()){
                        for (IItem itemNic:clase.getItemList()){ //Para cada NIC
                           NicItem item=(NicItem)itemNic;
                           if (itemNic.hasDetail()){ //Si el Item ha podido ser cargado lo analizamos (en otro caso no, porque está vacío)
                             ResultItem rItem=null;
                             for (SubItem sub:item.getActividades()){ //Revisamos cada una de las actividades del NIC
                                String desc=sub.getDescription();
                                LParrafo parNic=engineNLP.addParrafo(desc); //Analizamos el texto de la característica
                                LOracion orNanda=parNic.getOraciones().get(0); //Cogemos la primera oración
                                ArrayList<String> keyWordsOr=new ArrayList();
                                NLPUtil.getKeyWords(orNanda.getEstructura(), keyWordsOr); //obtenemos las keyWords de la característica

                                //** Analizamos la coincidencia del NIC con la oración leída del input
                                double ratio=NLPUtil.getCoincidenceRatio(keyWordsOr, keyWordsDest, SpanishDictionary.getDictionary());

                                if (ratio>(UMBRAL-0.1)){ //Si el ratio de coincidencia es significativo
                                    ResultSubItem rSubItem=new ResultSubItem(sub,or.getOracion()); //Guardaremos en el resultado esta oración y subItem asociados
                                    if (rItem==null){ //Si no tenemos ResultItem, lo creamos recogiendo el Item actual
                                       rItem=new ResultItem(item);
                                    }
                                    rItem.addResultSubItem(rSubItem); //Y añadimos el subItem cuya presencia se ha detectado
                                }
                             }//for subitem (caracteristicas, indicadores, etc)
                           
                             if (rItem!=null){ //Si se ha detectado la presencia de algún subItem de este Item
                               result.addNic(rItem); //Añadimos el Item NANDA
                             }    
                           }//If Item has detail
                        }//for Item NIC
                    }//for
                }//for
              
            } //IF tipo contiene NIC
            
            if (tipos.contains(NOC)){ //Recorremos los NOC para buscar coincidencias
                for (Dominio dom:NandaDefinitions.getDominiosNoc()){
                    for (Clase clase:dom.getClases()){
                        for (IItem itemNoc:clase.getItemList()){ //Para cada NOC
                           NocItem item=(NocItem)itemNoc;
                           if (itemNoc.hasDetail()){ //Si el Item ha podido ser cargado lo analizamos (en otro caso no, porque está vacío)
                             ResultItem rItem=null;
                             for (SubItem sub:item.getIndicadores()){ //Revisamos cada una de los indicadores del NOC
                                String desc=sub.getDescription();
                                LParrafo parNanda=engineNLP.addParrafo(desc); //Analizamos el texto de la característica
                                LOracion orNanda=parNanda.getOraciones().get(0); //Cogemos la primera oración
                                ArrayList<String> keyWordsOr=new ArrayList();
                                NLPUtil.getKeyWords(orNanda.getEstructura(), keyWordsOr); //obtenemos las keyWords de la característica

                                //** Analizamos la coincidencia del NANDA con la oración leída del input
                                double ratio=NLPUtil.getCoincidenceRatio(keyWordsOr, keyWordsDest, SpanishDictionary.getDictionary());

                                if (ratio>(UMBRAL-0.25)){ //Si el ratio de coincidencia es significativo
                                    ResultSubItem rSubItem=new ResultSubItem(sub,or.getOracion()); //Guardaremos en el resultado esta oración y subItem asociados
                                    if (rItem==null){ //Si no tenemos ResultItem, lo creamos recogiendo el Item actual
                                       rItem=new ResultItem(item);
                                    }
                                    rItem.addResultSubItem(rSubItem); //Y añadimos el subItem cuya presencia se ha detectado
                                }
                             }//for subitem (caracteristicas, indicadores, etc)
                           
                             if (rItem!=null){ //Si se ha detectado la presencia de algún subItem de este Item
                               result.addNoc(rItem); //Añadimos el Item NOC
                             }    
                           }//If Item has detail
                        }//for Item NOC
                    }//for
                }//for
              
            }//If tipo contine NOC
  
        }//for
        
        return result;
    }
    
}//class
