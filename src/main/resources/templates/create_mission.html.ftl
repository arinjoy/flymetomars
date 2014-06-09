<#-- @ftlvariable name="descriptionValue" type="java.lang.String" -->
<#-- @ftlvariable name="locationValue" type="java.lang.String" -->
<#-- @ftlvariable name="daoError" type="java.lang.String" -->
<#-- @ftlvariable name="descriptionError" type="java.lang.String" -->
<#-- @ftlvariable name="locationError" type="java.lang.String" -->
<#-- @ftlvariable name="timeError" type="java.lang.String" -->
<#-- @ftlvariable name="timeValue" type="java.lang.String" -->
<#-- @ftlvariable name="missionNameValue" type="java.lang.String" -->
<#-- @ftlvariable name="missionNameError" type="java.lang.String" -->
<#-- @ftlvariable name="errorMessage" type="java.util.Map<String, String>" -->
<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->
<#-- @ftlvariable name="availableLocations" type="java.util.Set<flymetomars.business.model.Location>" -->


<div id="title_pane">
    <h3>${title}</h3>
</div>

<div id="content_pane">

<p>
<h4 class="errorMsg">${errorMessage!""}</h4>

<form name="create_mission" enctype="multipart/form-data" action="${baseUrl}/mission/create" method="POST">

        <div>
        <fieldset>
            <legend>List of locations available:</legend>
            <#if availableLocations?? && availableLocations?has_content>
                <#list availableLocations as location>
                    <ul>
                        <li><a href="${baseUrl}/location/${location.id?c}">${location.id?c}</a></li>
                    </ul>
                </#list>
            <#else>
                <p>There are no locations available</p>
            </#if>
            </fieldset>
        </div>
    
	<div id="admin_left_pane" class="fieldset_without_border">
		<div class="legend_no_indent">Mission Details</div>
		<ol>
			<li>
				<label for="missionName" class="bold">Mission Name:
					<span icon="required"></span>
				</label>
				<input id="missionName" name="missionName" type="text" value="${missionNameValue!""}">
				<h6 class="errorMsg" id='errorUserName'>${missionNameError!""}</h6>
			</li>
			<li>
				<label for="time" class="bold">Date and time (dd/mm/yyyy, HH AM/PM):
					<span icon="required"></span>
				</label>
				<input id="time" name="time" type="text" value="${timeValue!""}">
				<h6 class="errorMsg" id='errorTime'>${timeError!""}</h6>
			</li>
			<li>
				<label for="location" class="bold">Location ID:
					<span icon="required"></span>
				</label>
				<input id="location" name="location" type="text" value="${locationValue}">
				<h6 class="errorMsg" id='errorLocation'>${locationError!""}</h6>
			</li>
			<li>
				<label for="description" class="bold">Description:
					<span icon="required"></span>
				</label>
				<input id="description" name="description" type="text" value="${descriptionValue}">
				<h6 class="errorMsg" id='errorDescription'>${descriptionError!""}</h6>
			</li>
		</ol>
	</div>

	<div id="buttonwrapper1">
        <h6 class="errorMsg" id='errorDao'>${daoError!""}</h6>
		<button type="submit">Create New Mission</button>
        <a href="${baseUrl}/">Cancel</a>
	</div>
    
        
        <a href="${baseUrl}/location/create">Create Location</a>
        
</form>

</div>  <!-- content pane -->