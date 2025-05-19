# CodePing Core Server 🚀

A robust backend service that integrates with multiple competitive programming platforms to provide unified data access and analytics.

## Features ✨

- **Multi-Platform Integration**
  - LeetCode
  - CodeChef
  - Codeforces

- **User Data**
  - Profile information
  - Contest history
  - Recent submissions
  - Ratings and rankings

- **Contest Management**
  - Upcoming contests
  - Contest history
  - Performance analytics

- **Smart Caching**
  - Efficient data caching
  - Automatic cache refresh
  - Manual cache invalidation

## Tech Stack 🛠

- Java 17
- Spring Boot 3.x
- Spring WebFlux
- Spring Cache
- PostgreSQL
- Redis (for caching)
- Swagger/OpenAPI
- Maven

## Getting Started 🏁

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL
- Redis

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/codeping-core-server.git
   cd codeping-core-server
   ```

2. Configure the application:
   - Copy `application.yml.example` to `application.yml`
   - Update the configuration with your database and Redis settings

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Documentation 📚

The API documentation is available at `/swagger-ui.html` when the application is running.

### Key Endpoints

#### LeetCode Integration
- `GET /api/leetcode/profile/{username}`
- `GET /api/leetcode/contest-history/{username}`
- `GET /api/leetcode/recent-submissions/{username}`
- `GET /api/leetcode/upcoming-contests`

#### CodeChef Integration
- `GET /api/codechef/profile/{username}`
- `GET /api/codechef/ratings/{username}`
- `GET /api/codechef/recent-submissions/{username}`
- `GET /api/codechef/upcoming-contests`

#### Codeforces Integration
- `GET /api/codeforces/profile/{handle}`
- `GET /api/codeforces/rating/{handle}`
- `GET /api/codeforces/recent-submissions/{handle}`
- `GET /api/codeforces/upcoming-contests`

## Caching Strategy 🔄

The application implements a smart caching strategy:

- **User Profiles**: Cached for 6 hours
- **Contest History**: Cached for 2 hours
- **Recent Submissions**: Cached for 15 minutes
- **Upcoming Contests**: Cached for 1 hour

Cache can be manually invalidated using the refresh endpoints.

## Contributing 🤝

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Links

Profile: [gowtham-2oo5](https://github.com/gowtham-2oo5)

Project Link: [https://github.com/gowtham-2oo5/CodePing](https://github.com/gowtham-2oo5/CodePing) 