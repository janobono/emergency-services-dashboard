# build environment
FROM public.ecr.aws/docker/library/node:lts-alpine AS build
RUN apk add --no-cache libc6-compat

WORKDIR /app

COPY . .

RUN npm ci
RUN npm run build

# production environment
FROM public.ecr.aws/nginx/nginx:alpine
COPY --from=build /app/dist/dashboard-ui/browser /usr/share/nginx/html
# new
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
