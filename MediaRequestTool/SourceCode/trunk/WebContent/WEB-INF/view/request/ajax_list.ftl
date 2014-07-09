<@compress single_line=true>
<#escape x as x?html>
<#if relationRequests?? && relationRequests.list??>
	<tbody total-page="${relationRequests.totalPage}"
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
	<tr row-id="${relationRequest.relation_request_id}" <#if relationRequest.status  == compare_status && currentUser == relationRequest.assign_user_id && isDirector == true || relationRequest.status  == compare_status && isDirector == false> class="account-disabled" </#if>>
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