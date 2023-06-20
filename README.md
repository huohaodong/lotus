# Lotus <img src="https://em-content.zobj.net/source/microsoft-teams/363/lotus_1fab7.png" alt="Octopus" width="30" height="30"/>

轻量级 HTTP API 网关中间件，支持基于 HTTP 请求路径、方法等特征的接口路由策略、支持 Route 粒度的流控与负载均衡，支持接口超时检测、降级与熔断，JMeter 测试单机吞吐量可达 18000。

## 特性

- 内置 YAML 文件解析逻辑，可以根据配置文件中 Route、Predicate 和 Filter 的定义动态构建路由策略。
- 支持基于 HTTP Header、Method、Path、Host、Query、Cookie 的路由匹配 Predicate。
- 基于责任链模式实现 FilterChain，采用 SPI 机制动态加载自定义 FilterFactory。
- 默认使用令牌桶限流策略，同时采用 Redis + Lua 脚本实现了基于滑动窗口算法的分布式限流器。
- 集成 Hystrix，支持以 Route 为粒度对 API 进行超时检测并执行相应的熔断与降级策略。
- 支持接入 Consul、Nacos 等服务发现中间件，并在此基础上提供服务粒度的负载均衡。

## 快速开始

## 配置

想要使用 Lotus 转发 HTTP 请求首先需要配置相应的路由规则，即定义相应的 `route` 规则。Lotus 的配置文件默认放置在 `resources` 目录下的 `GatewayProperties.yml` 文件中，下面给出一个标准的配置文件示例：

```yaml
host: 0.0.0.0 # Lotus 服务监听主机地址
port: 10086 # Lotus 服务端口号
redisHost: 10.201.0.222 # Redis 服务的地址，用于分布式限流器使用（可选）
redisPort: 22221 # Redis 服务的端口，用于分布式限流器使用（可选）
routes:
  - id: route1 # route 规则的 id
    uri: http://10.201.0.222:7777/ping # route 规则匹配后，转发请求时所对应的 API 服务 uri
    order: 1 # route 的顺序，Lotus 在匹配收到的 HTTP 请求时会根据 route 的 order 来进行匹配，越小越优先
    predicates: # route 对应的匹配逻辑，只有定义的 predicate 条件全部都满足时才会匹配上
      - Method=POST,GET # 当 HTTP 请求的方法为 POST 或者 GET 时满足条件，用逗号分隔开多个参数
      - Path=/api/service1/** # 当 HTTP 请求的路径匹配时满足条件，依照 Ant 风格的通配符规则进行匹配
      - Cookie=key1,value1 # 当 HTTP 请求的 Cookie 中带有 (key1, value1) 的数据对时满足条件
      - Query=q1,v1 # 当 HTTP 请求的参数中带有 (q1,v1) 的请求对时满足条件
      - Query=q2,v2 # 同上
      - Header=h1,h2 # 当 HTTP 请求的请求头中含有 (h1,h2) 的数据对时满足条件
    filters: # HTTP 请求的过滤器，对 HTTP 请求进行过滤操作
      - Auth=Authorization,Basic # 过滤请求头的 Authorization 中含有相应的认证参数的 HTTP 请求
      - TokenBucketRateLimiter=100000 # 令牌桶限流器，令牌数为 10w
      - RedisRateLimiter=100000,60 # Redis 分布式限流器，60 秒内窗口最多 10w 个请求
    hystrix: # hystrix 断路器配置
      enable: true # 是否对当前 route 启用
      threadCoreSize: 5 # 核心线程数
      executionTimeoutInMilliseconds: 5000 # 请求执行超时时间
      fallback: service is unavailable now # 请求超时后返回给客户端的响应结果
```

## 部署

从源码编译（JDK >= 17）：

```shell
./mvnw clean package -DskipTests
```

启动 Lotus：

```shell
java -jar ./target/lotus-x.y.z.jar # 根据当前编译得到的版本启动程序
```

## 实现参考

- Lotus 的 route、predicate 和 filter 的配置风格以及总体设计框架参考了 [Spring Cloud Gateway](https://github.com/spring-cloud/spring-cloud-gateway)。