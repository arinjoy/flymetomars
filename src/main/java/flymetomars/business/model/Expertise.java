package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.common.logic.BasicLogic;
import flymetomars.common.logic.RhinoBasicLogicImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
//import flymetomars.common.validation.StringValidator;

/**
 *
 * @author Apoorva Singh
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class Expertise extends IdentifiableModel<Expertise> implements TransferableModel<flymetomars.common.datatransfer.Expertise> {
    
    private String name;
    private String description;
    private flymetomars.common.datatransfer.Expertise.ExpertiseLevel level;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    private BasicLogic<flymetomars.common.datatransfer.Expertise> logic=
        new RhinoBasicLogicImpl<flymetomars.common.datatransfer.Expertise>(
            flymetomars.common.datatransfer.Expertise.class, dtoFactory
    );
     
    /**
     * Default constructor
     */
    public Expertise(){
        this.name = "";
        this.description = "";
        this.level = null;
    }
    
     /**
     * Copy constructor that copies a DTO to an Expertise model
     * @param dto 
     */
    public Expertise(flymetomars.common.datatransfer.Expertise dto) {
        this();
        this.setName(dto.getName());
        this.setDescription(dto.getDescription());
        this.setLevel(dto.getLevel());
        this.setId(dto.getId());
    }
     /**
     * To convert a Expertise model object to a dto
     */
    @Override
    public final flymetomars.common.datatransfer.Expertise toDTO() {
        flymetomars.common.datatransfer.Expertise result;
        try {
            result =  this.dtoFactory.createDTO(flymetomars.common.datatransfer.Expertise.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Expertise.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        result.setName(this.getName());
        result.setDescription(this.getDescription());
        result.setLevel(this.getLevel());
        result.setId(this.getId());
        return result;
    }
    /**
     * Method to get the name of the expertise
     * @return the name
     */
    public final String getName(){
        return name;
    }
    
    /**
     * Method to set the name of the expertise
     * @param name
     */
    public final void setName(String name){
        if (null == name) {
            throw new NullArgumentException("Expertise name cannot be null");
        }
        /* Old Validation (non-JS):
        StringValidator.validateStringLengthInRange(name, 5, 20);
        StringValidator.validateStringContainsOnly(name, " '.,-()" + StringValidator.LATIN_ALPHABET);
        */
        flymetomars.common.datatransfer.Expertise dto;
        try {
            dto =  this.dtoFactory.createDTO(flymetomars.common.datatransfer.Expertise.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Expertise.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setName(name);
        logic.applyRules(this.getClass().getSimpleName(),"name",dto);
        this.name = name;
    }
    
    /**
     * Method to get the expertise description
     * @return
     */
    public final String getDescription(){
        return description;
    }
   
    /**
     * Method to set the expertise description
     * @param description
     */
    public final void setDescription(String description){
        if(null==description) {
            throw new NullArgumentException("Description cannot be null");
        }
        /* Old Validation (non-JS):
        StringValidator.validateStringLengthInRange(description, 20, 120);
        StringValidator.validateStringContainsNo(description, Character.toString((char) 0) + "<>");  //blackbox
        */
        flymetomars.common.datatransfer.Expertise dto;
        try {
            dto =  this.dtoFactory.createDTO(flymetomars.common.datatransfer.Expertise.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Expertise.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setDescription(description);
        logic.applyRules(this.getClass().getSimpleName(),"description",dto);
        this.description = description;
    }
    
    /**
     * Method to get the expertise level
     * @return
     */
    public flymetomars.common.datatransfer.Expertise.ExpertiseLevel getLevel() {
        return level;
    }

    /**
     * Method to set the expertise level
     * @param level
     */
    public final void setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel level) {
        if (null == level) {
            throw new NullArgumentException("Expertise level field cannot be null");
        }
        this.level = level;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        try {
            return this.equalsModel((Expertise) obj);
        } catch (ClassCastException cc) {
            return super.equals(obj);
        }
    }
    
    @Override
    public boolean equalsModel(Expertise exp) {
        if (null==this.name||!this.name.equals(exp.getName())) { return false; }
        if (null==this.description||!this.description.equals(exp.getDescription())) { return false; }
        if (null==this.level||!this.level.equals(exp.getLevel())) { return false; }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 2+1+2,bit=(((2*2)+2)*(2+2+2+2+2))-1;
        hash = bit * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = bit * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = bit * hash + (this.level != null ? this.level.hashCode() : 0);
        return hash;
    }

}
