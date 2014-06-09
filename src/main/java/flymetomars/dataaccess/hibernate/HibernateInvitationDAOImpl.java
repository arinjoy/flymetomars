package flymetomars.dataaccess.hibernate;

import flymetomars.common.NullArgumentException;
import flymetomars.common.datatransfer.Invitation;
import flymetomars.common.datatransfer.Person;
import flymetomars.common.validation.DateValidator;
import flymetomars.dataaccess.InvitationDAO;
import flymetomars.dataaccess.UnserialisedEntityException;
import flymetomars.dataaccess.entities.AbstractEntityUtil;
import flymetomars.dataaccess.entities.InvitationEntity;
import flymetomars.dataaccess.entities.PersonEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.hibernate.TransientObjectException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Implements the query and persistence logic specified within the InvitationDAO
 * interface, utilising Hibernate framework as underlying backing-store.
 *
 * As an implementor of a DAO interface, this class performs CRUD operations.
 * 
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class HibernateInvitationDAOImpl extends AbstractHibernateDAOImpl<InvitationEntity> implements InvitationDAO {
   
    /**
     * 
     * @param creator is the Person DTO representing the desired invitation creator
     * @return List of Invitation DTO objects matching specified creator condition
     * @throws NullArgumentException thrown when supplied `creator' object is null
     */
    @Override
    public List<Invitation> getInvitationsByCreator(Person creator) {
        if(null==creator) {
            throw new NullArgumentException("cannot lookup Invitations created by a null Person ref");
        }
        if(null==creator.getId()) { throw new NullArgumentException("specified creator has null id value"); }
        PersonEntity c=PersonEntity.fromDTO(creator);
        if(null==c) { throw new NullArgumentException("specified creator not present in database"); }
        DetachedCriteria findCreator = DetachedCriteria.forClass(PersonEntity.class);
        findCreator.add(Restrictions.eq("id", c.getId().longValue())); c=null;
        for(PersonEntity p : (List<PersonEntity>)getHibernateTemplate().findByCriteria(findCreator)) {
            if(null!=p && p.getId().equals(creator.getId())) { c=p; }
        }
        if(null==c) { return Collections.emptyList(); }
        DetachedCriteria criteria = DetachedCriteria.forClass(InvitationEntity.class);
        criteria.add(Restrictions.eq("creator", c));
        List<InvitationEntity> list = (List<InvitationEntity>) getHibernateTemplate().findByCriteria(criteria);
        List<Invitation> result=new ArrayList<Invitation>();
        for(InvitationEntity ie : list) {
            result.add(ie.toDTO(true, false));
        }
        return result;//AbstractEntityUtil.fromEntityList(list,Invitation.class);
    }

    /**
     * 
     * @param recipient is the Person DTO representing the desired recipient creator
     * @return List of Invitation DTO objects matching specified recipient condition
     * @throws NullArgumentException thrown when supplied Person is null reference
     */
    @Override
    public List<Invitation> getInvitationsByRecipient(Person recipient) throws UnserialisedEntityException {
        if(null==recipient) {
            throw new NullArgumentException("cannot lookup Invitations recieved by a null Person ref");
        }
        if(null==recipient.getId()) { throw new NullArgumentException("specified recipient has null id value"); }
        try {
            PersonEntity r=PersonEntity.fromDTO(recipient);
            DetachedCriteria findRecipient = DetachedCriteria.forClass(PersonEntity.class);
            findRecipient.add(Restrictions.eq("id", r.getId())); r=null;
            for(PersonEntity p : (List<PersonEntity>)getHibernateTemplate().findByCriteria(findRecipient)) {
                if(null!=p && p.getId()==recipient.getId()) { r=p; }
            }
            if(null==r) { throw new NullArgumentException("specified recipient not found in store"); }
            DetachedCriteria criteria = DetachedCriteria.forClass(InvitationEntity.class);
            criteria.add(Restrictions.eq("recipient", r));
            List<InvitationEntity> list = (List<InvitationEntity>)getHibernateTemplate().findByCriteria(criteria);
            return AbstractEntityUtil.fromEntityList(list,Invitation.class);
        } catch (TransientObjectException toe) {
            throw new UnserialisedEntityException("Person", "HibernateInvitationDAOImpl.getInvitationsByRecipient", toe);
        }
    }

    /**
     * 
     * @param when is a Date object specifying the most recent Date to match on
     * @return List of Invitation DTO objects matching specified date condition
     * @throws NullArgumentException thrown when given Date is null
     */
    @Override
    public List<Invitation> getInvitationsBeforeDate(Date when) {
        if(null==when) {
            throw new NullArgumentException("cannot lookup Invitations before a null Date ref");
        }
        List<Invitation> result=new ArrayList<Invitation>();
        for(InvitationEntity inv : this.getAllEntity()) {
            if(inv.getLastUpdated().before(when)) { result.add(inv.toDTO()); }
        }
        /*
        DetachedCriteria criteria = DetachedCriteria.forClass(InvitationEntity.class);
        criteria.add(Restrictions.lt("lastUpdated", when));
        List<InvitationEntity> list = (List<InvitationEntity>)getHibernateTemplate().findByCriteria(criteria);
        return AbstractEntityUtil.fromEntityList(list,Invitation.class);
        */
        return result;
    }

    /**
     * 
     * 
     * @param when is a Date object specifying the oldest Date an invitation may have
     * @return List of Invitation DTO objects matching specified date condition
     * @throws NullArgumentException thrown when given Date is null
     * @throws DateInFutureException thrown when `when' has not yet occurred
     */
    @Override
    public List<Invitation> getInvitationsSinceDate(Date when) {
        if(null==when) {
            throw new NullArgumentException("cannot lookup Invitations since a null Date ref");
        }
        DateValidator.validatePastDate(when);  //throws DateInFutureException
        DetachedCriteria criteria = DetachedCriteria.forClass(InvitationEntity.class);
        criteria.add(Restrictions.gt("lastUpdated", when));
        List<InvitationEntity> list = (List<InvitationEntity>)getHibernateTemplate().findByCriteria(criteria);
        return AbstractEntityUtil.fromEntityList(list,Invitation.class);
    }

    @Override
    public Invitation load(Long id) {
        InvitationEntity ent=this.loadEntity(id);
        if(null==ent) { return (Invitation)null; }
        return ent.toDTO(false,false);
    }

    @Override
    public List<Invitation> getAll() {
        return AbstractEntityUtil.fromEntityList(this.getAllEntity(),Invitation.class);
    }

    @Override
    public void save(Invitation object) {
        InvitationEntity ent=InvitationEntity.fromDTO(object);
        if(null!=ent && null!=ent.getId()) {
            InvitationEntity inv=this.loadEntity(ent.getId());
            if(null!=inv) { ent=inv.copyValues(ent); }
        }
        resolveComplexIntraEntities(ent);
        this.saveEntity(ent);
        if(null!=ent.getId()){ object.setId(ent.getId()); }
    }

    @Override
    public void update(Invitation object) {
        this.getHibernateTemplate().merge(InvitationEntity.fromDTO(object));
        /*InvitationEntity ent=InvitationEntity.fromDTO(object);
        ent=this.loadEntity(ent.getId()).copyValues(ent);
        resolveComplexIntraEntities(ent);
        ent.setStatus(object.getStatus());
        this.updateEntity(ent);*/
    }

    @Override
    public void saveOrUpdate(Invitation object) {
        InvitationEntity ent=null==object?null:InvitationEntity.fromDTO(object);
        ent=this.loadEntity(ent.getId()).copyValues(ent);
        resolveComplexIntraEntities(ent);
        this.saveOrUpdateEntity(ent);
    }

    @Override
    public Invitation delete(Invitation object) {
        InvitationEntity ent=null==object?null:InvitationEntity.fromDTO(object);
        if(null!=ent && null!=ent.getId()){ ent=this.loadEntity(ent.getId()); }
        ent=this.deleteEntity(ent);
        return null==ent?null:ent.toDTO();
    }

    protected static InvitationEntity resolveComplexIntraEntities(InvitationEntity ent) {
        if(null==ent) { return null; }
        HibernatePersonDAOImpl personDao=(HibernatePersonDAOImpl)inter().get(HibernatePersonDAOImpl.class);
        HibernateMissionDAOImpl missionDao=(HibernateMissionDAOImpl)inter().get(HibernateMissionDAOImpl.class);
        //mission
        if(null!=ent.getMission() && null!=ent.getMission().getId()) {
            ent.setMission(missionDao.loadEntity(ent.getMission().getId()));
            ent.setMission(HibernateMissionDAOImpl.resolveComplexIntraEntities(ent.getMission()));
        }
        //creator
        if(null!=ent.getCreator() && null!=ent.getCreator().getId()) {
            ent.setCreator(personDao.loadEntity(ent.getCreator().getId()));
            ent.setCreator(HibernatePersonDAOImpl.resolveComplexIntraEntities(ent.getCreator()));
        }
        //recipient
        if(null!=ent.getRecipient() && null!=ent.getRecipient().getId()) {
            ent.setRecipient(personDao.loadEntity(ent.getRecipient().getId()));
            ent.setRecipient(HibernatePersonDAOImpl.resolveComplexIntraEntities(ent.getRecipient()));
        }
        return ent;
    }
}
