# 服务器配置指南 - 解决跨域和HTTP/HTTPS问题

## 问题诊断

当看到以下错误：
```
java.lang.IllegalArgumentException: Invalid character found in method name [0x160x030x010x020x00...]
```

这表明Tomcat HTTP服务器在试图解析HTTPS加密数据。这通常发生在：
1. 客户端使用HTTPS连接到HTTP服务器端口
2. Nginx配置不正确，没有正确终止SSL

## 解决方案

### 方案1：将前端配置为使用HTTP（临时方案）

我们已经将前端配置修改为使用HTTP而不是HTTPS：
```javascript
// config/api.js
const BASE_URLS = {
  [ENV.PROD]: 'http://your-domain.com:YOUR_API_PORT/api'
};
```

### 方案2：正确配置Nginx SSL终止

如果需要使用HTTPS（生产环境推荐），请确保：

1. **Nginx配置正确的SSL终止**：

```nginx
# 主域名HTTPS配置
server {
    listen 443 ssl;
    server_name your-domain.com;

    ssl_certificate     /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;
    
    # 针对API的反向代理
    location /api/ {
        # 重要：使用HTTP代理到Spring Boot应用
        proxy_pass http://127.0.0.1:8080;
        
        # 标准代理头
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto https;
        
        # CORS头 - 简化版本
        add_header 'Access-Control-Allow-Origin' '*' always;
        add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS' always;
        add_header 'Access-Control-Allow-Headers' 'Content-Type' always;
        
        # 处理OPTIONS请求
        if ($request_method = 'OPTIONS') {
            add_header 'Access-Control-Allow-Origin' '*';
            add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS';
            add_header 'Access-Control-Allow-Headers' 'Content-Type';
            add_header 'Access-Control-Max-Age' '86400';
            return 204;
        }
    }
}
```

2. **确保Spring Boot只监听localhost**：

编辑`application.yml`，确保Spring Boot应用只监听本地端口，由Nginx处理外部连接：

```yaml
server:
  port: 8080
  address: 127.0.0.1  # 只监听本地连接
```

3. **系统防火墙设置**：

确保防火墙只允许Nginx的端口(80, 443)暴露给外部，而Spring Boot端口(8080)只能从本地访问：

```bash
# 允许HTTP和HTTPS
sudo ufw allow 80
sudo ufw allow 443

# 如果需要独立端口也暴露
sudo ufw allow YOUR_API_PORT
```

## 最佳实践建议

1. **生产环境**：
   - 使用Nginx作为前端服务器，处理SSL终止
   - Spring Boot应用只监听本地接口
   - 前端配置使用HTTPS URLs

2. **开发/测试环境**：
   - 可以临时使用HTTP进行测试
   - 记得在微信开发者工具中勾选"不校验合法域名..."选项

## 注意事项

对于微信小程序正式上线:
1. 必须使用HTTPS
2. 必须使用经过ICP备案的域名
3. 域名必须在微信小程序管理后台的"开发设置"中配置 