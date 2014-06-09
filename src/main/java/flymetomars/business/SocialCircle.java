package flymetomars.business;

import flymetomars.business.model.Person;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 *  SocialCircle - custom type representing a group of Person model objects whom
 *  have all at one point participated within a single known Mission together.
 *  Essentially, this class represents the participants of some particular Mission.
 * 
 * As a direct member of the business package this class represents a custom type
 * 
 * @author Lawrence Colman
 */
public class SocialCircle implements Set<Person> {

    private Set<Person> data;

    /**
     * Public constructor
     * 
     * @param data the actual Set<Person> instance to type-wrap as Social Circle
     */
    public SocialCircle(Set<Person> data) {
        this.data=Collections.unmodifiableSet(data);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Set wrapping override boilerplate">
    @Override
    public int size() { return this.data.size(); }
    
    @Override
    public boolean isEmpty() { return this.data.isEmpty(); }
    
    @Override
    public boolean contains(Object p) { return this.data.contains(p); }
    
    @Override
    public Iterator<Person> iterator() { return this.data.iterator(); }
    
    @Override
    public Object[] toArray() { return this.data.toArray(); }
    
    @Override
    public <T> T[] toArray(T[] a) { return this.data.toArray(a); }
    
    @Override
    public boolean add(Person p) { return this.data.add(p); }
    
    @Override
    public boolean remove(Object p) { return this.data.remove(p); }
    
    @Override
    public boolean containsAll(Collection<?> c) { return this.data.containsAll(c); }
    
    @Override
    public boolean addAll(Collection<? extends Person> c) { return this.data.addAll(c); }
    
    @Override
    public boolean retainAll(Collection<?> c) { return this.data.retainAll(c); }
    
    @Override
    public boolean removeAll(Collection<?> c) { return this.data.removeAll(c); }
    
    @Override
    public void clear() { this.data.clear(); }
    //</editor-fold>
    
}
