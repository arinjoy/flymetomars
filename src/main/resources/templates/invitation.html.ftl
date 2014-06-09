<#-- @ftlvariable name="invitation" type="flymetomars.business.model.Invitation" -->
<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->
<#-- @ftlvariable name="errorMessage" type="java.lang.String" -->
<#-- @ftlvariable name="authenticatedUser" type="flymetomars.business.model.Person" -->

<div id="title_pane">
    <h3>Invitation Details</h3>
</div>

<div id="content_pane">
    <h4 class="errorMsg">${errorMessage!""}</h4>

    <#if invitation??>
        <div id="admin_right_pane">
            <fieldset>
            <legend>Details</legend>
                <span class="bold">Creator: </span><a href="${baseUrl}/user/${invitation.creator.userName}">${invitation.creator.firstName!""} ${invitation.creator.lastName!""}</a>
                <br/>
                <span class="bold">Recipient: </span>
                <#if invitation.recipient??>
                    <a href="${baseUrl}/user/${invitation.recipient.userName}">${invitation.recipient.firstName!""} ${invitation.recipient.lastName!""}</a>
                <#else>
                    Not yet sent. You need to implement this to send it to a recipient.
                </#if>
                <br/>
                <span class="bold">Mission: </span><a href="${baseUrl}/mission/${invitation.mission.id?c}">${invitation.mission.name!""}</a>
                <br/>
                <span class="bold">Status: </span>${invitation.status.toString()!""}
            </fieldset>
            <br/>
             
            
            <form name="invitation" enctype="multipart/form-data" action="${baseUrl}/invitation/${invitation.id?c}" method="POST">

                    <#if invitation.status.toString()=="sent" && invitation.recipient == authenticatedUser>
                        <button type="submit" name="newStatus" value="ACCEPTED">Accept</button>
                        <button type="submit" name="newStatus" value="DECLINED">Decline</button>
                    </#if>
                    <#if invitation.status.toString()=="accepted" && invitation.recipient == authenticatedUser>
                        <button type="submit" name="newStatus" value="DECLINED">Decline</button>
                    </#if>
                    <#if invitation.status.toString()=="declined" && invitation.recipient == authenticatedUser>
                        <button type="submit" name="newStatus" value="ACCEPTED">Accept</button>
                    </#if>
            </form>
            <br/>
        </div>
    </#if>
</div>	<!-- content pane -->