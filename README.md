# Imgur Image Uploader REST API

To start this service, execute the below command:

    mvn spring-boot:run

## Show All uploaded images
GET request

    GET http://localhost:8080/v1/images

The base link to show imgur url's of all images successfully uploaded to imgur.

Request body :: None

Response body ::
```json
{
    "uploaded": [
        "https://i.imgur.com/gHGJ9k.jpg",
        "https://i.imgur.com/iuFHgO.jpg"
    ]
}
```

## New Upload Job
POST request 

    POST http://localhost:8080/v1/images/upload

Will return immediately as the image download and consequent upload to imgur happens asynchronously.

Request body :: An array of URLs to images that will be uploaded. Duplicates are removed.
```json
{
    "urls":[
            "http://www.google.com",
            "https://farm3.staticflickr.com/2879/11234651086_681b3c2c00_b_d.jpg",
            "https://farm4.staticflickr.com/3790/11244125445_3c2f32cd83_k_d.jpg"

    ]
}
```

Response body :: The id of the upload job that was submitted.
```json
{
    "jobId" : "1"
}
```

## Get upload job status
GET request and the job id has to be given.

    GET http://localhost:8080/v1/images/upload/jobId

Request body :: None

Response body :: Job status fields

```json
{
    "id": "1",
    "created": "2018-12-27T16:48:29325Z",
    "finished": null,
    "status": "in-progress",
    "uploaded": {
        "pending": [
            "http://www.google.com",
        ],
        "complete": [
            "https://i.imgur.com/gHGJ9k.jpg",
            "https://i.imgur.com/iuFHgO.jpg"
        ],
        "failed": [
        ]
    }
}
```

