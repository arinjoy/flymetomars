package flymetomars.dataaccess.entities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a type-safe way of converting collections of
 * entity objects to collections of DTOs and vice-a-versa.
 * 
 * @author Lawrence Colman
 */
public abstract class AbstractEntityUtil {
    
    //<editor-fold defaultstate="collapsed" desc="default collections types">
    
    //the default Set<T> implementor to use when unable to reflect constructor
    private static final Class<? extends Set> DEFAULT_SET = HashSet.class.asSubclass(Set.class);
    
    //the default List<T> implementor to use when unable to reflect constructor
    private static final Class<? extends List> DEFAULT_LIST = ArrayList.class.asSubclass(List.class);
    
    //</editor-fold>
    
    //<editor-fold desc="Set operations">
    
    /**
     * Static helper method to translate a Set collection of Entity objects
     * to an equivalent Set collection of DTO object copies.
     *
     * Note: this method will first try to instantiate the same Set
     * implementation as the object passed - via constructor reflection. If/when
     * this is not possible the method will simply construct a DEFAULT_SET.
     *
     * @param <E> Type of Entity that the type-safe Set<E> `entities' contains
     * @param <D> Type desired for the type-safe Set<D> resulting return value
     * @param entities Set of Entity objects of type E
     * @param clazz The Class<?> of the DTO object copies to be created
     * @return a Set of DTO objects of the type D
     */
    /*package-private*/ static <E,D> Set<D> fromEntitySet(Set<E> entities,Class<D> clazz) {
        if(null==entities) { return null; }
        Set<D> result = AbstractEntityUtil.fetchSetImplementation(entities.getClass(), clazz);
        if(entities.isEmpty()) { return result; }
        E e=entities.iterator().next();
        List<E> outsource = (List<E>)AbstractEntityUtil.fetchListImplementation(AbstractEntityUtil.DEFAULT_LIST, e.getClass());
        for(E entity : entities) { outsource.add(entity); }
        for(D dto : AbstractEntityUtil.fromEntityList(outsource, clazz)) { result.add(dto); }
        return result;
    }
    
    /**
     * Static helper method to translate a Set collection of DTO objects
     * to an equivalent Set collection of Entity object copies.
     *
     * Note: this method will first try to instantiate the same Set
     * implementation as the object passed - via constructor reflection. If/when
     * this is not possible the method will simply construct a DEFAULT_SET.
     *
     * @param <E> Type desired for the type-safe Set<E> resulting return value
     * @param <D> Type of DTO that the type-safe Set<D> `dtoSet' contains
     * @param dtoSet Set of DTO objects of type D
     * @param clazz The Class<?>  of the Entity object copies to be created
     * @return A Set of Entity objects of type E
    */
     /*package-private*/ static <E,D> Set<E> toEntitySet(Set<D> dtoSet,Class<E> clazz) {
         if(null==dtoSet) { return null; }
        Set<E> result = AbstractEntityUtil.fetchSetImplementation(dtoSet.getClass(), clazz);
        if(dtoSet.isEmpty()) { return result; }
        D d=dtoSet.iterator().next();
        List<D> outsource = (List<D>)AbstractEntityUtil.fetchListImplementation(AbstractEntityUtil.DEFAULT_LIST, d.getClass());
        for(D dto : dtoSet) { outsource.add(dto); }
        for(E entity : AbstractEntityUtil.toEntityList(outsource, clazz)) { result.add(entity); }
        return result;
    }
     
    //</editor-fold>
    
     
     //<editor-fold defaultstate="collapsed" desc="List operations">
     
     /**
      * Static helper method to translate a List collection of Entity objects
      * to an equivalent List collection of DTO object copies.
      *
      * Note: this method will first try to instantiate the same List
      * implementation as the object passed - via constructor reflection. If/when
      * this is not possible the method will simply construct a DEFAULT_LIST.
      *
      * @param <E> Type of Entity that the type-safe List<E> `entities' contains
      * @param <D> Type desired for the type-safe List<D> resulting return value
      * @param entities List of Entity objects of type E
      * @param clazz The Class<?> of the DTO object copies to be created
      * @return a List of DTO objects of the type D
      */
     public static <E,D> List<D> fromEntityList(List<E> entities,Class<D> clazz) {
         if(null==entities) { return null; }
         List<D> result=AbstractEntityUtil.fetchListImplementation(entities.getClass(), clazz);
         if(entities.isEmpty()) { return result; }
         for(E entity : entities) {
             D dto;
             try {
                 dto = (D)(entity.getClass().getMethod("toDTO").invoke(entity));
                 result.add(dto);
             } catch (NoSuchMethodException ex) {
                 Logger.getLogger(AbstractEntityUtil.class.getName()).log(Level.SEVERE, "Specified Entity class is missing toDTO method", ex);
                 return null;
             } catch (InvocationTargetException ex) {
                 Logger.getLogger(AbstractEntityUtil.class.getName()).log(Level.SEVERE, "Specified Entity class toDTO method not public", ex);
                 return null;
             } catch (IllegalAccessException ex) {
                 Logger.getLogger(AbstractEntityUtil.class.getName()).log(Level.SEVERE, "Specified Entity class toDTO method not accessible", ex);
                 return null;
             } catch (IllegalArgumentException ex) {
                 Logger.getLogger(AbstractEntityUtil.class.getName()).log(Level.SEVERE, "Entity object encountered could not be converted into DTO", ex);
                 return null;
             } catch (SecurityException ex) {
                 Logger.getLogger(AbstractEntityUtil.class.getName()).log(Level.SEVERE, "Error invoking specified Entity toDTO method", ex);
                 return null;
             }
         }
         return result;
     }
     
     /**
      * Static helper method to translate a List collection of DTO objects
      * to an equivalent List collection of Entity object copies.
      *
      * Note: this method will first try to instantiate the same List
      * implementation as the object passed - via constructor reflection. If/when
      * this is not possible the method will simply construct a DEFAULT_LIST.
      *
      * @param <E> Type desired for the type-safe List<E> resulting return value
      * @param <D> Type of DTO that the type-safe List<D> `dtoList' contains
      * @param dtoList List of DTO objects of type D
      * @param clazz The Class<?>  of the Entity object copies to be created
      * @return A List of Entity objects of type E
      */
     public static <E,D> List<E> toEntityList(List<D> dtoList,Class<E> clazz) {
         if(null==dtoList) { return null; }
         List<E> result = AbstractEntityUtil.fetchListImplementation(dtoList.getClass(), clazz);
         if(dtoList.isEmpty()) { return result; }
         for(D dto : dtoList) {
             E entity;
             try {
                 entity = (E)clazz.getMethod("fromDTO", dto.getClass().getInterfaces()[1]).invoke(null, dto);
                 result.add(entity);
             } catch (NoSuchMethodException ex) {
                 Logger.getLogger(AbstractEntityUtil.class.getName()).log(Level.SEVERE, "Specified Entity class is missing fromDTO method", ex);
                 return null;
             } catch (InvocationTargetException ex) {
                 Logger.getLogger(AbstractEntityUtil.class.getName()).log(Level.SEVERE, "Specified Entity class fromDTO method not static", ex);
                 return null;
             } catch (IllegalAccessException ex) {
                 Logger.getLogger(AbstractEntityUtil.class.getName()).log(Level.SEVERE, "Specified Entity class fromDTO method not accessible", ex);
                 return null;
             } catch (IllegalArgumentException ex) {
                 Logger.getLogger(AbstractEntityUtil.class.getName()).log(Level.SEVERE, "DTO object encountered could not be converted into Entity", ex);
                 return null;
             } catch (SecurityException ex) {
                 Logger.getLogger(AbstractEntityUtil.class.getName()).log(Level.SEVERE, "Error invoking specified Entity fromDTO method", ex);
                 return null;
             }
         }
         return result;
     }
     
     //</editor-fold>
    
     
     //<editor-fold defaultstate="collapsed" desc="collection constructor reflection">
     
     private static <T> Set<T> fetchSetImplementation(Class<? extends Set> setImpl, Class<T> containedType) {
         boolean t=false;
        try {
            try {
                return DEFAULT_SET.equals(containedType)?setImpl.newInstance():new HashSet<T>();
            } catch (InstantiationException ex) {
                t=!t;
            }
            if(t) { throw new IllegalAccessException(); }
            return null;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractEntityUtil.class.getName()).log(Level.SEVERE, null, ex);
            return new HashSet<T>();
        }
     }
     
     private static <T> List<T> fetchListImplementation(Class<? extends List> listImpl, Class<T> containedType) {
         List<T> result=new ArrayList<T>();
         return DEFAULT_SET.equals(containedType)?List.class.equals(listImpl)?null:result:result;
     }
     
     //</editor-fold>
     
}
