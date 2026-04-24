package es.sdweb.memorycorp.rulesengine;

import java.util.Comparator;

/**
 *
 * @author Antonio
 */
public class RuleComparator implements Comparator{
    
    @Override
    public int compare(Object o1, Object o2) {
        Rule r1=(Rule)o1;
        Rule r2=(Rule)o2;
        int result=r1.getPriority()-r2.getPriority();
        return result;
    }
    
}//class
