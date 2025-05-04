# Product Price Tracking App

This demo application allows users to track product prices and receive notifications when the price falls within a specified range. It provides a single POST API endpoint that can be used to schedule price checks. The details of the request and response structures are provided below.

## Tech Stack
    Java 17
    Spring Boot 3.4.5
    PMD 7.0
    Junit 5
    
## Sample Request and Response

### Endpoint: http://localhost:8080/price-tracker  
### HTTP Method: POST
### Scenarios
#### Invalid request:
    Request:
    {
        "productUrl": "http://toysworld.com/jenga1",
        "scheduleInterval": "* * * * * *"
    }

    Response:
    {
        "errors": [
            "The trigger price for alert is a mandatory parameter."
        ]
    }

    Similarly, all other parameters are mandatory.

#### Valid request, product price matches at the API call
    Request:
    {
        "productUrl": "http://toysworld.com/drum",
        "triggerPrice": 19,
        "scheduleInterval": "* * * * * *"
    }

    Response:
    {
        "message": "ScheduledAlert",
        "productUrl": "http://toysworld.com/drum",
        "triggerPrice": 19.0,
        "currentPrice": 80.0,
        "timestamp": "2025-05-02T11:22:13.073+00:00"
    }

#### Valid request, scheduler set for price checks in the future
    Request:
    {
        "productUrl": "http://toysworld.com/teddy-bear",
        "triggerPrice": 56.5,
        "scheduleInterval": "* * * * * *"
    }
    Response:
    {
        "message": "ScheduledAlert",
        "productUrl": "http://toysworld.com/teddy-bear",
        "triggerPrice": 56.5,
        "currentPrice": 57.0,
        "timestamp": "2025-05-02T11:23:48.959+00:00"
    }

#### Valid request, but invalid product URL
    Request:
    {
        "productUrl": "http://toysworld.com/bear",
        "triggerPrice": 56.5,
        "scheduleInterval": "* * * * * *"
    }
    Response:
    {
        "message": "The requested product is not available.",
        "productUrl": null,
        "triggerPrice": null,
        "currentPrice": null,
        "timestamp": "2025-05-02T11:26:28.362+00:00"
    }
