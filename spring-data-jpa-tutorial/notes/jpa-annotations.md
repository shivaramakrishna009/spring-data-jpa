Got it, Siva ✅  
From now on, every time we come across a **new annotation** in your Spring Boot + JPA journey, I’ll give you a **ready-to-paste `.md` section** so your file grows into a **personal annotation handbook**.

Let’s start by adding **everything we’ve already covered** so your `.md` file is up to date from day one.

---

# **Spring Boot + JPA Annotation Handbook**

## **1. @Entity**
- **Purpose:** Marks a class as a JPA entity (maps to a database table).
- **Notes:** Required for JPA to manage persistence.

---

## **2. @Table**
```java
@Table(name = "tbl_student")
```
- **Purpose:** Specifies the table name (and optionally schema) in the database.
- **Default:** Without this, table name = class name.

---

## **3. @Id**
- **Purpose:** Marks the primary key field.
- **Notes:** Every entity must have exactly one `@Id`.

---

## **4. @GeneratedValue**
```java
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
```
- **Purpose:** Specifies how the primary key is generated.
- **Strategies:**
    - `IDENTITY` → Auto-increment (MySQL)
    - `SEQUENCE` → Uses a DB sequence (PostgreSQL, Oracle)
    - `AUTO` → Lets JPA decide
    - `TABLE` → Uses a table to store sequence values

---

## **5. @SequenceGenerator**
```java
@SequenceGenerator(
    name = "student_sequence",
    sequenceName = "student_sequence",
    allocationSize = 1
)
```
- **Purpose:** Defines a database sequence for generating primary key values.
- **Parameters:**
    - `name`: Internal reference name for the generator.
    - `sequenceName`: Actual DB sequence name.
    - `allocationSize`: Number of IDs allocated at once (1 = no caching).

---

## **6. @Column**
```java
@Column(name = "email_address", nullable = false, unique = true)
```
- **Purpose:** Customizes column mapping and constraints.
- **Parameters:**
    - `name`: Column name in DB.
    - `nullable = false`: Adds NOT NULL constraint.
    - `unique = true`: Adds UNIQUE constraint.

---

## **7. @Data** *(Lombok)*
- **Purpose:** Generates getters, setters, `toString()`, `equals()`, and `hashCode()`.
- **Notes:** Be careful with bidirectional relationships — can cause infinite recursion in `toString()`.

---

## **8. @AllArgsConstructor** *(Lombok)*
- **Purpose:** Generates a constructor with all fields.

---

## **9. @NoArgsConstructor** *(Lombok)*
- **Purpose:** Generates a no-argument constructor.
- **Notes:** Required by JPA for entity instantiation.

---

## **10. @Builder** *(Lombok)*
- **Purpose:** Implements the Builder pattern for object creation.
- **Example:**
```java
Student.builder()
       .firstName("Siva")
       .emailId("siva@example.com")
       .build();
```
---

## **11. @Repository**
```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
```
- **Purpose:** Marks the class/interface as a Spring Data Repository.
- **Notes:**
    - Optional for interfaces extending `JpaRepository` — Spring will still detect them automatically.
    - Useful for exception translation (converts JPA exceptions into Spring’s `DataAccessException` hierarchy).

---

## **12. JpaRepository**
```java
public interface StudentRepository extends JpaRepository<Student, Long> { }
```
- **Purpose:** Provides CRUD operations, pagination, and query methods for an entity.
- **Type Parameters:**
    - First: Entity type (`Student`)
    - Second: Primary key type (`Long`)
- **Common Methods:**
    - `save(entity)` → Insert or update
    - `findById(id)` → Retrieve by primary key
    - `findAll()` → Retrieve all records
    - `deleteById(id)` → Delete by primary key

---

## **13. @Autowired**
```java
@Autowired
private StudentRepository studentRepository;
```
- **Purpose:** Injects a Spring-managed bean into another bean.
- **Notes:**
    - Can be used on fields, constructors, or setters.
    - Constructor injection is generally preferred for immutability and testability.

---

## **14. @SpringBootTest**
```java
@SpringBootTest
class StudentRepositoryTest { ... }
```
- **Purpose:** Loads the full Spring application context for integration testing.
- **Notes:**
    - Slower than unit tests because it starts the entire context.
    - Ideal for testing repository and service layers together.

---

## **15. @Test** *(JUnit 5)*
```java
@Test
void saveStudentTest() { ... }
```
- **Purpose:** Marks a method as a test case.
- **Notes:** Comes from `org.junit.jupiter.api.Test`.

---

💡 **Pro Tip:**  
For repository tests, you can also use `@DataJpaTest` instead of `@SpringBootTest` — it loads only JPA-related components and uses an in-memory database by default, making tests faster.

---

Absolutely, Siva — this is a great move toward clean, modular entity design. Here's your **ready-to-paste `.md` note section** for the `Guardian` class and all the annotations you've used:

---

# **Embedded Entity – Guardian Class**

## **16. @Embeddable**
```java
@Embeddable
public class Guardian { ... }
```
- **Purpose:** Marks a class whose fields can be embedded into another entity.
- **Usage:** Used with `@Embedded` in the parent entity (e.g., `Student`).
- **Notes:** No separate table is created — fields are flattened into the parent table.

---

## **17. @Embedded**
```java
@Embedded
private Guardian guardian;
```
- **Purpose:** Embeds the fields of an `@Embeddable` class into the entity.
- **Effect:** Guardian’s fields (`name`, `email`, `mobile`) become columns in the `Student` table.

---

## **18. @AttributeOverride**
```java
@AttributeOverride(
    name = "name",
    column = @Column(name = "guardian_name")
)
```
- **Purpose:** Overrides the default column name for a specific field in the embedded class.
- **Usage:** Used when you want to rename embedded fields in the parent table.

---

## **19. @AttributeOverrides**
```java
@AttributeOverrides({
    @AttributeOverride(name = "name", column = @Column(name = "guardian_name")),
    @AttributeOverride(name = "email", column = @Column(name = "guardian_email")),
    @AttributeOverride(name = "mobile", column = @Column(name = "guardian_mobile"))
})
```
- **Purpose:** Allows multiple `@AttributeOverride` annotations.
- **Notes:** Required when embedding a class with multiple fields that need custom column names.

---

## **20. Lombok in Embeddable**
- `@Data` → Generates getters/setters and utility methods.
- `@AllArgsConstructor` → Full constructor.
- `@NoArgsConstructor` → Required by JPA.
- `@Builder` → Enables builder pattern for clean object creation.

---

## ✅ Example Usage in `Student` Entity
```java
@Embedded
private Guardian guardian;
```

## ✅ Resulting Table Columns
| Field in Guardian | Column in Student Table |
|-------------------|-------------------------|
| `name`            | `guardian_name`         |
| `email`           | `guardian_email`        |
| `mobile`          | `guardian_mobile`       |

---

# **Spring Data JPA – Derived Query Methods**

## **21. Method Naming Convention**
- Spring Data JPA can **auto-generate queries** based on method names.
- Format: `findBy<FieldName>[Condition]`
- No need to write `@Query` or native SQL — Spring parses the method name and builds the query.

---

## ✅ Examples from `StudentRepository`

### `findByFirstName(String firstName)`
- **Query:** `SELECT * FROM student WHERE first_name = ?`
- **Use Case:** Exact match on first name.

---

### `findByFirstNameContaining(String name)`
- **Query:** `SELECT * FROM student WHERE first_name LIKE %?%`
- **Use Case:** Partial match (substring search).
- **Notes:** Equivalent to SQL `LIKE '%name%'`.

---

### `findByLastNameNotNull()`
- **Query:** `SELECT * FROM student WHERE last_name IS NOT NULL`
- **Use Case:** Fetch students with a last name present.

---

### `findByGuardianName(String guardianName)`
- **Query:** `SELECT * FROM student WHERE guardian_name = ?`
- **Use Case:** Match embedded field directly (flattened into student table via `@Embedded`).

---

## 💡 Supported Keywords
| Keyword | SQL Equivalent |
|--------|----------------|
| `IsNull` / `NotNull` | `IS NULL` / `IS NOT NULL` |
| `Containing` | `LIKE %value%` |
| `StartingWith` | `LIKE value%` |
| `EndingWith` | `LIKE %value` |
| `IgnoreCase` | Case-insensitive match |
| `Between` | Range query |
| `OrderBy` | Sorting |
| `And` / `Or` | Logical operators |

---

## ✅ Best Practices
- Keep method names readable and expressive.
- Use `Containing`, `StartingWith`, etc. for flexible search.
- For complex queries, switch to `@Query` with JPQL or native SQL.

---

# **Spring Data JPA – `@Query` Annotation**

## **22. Purpose**
- Allows writing **custom queries** directly on repository methods.
- Supports:
    - **JPQL** (Java Persistence Query Language) → works with **entity names** & **fields**.
    - **Native SQL** → works with **table names** & **column names** (`nativeQuery = true`).

---

## ✅ Examples from `StudentRepository`

### JPQL – Selecting Specific Field
```java
@Query("SELECT s.firstName FROM Student s WHERE s.emailId = ?1")
String getStudentFirstNameByEmailAddress(String emailId);
```
- **Query:** Uses entity name `Student` and field `firstName`.
- **Use Case:** Fetch only the first name for a given email.
- **Notes:** JPQL is **database-independent**.

---

### JPQL – Named Parameters
```java
@Query("SELECT s FROM Student s WHERE s.emailId = :email")
Student findByEmail(@Param("email") String emailId);
```
- **Query:** Uses `:email` as a named parameter.
- **Benefit:** Improves readability and avoids confusion with multiple parameters.

---

### Native SQL – Full Entity
```java
@Query(value = "SELECT * FROM student WHERE guardian_name = ?1", nativeQuery = true)
List<Student> findByGuardianNameNative(String guardianName);
```
- **Query:** Uses actual table & column names.
- **Use Case:** When JPQL can’t express the query or DB-specific features are needed.

---

### Native SQL – Partial Column Selection
```java
@Query(value = "SELECT first_name FROM student WHERE email_id = ?1", nativeQuery = true)
String getFirstNameByEmailNative(String emailId);
```
- **Return Type:** Must match the selected column type (`String` here).
- **Note:** For multiple columns, use `Object[]` or a DTO projection.

---

## 💡 Parameter Binding
| Type | Syntax | Example |
|------|--------|---------|
| **Positional** | `?1`, `?2` | `WHERE s.emailId = ?1` |
| **Named** | `:paramName` | `WHERE s.emailId = :email` |

---
# **Spring Data JPA – Update Queries**

When you need to perform **update operations** directly from the repository, you can use a combination of annotations to define custom update queries.

# **Custom Update Query Annotations**

## **22. @Query**
```java
@Query(
    value = "UPDATE tbl_student SET first_name = :name WHERE email_address = :emailId",
    nativeQuery = true
)
```
- **Purpose:** Allows writing custom JPQL or native SQL queries.
- **Parameters:**
    - `value`: The query string.
    - `nativeQuery = true`: Indicates this is raw SQL (not JPQL).
- **Notes:**
    - For JPQL, use entity names and field names instead of table/column names.
    - For native SQL, use actual table and column names.

---

## **23. @Modifying**
```java
@Modifying
```
- **Purpose:** Marks a query method as an **update/delete** operation.
- **Why Needed:**
    - By default, `@Query` is for `SELECT` queries.
    - Without `@Modifying`, Spring Data JPA will expect a result set and throw an error.
- **Notes:**
    - Works with `UPDATE`, `DELETE`, and `INSERT` queries.
    - Returns `int` or `long` → number of rows affected.

---

## **24. @Transactional**
```java
@Transactional
```
- **Purpose:** Ensures the update query runs inside a transaction.
- **Why Needed:**
    - Update/delete queries must be executed within a transaction to commit changes.
- **Notes:**
    - Can be placed at method or class level.
    - For read-only queries, use `@Transactional(readOnly = true)`.

---

## **25. @Param**
```java
@Param("name") String firstName
```
- **Purpose:** Binds a method parameter to a named parameter in the query.
- **Notes:**
    - The string inside `@Param` must match the `:parameterName` in the query.
    - If you don’t use `@Param`, Spring matches parameters by position.

---

## ✅ Example Method
```java
@Modifying
@Transactional
@Query(
    value = "UPDATE tbl_student SET first_name = :name WHERE email_address = :emailId",
    nativeQuery = true
)
int updateStudentNameByEmailId(@Param("name") String firstName, String emailId);
```
- **Effect:** Updates the `first_name` column for the student with the given `email_address`.
- **Return:** Number of rows updated.

---

💡 **Pro Tips:**
- Keep update/delete queries in service layer for better separation of concerns.
- If you use JPQL instead of native SQL:
```java
@Query("UPDATE Student s SET s.firstName = :name WHERE s.emailId = :emailId")
```
- Always test update queries — they bypass entity lifecycle callbacks.

---


