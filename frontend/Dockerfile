FROM node:16-slim as angular-builder

WORKDIR /app

COPY package*.json /app/
RUN npm ci --no-audit --silent

COPY ./ /app/

ARG ENV
RUN echo 'ENVIRONMENT:' $ENV
RUN npm run $ENV 

FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

WORKDIR /usr/share/nginx/html
COPY --from=angular-builder /app/dist/frontend .