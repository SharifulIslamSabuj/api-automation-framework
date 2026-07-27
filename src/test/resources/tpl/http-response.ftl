<#ftl output_format="HTML">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpResponseAttachment" -->
<#-- Overrides allure-rest-assured's bundled template solely to redact the "accessToken"
     field in response bodies (the only body content in this API that ever carries a live
     secret) before it is rendered into the Allure attachment. Everything else is unchanged
     from the library default. -->
<div>Status code <#if data.responseCode??>${data.responseCode} <#else>Unknown</#if></div>
<#if data.url??><div>${data.url}</div></#if>

<#if data.body??>
<h4>Body</h4>
<div>
    <pre class="preformated-text">
    <#t>${data.body?replace('("accessToken"\\s*:\\s*")[^"]*(")', '$1[ BLACKLISTED ]$2', 'r')}
    </pre>
</div>
</#if>

<#if (data.headers)?has_content>
<h4>Headers</h4>
<div>
    <#list data.headers as name, value>
        <div>${name}: ${value!"null"}</div>
    </#list>
</div>
</#if>


<#if (data.cookies)?has_content>
<h4>Cookies</h4>
<div>
    <#list data.cookies as name, value>
        <div>${name}: ${value!"null"}</div>
    </#list>
</div>
</#if>
