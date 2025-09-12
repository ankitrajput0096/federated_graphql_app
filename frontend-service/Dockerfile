FROM node:18-alpine
WORKDIR /app
COPY student-dashboard/package*.json ./
RUN npm install --legacy-peer-deps
COPY student-dashboard/ .
EXPOSE 3000
CMD ["npm", "start"]