<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->
<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="userNameValue" type="java.lang.String" -->
<#-- @ftlvariable name="emailValue" type="java.lang.String" -->
<#-- @ftlvariable name="firstNameValue" type="java.lang.String" -->
<#-- @ftlvariable name="lastNameValue" type="java.lang.String" -->

<#-- @ftlvariable name="errorMessage" type="java.lang.String" -->
<#-- @ftlvariable name="userNameError" type="java.lang.String" -->
<#-- @ftlvariable name="emailError" type="java.lang.String" -->
<#-- @ftlvariable name="passwordError" type="java.lang.String" -->
<#-- @ftlvariable name="firstNameError" type="java.lang.String" -->
<#-- @ftlvariable name="lastNameError" type="java.lang.String" -->


<div id="title_pane">
    <h3>${title}</h3>
</div>

<div id="content_pane">

    <p>
    <h4 class="errorMsg">${errorMessage!""}</h4>

    <form name="create_user" enctype="multipart/form-data" action="${baseUrl}/register" method="POST">

        <div id="admin_left_pane" class="fieldset_without_border">
            <div class="legend_no_indent">Account Details</div>
            <ol>
                <li>
                    <label for="userName" class="bold">User Name:
                        <span icon="required"></span>
                    </label>
                    <input id="userName" name="userName" type="text" value="${userNameValue!""}">
                    <h6 class="errorMsg" id='errorUserName'>${userNameError!""}</h6>
                </li>
                <li>
                    <label for="email" class="bold">Email Address:
                        <span icon="required"></span>
                    </label>
                    <input id="email" name="email" type="text" value="${emailValue!""}">
                    <h6 class="errorMsg" id='errorEmail'>${emailError!""}</h6>
                </li>
                <li>
                    <label for="password" class="bold">Password:
                        <span icon="required"></span>
                    </label>
                    <input id="password" name="password" type="password">
                    <h6 class="errorMsg" id='errorPassword'>${passwordError!""}</h6>
                </li>
                <li>
                    <label for="confirmPassword" class="bold">Confirm Password:
                        <span icon="required"></span>
                    </label>
                    <input id="confirmPassword" name="confirmPassword" type="password">
                    <h6 class="errorMsg" id='errorConfirmPassword'></h6>
                </li>
                <li>
                    <label for="firstName" class="bold">First Name:
                        <span icon="required"></span>
                    </label>
                    <input id="firstName" name="firstName" type="text" value="${firstNameValue!""}">
                    <h6 class="errorMsg" id='errorFirstName'>${firstNameError!""}</h6>
                </li>
                <li>
                    <label for="lastName" class="bold">Last Name:
                        <span icon="required"></span>
                    </label>
                    <input id="lastName" name="lastName" type="text" value="${lastNameValue!""}">
                    <h6 class="errorMsg" id='errorLastName'>${lastNameError!""}</h6>
                </li>
            </ol>
        </div>

        <div id="buttonwrapper">
            <button type="submit">Create New User</button>
            <a href="${baseUrl}/login">Cancel</a>
        </div>
    </form>

</div>  <!-- content pane -->