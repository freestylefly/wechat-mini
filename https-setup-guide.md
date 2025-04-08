# HTTPS配置指南

本指南将帮助你在服务器上正确配置HTTPS，以支持微信小程序通过HTTPS协议访问你的API。

## 步骤1: 获取SSL证书

如果你已经有SSL证书，请跳过此步骤。否则，你可以：

1. 使用免费的Let's Encrypt证书:
   ```bash
   # 安装certbot
   sudo apt-get update
   sudo apt-get install certbot python3-certbot-nginx
   
   # 生成证书
   sudo certbot --nginx -d canghecode.com
   ```

2. 或者从SSL证书提供商购买证书。

## 步骤2: 配置Nginx

### 2.1 修改Nginx配置

1. 使用提供的`nginx-https-config.conf`文件作为模板：
   ```bash
   # 备份当前配置
   sudo cp /etc/nginx/nginx.conf /etc/nginx/nginx.conf.bak
   
   # 复制新配置
   sudo cp nginx-https-config.conf /etc/nginx/nginx.conf
   ```

2. 或者，如果你的服务器已有其他配置，可以修改特定的服务器块：
   ```bash
   # 创建新的配置文件
   sudo nano /etc/nginx/conf.d/bazi-api.conf
   ```
   
   然后复制`nginx-https-config.conf`中的相关部分。

### 2.2 调整配置文件路径

确保修改以下路径以匹配你的实际情况：

1. SSL证书路径:
   ```
   ssl_certificate /etc/nginx/cert/canghecode.com.pem;
   ssl_certificate_key /etc/nginx/cert/canghecode.com.key;
   ```

2. 网站根目录:
   ```
   root /var/www/html;
   ```

3. 代理目标端口:
   ```
   proxy_pass http://127.0.0.1:8080;
   ```
   将这里的`8080`改为你的Spring Boot实际运行端口。

## 步骤3: 修改Spring Boot配置

编辑`application.yml`文件，确保Spring Boot只监听本地接口：

```yaml
server:
  port: 8080
  address: 127.0.0.1  # 只监听本地连接
  servlet:
    context-path: /
```

这样配置后，Spring Boot应用将只接受来自本地的连接，由Nginx处理所有外部请求。

## 步骤4: 检查防火墙设置

确保防火墙允许必要的端口：

```bash
sudo ufw status
sudo ufw allow 80
sudo ufw allow 443
sudo ufw allow 6803
```

## 步骤5: 重启服务

应用所有配置更改：

```bash
# 测试Nginx配置
sudo nginx -t

# 如果配置正确，重启Nginx
sudo systemctl restart nginx

# 重启Spring Boot应用
# 具体命令取决于你的部署方式，可能是:
sudo systemctl restart bazi-backend
# 或者
cd /path/to/your/app && java -jar bazi-backend.jar
```

## 步骤6: 验证HTTPS设置

测试HTTPS是否正常工作：

```bash
curl -v https://canghecode.com:6803/api/bazi/calculate
```

应该看到SSL握手成功，并且API返回了响应。

## 故障排查

如果遇到问题，请查看以下日志：

1. Nginx错误日志:
   ```bash
   sudo tail -f /var/log/nginx/error.log
   ```

2. Spring Boot日志:
   ```bash
   # 具体路径取决于你的配置
   tail -f /path/to/your/app/logs/bazi-backend.log
   ```

常见问题：

1. **证书问题**:
   - 检查证书路径是否正确
   - 确保证书文件权限正确: `sudo chmod 644 /etc/nginx/cert/*.pem`
   - 确保私钥权限安全: `sudo chmod 600 /etc/nginx/cert/*.key`

2. **代理问题**:
   - 确保Spring Boot应用在运行并监听端口: `netstat -tlnp | grep 8080`
   - 检查代理设置中端口是否正确

3. **HTTPS连接被拒绝**:
   - 检查防火墙设置
   - 验证SSL配置: `openssl s_client -connect canghecode.com:6803` 