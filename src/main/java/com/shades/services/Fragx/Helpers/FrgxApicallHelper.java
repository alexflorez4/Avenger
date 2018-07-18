package com.shades.services.fragx.Helpers;


import com.google.gson.*;
import com.shades.services.fragx.Exceptions.BadAccessIdOrKeyException;
import com.shades.services.fragx.Exceptions.HttpErrorException;
import com.shades.services.fragx.Exceptions.NullResponseException;
import com.shades.services.fragx.Interfaces.IFrgxApicallHelper;
import com.shades.services.fragx.Models.BulkOrder;
import com.shades.services.fragx.Models.BulkOrderResult;
import com.shades.services.fragx.Models.Product;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Used for sending HTTP requests and receiving HTTP responses
 */
public class FrgxApicallHelper implements IFrgxApicallHelper {
    protected static HttpClient client;
    private static boolean initialized;
    private static String accessId, accessKey;

    public FrgxApicallHelper(String accessId, String accessKey)
    {
        this.accessId = accessId;
        this.accessKey = accessKey;
        client = HttpClients.createDefault();
    }
    /**
     * Authenticates the user using their Api access ID and Api access key.
     * @throws IOException Throws IO Exception, BadAccessIdOrKeyException
     */
    @Override
    public void authenticate() throws IOException, BadAccessIdOrKeyException {
        try{
            HttpPost post = new HttpPost(Constants.FRGXAPI_TOKEN);
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "apiAccessKey"));
            params.add(new BasicNameValuePair("apiAccessId", accessId));
            params.add(new BasicNameValuePair("apiAccessKey", accessKey));
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            HttpResponse response = client.execute(post);
            String a = response.getStatusLine().toString();

            if(a.equals("HTTP/1.1 400 Bad Request")){
                throw (new BadAccessIdOrKeyException("Bad Access Id Or Key"));
            }
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(response.getEntity());

            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(responseString);
            if(jo.get("access_token") == null){
                throw new NullResponseException("The Access Token you get is null.");
            }
            String accessToken = jo.get("access_token").getAsString();
            List<Header> headers = new ArrayList<>();
            headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
            headers.add(new BasicHeader("Authorization", "Bearer " + accessToken));

            client = HttpClients.custom().setDefaultHeaders(headers).build();
        } catch (NullResponseException e) {
            System.out.println(e.getMsg());
        }
    }

    /**
     * For get Request
     * @param url
     * @return Return the response as a String. If the response is 401, reauthenticate. 500, return null.
     * @throws IOException
     * @throws BadAccessIdOrKeyException
     */
    public String GetApi(String url) throws IOException, BadAccessIdOrKeyException, HttpErrorException {
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = null;
        Product res = null;

        response = client.execute(httpget);
        String status = response.getStatusLine().toString();
        if(status.equals("HTTP/1.1 401 Unauthorized")){
            authenticate();
            httpget = new HttpGet(url);
            response = client.execute(httpget);
        }
        else if(!status.equals("HTTP/1.1 200 OK")){
            throw (new HttpErrorException(status));
        }
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * For Post Request
     * @param bulkOrder This is the order that the customer wanna buy
     * @return return a message to show if the placing order success or not
     * @throws IOException
     */
    public String PostApi(BulkOrder bulkOrder) throws IOException {
        String j = new Gson().toJson(bulkOrder);
        JsonObject jsonobj = new JsonParser().parse(j).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonobj);

        BulkOrderResult bulkOrderResult = null;
        HttpPost post = new HttpPost(Constants.FRGXAPI_BULK_ORDER);
        StringEntity params = new StringEntity(json);
        post.addHeader("content-type", "application/json");
        post.setEntity(params);

        HttpResponse response = client.execute(post);
        return EntityUtils.toString(response.getEntity());
    }
}
