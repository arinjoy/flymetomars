package flymetomars.common.factories;

import flymetomars.common.datatransfer.Expertise;
import flymetomars.common.datatransfer.Invitation;
import flymetomars.common.datatransfer.Location;
import flymetomars.common.datatransfer.Mission;
import flymetomars.common.datatransfer.Person;
import flymetomars.common.datatransfer.Salt;
import flymetomars.common.datatransfer.SaltedPassword;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public class DTOFactoryImplUnitTest {
    
    @Test
    public void testGetInstance() {
        DTOFactory result = DTOFactoryImpl.getInstance();
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateDTOforLocation() throws ClassNotFoundException {
        DTOFactory instance = DTOFactoryImpl.getInstance();
        Location result = instance.createDTO(Location.class);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testCreateDTOforMission() throws ClassNotFoundException {
        DTOFactory instance = DTOFactoryImpl.getInstance();
        Mission result = instance.createDTO(Mission.class);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testCreateDTOforExpertise() throws ClassNotFoundException {
        DTOFactory instance = DTOFactoryImpl.getInstance();
        Expertise result = instance.createDTO(Expertise.class);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testCreateDTOforPerson() throws ClassNotFoundException {
        DTOFactory instance = DTOFactoryImpl.getInstance();
        Person result = instance.createDTO(Person.class);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testCreateDTOforInvitation() throws ClassNotFoundException {
        DTOFactory instance = DTOFactoryImpl.getInstance();
        Invitation result = instance.createDTO(Invitation.class);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testCreateDTOforSaltedPassword() throws ClassNotFoundException {
        DTOFactory instance = DTOFactoryImpl.getInstance();
        SaltedPassword result = instance.createDTO(SaltedPassword.class);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testCreateDTOforSalt() throws ClassNotFoundException {
        DTOFactory instance = DTOFactoryImpl.getInstance();
        Salt result = instance.createDTO(Salt.class);
        Assert.assertNotNull(result);
    }
    
}
