# Stage 1: Build React App
FROM node:18-alpine AS build
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . ./
RUN npm run build

# Stage 2: Serve bằng Nginx
FROM nginx:1.25-alpine
WORKDIR /usr/share/nginx/html

# Xóa file config mặc định
RUN rm /etc/nginx/conf.d/default.conf

# Copy các file đã build từ stage 1
COPY --from=build /app/build .

# Quan trọng: Đảm bảo React Router hoạt động
# Nếu không tìm thấy file, Nginx sẽ trả về index.html
COPY --from=build /app/build/index.html /usr/share/nginx/html/
RUN sed -i '/index  index.html index.htm;/a \    try_files $uri $uri/ /index.html;' /etc/nginx/conf.d/default.conf || \
    echo "location / { \n    root   /usr/share/nginx/html; \n    index  index.html index.htm; \n    try_files $uri $uri/ /index.html; \n}" > /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]