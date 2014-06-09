<#-- @ftlvariable name="nameValue" type="java.lang.String" -->
<#-- @ftlvariable name="descriptionValue" type="java.lang.String" -->

<#-- @ftlvariable name="nameError" type="java.lang.String" -->
<#-- @ftlvariable name="descriptionError" type="java.lang.String" -->

<#-- @ftlvariable name="user" type="flymetomars.business.model.Person" -->
<#-- @ftlvariable name="errorMessage" type="java.util.Map<String, String>" -->
<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->


<div id="title_pane">
    <h3>${title}</h3>
</div>

<div id="content_pane">

<p>
<h4 class="errorMsg">${errorMessage!""}</h4>

<form name="add_expertise" enctype="multipart/form-data" action="${baseUrl}/user/${user.userName}/add_expertise" method="POST">

    <div id="admin_left_pane" class="fieldset_without_border">
        <div class="legend_no_indent">Expertise Details</div>
        <ol>
            <li>
                <label for="name" class="bold">Name:
                    <span icon="required"></span>
                </label>
                <input id="name" name="name" type="text" value="${nameValue!""}">
                <h6 class="errorMsg" id='errorName'>${nameError!""}</h6>
            </li>

            <li>
                <label for="description" class="bold">Description:
                    <span icon="required"></span>
                </label>
                <input id="description" name="description" type="text" value="${descriptionValue!""}">
                <h6 class="errorMsg" id='errorDescription'>${descriptionError!""}</h6>
            </li>

            <li>
                <label class="bold">Level:
                <span icon="required"></span>
                <input name="level" type="radio" value="AMATURE" checked="checked">Amature<br/>
                    <input name="level" type="radio" value="INTRODUCTORY">Introductory<br/>
                    <input name="level" type="radio" value="PROFESSIONAL">Professional<br/>
                    <input name="level" type="radio" value="SEMINAL">Seminal<br/>
                    <input name="level" type="radio" value="EMINENT">Eminent<br/>
                </label>
            </li>

        </ol>
    </div>

	<div id="buttonwrapper">
        <h6 class="errorMsg" id='errorDao'>${daoError!""}</h6>
		<button type="submit">Add New Expertise</button>
                <a href="${baseUrl}/">Cancel</a>
	</div>
        
    
</form>

</div>  <!-- content pane -->