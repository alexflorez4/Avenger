package com.shades.services.fragx.Interfaces;

import com.shades.services.fragx.Exceptions.BadAccessIdOrKeyException;
import com.shades.services.fragx.Exceptions.HttpErrorException;
import com.shades.services.fragx.Models.BulkOrder;

import java.io.IOException;

/**
 * Interface for FrgxApiCallHelper
 */
public abstract interface IFrgxApicallHelper {
    String GetApi(String url) throws IOException, BadAccessIdOrKeyException, HttpErrorException;

    String PostApi(BulkOrder bulkOrder) throws IOException;

    void authenticate() throws IOException, BadAccessIdOrKeyException;

}
