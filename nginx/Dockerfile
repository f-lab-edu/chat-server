# Nginx 공식 이미지를 사용합니다.
FROM nginx:latest

# 컨테이너 내부의 Nginx 설정 파일을 복사합니다.
COPY nginx.conf /etc/nginx/nginx.conf

# 컨테이너 실행 시 Nginx를 시작합니다.
CMD ["nginx", "-g", "daemon off;"]
