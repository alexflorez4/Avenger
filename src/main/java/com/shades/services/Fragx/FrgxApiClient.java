package com.shades.services.fragx;

import com.shades.services.fragx.Exceptions.BadAccessIdOrKeyException;
import com.shades.services.fragx.Helpers.FrgxApicallHelper;
import com.shades.services.fragx.Interfaces.IFrgxApiClient;
import com.shades.services.fragx.Interfaces.IFrgxApicallHelper;

import java.io.IOException;

/**
 * An abstract class for instantiating new API clients
 */
public abstract class FrgxApiClient implements IFrgxApiClient {
    /**
     * Helper class used to execute HTTP requests
     */
    protected static IFrgxApicallHelper _frgxApicallHelper;
    private static boolean initialized;


    /**
     * Initializes an API client.
     * @param accessId Api access ID found in FragranceX Api documentation
     * @param accessKey Api access ID found in FragranceX Api documentation
     * @throws IOException Throws IO exception
     */
    protected FrgxApiClient(String accessId, String accessKey) throws IOException, BadAccessIdOrKeyException {
        if(_frgxApicallHelper == null){
            _frgxApicallHelper = new FrgxApicallHelper(accessId, accessKey);
            _frgxApicallHelper.authenticate();
            initialized = true;
        }
    }

    /**
     * Initializes a new API client.  This constructor should only be
     * used after a constructor with API access Id and API access key.
     * @throws BadAccessIdOrKeyException Access ID or Access Key is wrong
     */
    protected FrgxApiClient() throws BadAccessIdOrKeyException {
        if (!initialized)
        {
            throw (new BadAccessIdOrKeyException("BadAccessIdOrKeyException: First child of abstract class FrgxApiClient, must pass access ID and key in constructor"));
        }
    }
    protected FrgxApiClient(IFrgxApicallHelper frgxApicallHelper)
    {
        _frgxApicallHelper = frgxApicallHelper;
    }
}
