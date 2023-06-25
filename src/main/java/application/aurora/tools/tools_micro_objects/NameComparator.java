package application.aurora.tools.tools_micro_objects;

import application.aurora.micro_objects.AstronautIntern;

import java.util.Comparator;

public class NameComparator implements Comparator<AstronautIntern> {
    @Override
    public int compare(AstronautIntern o1, AstronautIntern o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
