package flymetomars.dataaccess.hibernate;

import flymetomars.common.NullArgumentException;
import flymetomars.common.datatransfer.Expertise;
import flymetomars.common.datatransfer.Expertise.ExpertiseLevel;
import flymetomars.dataaccess.ExpertiseDAO;
import flymetomars.dataaccess.entities.AbstractEntityUtil;
import flymetomars.dataaccess.entities.ExpertiseEntity;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implements the query and persistence logic specified within the ExpertiseDAO
 * interface, utilising Hibernate framework as underlying backing-store.
 * 
 * As an implementor of a DAO interface, this class performs CRUD operations.
 *
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class HibernateExpertiseDAOImpl extends AbstractHibernateDAOImpl<ExpertiseEntity> implements ExpertiseDAO {

    /**
     * 
     * @param name is the name of the particular expertise to load a list of
     * @return List of Expertise DTOs matching specified name
     */
    @Override
    public List<Expertise> getExpertiseListByName(String name) {
        if (name == null) {
            throw new NullArgumentException("cannot lookup an experise if the name is null");
        }
        DetachedCriteria criteria = DetachedCriteria.forClass(ExpertiseEntity.class);
        criteria.add(Restrictions.eq("name", name));
        List<ExpertiseEntity> list = (List<ExpertiseEntity>)getHibernateTemplate().findByCriteria(criteria);
        return AbstractEntityUtil.fromEntityList(list,Expertise.class);
    }

    /**
     * 
     * @param level is the ExpertiseLevel enumeration value desired to query
     * @return List of Expertise DTOs matching specified level
     */
    @Override
    public List<Expertise> getExpertiseListByLevel(ExpertiseLevel level) {
        if (level == null) {
            throw new NullArgumentException("cannot lookup an expertise if the level is null");
        } else {
            DetachedCriteria criteria = DetachedCriteria.forClass(ExpertiseEntity.class);
            criteria.add(Restrictions.eq("level", level));
            List<ExpertiseEntity> list = (List<ExpertiseEntity>)getHibernateTemplate().findByCriteria(criteria);
            return AbstractEntityUtil.fromEntityList(list,Expertise.class);
        }
    }


    @Override
    public Expertise load(Long id) {
        ExpertiseEntity ent=this.loadEntity(id);
        if(null==ent) { return (Expertise)null; }
        return ent.toDTO(false);
    }
    
    @Override
    public List<Expertise> getAll() {
        return AbstractEntityUtil.fromEntityList(this.getAllEntity(),Expertise.class);
    }

    @Override
    public void save(Expertise object) {
        ExpertiseEntity ent=null==object?null:ExpertiseEntity.fromDTO(object);
        this.saveEntity(ent);
        object.setId(ent.getId());
    }

    @Override
    public void update(Expertise object) {
        ExpertiseEntity ent=null==object?null:ExpertiseEntity.fromDTO(object);
        ent=this.loadEntity(ent.getId()).copyValues(ent);
        this.updateEntity(ent);
    }

    @Override
    public void saveOrUpdate(Expertise object) {
        ExpertiseEntity ent=null==object?null:ExpertiseEntity.fromDTO(object);
        ent=this.loadEntity(ent.getId()).copyValues(ent);
        this.saveOrUpdateEntity(ent);
    }

    @Override
    public Expertise delete(Expertise object) {
        ExpertiseEntity ent=null==object?null:ExpertiseEntity.fromDTO(object);
        if(null!=ent && null!=ent.getId()){ ent=this.loadEntity(ent.getId()); }
        ent=this.deleteEntity(ent);
        return null==ent?null:ent.toDTO();
    }
    
}
