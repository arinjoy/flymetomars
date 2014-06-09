package flymetomars.dataaccess.hibernate;

import flymetomars.common.NullArgumentException;
import flymetomars.common.datatransfer.Location;
import flymetomars.dataaccess.LocationDAO;
import flymetomars.dataaccess.entities.AbstractEntityUtil;
import flymetomars.dataaccess.entities.LocationEntity;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implements the query and persistence logic specified within the LocationDAO
 * interface, utilising Hibernate framework as underlying backing-store.
 *
 * As an implementor of a DAO interface, this class performs CRUD operations.
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class HibernateLocationDAOImpl extends AbstractHibernateDAOImpl<LocationEntity> implements LocationDAO {

    /**
     * 
     * @param county is the String country value to test for in each Location
     * @return List of matching Location DTO objects to specified country value
     */
    @Override
    public List<Location> getLocationsByCountry(String country) {
        if (country == null) {
            throw new NullArgumentException("cannot lookup a location by null country");
        }
        DetachedCriteria criteria = DetachedCriteria.forClass(LocationEntity.class);
        criteria.add(Restrictions.eq("country", country));
        List<LocationEntity> list = (List<LocationEntity>)getHibernateTemplate().findByCriteria(criteria);
        return AbstractEntityUtil.fromEntityList(list,Location.class);
    }

    /**
     * 
     * @param town is the String town value to test for in each Location
     * @return List of matching Location DTO objects to specified town value
     */
    @Override
    public List<Location> getLocationsByTown(String town) {
        if (town == null) {
            throw new NullArgumentException("cannot lookup a location by null town");
        }
        DetachedCriteria criteria = DetachedCriteria.forClass(LocationEntity.class);
        criteria.add(Restrictions.eq("town", town));
        List<Location> list = (List<Location>) getHibernateTemplate().findByCriteria(criteria);
        return AbstractEntityUtil.fromEntityList(list,Location.class);
    }

    @Override
    public Location load(Long id) {
        LocationEntity ent=this.loadEntity(id);
        if(null==ent) { return (Location)null; }
        return ent.toDTO();
    }

    @Override
    public List<Location> getAll() {
        return AbstractEntityUtil.fromEntityList(this.getAllEntity(),Location.class);
    }

    @Override
    public void save(Location object) {
        LocationEntity ent=LocationEntity.fromDTO(object);
        if(null!=ent) {
            LocationEntity miss=this.loadEntity(ent.getId());
            if(null!=miss) { ent=miss.copyValues(ent); }
            ent.setId(object.getId());
        }
        this.saveEntity(ent);
        object.setId(ent.getId());
    }

    @Override
    public void update(Location object) {
        LocationEntity ent=null==object?null:LocationEntity.fromDTO(object);
        ent=this.loadEntity(ent.getId()).copyValues(ent);
        this.updateEntity(ent);
    }

    @Override
    public void saveOrUpdate(Location object) {
        LocationEntity ent=null==object?null:LocationEntity.fromDTO(object);
        LocationEntity miss=null==ent?null:this.loadEntity(ent.getId());
        if(null!=miss) { ent=miss.copyValues(ent); }
        this.saveOrUpdateEntity(ent);
    }

    @Override
    public Location delete(Location object) {
        LocationEntity ent=null==object?null:LocationEntity.fromDTO(object);
        if(null!=ent && null!=ent.getId()){ ent=this.loadEntity(ent.getId()); }
        ent=this.deleteEntity(ent);
        return null==ent?null:ent.toDTO();
    }
    
}
