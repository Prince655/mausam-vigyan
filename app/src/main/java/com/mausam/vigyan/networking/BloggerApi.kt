package com.mausam.vigyan.networking

/**
 * Created by Azhar Rivaldi on 22-12-2019.
 */

object BloggerApi {

    var BASE_URL = "https://www.googleapis.com/blogger/v3/blogs/"
    var API_KEY = "AIzaSyCRjxobkUv8a4FbcqvbWNdtICDXqc2Hk2I"
    var BLOGGER_ID = "1238498375113190996"

    var ListPost = "$BASE_URL$BLOGGER_ID/posts?key=$API_KEY"

    var About = "$BASE_URL$BLOGGER_ID/pages/ID_PAGES?key=$API_KEY"

    var PrivacyPolicy = "$BASE_URL$BLOGGER_ID/pages/ID_PAGE?key=$API_KEY"

    var Disclaimer = "$BASE_URL$BLOGGER_ID/pages/ID_PAGE?key=$API_KEY"

}
