package flymetomars.business.handling;

import flymetomars.business.model.Invitation;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Arinjoy Biswas
 */
public class InvitationHandlerIntegrationTest {

    private Mission mis;

    @Before
    public void beforeEachTestCase() {
        Person cap = new Person();
        cap.setEmail("arin@hoy.com");
        cap.setUserName("hello_world");
        mis = new Mission();
        mis.setName("nice mision");
        mis.setCaptain(cap);
    }
    
    /**
     * Success test case for sending invitation
     */
    @Test
    public void testSendInvitationSucess() {
        //mocking a person model object 
        Person per = Mockito.mock(Person.class);
        Set<Invitation> invitationsReceived = new HashSet<Invitation>();
        // mocking the behaviour to return the person's invitation received
        Mockito.when(per.getInvitationsReceived()).thenReturn(invitationsReceived);

        // creating a new invitation
        Invitation inv = new Invitation();
        inv.setStatus(flymetomars.common.datatransfer.Invitation.InvitationStatus.CREATED);
        Date curDate = Calendar.getInstance().getTime();
        inv.setLastUpdated(curDate);

        // calling the invitation handler to send the invitaton inv to the person per
        InvitationHandler invHandler = new InvitationHandler();
        invHandler.sendInvitation(inv, per);

        // assert that this invitationrecieved size is 1 after adding sending/adding one invitation
        Assert.assertEquals(invitationsReceived.size(), 1);
        // the 'inv' obhect must be the only object of the Set
        Assert.assertEquals(invitationsReceived.iterator().next().hashCode(), inv.hashCode());
        Assert.assertEquals(invitationsReceived.iterator().next().getStatus(), flymetomars.common.datatransfer.Invitation.InvitationStatus.SENT);
    }

    /**
     * Success test case for accepting invitation
     */
    @Test
    public void testAcceptInvitationSuccess() {
        Person per = Mockito.mock(Person.class);
        Invitation inv = new Invitation();
        inv.setMission(mis);
        // a complete mission object created in @before method
        Set<Invitation> invSet = new HashSet<Invitation>();
        invSet.add(inv);
        Mockito.when(per.getInvitationsReceived()).thenReturn(invSet);

        InvitationHandler invHandler = new InvitationHandler();
        invHandler.acceptInvitation(inv, per);

        Assert.assertTrue(inv.getMission().getParticipantSet().contains(per));
        Assert.assertEquals(inv.getStatus(), flymetomars.common.datatransfer.Invitation.InvitationStatus.ACCEPTED);
    }

    /**
     * Failure test case for accepting invitation
     */
    //@Test(expected = IllegalArgumentException.class)
    public void testAcceptInvitationUnknownInvitationFailure() {
        Person per = Mockito.mock(Person.class);
        Invitation inv = new Invitation();
        Set<Invitation> invSet = new HashSet<Invitation>();
        inv.setRecipient(per);
        Mockito.when(per.getInvitationsReceived()).thenReturn(invSet);
        InvitationHandler invHandler = new InvitationHandler();
        invHandler.acceptInvitation(inv, per);
    }

    /**
     * Success test case for rejecting invitation
     */
    @Test
    public void testRejectInvitationSuccess() {
        Person per = Mockito.mock(Person.class);
        Invitation inv = new Invitation();
        inv.setMission(mis);
        Set<Invitation> invSet = new HashSet<Invitation>();
        invSet.add(inv);
        Set<Person> partSet = new HashSet<Person>();
        partSet.add(per);
        mis.setParticipantSet(partSet);

        Mockito.when(per.getInvitationsReceived()).thenReturn(invSet);

        InvitationHandler invHandler = new InvitationHandler();
        invHandler.rejectInvitation(inv, per);

        Assert.assertFalse(inv.getMission().getParticipantSet().contains(per));
        Assert.assertEquals(inv.getStatus(), flymetomars.common.datatransfer.Invitation.InvitationStatus.DECLINED);
    }

    /**
     * Failure test case for rejecting invitation
     */
    //@Test(expected = IllegalArgumentException.class)
    public void testRejectInvitationUnknownInvitation() {
        Person per = Mockito.mock(Person.class);
        Invitation inv = new Invitation();
        Set<Invitation> invSet = new HashSet<Invitation>();

        Mockito.when(per.getInvitationsReceived()).thenReturn(invSet);

        InvitationHandler invHandler = new InvitationHandler();
        invHandler.rejectInvitation(inv, per);
    }
}
