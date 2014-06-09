
<#-- @ftlvariable name="expertiseGained" type="java.util.Set<flymetomars.business.model.Expertise>" -->
<#-- @ftlvariable name="invitationsReceived" type="java.util.Set<flymetomars.business.model.Invitation>" -->
<#-- @ftlvariable name="missionsRegistered" type="java.util.Set<flymetomars.business.model.Mission>" -->
<#-- @ftlvariable name="invitationsCreated" type="java.util.Set<flymetomars.business.model.Invitation>" -->
<#-- @ftlvariable name="interestsCreated" type="java.util.Set<flymetomars.business.model.Interest>" -->
<#-- @ftlvariable name="missionsCreated" type="java.util.Set<flymetomars.business.model.Mission>" -->
<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->
<#-- @ftlvariable name="errorMessage" type="java.lang.String" -->
<#-- @ftlvariable name="authenticatedUsername" type="java.lang.String" -->
<#-- @ftlvariable name="requestedUser" type="flymetomars.business.model.Person" -->
<#-- @ftlvariable name="popularPerson" type="flymetomars.business.model.Person" -->
<#-- @ftlvariable name="buddies" type="java.util.List<flymetomars.business.model.Person>" -->
<#-- @ftlvariable name="celebrities" type="java.util.List<flymetomars.business.model.Person>" -->
<#-- @ftlvariable name="friends" type="java.util.Set<flymetomars.business.model.Person>" -->
<#-- @ftlvariable name="powerBrokers" type="java.util.Set<flymetomars.business.model.Person>" -->
<#-- @ftlvariable name="socialCircle" type="java.util.Set<flymetomars.business.model.Person>" -->
<#-- @ftlvariable name="sourGrapes" type="java.util.List<flymetomars.business.model.Mission>" -->

<div id="title_pane">
    <h3>User Details</h3>
</div>

<div id="content_pane">
    <h4 class="errorMsg">${errorMessage!""}</h4>

    <#if requestedUser??>
        <div id="admin_right_pane">
            <fieldset>
            <legend>Details</legend>
                <span class="bold">User Name: </span>${requestedUser.userName!""}
                <br/>
                <span class="bold">Email Address: </span>${requestedUser.email!""}
                <br/>
                <span class="bold">First Name: </span>${requestedUser.firstName!""}
                <br/>
                <span class="bold">Last Name: </span>${requestedUser.lastName!""}
            </fieldset>
            <br/>
            <fieldset>
            <legend>My Expertise:</legend>
            <#if expertiseGained?? && expertiseGained?has_content>
                <#list expertiseGained as expertise>
                    <ul>
                        <li><a href="${baseUrl}/expertise/${expertise.id?c}">${expertise.name}</a></li>
                    </ul>
                </#list>
            <#else>
                <p>You have no expertise!</p>
            </#if>
            </fieldset>
        </div>

    <div>
        <fieldset>
            <legend>Missions created by me:</legend>
            <#if missionsCreated?? && missionsCreated?has_content>
                <#list missionsCreated as mission>
                    <ul>
                        <li><a href="${baseUrl}/mission/${mission.id?c}">${mission.name}</a></li>
                    </ul>
                </#list>
            <#else>
                <p>You haven't created any mission yet!</p>
            </#if>
            </fieldset>
    </div>

    <div>
        <fieldset>
            <legend>Invitations created by me:</legend>
            <#if invitationsCreated?? && invitationsCreated?has_content>
                <#list invitationsCreated as invitation>
                    <ul>
                        <li><a href="${baseUrl}/invitation/${invitation.id?c}">Invitation for ${invitation.mission.name} to ${invitation.recipient.userName}, status: ${invitation.status}</a>
                        <#if invitation.status == "created">
                        	<form style="display:inline;" name="edit_invitation" enctype="multipart/form-data" action="${baseUrl}/invitation/${invitation.id?c}" method="POST">
                        		<input style="display:none;" name="newStatus" type="hidden" value="SENT">
                        		<button type="submit">Send Invitation</button>
                        	</form>
                        </#if>
                        </li>
                    </ul>
                </#list>
            <#else>
                <p>You haven't created any invitation yet!</p>
            </#if>
        </fieldset>
    </div>

    <div>
        <fieldset>
            <legend>Missions registered:</legend>
            <#if missionsRegistered?? && missionsRegistered?has_content>
                <#list missionsRegistered as missionRegistered>
                    <ul>
                        <li><a href="${baseUrl}/mission/${missionRegistered.id?c}">${missionRegistered.name}</a></li>
                    </ul>
                </#list>
            <#else>
                <p>You don't have any mission registered!</p>
            </#if>
        </fieldset>
    </div>

    <div>
        <fieldset>
            <legend>Invitations received:</legend>
            <#if invitationsReceived?? && invitationsReceived?has_content>
                <#list invitationsReceived as invitation>
                    <ul>
                    	<#switch invitation.status>
                    		<#case "created">
                    			<#break>
                    		<#case "sent">
                    			<li><a href="${baseUrl}/invitation/${invitation.id?c}">Invitation to ${invitation.mission.name} from ${invitation.creator.userName}</a>, status: ${invitation.status}
	                    			<form style="display:inline" name="edit_invitation" enctype="multipart/form-data" action="${baseUrl}/invitation/${invitation.id?c}" method="POST">
		                        		<input style="display:none;" name="newStatus" type="hidden" value="ACCEPTED">
		                        		<button type="submit">Accept Invitation</button>
		                        	</form>
		                        	<form style="display:inline" name="edit_invitation" enctype="multipart/form-data" action="${baseUrl}/invitation/${invitation.id?c}" method="POST">
		                        		<input style="display:none" name="newStatus" type="hidden" value="DECLINED">
		                        		<button type="submit">Decline Invitation</button>
		                        	</form>
                    			</li>
                    			<#break>
                    		<#case "accepted">
                    			<li><a href="${baseUrl}/invitation/${invitation.id?c}">Invitation to ${invitation.mission.name}</a> from ${invitation.creator.userName}, status: ${invitation.status}
                    				<form style="display:inline" name="edit_invitation" enctype="multipart/form-data" action="${baseUrl}/invitation/${invitation.id?c}" method="POST">
		                        		<input style="display:none;" name="newStatus" type="hidden" value="DECLINED">
		                        		<button type="submit">I've change my mind! Decline Invitation</button>
		                        	</form>
                    			</li>
                    			<#break>
                    		<#case "declined">
                    			<li><a href="${baseUrl}/invitation/${invitation.id?c}">Invitation to ${invitation.mission.name}</a> from ${invitation.creator.userName}, status: ${invitation.status}
                    				<form style="display:inline" name="edit_invitation" enctype="multipart/form-data" action="${baseUrl}/invitation/${invitation.id?c}" method="POST">
		                        		<input style="display:none;" name="newStatus" type="hidden" value="ACCEPTED">
		                        		<button type="submit">I change my mind! Accept Invitation</button>
		                        	</form>
                    			</li>
                    			<#break>
                    	</#switch>
                    </ul>
                </#list>
            <#else>
                <p>You haven't received any invitation yet!</p>
            </#if>
        </fieldset>
    </div>

    <div>
        <fieldset>
            <legend>Your buddies are:</legend>
            <#if buddies?? && buddies?has_content>
                <#list buddies as bud>
                    <ul>
                        <li><a href="${baseUrl}/user/${bud.userName}">${bud.firstName} ${bud.lastName}</a></li>
                    </ul>
                </#list>
            <#else>
                <p>No, you have no buddies!</p>
            </#if>
        </fieldset>
    </div>
    <div>
            <fieldset>
                <legend>The Celebrities are:</legend>
                <#if celebrities?? && celebrities?has_content>
                    <#list celebrities as cel>
                        <ul>
                            <li><a href="${baseUrl}/user/${cel.userName}">${cel.firstName} ${cel.lastName}</a></li>
                        </ul>
                    </#list>
                <#else>
                    <p>No, there are no celebrities.. Rejoice!</p>
                </#if>
            </fieldset>
        </div>
        <div>
            <fieldset>
                <legend>Your friends are:</legend>
                <#if friends?? && friends?has_content>
                    <#list friends as fry>
                        <ul>
                            <li><a href="${baseUrl}/user/${fry.userName}">${fry.firstName} ${fry.lastName}</a></li>
                        </ul>
                    </#list>
                <#else>
                    <p>No!</p>
                </#if>
            </fieldset>
        </div>
        <div>
            <fieldset>
                <legend>The most popular person is:</legend>
                    <ul>
                        <li><a href="${baseUrl}/user/${popularPerson.userName}">${popularPerson.firstName} ${popularPerson.lastName}</a></li>
                    </ul>
            </fieldset>
        </div>
        <div>
            <fieldset>
                <legend>Your social circle is:</legend>
                <#if socialCircle?? && socialCircle?has_content>
                    <#list socialCircle as soc>
                        <ul>
                            <li><a href="${baseUrl}/user/${soc.userName}">${soc.userName}">${soc.firstName} ${soc.lastName}</a></li>
                        </ul>
                    </#list>
                <#else>
                    <p>No!</p>
                </#if>
            </fieldset>
        </div>
        <div>
            <fieldset>
                <legend>Sour grapes:</legend>
                <#if sourGrapes?? && sourGrapes?has_content>
                    <#list sourGrapes as sgs>
                        <ul>
                            <li><a href="${baseUrl}/mission/${sgs.id}">${sgs.id}">${sgs.name}</a> ${sgs.description}</li>
                        </ul>
                    </#list>
                <#else>
                    <p>No!</p>
                </#if>
            </fieldset>
        </div>

        <br/>
        <div id="buttonwrapper">
        <a href="${baseUrl}/location/create">Create Location</a>
        <br />
        <a href="${baseUrl}/mission/create">Create Mission</a>
        <br />
        <a href="${baseUrl}/user/${requestedUser.userName}/add_expertise">Add Expertise</a>
        <br />
    </div>

    </#if>
</div>	<!-- content pane -->