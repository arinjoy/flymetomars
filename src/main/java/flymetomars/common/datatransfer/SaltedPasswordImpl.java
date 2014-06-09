package flymetomars.common.datatransfer;

import flymetomars.common.NullArgumentException;

/**
 * A representative class for the SaltedPassword belonging to a Person
 * 
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 * @author Apoorva Singh
 * 
 * JavaDoc copied from interface.
 */
public class SaltedPasswordImpl extends SeriablizableEntityImpl<SaltedPassword> implements EqualableStructure<SaltedPassword>, SaltedPassword {
    
    private String digest;
    private String saltId;
    
    /**
     * default constructor
     */
    public SaltedPasswordImpl() {}
    
    //<editor-fold desc="Digest property">
    
    /**
     * Property accessor for the `digest' of a SaltedPassword
     * 
     * @return String representation of the hash digest value for this password
     */
    @Override
    public String getDigest() {
        return digest;
    }
    
    /**
     * Property mutator for the `digest' of a SaltedPassword
     * 
     * @param passwordHash String representation of hash digest value password
     * @throws NullArgumentException thrown when `passwordHash' is a null ref
     */
    @Override
    public void setDigest(String passwordHash) {
        if(null==passwordHash) { throw new NullArgumentException("SaltedPassword cannot contain null digest"); }
        this.digest = passwordHash;
    }
    
    //</editor-fold>

    //<editor-fold desc="SaltId property">
    
    /**
     * Property accessor for the `saltId' of a SaltedPassword
     * 
     * @return String hash value of salt string identifier referenced inside
     */
    @Override
    public String getSaltId() {
        return saltId;
    }

    /**
     * Property mutator for the `saltId' of a SaltedPassword
     * 
     * @param saltId String hash value of salt string identifier to be used
     * @throws NullArgumentException thrown when `saltKey' is a null reference
     */
    @Override
    public void setSaltId(String saltKey) {
        if(null==saltKey){
           throw new NullArgumentException("SaltedPassword cannot have a null Salt field");
        }
        this.saltId = saltKey;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="equals and hashCode implementations">
    
    /**
     * Standard Java Object.equals(Object) method signature override for sorting
     * 
     * @param obj Object to be checked for equality with this object
     * @return boolean value indicating if the representations contained within
     * this object and obj are effectively equivalent (thus equal for sorting).
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        final SaltedPasswordImpl other = (SaltedPasswordImpl)obj;
        return this.equalsDTO(other);
    }

    /**
     * Standard Java hashCode overide method implementation
     * 
     * @return Unique (within object-typed scope) integer deterministically
     *           generated to reflect the inner-contents represented by object
     */
    @Override
    public int hashCode() {
        int hash = 2+1,bits=(2*2*2*2*2)+((2*2*2)-1);
        hash = bits * hash + (this.digest != null ? this.digest.hashCode() : 0);
        hash = bits * hash + (this.saltId != null ? this.saltId.hashCode() : 0);
        return hash;
    }

    /**
     * 
     * 
     * @param sp
     * @return 
     */
    @Override
    public boolean equalsDTO(SaltedPassword sp) {
        return null==this.digest ? (null==sp.getDigest()) : this.digest.equals(sp.getDigest());
    }
    
    //</editor-fold>
    
}
