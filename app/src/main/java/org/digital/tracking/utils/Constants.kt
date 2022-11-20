package org.digital.tracking.utils

object Constants {
    const val DEFAULT_INDIA_LAT: Double = 22.0
    const val DEFAULT_INDIA_LONG: Double = 77.0

    const val INTENT_KEY_TITLE = "title"
    const val INTENT_KEY_EXTRA_VEHICLE = "vehicle"
    const val INTENT_KEY_SIGN_UP_TYPE = "signUpType"
    const val INTENT_KEY_EMAIL = "email"
    const val INTENT_KEY_PHONE = "phone"
    const val INTENT_KEY_LAST_LAT = "lastLatitude"
    const val INTENT_KEY_LAST_LONG = "lastLongitude"
    const val INTENT_KEY_VEHICLE_SPEED = "vehicleSpeed"
    const val INTENT_KEY_VEHICLE_IMEI = "vehicleIMEI"
    const val INTENT_KEY_VEHICLE_NUMBER = "vehicleNumber"
    const val INTENT_KEY_VEHICLE_STATUS = "vehicleStatus"
    const val INTENT_KEY_VEHICLE_GSM_STATUS = "vehicleGsmStatus"
    const val INTENT_KEY_VEHICLE_GPS_STATUS = "vehicleGpsStatus"
    const val INTENT_KEY_VEHICLE_STOP_STATUS = "vehicleStopStatusWithTime"
    const val INTENT_KEY_LIVE_LOCATIONS_TYPE = "liveLocationType"
    const val INTENT_KEY_VEHICLE_IGNITION_STATUS = "vehicleIgnitionStatus"
    const val INTENT_KEY_VEHICLE_DRIVEN_TODAY = "vehicleDrivenToday"
    const val INTENT_KEY_VEHICLE_LAST_CONTACT_DATE = "vehicleLastContactDate"
    const val INTENT_KEY_VEHICLE_LAST_CONTACT_TIME = "vehicleLastContactTime"

    const val INTENT_KEY_LIVE_LOCATIONS_TYPE_REPLAY = "locationTypeReplay"
    const val INTENT_KEY_LIVE_LOCATIONS_TYPE_HISTORY = "locationTypeHistory"

    //UAT
//    const val SERVER_URL_ON_BOARDING_API = "https://boarding-api.testmbtrsas.com"
//    const val GRAPHQL_TRACKING_SERVER_URL = "https://tracking-api.testmbtrsas.com/graphql"
//    const val GRAPHQL_TRACKING_SOCKET_SERVER_URL = "wss://tracking-api.testmbtrsas.com/graphql"
    //const val GOOGLE_MAPS_API_KEY = "AIzaSyDf_CQG-iKl9f3zDQZjbB6fezgHp9f-VZ8" //TODO uncomment in Manifest

    //Production
    const val SERVER_URL_ON_BOARDING_API = "https://cus-bord.livemtr.com"
    const val GRAPHQL_TRACKING_SERVER_URL = "https://trk.livemtr.com/graphql"
    const val GRAPHQL_TRACKING_SOCKET_SERVER_URL = "wss://trk.livemtr.com/graphql"
////    const val GOOGLE_MAPS_API_KEY = "AIzaSyAeqIjATMfUG1pnWTTwrp-zVluMpu1Br5A"

    const val GRAPHQL_SOCKET_CONNECTION_TIMEOUT = 30 * 1000L //In millis

    const val MAP_ZOOM_LEVEL_WORLD = 1f
    const val MAP_ZOOM_LEVEL_CONTINENT = 5f
    const val MAP_ZOOM_LEVEL_CITY = 10f
    const val MAP_ZOOM_LEVEL_BTW_CITY_AND_STREETS = 13f
    const val MAP_ZOOM_LEVEL_BTW_CITY_AND_STREETS_14 = 14f
    const val MAP_ZOOM_LEVEL_STREETS = 15f
    const val MAP_ZOOM_LEVEL_DRIVING = 18f
    const val MAP_ZOOM_LEVEL_BUILDINGS = 20f

    const val VEHICLE_STATUS_KEY_IDLE = "idle"
    const val VEHICLE_STATUS_KEY_RUNNING = "running"
    const val VEHICLE_STATUS_KEY_STOP = "stop"
    const val VEHICLE_STATUS_KEY_UNKNOWN = "unknown"

    const val IGNITION_STAT_ON = 1
    const val IGNITION_STAT_OFF = 0

    const val GPS_FIXED_STATE_ON = 1
    const val GPS_FIXED_STATE_OFF = 0

    const val GSM_SIGNAL_STRENGTH_WEEK_UPPER_LIMIT = 14
    const val GSM_SIGNAL_STRENGTH_NORMAL_UPPER_LIMIT = 20
    const val GSM_SIGNAL_STRENGTH_STRONG_LOWER_LIMIT = 21

    const val UNITS_SPEED = "Km/hrs"
    const val UNITS_DISTANCE = "Kms"

    const val URL_TERMS_AND_CONDITIONS = "https://www.mobitra.in/post/terms-conditions"
    const val URL_PRIVACY_POLICY = "https://www.mobitra.in/post/privacy-policy"

    const val DEVICE_REFERENCE_ID = "MobiTra Mobile App"

    object HelpAndSupport {
        const val EMAIL = "support@mobitra.in"
        const val EMAIL_SUBJECT = "App Feedback"
    }
}


