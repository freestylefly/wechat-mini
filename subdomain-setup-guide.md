# 子域名设置指南

本指南将帮助你设置子域名 `bazi.canghecode.com` 用于八字命理小程序的API访问。

## 步骤1: 添加DNS记录

1. 登录你的域名注册商或DNS管理面板
2. 为 `canghecode.com` 添加一条子域名记录:
   - 类型: `A` 记录
   - 主机名: `bazi`
   - 值: 你服务器的IP地址
   - TTL: 可设置为300或3600 (秒)

## 步骤2: 配置Nginx

1. 在服务器上设置子域名配置:
   ```bash
   # 创建配置文件
   sudo nano /etc/nginx/conf.d/bazi.canghecode.com.conf
   ```

2. 将提供的 `bazi-subdomain-nginx.conf` 内容复制到此文件
   
3. 调整配置中的SSL证书路径:
   ```
   ssl_certificate /etc/nginx/cert/canghecode.com.pem;
   ssl_certificate_key /etc/nginx/cert/canghecode.com.key;
   ```
   确保路径指向正确的证书文件。

## 步骤3: 验证并重启Nginx

1. 测试Nginx配置:
   ```bash
   sudo nginx -t
   ```

2. 如果测试通过，重启Nginx:
   ```bash
   sudo systemctl restart nginx
   ```

## 步骤4: 更新微信小程序配置

1. 在微信公众平台的开发管理中，将请求合法域名更新为:
   - `bazi.canghecode.com`

2. 注意域名必须有备案，并且使用HTTPS协议

## 步骤5: 测试连接

测试子域名是否正常工作:

```bash
curl -v https://bazi.canghecode.com/api/bazi/calculate
```

## 故障排查

如果子域名无法访问，请检查:

1. **DNS解析**: 使用以下命令检查DNS是否已解析:
   ```bash
   nslookup bazi.canghecode.com
   ```

2. **Nginx日志**:
   ```bash
   sudo tail -f /var/log/nginx/error.log
   ```

3. **防火墙设置**:
   确保80和443端口已开放:
   ```bash
   sudo ufw status
   ```

4. **证书问题**:
   如果证书配置有误，可以使用:
   ```bash
   sudo certbot --nginx -d bazi.canghecode.com
   ```
   为子域名单独申请一个Let's Encrypt证书。 