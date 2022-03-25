package com.example.appclinica.sdk.paymentez.rest.utils;

/**
 * Created by mmucito on 02/06/16.
 */
public class Constants {

    //MessagingService
    public static  String BASE_URL = "https://fcm.googleapis.com/";
    public static  String SERVER_KEY = "AAAA7xQUEHE:APA91bHRe27BMYWP5POyzyVynlxi-o6rKK8e_-NEy25q1UgV3iAIXFKPXf_nq-JRO_9pYt30w0orntnZfrNSXzg8oucKhKE0RlAgZW6A7YCW7fXiq3DnV1NWFfj1eLRk828apsG5ejEH";
    public static  String CONTENT_TYPE = "application/json";

    //This is the identifier of the user you use inside your application.
    public static String USER_ID = "uid12345";

    //Email of the user initiating the purchase.
    //public static String USER_EMAIL = "dev@paymentez.com";
    public static String USER_EMAIL = "fausto.laminia@gmail.com";

    //Is Paymentez SDK DEV environment?
    public static boolean PAYMENTEZ_IS_TEST_MODE = true;

    //Ask the Paymentez team for it
    public static String PAYMENTEZ_CLIENT_APP_CODE = "IOS-CO-CLIENT";

    //Ask the Paymentez team for it.
    public static String PAYMENTEZ_CLIENT_APP_KEY = "AKKqsezFDHtanNv1G0ARyxb8DiYARE";

    //Backend Deployed from https://github.com/paymentez/example-java-backend
    public static String BACKEND_URL = "https://example-paymentez-backend.herokuapp.com";


}
