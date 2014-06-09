<#-- @ftlvariable name="location" type="flymetomars.business.model.Location" -->
<#-- @ftlvariable name="mission" type="flymetomars.business.model.Mission" -->
<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->
<#-- @ftlvariable name="errorMessage" type="java.lang.String" -->

<div id="title_pane">
    <h3>Mission Details</h3>
</div>

<div id="content_pane">
    <h4 class="errorMsg">${errorMessage!""}</h4>

    <#if mission??>
        <div id="admin_right_pane">
            <fieldset>
            <legend>Mission Details</legend>
                <span class="bold">Name: </span>${mission.name!""}
                <br/>
                <span class="bold">Time: </span>${mission.time!""}
                <br/>
                <span class="bold">Location: </span>
                <a href="${baseUrl}/location/${mission.location.id?c}">${mission.location.town!""}</a>
                <br/>
                <span class="bold">Description: </span>${mission.description!""}
            </fieldset>
            <br/>
            <fieldset>
                <legend>Invitations</legend>
                <ul>
                <#list mission.invitationSet as invitation>
                    <li><a href="${baseUrl}/invitation/${invitation.id?c}">${invitation.id?c}</a></li>
                </#list>
                </ul>

            </fieldset>
        </div>
        <div id="buttonwrapper">
            <a href="./${mission.id?c}/create_invitation">Create invitation</a>
        </div>
    </#if>

</div>	<!-- content pane -->