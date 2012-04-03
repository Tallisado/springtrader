package org.springframework.nanotrader.web.controller;


import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.nanotrader.service.TradingServiceImpl;
import org.springframework.nanotrader.web.configuration.ServiceTestConfiguration;
import org.springframework.util.FileCopyUtils;


/**
 *  OrderControllerTest tests the Order  REST api
 *  
 *  @author Brian Dussault 
 *  @author
 */


public class OrderControllerTest extends AbstractSecureControllerTest {
	private static String PURCHASE_DATE = "2012-02-20";

	@Test
	public void getOrderByIdJson() throws Exception {
		mockMvc.perform(get("/account/" + ServiceTestConfiguration.ACCOUNT_ID + "/order/999/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().type(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.orderid").value(ServiceTestConfiguration.ORDER_ID))
				.andExpect(jsonPath("$.completiondate").value(PURCHASE_DATE))
				.andExpect(jsonPath("$.opendate").value(PURCHASE_DATE))
				.andExpect(jsonPath("$.orderfee").value(TradingServiceImpl.DEFAULT_ORDER_FEE.doubleValue()))
				.andExpect(jsonPath("$.price").value(ServiceTestConfiguration.ORDER_PRICE.intValue()))
				.andExpect(jsonPath("$.quantity").value(ServiceTestConfiguration.ORDER_QUANTITY.intValue()))
				.andExpect(jsonPath("$.ordertype").value(ServiceTestConfiguration.ORDER_TYPE_BUY))
				.andExpect(jsonPath("$.orderstatus").value(ServiceTestConfiguration.ORDER_STATUS_CLOSED))				
				.andExpect(jsonPath("$.quote.symbol").value(ServiceTestConfiguration.SYMBOL))
				.andExpect(jsonPath("$.quote.companyname").value(ServiceTestConfiguration.COMPANY_NAME))
				.andDo(print());
	}

	@Test
	public void getOrderByIdNoRecordsFoundJson() throws Exception {
		mockMvc.perform(get("/account/" + ServiceTestConfiguration.ACCOUNT_ID + "/order/"+ ServiceTestConfiguration.ORDER_ID).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andDo(print());
	}
	
	@Test
	public void getOrders() throws Exception {
		mockMvc.perform(get("/account/" + ServiceTestConfiguration.ACCOUNT_ID + "/order").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().type(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].orderid").value(ServiceTestConfiguration.ORDER_ID))
				.andExpect(jsonPath("$.[0].completiondate").value(PURCHASE_DATE))
				.andExpect(jsonPath("$.[0].opendate").value(PURCHASE_DATE))
				.andExpect(jsonPath("$.[0].orderfee").value(TradingServiceImpl.DEFAULT_ORDER_FEE.doubleValue()))
				.andExpect(jsonPath("$.[0].price").value(ServiceTestConfiguration.ORDER_PRICE.intValue()))
				.andExpect(jsonPath("$.[0].quantity").value(ServiceTestConfiguration.ORDER_QUANTITY.intValue()))
				.andExpect(jsonPath("$.[0].ordertype").value(ServiceTestConfiguration.ORDER_TYPE_BUY))
				.andExpect(jsonPath("$.[0].orderstatus").value(ServiceTestConfiguration.ORDER_STATUS_CLOSED))				
				.andExpect(jsonPath("$.[0].quote.symbol").value(ServiceTestConfiguration.SYMBOL))
				.andExpect(jsonPath("$.[0].quote.companyname").value(ServiceTestConfiguration.COMPANY_NAME))
				.andDo(print());
	}
	
	
	@Test
	public void getOrdersNoRecordsFoundJson() throws Exception {
		mockMvc.perform(get("/account/3/order/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
				
	}

	@Test
	public void getClosedOrders() throws Exception {
		mockMvc.perform(get("/account/" + ServiceTestConfiguration.ACCOUNT_ID + "/order").accept(MediaType.APPLICATION_JSON).param("status", "closed"))
				.andExpect(status().isOk())
				.andExpect(content().type(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.[0].orderid").value(ServiceTestConfiguration.ORDER_ID))
				.andExpect(jsonPath("$.[0].completiondate").value(PURCHASE_DATE))
				.andExpect(jsonPath("$.[0].opendate").value(PURCHASE_DATE))
				.andExpect(jsonPath("$.[0].orderfee").value(TradingServiceImpl.DEFAULT_ORDER_FEE.doubleValue()))
				.andExpect(jsonPath("$.[0].price").value(ServiceTestConfiguration.ORDER_PRICE.intValue()))
				.andExpect(jsonPath("$.[0].quantity").value(ServiceTestConfiguration.ORDER_QUANTITY.intValue()))
				.andExpect(jsonPath("$.[0].ordertype").value(ServiceTestConfiguration.ORDER_TYPE_BUY))
				.andExpect(jsonPath("$.[0].orderstatus").value(ServiceTestConfiguration.ORDER_STATUS_CLOSED))				
				.andExpect(jsonPath("$.[0].quote.symbol").value(ServiceTestConfiguration.SYMBOL))
				.andExpect(jsonPath("$.[0].quote.companyname").value(ServiceTestConfiguration.COMPANY_NAME))
				.andDo(print());
	}

	
	@Test
	public void createOrderBuyJson() throws Exception {
		byte[] jsonRequest = FileCopyUtils.copyToByteArray(new ClassPathResource("create-order.json").getFile());
		mockMvc.perform(
				post("/account/" + ServiceTestConfiguration.ACCOUNT_ID + "/order").accept(MediaType.APPLICATION_JSON).body(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()) // HTTP 201 - Created
				.andDo(print());
	}

	@Test
	public void createOrderSellJson() throws Exception {
		byte[] jsonRequest = FileCopyUtils.copyToByteArray(new ClassPathResource("create-order-sell.json").getFile());
		mockMvc.perform(
				post("/account/" + ServiceTestConfiguration.ACCOUNT_ID + "/order").accept(MediaType.APPLICATION_JSON).body(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()) // HTTP 201 - Created
				.andDo(print());
	}


}
