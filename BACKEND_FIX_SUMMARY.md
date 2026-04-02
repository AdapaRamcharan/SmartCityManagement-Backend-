# Backend Connection - Issues Fixed

## Problem Identified
The frontend was getting a **400 Bad Request** when trying to login because:
1. **Missing `/auth/login` endpoint** - The backend AuthController only had `/auth/register`, not login
2. **Field mismatch** - Frontend was sending `{email, password}` but the backend expected `{username, password}`

## Changes Made to Backend

### 1. ✅ Added `/auth/login` Endpoint
**File:** `src/main/java/com/smartcity/backend/controller/AuthController.java`
- New POST endpoint at `/auth/login` that accepts `{email, password}`
- Finds user by email
- Authenticates password securely
- Generates JWT token
- Logs login activity
- Returns user details with token

### 2. ✅ Created LoginRequestEmail DTO
**File:** `src/main/java/com/smartcity/backend/dto/LoginRequestEmail.java`
- New DTO to handle email-based login requests
- Accepts `email` and `password` fields

### 3. ✅ Updated JwtResponse DTO
**File:** `src/main/java/com/smartcity/backend/dto/JwtResponse.java`
- Added `email` field
- Added `id` field
- Added getter/setter methods

### 4. ✅ Updated UserRepository
**File:** `src/main/java/com/smartcity/backend/repository/UserRepository.java`
- Added `findByEmail(String email)` method for email-based user lookup

## CORS Configuration
✅ Already configured correctly in `WebSecurityConfig.java` for:
- Frontend origin: `http://localhost:3000`
- Allowed methods: GET, POST, PUT, DELETE, OPTIONS, PATCH
- Allowed headers: Authorization, Content-Type, Accept

## NEXT STEPS - What You Need to Do

### 1. Build the Backend
Navigate to the backend directory and build with Maven:
```bash
cd c:\Users\DELL\Documents\workspace-spring-tools-for-eclipse-4.32.2.RELEASE\SmartCityBackend
mvn clean package -DskipTests
# OR in Spring Tools IDE: Right-click project → Run As → Maven build → enter "clean package"
```

### 2. Ensure MySQL Database Exists
The backend expects a MySQL database called `smart_city_db` with user `root` and password `root`:
```sql
CREATE DATABASE IF NOT EXISTS smart_city_db;
-- Spring will auto-create tables with spring.jpa.hibernate.ddl-auto=update
```

**To connect to MySQL:**
```bash
mysql -u root -p
# Enter password: root
```

### 3. Start the Backend
After successful build, run:
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
# OR in Spring Tools IDE: Right-click project → Run As → Spring Boot App
# OR: mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 4. Test the Connection
The frontend (`http://localhost:3000`) should now successfully connect to the backend:

**Test Signup:**
- POST to `http://localhost:8080/auth/register`
- Body: `{username: "testuser", email: "test@example.com", password: "password123", role: "USER"}`
- Expected: 200 OK with message "User registered successfully!"

**Test Login:**
- POST to `http://localhost:8080/auth/login`
- Body: `{email: "test@example.com", password: "password123"}`
- Expected: 200 OK with JWT token and user details

## Configuration Summary

| Component | Value |
|-----------|-------|
| Backend Port | 8080 (default) |
| Frontend Port | 3000 |
| Database | MySQL at localhost:3306 |
| Database Name | smart_city_db |
| DB Username | root |
| DB Password | root |
| CORS Origin | http://localhost:3000 ✅ |
| JWT Expiration | 24 hours (86400000 ms) |
| Auth Endpoints | `/auth/login`, `/auth/register` |
| API Base | `/api/` (cities, issues, feedback, etc.) |

## API Endpoints Available

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login with email & password (NEW)

### Cities & Data
- `GET /api/cities` - Get all cities
- `GET /api/amenities/search` - Search amenities
- `POST /api/issues` - Report an issue
- `GET /api/issues/user/{userId}` - Get user's issues
- `POST /api/feedback` - Submit feedback

### Admin Only (`/api/admin/`)
- `GET /api/admin/stats` - Dashboard statistics
- `GET /api/admin/issues` - All issues
- `PUT /api/admin/issues/{id}` - Update issue status
- `GET /api/admin/feedback` - All feedback
- `GET /api/admin/logs` - Login history

## Troubleshooting

If you still get 400 errors after building:
1. **Check backend logs** - Look for any error messages
2. **Verify MySQL is running** - Check if database connection works
3. **Verify CORS** - Frontend and backend should both be accessible
4. **Check request format** - Frontend should send `{email, password}` not `{username, password}`
5. **Verify JWT token generation** - Check if JwtUtils is properly configured

## Files Modified
- `AuthController.java` - Added login endpoint
- `LoginRequestEmail.java` - Created new DTO
- `JwtResponse.java` - Added email and id fields
- `UserRepository.java` - Added findByEmail method
