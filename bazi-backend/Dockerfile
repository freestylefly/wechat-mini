FROM openjdk:11-jre-slim

WORKDIR /app

# 复制项目JAR包
COPY target/bazi-backend-0.1.0.jar /app/bazi-backend.jar

# 设置环境变量（可选，也可以通过docker-compose或启动命令设置）
ENV SPRING_PROFILES_ACTIVE=prod

# 暴露端口
EXPOSE 6803

# 启动命令
ENTRYPOINT ["java", "-jar", "/app/bazi-backend.jar"] 