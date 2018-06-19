package com.shades.services.fragx.Interfaces;


import com.shades.services.fragx.Exceptions.BadAccessIdOrKeyException;
import com.shades.services.fragx.Exceptions.HttpErrorException;
import com.shades.services.fragx.Models.TrackingInfo;

import java.io.IOException;

/**
 * Interface for FrgxTrackingApiClient
 */
public interface IFrgxTrackingApiClient{
    TrackingInfo getTracking(String orderId) throws IOException, BadAccessIdOrKeyException, HttpErrorException;
}

