<@compress single_line=true>
<#escape x as x?html>
<#if pagingCompanies?? && !pagingCompanies.exceed>
	<tbody current-page="${pagingCompanies.currentPage}" total-page="${pagingCompanies.totalPage}">
	<#list pagingCompanies.list as company>
		<tr company-id="${company.id}">
			<td>${company.company_id?trim}</td>
			<td>${company.company_name?trim}</td>
		</tr>
	</#list>
	</tbody>
</#if>
<#if fullCompanies??>
	<tbody>
	<#list fullCompanies as company>
		<tr company-id="${company.id}">
			<td>${company.company_id?trim}</td>
			<td>${company.company_name?trim}</td>
		</tr>
	</#list>
	</tbody>
</#if>
</#escape>
</@compress>