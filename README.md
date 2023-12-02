# Weather analyzer

This is project for working with remote API. URL of remote API is https://weatherapi-com.p.rapidapi.com. In this
project used only one URI of this API, this URI is https://weatherapi-com.p.rapidapi.com/current.json. If you want to use
this URI you'll have to add one parameter: geographic coordinates of place, for example: <br/> ?q=53.9,27.56. That parameter
represents coordinates of city Minsk.

# Project features
1. Getting weather data from the API and saving to the database;
2. Getting the latest data from the database;
3. Getting average temperature between two dates;

# Project endpoints
1. "/weather/api/latestData" - endpoint for getting the latest data from the db. Use this endpoint for GET request without
any parameters.<br/> 
Example of response:<br/>
    {<br/>
        "temperature": "-5.0", <br/>
        "windSpeed": "9.1", <br/>
        "atmosphericPressure": "1007.1", <br/
        "airHumidity": "89", <br/>
        "weatherConditions": "Mist", <br/>
        "weatherLocation": "Minsk", <br/>
        "weatherDate": "2023-12-02" <br/>
    }<br/>
<br/>
2. "/weather/api/averageTemperature" - endpoint for getting average temperature between two dates. Use this endpoint for
POST request with the first date and the second date in request-body.<br/> 
Example of request:<br/>
    {<br/>
        "startDate": "2023-12-01", <br/>
        "endDate": "2023-12-31" <br/>
    }<br/>
<br/>
Example of response:<br/>
{<br/>
   "startDate": "2023-12-01", <br/>
   "endDate": "2023-12-31", <br/>
   "averageTemperature": "-5.2657" <br/>
}<br/>

# Customization options
If you want to customize any project options for yourself, you can change some options in the application.properties file.
You can change 'q' parameter in 'api_uri' for changing place of analyze. Also, you can change 'timer_period'. This value
indicates how often data from the API will be read and written to the database.
