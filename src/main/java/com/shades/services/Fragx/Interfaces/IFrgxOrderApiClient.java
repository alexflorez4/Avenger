package com.shades.services.fragx.Interfaces;

import com.shades.services.fragx.Exceptions.EmptyOrderException;
import com.shades.services.fragx.Models.BulkOrder;
import com.shades.services.fragx.Models.BulkOrderResult;

import java.io.IOException;

/**
 * Interface for FrgxOrderApiClient
 */
public interface IFrgxOrderApiClient extends IFrgxApiClient{
    BulkOrderResult PlaceBulkOrder(BulkOrder bulkOrder) throws IOException, EmptyOrderException;
}


