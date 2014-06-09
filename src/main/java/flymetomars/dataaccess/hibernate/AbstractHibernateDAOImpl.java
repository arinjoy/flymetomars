package flymetomars.dataaccess.hibernate;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public abstract class AbstractHibernateDAOImpl<T> extends HibernateDaoSupport /*implements DAO<T>*/ {
    
    private static Map<Class<? extends AbstractHibernateDAOImpl>,AbstractHibernateDAOImpl<?>> intra=new HashMap<Class<? extends AbstractHibernateDAOImpl>,AbstractHibernateDAOImpl<?>>();
    
    /**
     * Default constructor
     */
    public AbstractHibernateDAOImpl() {this.intra.put(this.getClass(), this);}

    /*package-private*/ /*used to be:*/ /*public*/ T loadEntity(Long id) {
        if(null==id) { return (T)null; }
        Class<T> actualClassParameter = getClassParameter();
        String query = "SELECT e FROM " + actualClassParameter.getName() + " e WHERE e.id=?";
        HibernateTemplate thing = getHibernateTemplate();
        if(null==thing) { throw new HibernateException("Unable to reach Hibernate Framework - is the bean misconfigured?"); }
        Object[] stuff=new Object[]{(Object)id};
        List list = thing.find(query, stuff);
        if (list.size() > 0) {
            return (T)list.get(0);
        } else {
            return (T)null;
        }
    }

    private Class<T> getClassParameter() {
        return (Class<T>) ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /*package-private*/ /*used to be:*/ /*public*/ List<T> getAllEntity() {
        Class<T> actualClassParameter = getClassParameter();
        return getHibernateTemplate().find("SELECT c FROM " + actualClassParameter.getName() + " c");
    }

    /*package-private*/ /*used to be:*/ /*public*/ void saveEntity(T object) {
        try {
            getHibernateTemplate().save(object);
        } catch (DuplicateKeyException dke) {
            Logger.getLogger(AbstractHibernateDAOImpl.class.getCanonicalName()).log(Level.INFO, "Re-referenced DTO object earsure caught", dke);
            getHibernateTemplate().merge(object);
        }
    }

    /*package-private*/ /*used to be:*/ /*public*/ void updateEntity(T object) {
        try {
            getHibernateTemplate().update(object);
        } catch (DuplicateKeyException dke) {
            Logger.getLogger(AbstractHibernateDAOImpl.class.getCanonicalName()).log(Level.INFO, "Re-referenced DTO object earsure caught", dke);
            getHibernateTemplate().merge(object);
        }
    }

    /*package-private*/ /*used to be:*/ /*public*/ void saveOrUpdateEntity(T object) {
        try {
            getHibernateTemplate().saveOrUpdate(object);
        } catch (DuplicateKeyException dke) {
            Logger.getLogger(AbstractHibernateDAOImpl.class.getCanonicalName()).log(Level.INFO, "Re-referenced DTO object earsure caught", dke);
            try {
                getHibernateTemplate().merge(object);
            } catch(DuplicateKeyException de) {
                Logger.getLogger(AbstractHibernateDAOImpl.class.getCanonicalName()).log(Level.INFO, "Re-referenced DTO merge-object issue found", de);
                throw de;
            }
        }
    }

    /*package-private*/ /*used to be:*/ /*public*/ T deleteEntity(T object) {
        if(null!=object){ getHibernateTemplate().delete(object); }
        return object;
    }
    
    protected static Map<Class<? extends AbstractHibernateDAOImpl>,AbstractHibernateDAOImpl<?>> inter() { return intra; }
    
}
