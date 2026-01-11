FROM node:20-alpine AS build
ARG REACT_APP_KEYCLOAK_URL=http://localhost:8080
ENV REACT_APP_KEYCLOAK_URL=$REACT_APP_KEYCLOAK_URL
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:1.27-alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
