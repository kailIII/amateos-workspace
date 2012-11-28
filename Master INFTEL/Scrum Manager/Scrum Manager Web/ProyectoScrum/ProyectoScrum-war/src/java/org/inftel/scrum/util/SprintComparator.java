/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.util;

import java.util.Comparator;
import org.inftel.scrum.modelXML.Sprint;

/**
 *
 * @author inftel
 */
public class SprintComparator implements Comparator{

    @Override
    public int compare(Object t, Object t1) {
        Sprint sprint1=(Sprint) t;
        Sprint sprint2=(Sprint) t1;
       if(sprint1.getInicio().before(sprint2.getInicio()))
            return -1;
        else if (sprint1.getInicio().after(sprint2.getInicio()))
            return 1;
        else
            return 0;
    }
}
