<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->
<#-- @ftlvariable name="user" type="flymetomars.business.model.Person" -->

<div id="title_pane">
    <h3>Insufficent Access</h3>
</div>

<div id="content_pane">
<#if user??>
    <p>You have insufficent privileges to access the requested page.</p>
    <p>If you wish to continue you will need to <a href="${baseUrl}/j_spring_security_logout">logout</a>
    and login with an appropriate user account.</p>
<#else>
    <p>You must login to access the requested page.</p>
    <p><a href="${baseUrl}/login">login</a></p>
</#if>
</div>  <!-- content pane -->