<#-- @ftlvariable name="expertise" type="flymetomars.business.model.expertise" -->
<#-- @ftlvariable name="baseUrl" type="java.lang.String" -->
<#-- @ftlvariable name="errorMessage" type="java.lang.String" -->

<div id="title_pane">
    <h3>Expertise Details</h3>
</div>

<div id="content_pane">
    <h4 class="errorMsg">${errorMessage!""}</h4>

    <#if expertise??>
        <div id="admin_right_pane">
            <fieldset>
            <legend>Expertise Details</legend>
                <span class="bold">Name: </span>${expertise.name!""}
                <br/>
                <span class="bold">Level: </span>${expertise.level.toString()!""}
                <br/>
                <span class="bold">Description: </span>${expertise.description!""}
            </fieldset>
            <br/>
        </div>
    </#if>

</div>	<!-- content pane -->