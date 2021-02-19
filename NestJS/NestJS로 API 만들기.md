# 01 - Introduction

### Nest.js란?

효율적이고, 안정적이며, 확장가능한 서버 어플리케이션을 구축하기 위한 Node.js 프레임워크이다. Nest.js는 모듈 구조를 제공함으로써 Node.js 문제를 해결하고자 했다. 

또한 Typescript로 구성되어 있으며 프레임워크에서 DI(Dependency Injection)를 지원하여 개발의 편의를 제공한다.

### 설치

nest cli 설치

```cpp
$ npm i -g @nestjs/cli
```

프로젝트 셍성

```cpp
$ nest new my-nest-project
```

## 02 - Nest.js 구조

### Controllers

대부분의 웹 프레임워크 처럼 Nest.js의 컨트롤러도 요청을 처리하고 클라이언트에 응답을 반환하는 역할을 한다.

Nest.js에서 컨트롤러를 정의하기 위해서는 아래와 같이 `@Controller(...)` 데코레이터를 붙이면 된다

```tsx
// users.controller.ts 

import { Controller, Get } from '@nestjs/common';

@Controller('/users')
export class UsersController {
 @Get()
 findAll() { 
   return 'Users';
 }
}
```

응용 프로그램 내의 모든 /users GET 요청을 `findAll()` 함수가 처리하게 된다.

Controller를 추가한 뒤 Nest.js가 해당 Controller를 인식하게 하기 위해 모듈 정의에 추가해주어야 한다. 루트 모듈 또는 서브 모듈에 추가해주면 된다. 

### Providers

Nest.js는 Angular에서 많은 영감을 받았기 때문에 Angular와 비슷하게 쉽게 Provider를 만들어 Controller나 다른 Provider에도 주입할 수 있다. 이러한 Provider들은 Service라고도 불린다. 

Nest.js의 Service Provider는 일반적인 Javascript 클래스에 `@Injectable()` 데코레이터가 붙은 것이다. 

예를 들어 아래와 같이 사용자를 가져오는 Service를 간단히 생성할 수 있다.

```tsx
// users.service.ts

import { Injectable } from '@nestjs/common';
import { User } from './interfaces/user.interface';

@Injectable()
export class UsersService {
  private readonly users: User[] = [];

  create(user: User) { 
    this.users.push(user);   
	}

  findAll(): User[] {
    return this.users;
  }
}
```

### Modules

`@Module` 데코레이터를 통해 해당 코드가 모듈임을 명시한다. 해당 데코레이터에 작성된 메타데이터를 통해 Nest.js에 조직화된 구조를 제공한다. 

Nest.js는 적어도 하나 이상의 모듈을 가져야한다. 보통은 root module을 가지는데 이 root module은 top-level 모듈로써 어플리케이션이 시작될 때 등록된다

```tsx
import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule, {});
  await app.listen(3000);
}
bootstrap();
```

또한 어플리케이션이 복잡해진다면 특정 기능을 하는 모듈들을 분리하여 관리할 수 있다. 

```tsx
// user.module.ts

import { Module } from '@nestjs/common';
import { UsersController } from './users.controller.ts';
import { UsersService } from './users.service.ts';

@Module({
  controllers: [UsersController],
  providers: [UsersService]
})

export class UsersModule {}
```

단, 이렇게 분리한 모듈은 root module에 등록해주어야 한다 

```tsx
// app.module.ts

import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { UserModule } from './user/user.module';

@Module({
  imports: [UserModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
```

### Middleware

미들웨어는 요청에 대한 Router Handler가 처리되기 전에 호출되는 함수이다. Nest.js의 미들웨어는 기본적으로 express의 미들웨어와 동일하다. 

커스텀 미들웨어를 만들기 위해서는 NestMiddleware를 implements한 클래스에 @Injectable() 데코레이터를 붙여주면 된다. 

```tsx
// logger.middleware.ts
import { Injectable, NestMiddleware } from '@nestjs/common';
import { Request, Response, NextFunction } from 'express';

@Injectable()
export class LoggerMiddleware implements NestMiddleware {
  use(req: Request, res: Response, next: NextFunction) {
    console.log('Request...');
    next();
  }
}
```

이렇게 생성한 미들웨어를 모듈 클래스의 `configure()` 함수를 통해 등록해주면 된다.

```tsx
// app.module.ts
import { Module, NestModule, MiddlewareConsumer } from '@nestjs/common';
import { LoggerMiddleware } from './common/middleware/logger.middleware';
import { UsersController } from './users.controller.ts';
import { UsersService } from './users.service.ts';
import { UserModule } from './user/user.module';

@Module({
  imports: [UserModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule implements NestModule {
  configure(consumer: MiddlewareConsumer) {
    consumer
      .apply(LoggerMiddleware)
			.forRoutes('*');
  }
}
```
## 참고

- [https://docs.nestjs.com/](https://docs.nestjs.com/)
- [https://morioh.com/p/e9368882c1cb](https://morioh.com/p/e9368882c1cb)