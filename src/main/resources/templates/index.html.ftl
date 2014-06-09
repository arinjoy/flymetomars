<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->
<#-- @ftlvariable name="user" type="flymetomars.business.model.Person" -->

<div id="title_pane">
    <p></p>
</div>

<div id="content_pane">
<#if user??>
    <p>Welcome, ${user.firstName!""}  ${user.lastName!""}.</p>
    <p>Places to go to: <a href="${baseUrl}/user/${user.userName!"unknown-username"}">User page</a></p>
<#else>
    <p>Welcome to Fly me to Mars, please <a href="${baseUrl}/login">login</a> or <a href="${baseUrl}/register">register</a>.</p>
</#if>
</div>  <!-- content pane -->