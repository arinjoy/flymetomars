<#-- @ftlvariable name="floorValue" type="java.lang.String" -->
<#-- @ftlvariable name="streetNoValue" type="java.lang.String" -->
<#-- @ftlvariable name="streetValue" type="java.lang.String" -->
<#-- @ftlvariable name="landmarkValue" type="java.lang.String" -->
<#-- @ftlvariable name="townValue" type="java.lang.String" -->
<#-- @ftlvariable name="regionValue" type="java.lang.String" -->
<#-- @ftlvariable name="postcodeValue" type="java.lang.String" -->
<#-- @ftlvariable name="countryValue" type="java.lang.String" -->


<#-- @ftlvariable name="floorError" type="java.lang.String" -->
<#-- @ftlvariable name="streetNoError" type="java.lang.String" -->
<#-- @ftlvariable name="streetError" type="java.lang.String" -->
<#-- @ftlvariable name="landmarkError" type="java.lang.String" -->
<#-- @ftlvariable name="townError" type="java.lang.String" -->
<#-- @ftlvariable name="regionError" type="java.lang.String" -->
<#-- @ftlvariable name="postcodeError" type="java.lang.String" -->
<#-- @ftlvariable name="countryError" type="java.lang.String" -->

<#-- @ftlvariable name="daoError" type="java.lang.String" -->

<#-- @ftlvariable name="errorMessage" type="java.util.Map<String, String>" -->
<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->


<div id="title_pane">
    <h3>${title}</h3>
</div>

<div id="content_pane">

<p>
<h4 class="errorMsg">${errorMessage!""}</h4>

<form name="create_location" enctype="multipart/form-data" action="${baseUrl}/location/create" method="POST">

    <div id="admin_left_pane" class="fieldset_without_border">
        <div class="legend_no_indent">Location Details</div>
        <ol>
            <li>
                <label for="floor" class="bold">Floor:
                    <span icon="required"></span>
                </label>
                <input id="floor" name="floor" type="text" value="${floorValue!""}">
                <h6 class="errorMsg" id='errorFloor'>${floorError!""}</h6>
            </li>

            <li>
                <label for="streetNo" class="bold">Street No:
                    <span icon="required"></span>
                </label>
                <input id="streetNo" name="streetNo" type="text" value="${streetNoValue!""}">
                <h6 class="errorMsg" id='errorStreetNo'>${streetNoError!""}</h6>
            </li>

            <li>
                <label for="street" class="bold">Street:
                    <span icon="required"></span>
                </label>
                <input id="street" name="street" type="text" value="${streetValue!""}">
                <h6 class="errorMsg" id='errorStreet'>${streetError!""}</h6>
            </li>
            
            <li>
                <label for="landmark" class="bold">Landmark:
                    <span icon="required"></span>
                </label>
                <input id="landmark" name="landmark" type="text" value="${landmarkValue!""}">
                <h6 class="errorMsg" id='errorLandmark'>${landmarkError!""}</h6>
            </li>
            <li>
                <label for="town" class="bold">Town/City:
                    <span icon="required"></span>
                </label>
                <input id="town" name="town" type="text" value="${townValue!""}">
                <h6 class="errorMsg" id='errorTown'>${townError!""}</h6>
            </li>
            
           <li>
                <label for="region" class="bold">Region:
                    <span icon="required"></span>
                </label>
                <input id="region" name="region" type="text" value="${regionValue!""}">
                <h6 class="errorMsg" id='errorRegion'>${regionError!""}</h6>
            </li>
            
             <li>
                <label for="postcode" class="bold">Postcode:
                    <span icon="required"></span>
                </label>
                <input id="postcode" name="postcode" type="text" value="${postcodeValue!""}">
                <h6 class="errorMsg" id='errorPostcode'>${postcodeError!""}</h6>
            </li>
            
            <li>
                <label for="country" class="bold">Country:
                    <span icon="required"></span>
                </label>
                <input id="country" name="country" type="text" value="${countryValue!""}">
                <h6 class="errorMsg" id='errorCountry'>${countryError!""}</h6>
            </li>      

        </ol>
    </div>

	<div id="buttonwrapper">
        <h6 class="errorMsg" id='errorDao'>${daoError!""}</h6>
		<button type="submit">Create New Location</button>
                <a href="${baseUrl}/">Cancel</a>
	</div>
    
        <br />
        <a href="${baseUrl}/mission/create">Create Mission</a>
        <br />
    
    
</form>

</div>  <!-- content pane -->