FROM node:16-alpine3.16

WORKDIR /app
EXPOSE 4200

COPY package*.json ./
RUN npm ci
COPY . .
CMD ["npm", "run", "start", "--", "--host", "0.0.0.0"]
