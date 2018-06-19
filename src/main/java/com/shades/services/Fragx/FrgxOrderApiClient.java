package com.shades.services.fragx;

import com.google.gson.*;
import com.shades.services.fragx.Exceptions.BadAccessIdOrKeyException;
import com.shades.services.fragx.Exceptions.BadItemIdException;
import com.shades.services.fragx.Exceptions.EmptyOrderException;
import com.shades.services.fragx.Exceptions.NullResponseException;
import com.shades.services.fragx.Interfaces.IFrgxOrderApiClient;
import com.shades.services.fragx.Models.BulkOrder;
import com.shades.services.fragx.Models.BulkOrderResult;
import com.shades.services.fragx.Models.Order;

import java.io.IOException;
import java.util.List;

/**
 * API client used to place bulk orders.
 */
public class FrgxOrderApiClient extends FrgxApiClient implements IFrgxOrderApiClient {
    /**
     * Initializes a new order API client.
     * @param AccessId Api access ID found in FragranceX Api documentation
     * @param AccessKey Api access key found in FragranceX Api documentation
     * @throws IOException throws IO Exception
     */
    public FrgxOrderApiClient(String AccessId, String AccessKey) throws IOException, BadAccessIdOrKeyException {
        super(AccessId, AccessKey);
    }

    /**
     * Initializes a new order API client.  This constructor should only be
     * used after a constructor with API access Id and API access key.
     * @throws BadAccessIdOrKeyException Access Id or Access key is wrong
     */
    public FrgxOrderApiClient() throws BadAccessIdOrKeyException {
        super();
    }

    /**
     * Places bulk order.  Do not place one order at a time, it is best to send
     * multiple orders together in a batch and submit the batch a few times per day.
     * The Api uses your default credit card for payment.  You can add or modify your credit card
     * from the My Account &gt; Payment
     * @param bulkOrder A bulk order object
     * @return An object of BulkOrderResult
     */
    public BulkOrderResult PlaceBulkOrder(BulkOrder bulkOrder) throws IOException {
        BulkOrderResult bulkOrderResult = null;
        List<Order> orderList = bulkOrder.getOrders();
        try {
        	if(orderList == null || orderList.size()==0){
        		throw new EmptyOrderException("No order exists!");
        	}
        	
            for(int i = 0; i < orderList.size(); i++){
                if(orderList.get(i).getOrderItems().size() == 0){
                    throw new EmptyOrderException("The Order " + i + ": No Order added!");
                }
            }

            String result = _frgxApicallHelper.PostApi(bulkOrder);
            JsonObject jsonResponse = new JsonParser().parse(result).getAsJsonObject();
            if(jsonResponse.get("OrderResults") == null){
                throw new NullResponseException("The Placing Order Response is null.");
            }
            JsonObject jo = jsonResponse.get("OrderResults").getAsJsonArray().get(0).getAsJsonObject();
            if(jo.get("Message") == null){
                throw new NullResponseException("The Placing Order Response Message is null.");
            }
            String msg = jo.get("Message").getAsString();
            if(!msg.equals("")){
                throw new BadItemIdException("BadItemIdException: " + msg + "\nResult: " + jsonResponse.get("Message").toString());
            }

            Gson gsonIn = new GsonBuilder().setPrettyPrinting().create();
            bulkOrderResult = gsonIn.fromJson(jsonResponse, BulkOrderResult.class);

            return bulkOrderResult;
        } catch (BadItemIdException e) {
            System.out.println(e.getMsg());
        } catch (EmptyOrderException e) {
            System.out.println(e.getMsg());
        } catch (NullResponseException e) {
            System.out.println(e.getMsg());
        }
        return bulkOrderResult;
    }
}
