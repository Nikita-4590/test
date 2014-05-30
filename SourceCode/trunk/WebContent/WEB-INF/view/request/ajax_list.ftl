<@compress single_line=true>
<#escape x as x?html>
<#if relationRequests?? && relationRequests.list??>
	<tbody current-page="${relationRequests.currentPage}" total-page="${relationRequests.totalPage}">
	<#list relationRequests.list as relationRequest>
	<tr row-id="${relationRequest.relation_request_id}" <#if relationRequest.status_description ? lower_case  == compare_status> class="account-disabled" </#if>>
		<td>
			<#if relationRequest.status_description??>
				${relationRequest.status_description}
			</#if>
		</td>	
		<td>
			<#if relationRequest.created_at??>
					${relationRequest.created_at}
			</#if>
		</td>
		<td>
			<#if relationRequest.crawl_date??>
				${relationRequest.crawl_date}
			</#if>
		</td>
		<td>
			<#if relationRequest.company_id??>
				${relationRequest.company_id}
			</#if>
		</td>
		
		<td>
			<#if relationRequest.company_name??>
				${relationRequest.company_name}
			</#if>
		</td>
		<td>
			<#if relationRequest.media_name??>
				${relationRequest.media_name}
			</#if>	
		</td>	
		<td>
			<#if relationRequest.relation_request_id??>
				${relationRequest.relation_request_id}
			</#if>	
		</td>
		<td>
			<#if relationRequest.assign_user_name??>
				${relationRequest.assign_user_name}
			</#if>
		</td>	
	</tr>	 
	</#list>
	</tbody>
</#if>
</#escape>
</@compress>