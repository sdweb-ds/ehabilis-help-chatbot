package es.sdweb.memorycorp.core;

/**
 * Define los cuantificadores disponibles.
 * @author Antonio Carro Mariño
 */
public class Quantifier {
    
    //Cuantificadores lógicos (de primer orden)
    public static final String QUANT_UNIVERSAL="QUANT_UNIVERSAL"; //Para Todo "X"
    public static final int[] QUANT_UNIVERSAL_RANGE={100,100}; // 100>=X>=100
    public static final String QUANT_EXISTENCIAL="QUANT_EXISTENCIAL"; //Existe al menos un "X"
    public static final int[] QUANT_EXISTENCIAL_RANGE={0,0}; // 0>=X>=0
    public static final String QUANT_DISCRETO="QUANT_DISCRETO"; //Permite indicar una cantidad concreta de "X"
    public static final int[] QUANT_DISCRETO_RANGE={1,1}; // 1>=X>=1 Por defecto la cantidad es UNO (salvo que se diga otra cosa)

    //Cuantificadores difusos (no incluyen el ES (=100%) y el NO ES (=0%), pues ya están presentes en cada Link (Acción), en le flag "Negado".
    //Son utilizables tanto para graduar la intensidad de un ADJ, como para el conteo aproximado de objetos (si se sabe el tamaño del conjunto)
    public static final String QUANT_MUCHOS="QUANT_MUCHOS"; //Bastantes, muchos, un buen número, muchísimos
    public static final int[] QUANT_MUCHOS_RANGE={99,65}; // 99>=X>=65
    public static final String QUANT_MODERADAMENTE="QUANT_MODERADAMENTE"; //Algo, Algunos, Moderadamente
    public static final int[] QUANT_MODERADAMENTE_RANGE={64,35}; //64>=X>=35
    public static final String QUANT_POCOS="QUANT_POCOS"; //Pocos, muy pocos, algunos, al menos uno
    public static final int[] QUANT_POCOS_RANGE={34,1}; //34>=X>=1
    
    public static int[] getQuantifierRange(String quantifier){
        int[] result=null;
        switch (quantifier){
            case QUANT_UNIVERSAL:
                result=QUANT_UNIVERSAL_RANGE;
                break;
            case QUANT_EXISTENCIAL:
                result=QUANT_EXISTENCIAL_RANGE;
                break;
            case QUANT_DISCRETO:
                result=QUANT_DISCRETO_RANGE;
                break;
            case QUANT_MUCHOS:
                result=QUANT_MUCHOS_RANGE;
                break;
            case QUANT_MODERADAMENTE:
                result=QUANT_MODERADAMENTE_RANGE;
                break;
            case QUANT_POCOS:
                result=QUANT_POCOS_RANGE;
                break;
        }
        return result;
    }
    
}//class
