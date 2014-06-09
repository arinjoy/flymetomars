package flymetomars.dataaccess.hibernate;

import flymetomars.common.NullArgumentException;
import flymetomars.common.datatransfer.Location;
import flymetomars.common.datatransfer.Mission;
import flymetomars.common.datatransfer.Person;
import flymetomars.dataaccess.MissionDAO;
import flymetomars.dataaccess.UnserialisedEntityException;
import flymetomars.dataaccess.entities.AbstractEntityUtil;
import flymetomars.dataaccess.entities.LocationEntity;
import flymetomars.dataaccess.entities.MissionEntity;
import flymetomars.dataaccess.entities.PersonEntity;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.TransientObjectException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * 
 * As an implementor of a DAO interface, this class performs CRUD operations.
 *
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class HibernateMissionDAOImpl extends AbstractHibernateDAOImpl<MissionEntity> implements MissionDAO {
    
    /**
     * 
     * 
     * @param captain
     * @return
     * @throws UnserialisedEntityException 
     * @throws NullArgumentException
     */
    @Override
    public List<Mission> getMissionsByCaptain(Person captain) throws UnserialisedEntityException {
        if(null==captain) {
                throw new NullArgumentException("cannot lookup missions captained by a null Person ref");
        }
        try {
            DetachedCriteria fetchPerson = DetachedCriteria.forClass(PersonEntity.class);
            fetchPerson.add(Restrictions.idEq(captain.getId()));
            List<PersonEntity> capt=(List<PersonEntity>)getHibernateTemplate().findByCriteria(fetchPerson);
            if(null==capt || capt.isEmpty()) {
                throw new UnserialisedEntityException(captain.getClass().getName(), "HibernateMissionDAOImpl.getMissionsByCaptain", null);
            } else if(capt.size()==1) {
                PersonEntity c=capt.get(0);
                DetachedCriteria criteria = DetachedCriteria.forClass(MissionEntity.class);
                criteria.add(Restrictions.eq("captain", c));
                List<MissionEntity> list = (List<MissionEntity>)getHibernateTemplate().findByCriteria(criteria);
                return AbstractEntityUtil.fromEntityList(list,Mission.class);
            } else {
                return Collections.emptyList();
            }
        } catch (TransientObjectException toe) {
            throw new UnserialisedEntityException(captain.getClass().getName(), "HibernateMissionDAOImpl.getMissionsByCaptain", toe);
        }
    }
    
    /**
     * 
     * 
     * @param name
     * @return 
     */
    @Override
    public Mission getMissionByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(MissionEntity.class);
        criteria.add(Restrictions.eq("name", name));
        List<MissionEntity> list = getHibernateTemplate().findByCriteria(criteria);
        if (list.size() <= 1) { //if 0 then return null
            return 0==list.size()?null:null==list.get(0)?null:list.get(0).toDTO(); //otherwise return only item
        } else { //more than one missions with same name found
            throw new IllegalStateException("database is in an illegal state");
        }
    }

    /**
     * 
     * 
     * @param begin
     * @param end
     * @return 
     * @throws NullArgumentException
     */
    @Override
    public List<Mission> getMissionsByDateRange(Date begin, Date end) {
        if(null==begin||null==end) {
            throw new NullArgumentException("cannot get Missions in a null Date range");
        }
        DetachedCriteria criteria = DetachedCriteria.forClass(MissionEntity.class);
        criteria.add(Restrictions.gt("time", begin));
        criteria.add(Restrictions.lt("time", end));
        List<MissionEntity> list = (List<MissionEntity>)getHibernateTemplate().findByCriteria(criteria);
        return AbstractEntityUtil.fromEntityList(list,Mission.class);
    }

    /**
     * 
     * 
     * @param loc
     * @return 
     */
    @Override
    public List<Mission> getMissionsByLocation(Location loc) throws UnserialisedEntityException {
        if(null==loc) {
            throw new NullArgumentException("cannot get Missions by a null Location");
        }
        DetachedCriteria fetchLoc = DetachedCriteria.forClass(LocationEntity.class);
        fetchLoc.add(Restrictions.idEq(loc.getId()));
        List<LocationEntity> locs = (List<LocationEntity>)getHibernateTemplate().findByCriteria(fetchLoc);
        if(null==locs || locs.isEmpty()) {
                throw new UnserialisedEntityException("Location", "HibernateMissionDAOImpl.getMissionsByLocation", null);
        } else if(locs.size()==1) {
            DetachedCriteria criteria = DetachedCriteria.forClass(MissionEntity.class);
            criteria.add(Restrictions.eq("location", locs.get(0)));
            List<MissionEntity> list = (List<MissionEntity>)getHibernateTemplate().findByCriteria(criteria);
            return AbstractEntityUtil.fromEntityList(list,Mission.class);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 
     * 
     * @param id
     * @return 
     */
    @Override
    public Mission load(Long id) {
        MissionEntity ent=this.loadEntity(id);
        if(null==ent) { return (Mission)null; }
        return ent.toDTO();
    }

    @Override
    public List<Mission> getAll() {
        return AbstractEntityUtil.fromEntityList(this.getAllEntity(),Mission.class);
    }

    @Override
    public void save(Mission object) {
        MissionEntity ent=MissionEntity.fromDTO(object);
        if(null!=ent) {
            MissionEntity miss=this.loadEntity(ent.getId());
            if(null!=miss) { ent=miss.copyValues(ent); }
            ent.setId(object.getId());
            resolveComplexIntraEntities(ent);
        }
        this.saveEntity(ent);
        object.setId(ent.getId());
    }

    @Override
    public void update(Mission object) {
        MissionEntity ent=MissionEntity.fromDTO(object);
        if(null!=ent) {
            if(null==ent.getId()) {
                throw new HibernateException(new UnserialisedEntityException(
                    "Mission", "HibernateMissionDAOImpl.update",
                    new NullArgumentException("cannot update Mission with null Id"
                )));
            }
            ent=this.loadEntity(ent.getId());
            if(null==ent) {
                throw new HibernateException(new UnserialisedEntityException(
                    "Mission", "HibernateMissionDAOImpl.update",
                    new NullArgumentException("unable to load existing mission for update"
                )));
            }
            ent.copyValues(MissionEntity.fromDTO(object));
            //resolveComplexIntraEntities(ent);
            this.getHibernateTemplate().merge(MissionEntity.fromDTO(object));
        } else {
            this.updateEntity(ent);
        }
    }

    @Override
    public void saveOrUpdate(Mission object) {
        MissionEntity ent=null==object?null:MissionEntity.fromDTO(object);
        MissionEntity miss=null==ent?null:this.loadEntity(ent.getId());
        if(null!=miss) { ent=miss.copyValues(ent); }
        this.saveOrUpdateEntity(ent);
    }

    @Override
    public Mission delete(Mission object) {
        MissionEntity ent=null==object?null:MissionEntity.fromDTO(object);
        if(null!=ent && null!=ent.getId()){ ent=this.loadEntity(ent.getId()); }
        ent=this.deleteEntity(ent);
        return null==ent?null:ent.toDTO();
    }

    private static void nullCheck(Object o,RuntimeException e) {if(null==o){throw e;}}
    protected static MissionEntity resolveComplexIntraEntities(MissionEntity ent) {
        HibernateLocationDAOImpl locationDao=(HibernateLocationDAOImpl)inter().get(HibernateLocationDAOImpl.class);
        HibernatePersonDAOImpl personDao=(HibernatePersonDAOImpl)inter().get(HibernatePersonDAOImpl.class);
        //location
        if(null!=ent.getLocation() && null!=ent.getLocation().getId()) {
            LocationEntity loc=locationDao.loadEntity(ent.getLocation().getId());
            ent.setLocation(null==loc?ent.getLocation():loc);
        }
        //captain
        if(null!=ent.getCaptain() && null!=ent.getCaptain().getId()) {
            PersonEntity capt=personDao.loadEntity(ent.getCaptain().getId());
            nullCheck(capt,
                new HibernateException(new UnserialisedEntityException(
                    "Person", "HibernateMissionDAOImpl.resolveComplexIntraEntities",
                    new NullArgumentException("Captain not found in db"
                )))
            );
            HibernatePersonDAOImpl.resolveComplexIntraEntities(ent.getCaptain());
            ent.setCaptain(capt);
        }
        //participants
        if(null!=ent.getParticipantSet()) {
            Set<PersonEntity> result=new HashSet<PersonEntity>();
            for(PersonEntity p : ent.getParticipantSet()) {
                if(null!=p && null!=p.getId()) {
                    PersonEntity pr=personDao.loadEntity(p.getId());
                    nullCheck(pr,
                        new HibernateException(new UnserialisedEntityException(
                            "Person", "HibernateMissionDAOImpl.resolveComplexIntraEntities",
                            new NullArgumentException("Mission ParticipantSet property cannot contain a null Person ref"
                        )))
                    );
                    HibernatePersonDAOImpl.resolveComplexIntraEntities(pr);
                    result.add(pr);
                }
            }
            ent.setParticipantSet(result);
        }
        return ent;
    }
    
}
