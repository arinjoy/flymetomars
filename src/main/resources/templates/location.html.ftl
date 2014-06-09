<#-- @ftlvariable name="location" type="flymetomars.business.model.location" -->
<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->
<#-- @ftlvariable name="errorMessage" type="java.lang.String" -->

<div id="title_pane">
    <h3>Location Details</h3>
</div>

<div id="content_pane">
    <h4 class="errorMsg">${errorMessage!""}</h4>

    <#if location??>
        <div id="admin_right_pane">
            <fieldset>
            <legend>Location Details</legend>
                <span class="bold">Unique ID: </span>${location.id!""}
                <br/>
                <span class="bold">Floor: </span>${location.floor!""}
                <br/>
                <span class="bold">Street No: </span>${location.streetNo!""}
                <br/>
                <span class="bold">Street: </span>${location.street!""}
                <br/>
                 <span class="bold">Landmark: </span>${location.landmark!""}
                <br/>
                <span class="bold">Town/City: </span>${location.town!""}
                <br/>
                <span class="bold">Region: </span>${location.region!""}
                <br/>
                <span class="bold">Postcode: </span>${location.postcode!""}
                <br/>
                <span class="bold">Country: </span>${location.country!""}
                
            </fieldset>
            <br/>
        </div>
    </#if>
    
        <div id="buttonwrapper">
         <a href="${baseUrl}/mission/create">Create New Mission</a>
        <br />

</div>	<!-- content pane -->