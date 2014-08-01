<@compress single_line=true>
<#escape x as x?html>
<#if relationRequests?? && relationRequests.list??>
	<tbody total-page="${relationRequests.totalPage}" <#if info??>message="${info}"</#if>
                           <#if sort?? && direction??>
                               sort="${sort}"
                               current-page="${relationRequests.currentPage}"
                               direction="${direction}"
                           </#if>
                           <#if searchStatus??>status_select_option="${searchStatus}"</#if>
                           <#if searchText??>input_text_search="${searchText}"</#if>
                           >
	>
	<#list relationRequests.list as relationRequest>
	<tr row-id="${relationRequest.relation_request_id}" 
		<#if requestId ?? && relationRequest.relation_request_id  == requestId> class="request-change-hightlight" <#else> 
		<#if relationRequest.status  == compare_status> class="account-disabled" </#if>
		 </#if>>
		<td>
		    ${relationRequest.status_description!"－"}
		</td>
		<td>
			${relationRequest.created_at!"－"}
		</td>
		<td>
			(${relationRequest.company_id!"－"})
			${relationRequest.company_name!"－"}
		</td>
		<td>
			${relationRequest.requester_name!"－"}
		</td>
		
		<td>
			<#if relationRequest.requester_mail??>
				<a href="mailto:${relationRequest.requester_mail}">${relationRequest.requester_mail}</a>
			<#else>
				－
			</#if>
			
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