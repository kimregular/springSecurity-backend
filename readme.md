# Spring Security JWT Practice

## Architecture

![구조](https://junhyunny.github.io/images/make-authentication-provider-1.JPG)

## JWT

### 구조와 특징

JWT는

`header.payload.signature`

구조로 이루어져 있다. 각 요소는 다음의 기능을 수행한다.

- header
    - 클라이언트가 보낸 토큰이 JWT임을 명시한다
    - 검증에 필요한 암호화 알고리즘을 명시한다.
- payload
    - 실제로 사용자가 입력한 데이터가 들어가 있다.
    - username, role, expire 등
    ```json

    {
        "username" : "test",
        "role" : "ROLE_ADMIN",
        ...
  }
    ```
  각각의 키-값 항목을 클레임이라고 부른다
- signature
    - 토큰을 발행한 서버에서만 확인할 수 있도록 서명
    - `BASE64(header) + BASE64(payload) + 비밀키`

내부 정보를 단순 base64 방식으로 인코딩하기 때문에 외부에서 쉽게 디코딩할 수 있다. 외부에서 열람해도 되는 정보를 담아야하며, 토큰 자체의 발급처를 확인하기 위해 사용해야 한다.

**따라서 토큰 내부에 비밀번호와 같은 민감한 정보를 담아서는 안 된다!**

### 암호화 방식

- 암호화 종류
    - 양방향 -> 암호화 후 다시 복호화 가능
        - 대칭키
        - 비대칭키
    - 단방향

비밀키는 내부에 넣지 말고 properties 파일등에 따로 저장한다.