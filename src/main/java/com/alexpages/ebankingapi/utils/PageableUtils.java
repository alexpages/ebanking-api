package com.alexpages.ebankingapi.utils;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.alexpages.ebankingapi.domain.PaginationBody;
import com.alexpages.ebankingapi.domain.PageResponse;
 
public class PageableUtils
{
	private PageableUtils() {
		 throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
	public static Pageable getPageable(PaginationBody paginationBody)
	{
		Pageable pageable = null;
		if(paginationBody == null) {
			pageable = Pageable.unpaged();
		} else {
			int pageIndex = paginationBody.getPage().intValueExact() - 1;
			pageable = PageRequest.of(pageIndex, paginationBody.getSize().intValueExact());
		}
		return pageable;
	}
	public static PageResponse getPaginationResponse(Page<?> page, Pageable pageable)
	{
		PageResponse paginationResponse = null;
		if(pageable.isPaged())
		{
			paginationResponse = new PageResponse();
			Integer pageNumer = page.getNumber() + 1;
			paginationResponse.setNumber(pageNumer);
			paginationResponse.setSize(page.getSize());
			paginationResponse.setTotalPages(page.getTotalPages());
			paginationResponse.setTotalElements((long) page.getTotalElements());
		}
		return paginationResponse;
	}
}