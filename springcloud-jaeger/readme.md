官网地址：https://www.jaegertracing.io

使用的版本是1.34：https://www.jaegertracing.io/docs/1.34/getting-started/

使用docker 部署

```shell
docker run -d --name jaeger \
  -e COLLECTOR_ZIPKIN_HOST_PORT=:9411 \
  -p 5775:5775/udp \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14250:14250 \
  -p 14268:14268 \
  -p 14269:14269 \
  -p 9411:9411 \
  jaegertracing/all-in-one:1.34
```

部署后的访问连接：http://localhost:16686