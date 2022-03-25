package com.example.appclinica.notification

class Constants {

    companion object {
        //MessagingService
        const val BASE_URL = "https://fcm.googleapis.com/"
        const val SERVER_KEY = "AAAA7xQUEHE:APA91bHRe27BMYWP5POyzyVynlxi-o6rKK8e_-NEy25q1UgV3iAIXFKPXf_nq-JRO_9pYt30w0orntnZfrNSXzg8oucKhKE0RlAgZW6A7YCW7fXiq3DnV1NWFfj1eLRk828apsG5ejEH"
        const val CONTENT_TYPE = "application/json"

        //Paymantel
        const val USER_ID = "uid12345"
        const val USER_EMAIL = "fausto.laminia@gmail.com"
        const val PAYMENTEZ_IS_TEST_MODE = true

        //Ask the Paymentez team for it
        const val PAYMENTEZ_CLIENT_APP_CODE = "IOS-CO-CLIENT"

        //Ask the Paymentez team for it.
        const val PAYMENTEZ_CLIENT_APP_KEY = "AKKqsezFDHtanNv1G0ARyxb8DiYARE"

        const val BACKEND_URL = "https://example-paymentez-backend.herokuapp.com"
        //const val BACKEND_URL = "https://appclinicadesarollo.herokuapp.com/"



       /* //This is the identifier of the user you use inside your application.
        var USER_ID = "uid12345"

        //Email of the user initiating the purchase.
        var USER_EMAIL = "fausto.laminia@gmail.com"

        //Is Paymentez SDK DEV environment?
        var PAYMENTEZ_IS_TEST_MODE = true

        //Ask the Paymentez team for it
        var PAYMENTEZ_CLIENT_APP_CODE = "IOS-CO-CLIENT"

        //Ask the Paymentez team for it.
        var PAYMENTEZ_CLIENT_APP_KEY = "AKKqsezFDHtanNv1G0ARyxb8DiYARE"

        //Backend Deployed from https://github.com/paymentez/example-java-backend
        //public static String BACKEND_URL = "https://example-paymentez-backend.herokuapp.com";
        var BACKEND_URL = "https://example-paymentez-backend.herokuapp.com"
        */
    }

}