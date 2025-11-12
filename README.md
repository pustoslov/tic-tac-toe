# Tic-tac-toe Service

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4%2B-brightgreen?logo=springboot)](https://spring.io/)
[![Java](https://img.shields.io/badge/Java-21%2B-orange?logo=openjdk)](https://openjdk.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15%2B-blue?logo=postgresql)](https://www.postgresql.org/)
[![Gradle](https://img.shields.io/badge/Gradle-9.0%2B-green?logo=gradle)](https://gradle.org/)

A RESTful API service for playing tic-tac-toe online. Allows users to register, create and play games both with a computer and with other players.


## Table of contents
- [Quick start](#quick-start)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [API Documentation](#api-documentation)
    - [Base URL](#base-url)
    - [Authentication](#authentication)
    - [Available Endpoints](#available-endpoints)
    - [Request/Response Models](#requestresponse-models)


## Quick start

### Prerequisites

- **Java 21** or higher
- **PostgreSQL 15** or higher
- **Gradle 9.0** or higher

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/pustoslov/tic-tac-toe.git
   cd tic-tac-toe
   ```

2. **Configure database:**

    ```bash
    sudo -u postgres psql
    ```
    ```sql
    CREATE DATABASE mydatabase;
    CREATE USER myuser WITH PASSWORD 'mysecurepassword';
    GRANT ALL PRIVILEGES ON DATABASE mydatabase TO myuser;
    ```

3. **Configure environment variables:**

    ```bash
    #place it in .bashrc or .zshrc or any other place where you store enviroment variables.
    
    TIC_TAC_TOE_DB_URL=jdbc:postgresql://localhost:5432/mydatabase
    TIC_TAC_TOE_DB_USER=myuser
    TIC_TAC_TOE_DB_PASSWORD=mysecurepassword
    ```

4. **Run the application:**

    ```bash
    ./gradlew bootRun
    ```

## API Documentation

### Base URL

```bash
http://localhost:8080/api/v1
```

### Authentication

This API uses JWT Auth. All endpoints except `/auth/**` require Bearer Token header.


### Available Endpoints

#### Game Management

| Method | Endpoint | Description | Parameters | Request Body | Response | Status Codes |
|--------|----------|-------------|------------|--------------|----------|--------------|
| **POST** | `/game/new` | Create new game | `mode` (query) - Game mode | None | `GameResponse` | 200 - Success<br>400 - Invalid mode<br>401 - Unauthorized |
| **POST** | `/game/{gameId}/join` | Join existing game | `gameId` (path) - Game UUID | None | `GameResponse` | 200 - Success<br>404 - Game not found<br>409 - Cannot join game |
| **POST** | `/game/{gameId}/move` | Make a move in game | `gameId` (path) - Game UUID | `MoveRequest` | None | 200 - Success<br>400 - Invalid move<br>404 - Game not found |

#### Game Information

| Method | Endpoint | Description | Parameters | Response | Status Codes |
|--------|----------|-------------|------------|----------|--------------|
| **GET** | `/game/{gameId}` | Get game details | `gameId` (path) - Game UUID | `GameResponse` | 200 - Success<br>404 - Game not found |
| **GET** | `/game/ongoing` | Get user's ongoing games | None | `List<UUID>` | 200 - Success<br>401 - Unauthorized |
| **GET** | `/game/available` | Get available games to join | None | `List<UUID>` | 200 - Success<br>401 - Unauthorized |

#### User Management

| Method  | Endpoint         | Description             | Parameters                        | Response                    | Status Codes                                                     |
|---------|------------------|-------------------------|-----------------------------------|-----------------------------|------------------------------------------------------------------|
| **GET** | `/user/{userId}` | Get user data by id     | `userId` (path) - User UUID       | `UserDataResponse`          | 200 - Success<br>404 - User not found<br>401 - Unauthorized      |
| **GET** | `/user/me`       | Get current user data   | None                              | `UserDataResponse`          | 200 - Success<br>401 - Unauthorized                              |
| **GET** | `/user/top`      | Get list of top players | `limit` (path) - Count of records | `List<RatingStatsResponse>` | 200 - Success<br>401 - Unauthorized<br>400 - Invalid linit value |

#### Auth Management

| Method   | Endpoint                    | Description         | Parameters | Response      | Status Codes                                 |
|----------|-----------------------------|---------------------|------------|---------------|----------------------------------------------|
| **POST** | `/auth/signup`              | Sign up             | None       | None          | 200 - Success<br>422 - User already exists   |
| **POST** | `/auth/login`               | Log in              | None       | None          | 200 - Success<br><br>401 - Validation error  |
| **POST** | `/auth/update_access_token` | Update access token | None       | `JwtResponse` | 200 - Success<br><br>401 - Validation error  |

### Request/Response Models

- **`JwtRequest`**:
    ```json
    {
      "userName": "john_doe",
      "password": "securePassword123"
    }
    ```
- **`JwtResponse`**:
  ```json
   {
      "type": "Bearer",
      "accessToken": "qwertyGeneratedAccessTokenHere1234",
      "refreshToken": "ytrewqGeneratedRefreshTokenHere4321"
   }
  ```

- **`RefreshJwtRequest`**:
  ```json
   {
      "refreshToken": "ytrewqGeneratedRefreshTokenHere4321"
   }
  ```


- **`MoveRequest`**:

    ```json
    {
      "row": 2,
      "col": 3
    }
    ```

- **`GameResponse`**:
    ```json
    {
      "gameId": "2def310b-0dc1-469f-914e-578865a1a116",
      "xPlayerId": "bd7261cd-2e04-46f8-8c1f-52aceda5d104",
      "oPlayerId": "14887174-33ec-4edc-9a15-9da76bbb8d87",
      "currentTurnId": "14887174-33ec-4edc-9a15-9da76bbb8d87",
      "board": [
    	[1, 1, -1],
        [0, -1, 0],
        [1, 0, 0]
      ]
    }
    ```
    **In `board`:**
    - **1** stands for **'X'**
    - **-1** stands for **'O'**
    - **0** stands for **empty**
    - 
- **`RatingStatsResponse`**:
  ```json
   {
      "id": "abcdefgh-jklm-nopq-12rs-tuvw3456789x",
      "winRation": "3.14"
   }
  ```
- **`UserDataResponse`**:
  ```json
   {
      "id": "abcdefgh-jklm-nopq-12rs-tuvw3456789x",
      "login": "guest"
   }
  ```

- **`ErrorResponse`**:
  ```json
  {
    "error": "Illegal move",
    "message": "Not your turn"
  }
  ```




