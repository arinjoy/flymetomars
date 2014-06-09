package flymetomars.dataaccess.hibernate;

import flymetomars.common.datatransfer.Person;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.entities.AbstractEntityUtil;
import flymetomars.dataaccess.entities.ExpertiseEntity;
import flymetomars.dataaccess.entities.MissionEntity;
import flymetomars.dataaccess.entities.PersonEntity;
import flymetomars.dataaccess.entities.SaltedPasswordEntity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * As an implementor of a DAO interface, this class performs CRUD operations.
 * 
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class HibernatePersonDAOImpl extends AbstractHibernateDAOImpl<PersonEntity> implements PersonDAO {

    @Override
    public Person getPersonByEmail(String email) {
        DetachedCriteria criteria = DetachedCriteria.forClass(PersonEntity.class);
        criteria.add(Restrictions.eq("email", email));
        List<PersonEntity> list = getHibernateTemplate().findByCriteria(criteria);
        if (null==list || 0 == list.size()) {
            return null;
        } else if (1 == list.size()) {
            return null==list.get(0)?null:list.get(0).toDTO();
        } else {
            String users = "";
            for (PersonEntity p : list) {
                users += "<" + p.getUserName() + "> ";
            }
            throw new IllegalStateException("Duplicate email addressed detected:" + users);
        }
    }

    @Override
    public Person getPersonByUserName(String userName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(PersonEntity.class);
        criteria.add(Restrictions.eq("userName", userName));
        List<PersonEntity> list = getHibernateTemplate().findByCriteria(criteria);
        if (0 == list.size()) {
            return null;
        } else if (1 == list.size()) {
            return null==list.get(0)?null:list.get(0).toDTO();
        } else {
            StringBuilder users = new StringBuilder();
            for (PersonEntity p : list) {
                users.append('<');
                users.append(p.getUserName());
                users.append('>');
            }
            throw new IllegalArgumentException("Duplicate user name detected:".concat(users.toString()));
        }
    }

    @Override
    public Person load(Long id) {
        PersonEntity ent=this.loadEntity(id);
        if(null==ent) { return (Person)null; }
        resolveComplexIntraEntities(ent);
        return ent.toDTO();
    }

    @Override
    public List<Person> getAll() {
        return AbstractEntityUtil.fromEntityList(this.getAllEntity(),Person.class);
    }

    @Override
    public void save(Person object) {
        if(null==object.getPassword()) {
            throw new IllegalArgumentException("Cannot save Person with null password");
        }
        if(null!=object.getId()) {
            throw new IllegalArgumentException("Cannot save Person with set ID - try update");
        }
        PersonEntity ent=PersonEntity.fromDTO(object);
        if(null!=ent && null!=ent.getId()) {
            PersonEntity guy=this.loadEntity(ent.getId());
            if(null!=guy) {
                ent=guy.copyValues(ent);
                resolveComplexIntraEntities(ent);
            } else {
                ent.setId(null);
            }
        }
        /*PersonEntity ent=null==object?null:this.loadEntity(object.getId());
        if(null==ent) { ent=PersonEntity.fromDTO(object); }
        else { ent=PersonEntity.fromDTO(object).copyValues(ent); }*/
        this.saveEntity(ent);
        object.setId(ent.getId());
    }

    @Override
    public void update(Person object) {
        PersonEntity ent=PersonEntity.fromDTO(object);
        if(null!=ent && null!=ent.getId()) {
            PersonEntity guy=this.loadEntity(ent.getId());
            if(null==guy) {
                throw new HibernateException("cannot update a vagrant person");
            } else {
                //resolveComplexIntraEntities(guy);
                ent=guy.copyValues(ent);
                resolveComplexIntraEntities(ent);
            }
            ent.setId(object.getId());
        }
        this.updateEntity(ent);
    }

    @Override
    public void saveOrUpdate(Person object) {
        PersonEntity ent=null==object?null:PersonEntity.fromDTO(object);
        if(null!=ent && null!=ent.getId()) { ent=this.loadEntity(ent.getId()).copyValues(ent); }
        resolveComplexIntraEntities(ent);
        this.saveOrUpdateEntity(ent);
        object.setId(ent.getId());
    }

    @Override
    public Person delete(Person object) {
        PersonEntity ent=null==object?null:PersonEntity.fromDTO(object);
        if(null!=ent && null!=ent.getId()){ ent=this.loadEntity(ent.getId()); }
        ent=this.deleteEntity(ent);
        return null==ent?null:ent.toDTO();
    }

    protected static PersonEntity resolveComplexIntraEntities(PersonEntity ent) {
        if(null==ent) { return null; }
        HibernateSaltedPasswordDAOImpl passwordDao=(HibernateSaltedPasswordDAOImpl)inter().get(HibernateSaltedPasswordDAOImpl.class);
        HibernateExpertiseDAOImpl expertDao=(HibernateExpertiseDAOImpl)inter().get(HibernateExpertiseDAOImpl.class);
        HibernateMissionDAOImpl missDao=(HibernateMissionDAOImpl)inter().get(HibernateMissionDAOImpl.class);
        //password
        SaltedPasswordEntity pass = passwordDao.loadEntity(ent.getPassword().longValue());
        if(null==pass) { throw new /*dependency*/IllegalArgumentException("password entity dependant not found"); }
        ent.setPassword(pass.getId());
        /*
        if(null!=ent.getPassword() && null!=ent.getPassword().getId()) {
            long id=ent.getPassword().getId();
            SaltedPasswordEntity pass=passwordDao.loadEntity(id);
            if(null!=pass) { ent.setPassword(pass); }
        }*/
        //missions
        if(null!=ent.getMissionsRegistered()) {
            Set<MissionEntity> miss=new HashSet<MissionEntity>();
            for(MissionEntity m : ent.getMissionsRegistered()) {
                miss.add(missDao.loadEntity(m.getId()));
            }
            ent.setMissionsRegistered(miss);
        }
        //expertise
        if(null!=ent.getExpertiseGained()) {
            Set<ExpertiseEntity> exp=new HashSet<ExpertiseEntity>();
            for(ExpertiseEntity e : ent.getExpertiseGained()) {
                exp.add(expertDao.loadEntity(e.getId()));
            }
            ent.setExpertiseGained(exp);
        }
        return ent;
    }
    
}
