package flymetomars.dataaccess.hibernate;

import flymetomars.common.datatransfer.Salt;
import flymetomars.common.datatransfer.SaltedPassword;
import flymetomars.dataaccess.SaltedPasswordDAO;
import flymetomars.dataaccess.entities.AbstractEntityUtil;
import flymetomars.dataaccess.entities.SaltedPasswordEntity;
import java.util.Collections;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * As an implementor of a DAO interface, this class performs CRUD operations.
 * 
 * @author Lawrence Colman
 */
public class HibernateSaltedPasswordDAOImpl extends AbstractHibernateDAOImpl<SaltedPasswordEntity> implements SaltedPasswordDAO {

    @Override
    public List<SaltedPassword> getSaltedPasswordsSharingSameSalt(Salt shared) {
        if (shared == null) {
            return Collections.emptyList();
        }
        /*List<SaltedPassword> result=new ArrayList<SaltedPassword>();
        for(SaltedPassword sp : this.getAll()) {
            if(null==sp) { continue; }
            if(sp.getSaltId().equals(shared.getHashedSaltKey())) {
                result.add(sp);
            }
        }
        return result;*/
        DetachedCriteria criteria = DetachedCriteria.forClass(SaltedPasswordEntity.class);
        criteria.add(Restrictions.eq("saltId", shared.getHashedSaltKey()));
        List<SaltedPasswordEntity> list = (List<SaltedPasswordEntity>)getHibernateTemplate().findByCriteria(criteria);
        return AbstractEntityUtil.fromEntityList(list,SaltedPassword.class);
    }

    @Override
    public SaltedPassword load(Long id) {
        SaltedPasswordEntity ent=this.loadEntity(id);
        if(null==ent) { return (SaltedPassword)null; }
        return ent.toDTO();
    }

    @Override
    public List<SaltedPassword> getAll() {
        return AbstractEntityUtil.fromEntityList(this.getAllEntity(),SaltedPassword.class);
    }

    @Override
    public void save(SaltedPassword object) {
        SaltedPasswordEntity ent=null==object?null:this.loadEntity(object.getId());
        if(null==ent) { ent=SaltedPasswordEntity.fromDTO(object); }
        else { ent=SaltedPasswordEntity.fromDTO(object).copyValues(ent); }
        this.saveEntity(ent);
        object.setId(ent.getId());
    }

    @Override
    public void update(SaltedPassword object) {
        SaltedPasswordEntity ent=SaltedPasswordEntity.fromDTO(object);
        ent=this.loadEntity(ent.getId()).copyValues(ent);
        this.updateEntity(ent);
    }

    @Override
    public void saveOrUpdate(SaltedPassword object) {
        SaltedPasswordEntity ent=null==object?null:this.loadEntity(object.getId());
        if(null==ent) { ent=SaltedPasswordEntity.fromDTO(object); }
        else { ent=SaltedPasswordEntity.fromDTO(object).copyValues(ent); }
        this.saveOrUpdateEntity(ent);
        object.setId(ent.getId());
    }

    @Override
    public SaltedPassword delete(SaltedPassword object) {
        SaltedPasswordEntity ent=null==object?null:SaltedPasswordEntity.fromDTO(object);
        if(null!=ent && null!=ent.getId()){ ent=this.loadEntity(ent.getId()); }
        ent=this.deleteEntity(ent);
        return null==ent?null:ent.toDTO();
    }
    
}
