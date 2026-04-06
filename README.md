# Departures Board

Android application that enhances the official PID Lítačka app by providing a customizable home 
screen widget for public transport departures.


## Development setup

To develop the app, API key for Golemio API is necessary. You can generate one for free at 
[following link](https://api.golemio.cz/api-keys). Then in your local copy create `/secrets.properties`
file, with following content:
```
GOLEMIO_API_KEY=<YOUR_API_KEY>
```

### Resources

Documentation for Golemio API can be found [here](https://api.golemio.cz/pid/docs/openapi)
Other open data for PID can be found [here](https://pid.cz/o-systemu/opendata/)