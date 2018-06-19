package com.shades.services.fragx;

import com.google.gson.*;
import com.shades.services.fragx.Exceptions.BadAccessIdOrKeyException;
import com.shades.services.fragx.Exceptions.HttpErrorException;
import com.shades.services.fragx.Helpers.Constants;
import com.shades.services.fragx.Interfaces.IFrgxTrackingApiClient;
import com.shades.services.fragx.Models.TrackingInfo;

import java.io.IOException;


/**
 * Api client used to track orders.
 */
public class FrgxTrackingApiClient extends FrgxApiClient implements IFrgxTrackingApiClient {
    /**
     *Initializes a new tracking API client using an access ID and access key.
     * @param AccessId Api access ID found in FragranceX Api documentation
     * @param AccessKey Api access key found in FragranceX Api documentation
     * @throws IOException throws IOException
     */
    public FrgxTrackingApiClient(String AccessId, String AccessKey) throws IOException, BadAccessIdOrKeyException {
        super(AccessId, AccessKey);
    }

    /**
     * Initializes a new tracking API client.  This constructor should only be
     * used after a constructor with API access Id and API access key.
     * @throws BadAccessIdOrKeyException Access Id or Access key is wrong
     */
    public FrgxTrackingApiClient() throws BadAccessIdOrKeyException {
        super();
    }


    /**
     * Gets tracking information for an order.
     * @param orderId Either FragranceX order ID or external order ID that you want
     *                to retrieve tracking information
     * @return A TrackingInfo object containing basic tracking information.
     * @throws IOException throws IOException
     */
    public TrackingInfo getTracking(String orderId) throws IOException, BadAccessIdOrKeyException, HttpErrorException {
        TrackingInfo res = null;

        String result = _frgxApicallHelper.GetApi(Constants.FRGXAPI_TRACKING + orderId);
        JsonObject json = new JsonParser().parse(result).getAsJsonObject();
        if(json.get("Carrier").toString().equals("\"\"")){
            return null;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);
        res = gson.fromJson(json.toString(), TrackingInfo.class);

        return res;
    }
}
