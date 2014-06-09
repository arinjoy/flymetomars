<#-- @ftlvariable name="springRedirect" type="java.lang.String" -->
<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->
<#-- @ftlvariable name="message" type="java.lang.String" -->
<#-- @ftlvariable name="errorMessage" type="java.lang.String" -->
<#-- @ftlvariable name="detailedMessage" type="java.lang.String" -->
<#-- @ftlvariable name="reg_success" type="java.lang.String" -->

<div id="title_pane">
    <h3>Login</h3>
</div>

<div id="content_pane">
    <#if reg_success??><h2><h2></#if>
    <form name="f" action="${baseUrl}/j_spring_security_check" method="POST">
	    <div class="fieldset" id="login">
			<div class="legend">Login with your Fly me to Mars username and password</div>
			<ol>
				<li>
					<label for="j_username" class="bold">Username: </label>
					<input id="j_username" class="medium" name="j_username" type="text" value="">
				</li>
				<li>
					<label for="j_password" class="bold">Password:</label>
					<input id="j_password" class="medium" name="j_password" type="password">
				</li>
                <#if errorMessage?? && errorMessage?has_content>
                    <li><h4 class="errorMsg">${errorMessage}</h4></li>
                </#if>
                <#if message?? && message?has_content>
                    <li><h4>${message!""}</h4></li>
                </#if>
                <#if detailedMessage?? && detailedMessage?has_content>
                    <li><p>${detailedMessage}</p></li>
                </#if>
                <#if  springRedirect?? && springRedirect !="">
                    <input type="hidden" name="spring-security-redirect" value="${springRedirect}"/>
                </#if>
            </ol>
        </div>

		<div id="buttonwrapper">
            <a href="${baseUrl}/register?init=true">Register</a>
            <br/>
			<button type="submit">Login</button>
		</div>
    </form>
</div>  <!-- content pane -->