<@compress single_line=true>
<#escape x as x?html>
<#if relationRequests?? && relationRequests.list??>
	<tbody current-page="${relationRequests.currentPage}" total-page="${relationRequests.totalPage}">
	<#list relationRequests.list as relationRequest>
	<tr row-id="${relationRequest.relation_request_id}" <#if relationRequest.status  == compare_status> class="account-disabled" </#if>>
		<td>
		    ${relationRequest.status_description!"－"}
		</td>
		<td>
			${relationRequest.created_at!"－"}
		</td>
		<td>
			${relationRequest.crawl_date!"－"}
		</td>
		<td>
			${relationRequest.company_id!"－"}
		</td>
		
		<td>
			${relationRequest.company_name!"－"}
		</td>
		<td>
			${relationRequest.media_name!"－"}
		</td>
		<td>
			${relationRequest.relation_request_id!"－"}
		</td>
		<td>
			${relationRequest.assign_user_name!"－"}
		</td>
	</tr>	 
	</#list>
	</tbody>
</#if>
</#escape>
</@compress>