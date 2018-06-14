package com.shades.cognito;


import com.amazonaws.services.cognitoidentity.model.Credentials;
import com.shades.cognito.CognitoHelper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class Authentication {


    @Autowired
    private CognitoHelper cognitoHelper;

    @RequestMapping(value = "/authenticate.do", method = RequestMethod.POST)
    public ModelAndView authenticateUser(@RequestParam("username") String username){

        String password = "@Engine25";

        String result = cognitoHelper.ValidateUser(username, password);

        if (result != null) {
            System.out.println("User is authenticated:" + result);
            JSONObject payload = CognitoJWTParser.getPayload(result);
            String provider = payload.get("iss").toString().replace("https://", "");

            Credentials credentails = cognitoHelper.GetCredentials(provider, result);

            System.out.println("User credentials: "+ credentails.getAccessKeyId() + " - "
             + credentails.getSecretKey() + " - " + credentails.getSessionToken() + " - " + credentails.getExpiration());

        } else {
            System.out.println("Username/password is invalid");
        }
        System.out.println("Username" + username + " - password:" + password);
        return null;
    }

}
