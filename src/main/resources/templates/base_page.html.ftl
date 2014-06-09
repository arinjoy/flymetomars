<#-- @ftlvariable name="deployMode" type="boolean" -->
<#-- @ftlvariable name="pageTitle" type="java.lang.String" -->
<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->
<#-- @ftlvariable name="contentTemplate" type="java.lang.String" -->
<#-- @ftlvariable name="user" type="flymetomars.model.Person" -->
<#-- @ftlvariable name="displayTabImport" type="boolean" -->


<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>${pageTitle}</title>

	<meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Fly me to Mars - a mission registration system.">
</head>

<body>

	    <span><h3>Fly me to Mars - a mission registration system.</h3></span>
	</div>

    <!-- navigation bar-->
    <div id="user_div">
        <!-- user details -->
        <#if user??>
            <p>Welcome, ${user.firstName!""} ${user.lastName!""}!</p>
            <ul id="user_list">
                <li ><a href="${baseUrl}/user/${user.userName!"unknown-username"}">${user.userName!""} - Settings</a></li>
                <li ><a href="${baseUrl}/j_spring_security_logout">Logout</a></li>
            </ul>
        <#else>
            <p><a href="${baseUrl}/login">Login</a></p>
        </#if>

        <hr align=left noshade size=10 width=50%>
    </div>
    <br>

	<!-- content pane -->
    <#include contentTemplate/>

</body>
</html>