# Spring Cloud Gateway (SCG)

### API Gateway
인증/모니터링/오케스트레이션 등과 같은 기능이 추가된 Reverse Proxy를 의미 한다. Netflix zuul, Amazon API Gateway, Apigee 및 Spring Cloud Gateway 같은 것들이 잘 알려진 Api Gateway 구현체들이다.

### Spring Cloud Gateway
Spring reactive ecosystem을 기반으로 Spring Cloud 팀이 구현한 API 게이트웨이이다. 또한 논블로킹(non-blocking), 비동기(Asynchronous) 방식의 Netty Server를 내부적으로 사용한다

요청에 대해 라우팅이 작동하는 대략적인 방식은 다음과 같다

![Spring CloudGateway Architecture](img/SpringCloudGatewayArchitecture.png)

Spring Cloud Gateway는 다음 세 가지 주요 구성 요소로 이루어져 있다

- **Route:** 게이트웨이의 기본 골격이다. ID, 목적지 URI, 조건부(predicate) 집합, 필터(filter) 집합으로 구성된다. 조건부가 맞게 되면 해당하는 경로로 이동하게 된다.

- **Predicate:** Java8의 Function Predicate이다. Input Type은 Spring Framework ServerWebExchange이다. 조건부를 통해 Header 나 Parameter같은 HTTP 요청의 모든 항목을 비교할 수 있다.

- **Filter(필터):** 특정 팩토리로 구성된 Spring Framework GatewayFilter 인스턴스다. Filter에서는 다운스트림 요청 전후에 요청/응답을 수정할 수 있다.

### Implementing Spring Cloud Gateway

Spring Cloud Gateway를 이용하려 경로를 생성하는 방법에는 두 가지가 있다.

- property 기반(application.properties 또는 application.yml)의 설정

    ```yaml
    spring:
      cloud:
        gateway:
          routes:
            - id: shop-service
              uri: http://localhost:8082
              predicates:
              filters:
                - RewritePath=/shop/(?<segment>.*), /$\{segment}
    ```

- 코드 기반의 설정

    ```kotlin
    @Bean
    	fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator? {
    		return builder.routes()
    				.route("shop-service") { r: PredicateSpec ->
    					r.path("/shop/**")
    							.filters { f -> f.rewritePath("/shop/(?<segment>.*)", "/\$\\{segment}") }
    							.uri("http://localhost:8082")

    				}
    				.build()
    	}
    ```

### 다양한 Route Predicate Factories

**The After Route Predicate Factory**

`After` route predicate는 datetime을 인자로 가진다. Reqeust가 해당 datetime 이후에 만들어 졌을 때 매칭 시킨다

2021-01-29T21:00:00 이전에 들어온 요청들은 `[http://localhost:8082](http://localhost:8082)` 로 라우팅 시키고 싶으면

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: shop-service
          uri: http://localhost:8082
          predicates:
            - Before=2021-01-29T21:00:00.000+09:00
```

또는

```kotlin
@Bean
	fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator? {
		return builder.routes()
				.route("shop-service") { r: PredicateSpec ->
					r.before(LocalDateTime.of(2021, 1, 29, 21, 0).atZone(ZoneId.systemDefault()))
							.uri("http://localhost:8082")
				}
				.build()
	}
```

**The Cookie Route Predicate Factory**

`Cookie` route predicate는 cookie name과 cookie value를 인자로 가진다. cookie value는 정규표현식으로 표현되며 해당 정규 표현식과 일치하는 cookie value를 가졌을 때 매칭시킨다

chocolate 라는 이름의 쿠키의 값이 정수로 이루어졌을 때만 라우팅 시키고 싶으면

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: shop-service
        uri: http://localhost:8082
        predicates:
        - Cookie=chocolate, \d+
```

또는

```kotlin
@Bean
	fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator? {
		return builder.routes()
				.route("shop-service") { r: PredicateSpec ->
						r.cookie("chocolate", "\\d+")
							.uri("http://localhost:8082")
				}
				.build()
	}
```

**The Header Route Predicate Factory**

`Header` route predicate는 header name과 header value를 인자로 가진다. header value는 정규 표현식으로 표현되며 해당 정규 표현식과 일치하는 header value를 가졌을 때 매칭시킨다

X-Request-Id 라는 이름의 header의 값이 정수로 이루어졌을 때만 라우팅 시키고 싶으면

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: shop-service
        uri: http://localhost:8082
        predicates:
        - Header=X-Request-Id, \d+
```

또는

```kotlin
@Bean
	fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator? {
		return builder.routes()
				.route("shop-service") { r: PredicateSpec ->
						r.header("X-Request-Id", "\\d+")
							.uri("http://localhost:8082")
				}
				.build()
	}
```

**The Path Route Predicate Factory**

`Path` route predicate 는 PathMatcher 패턴 리스트와 matchTrailingSlash를 인자로 받는다. matchTrailingSlash의 경우 optional이며 기본값은 true 이다

`/shop/orders/{orderId}` 의 경로오는 요청을 라우팅 시키고 싶으면

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: shop-service
        uri: http://localhost:8082
        predicates:
        - Path=/shop/orders/{orderId}
```

또는

```kotlin
@Bean
	fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator? {
		return builder.routes()
				.route("shop-service") { r: PredicateSpec ->
						r.path("/shop/orders/{orderId}")
							.uri("http://localhost:8082")
				}
				.build()
	}
```

만약 `Path=/shop/orders/{orderId}, false` 를 통해 matchTrailingSlash 옵션이 false가 된다면, `/shop/orders/1/` 와 같이 마지막에 `/` 가 붙어서 오는 요청에 대해 매칭되지 않는다.

만약 필터에서 {orderId}의 파라미터 값에 접근하고 싶다면

```kotlin
@Component
class CustomFilter: AbstractGatewayFilterFactory<Config>(Config::class.java) {

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange)
            println(uriVariables["orderId"])
            chain.filter(exchange).then(Mono.fromRunnable(Runnable { println("First post filter") }))
        }
    }
}
```

**The Query Route Predicate Factory**

`Query` route predicate 는 query name과 query value를 인자로 가진다. query value는 정규표현식으로 표현되며 해당 정규 표현식과 일치하는 query value를 가졌을 때 매칭시킨다. 또한 query value는 optional이기 때문에 해당 값이 존재하지 않을 경우 해당 query name을 가진 모든 요청을 매칭 시킨다

**The RemoteAddr Route Predicate Factory**

`RemoteAddr` route predicate 는 하나 이상의 CIDR (IPv4 또는 IPv6) 를 인자로 가진다.

만약 192.168.xxx.xxx 의 ip를 가지는 요청만 라우팅 시키고 싶으면

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: shop-service
        uri: http://localhost:8082
        predicates:
        - RemoteAddr=192.168.1.1/24
```

또는

```kotlin
@Bean
	fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator? {
		return builder.routes()
				.route("shop-service") { r: PredicateSpec ->
						r.remoteAddr("192.168.1.1/24")
							.uri("http://localhost:8082")
				}
				.build()
	}
```
### Spring Cloud Gateway에서의 속도 제한

Spring Cloud Gateway에서는 사용자의 초당 요청 속도를 제한할 수 있는 방법을 제공한다. 

내부적으로 rate limiting을 구현하기 위해 **token bucket algorithm**을 사용한다

**의존성 추가**

내부적으로 redis를 사용하기 때문에 `spring-boot-starter-data-redis-reactive` 을 추가해주어야 한다

```kotlin
// build.gradle.kts
...
project(":gateway") {
	dependencies {
		implementation("org.springframework.cloud:spring-cloud-starter-gateway")
		implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
	}
}
...
```

**KeyResolver**

요청을 식별할 수 있는 KeyResolver를 정의할 수 있다

```kotlin
@Bean
	fun userKeyResolver(): KeyResolver {
		return KeyResolver { exchange ->
			Mono.just(exchange.request.queryParams.getFirst("userId") ?: "1")
		}
	}
```

위의 경우는  `localhost:8080?userId=1234` 와 같이 param으로 userId가 오면 해당 값을 가지고 요청을 식별한다

**설정**

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: shop-service
          uri: http://localhost:8081
          predicates:
            - Path=/shop/**
          filters:
            - RewritePath=/shop/(?<segment>.*), /$\{segment}
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 6
                redis-rate-limiter.burstCapacity: 10
```

### Reference
 - [https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#glossary](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#glossary)
- [https://medium.com/@niral22/spring-cloud-gateway-tutorial-5311ddd59816](https://medium.com/@niral22/spring-cloud-gateway-tutorial-5311ddd59816)