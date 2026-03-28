# Environment Setup Guide

This project uses environment variables to securely store database credentials and OAuth2 secrets.

 **Do NOT commit real credentials to GitHub.**

---

## Required Environment Variables

Each developer must configure the following environment variables locally:

### Database (Supabase PostgreSQL)

- DB_URL=
- DB_USERNAME=
- DB_PASSWORD=
- ADMIN_GITHUB_LOGIN=your-github-username

#### Credentials will be provided via communication means (Slack)

---

## How To Set Environment Variables

### Option 0: Local Spring profile file (best for sharing with instructors)

This repo includes:

- `src/main/resources/application-local.properties.example`

Create your own local file by copying it to:

- `src/main/resources/application-local.properties`

Fill in these values in that file:

```properties
app.base-url=*******
spring.datasource.password=*******
spring.datasource.url=*******
spring.datasource.username=*******
spring.security.oauth2.client.registration.github.client-id=*******
spring.security.oauth2.client.registration.github.client-secret=*******
spring.security.oauth2.client.registration.google.client-id=*******
spring.security.oauth2.client.registration.google.client-secret=*******
```

Then run the backend with:

- `.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local`

This is usually the easiest option for someone cloning the repo because they do not need to configure environment variables in their IDE first.

### Option 1: IntelliJ (Recommended)

1. Go to **Run → Edit Configurations**
2. Select your Spring Boot run configuration
3. Add environment variables in this format:

- DB_URL=...
- DB_USERNAME=...
- DB_PASSWORD=...
- ADMIN_GITHUB_LOGIN=your-github-username

4. Save and run.

---

### Option 2: VS Code

If running via terminal:

Mac/Linux:

- export DB_URL="..."
- export DB_USERNAME="postgres"
- export DB_PASSWORD="..."



Windows (PowerShell):

- setx DB_URL "..."
- setx DB_USERNAME "postgres"
- setx DB_PASSWORD "..."
- setx ADMIN_GITHUB_LOGIN "your-github-username"



Restart terminal after setting variables.

---

## Security Rules

- Never commit credentials.
- Deployment platforms (Render/Heroku) must set environment variables in dashboard settings.

---

If you encounter connection errors:

- Check DB_URL format
- Verify password is correct
- Ensure SSL mode is included

## Admin Login

If you want your own GitHub account to sign in as an admin locally, set:

- ADMIN_GITHUB_LOGIN=your-github-username

When that GitHub user signs in, the backend will store or update their role as `ADMIN`.
