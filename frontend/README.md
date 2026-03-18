# Snippets Directory Angular Frontend

## Prerequisites

- Node
- Angular 16.1

## Local set-up

- Run:
```sh
npm install
ng serve
```
- Navigate to `http://localhost:4200/`. Angular will automatically load the local API URL from `/src/environments/environment.ts`.

## Running end-to-end tests

- Run `ng e2e` to execute the end-to-end tests _via_ a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

## UAT & Production environments

- The API URL of these environments is located in the `environment.uat.ts` and `environment.prod.ts` files of the `/src/environments` folder.

- The environment is determined at build time through the ___ANGULAR_ENV__ variable of the __Cloud Build triggers__, which is set to __uat__ or __prod__ (environments are configured in the angular.json file under the configurations key).