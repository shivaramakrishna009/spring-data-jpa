### **Spring Boot Database Configuration – Quick Notes**
- **spring.datasource.url** → JDBC connection string to DB.  
  Example: `jdbc:mysql://localhost:3306/school_db`
    - `localhost` = DB host
    - `3306` = MySQL default port
    - `school_db` = DB name

- **spring.datasource.username** → DB login username (e.g., `root`).  
  *Use a dedicated user in production.*

- **spring.datasource.password** → DB login password.  
  *Avoid hardcoding; use env variables in production.*

- **spring.datasource.driver-class-name** → JDBC driver class (MySQL = `com.mysql.cj.jdbc.Driver`).  
  *Usually auto-detected.*

- **spring.jpa.hibernate.ddl-auto** → Schema handling strategy:
    - `none` → No changes
    - `validate` → Check schema matches entities
    - `update` → Auto-update schema (dev only)
    - `create` → Create schema each start (drops old data)
    - `create-drop` → Create on start, drop on shutdown

- **spring.jpa.show-sql** → `true` = Show SQL queries in console (dev only).

---

💡 **Pro Tips:**
- Use `update` only in dev; in prod use `validate` + migration tools (Flyway/Liquibase).
- Hide credentials using environment variables or config server.
- Disable `show-sql` in production for performance & security.

---