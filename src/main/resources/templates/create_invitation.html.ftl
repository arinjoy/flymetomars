<#-- @ftlvariable name="receipientError" type="java.lang.String" -->
<#-- @ftlvariable name="receipientValue" type="java.lang.String" -->
<#-- @ftlvariable name="mission" type="flymetomars.business.model.Mission" -->
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


<div id="title_pane">
    <h3>${title}</h3>
</div>

<div id="content_pane">

<p>
<h4 class="errorMsg">${errorMessage!""}</h4>

<form name="create_invitation" enctype="multipart/form-data" action="${baseUrl}/mission/${mission.id?c}/create_invitation" method="POST">

	<div id="admin_left_pane" class="fieldset_without_border">
		<div class="legend_no_indent">Invitation Details</div>
		<ol>
			<li>
				<label for="receipient" class="bold">Recipient:
					<span icon="required"></span>
				</label>
				<input id="receipient" name="receipient" type="text" value="${receipientValue!""}">
				<h6 class="errorMsg" id='errorUserName'>${receipientError!""}</h6>
			</li>
			<li>
				<label class="bold">Send now?:
					<span icon="required"></span>
                                        <input name="toSend" type="radio" value="CREATED" checked="checked">No<br/>
                    <input name="toSend" type="radio" value="SENT">Yes<br/>
                </label>
                <h6 class="errorMsg" id='errorTime'>${timeError!""}</h6>
			</li>
		</ol>
	</div>

	<div id="buttonwrapper">
        <h6 class="errorMsg" id='errorDao'>${daoError!""}</h6>
		<button type="submit">Create New Invitation</button>
        <a href="${baseUrl}/">Cancel</a>
	</div>
</form>

</div>  <!-- content pane -->