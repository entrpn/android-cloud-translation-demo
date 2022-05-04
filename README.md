## Intro

This is sample code that shows how to embed Google Cloud Translation AutoML model into an android application.

## Setup

1. Train a [Google CLoud Platform AutoML Translation model](https://cloud.google.com/translate/automl/docs).
1. From the Google Cloud console, create a Service Account with the `Cloud Translation API User` role.
1. Download a json key for the Service Account and add it to `res >> raw` as key.json.
1. In FirstFragment.java, onViewCreated, fill in your `projectId` and `modelId`.
1. Run the application.