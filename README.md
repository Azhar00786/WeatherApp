# WeatherApp
...Android project made using Kotlin...

The project displays weather details of a particular location, the location is accepted into the project by two methods(manually i.e. by typing/GPS or using network service).
Retrofit lib is used to access the data from the API and Moshi lib is used to convert the JSON data from the API to Data objects.

Follow these steps before using this project :- 

1> Get your personal API key from https://weatherstack.com/

2> Use your API key in project by pasting it in WeatherApp/app/src/main/java/com/example/weatherapp/network/ApiService.kt
