
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
# **Spring Data JPA – Entity Relationships**

## **26. @OneToOne**
```java
@OneToOne
private Course course;
```
- **Purpose:** Defines a **one-to-one relationship** between two entities.
- **Effect:** Each `CourseMaterial` is linked to exactly one `Course`, and vice versa (unless mapped differently on the other side).
- **Defaults:**
    - Fetch type = `EAGER` (loads related entity immediately).
    - Cascade = none (changes don’t propagate unless specified).
- **Best Practice:**
    - Often set `fetch = FetchType.LAZY` to avoid unnecessary loading.
    - Use `cascade = CascadeType.ALL` if you want related entity changes to persist automatically.

---

## **27. @JoinColumn**
```java
@JoinColumn(
    name = "course_id",
    referencedColumnName = "courseId"
)
```
- **Purpose:** Specifies the **foreign key column** in the owning entity’s table.
- **Parameters:**
    - `name`: Column name in `course_material` table that stores the FK.
    - `referencedColumnName`: Column in the target entity (`Course`) that this FK points to.
- **Notes:**
    - If omitted, defaults to `<fieldName>_<referencedColumnName>`.
    - This annotation is placed on the **owning side** of the relationship.

---

💡 **Example Table Structure**
- **course_material**
    - `course_material_id` (PK)
    - `url`
    - `course_id` (FK → `course.course_id`)

---

## **28. Relationship Ownership**
- In a `@OneToOne` mapping:
    - The side with `@JoinColumn` is the **owning side** (controls the FK column).
    - The other side (if bidirectional) uses `mappedBy` to indicate it’s the inverse side.

---

✅ **Pro Tip:**  
If you make this relationship **bidirectional**, in `Course` you’d have:
```java
@OneToOne(mappedBy = "course")
private CourseMaterial courseMaterial;
```
This avoids creating two foreign keys and keeps the mapping clean.

---
# **29. Inverse Side of One-to-One Relationship**

```java
@OneToOne(mappedBy = "course")
private CourseMaterial courseMaterial;
```
- **Purpose:** Marks this side of the relationship as the **inverse (non-owning) side** in a one-to-one mapping.
- **`mappedBy` parameter:**
    - Refers to the field name in the owning entity (`CourseMaterial.course`) that controls the relationship and foreign key.
- **Effect:**
    - No separate foreign key column is created in the `course` table.
    - JPA uses the mapping defined in the owning side (`CourseMaterial`) to join the entities.
- **Best Practice:**
    - Always ensure `mappedBy` matches the exact field name in the owning entity.
    - Use this when you want bidirectional navigation without duplicate foreign keys.

---
### ✅ Example: Bidirectional Mapping
**CourseMaterial.java**
```java
@OneToOne
@JoinColumn(name = "course_id", referencedColumnName = "courseId")
private Course course;
```

**Course.java**
```java
@OneToOne(mappedBy = "course")
private CourseMaterial courseMaterial;
```

---

## Quick Recap — One-to-One Ownership Rules

| **Side**        | **Annotation Example**                                      | **Controls FK?** | **Table Containing FK**      | **Notes** |
|-----------------|--------------------------------------------------------------|------------------|------------------------------|-----------|
| **Owning Side** | `@OneToOne @JoinColumn(name = "course_id", referencedColumnName = "courseId")` | ✅ Yes           | Owning entity’s table        | Has the `@JoinColumn` annotation; defines and manages the foreign key column. |
| **Inverse Side**| `@OneToOne(mappedBy = "course")`                              | ❌ No            | Uses FK from owning side     | Refers to the field name in the owning entity; no extra FK column is created. |

---
# **Cascade Types in JPA**

## **30. @OneToOne with Cascade**

### Annotation
```java
@OneToOne(cascade = CascadeType.ALL)
private Course course;
```

### **Purpose**
- Enables **automatic propagation** of operations (like `save`, `delete`) from the **owning entity** to the **associated entity**.
- In this case, when you persist or delete a `CourseMaterial`, the associated `Course` is also persisted or deleted.

---

### **CascadeType Options**

| **Cascade Type**     | **Effect**                                                                 |
|----------------------|----------------------------------------------------------------------------|
| `ALL`                | Applies all cascade operations listed below.                              |
| `PERSIST`            | Saves the associated entity when the parent is saved.                     |
| `MERGE`              | Merges changes from the associated entity when the parent is merged.      |
| `REMOVE`             | Deletes the associated entity when the parent is deleted.                 |
| `REFRESH`            | Refreshes the associated entity when the parent is refreshed.             |
| `DETACH`             | Detaches the associated entity when the parent is detached from the context. |

---

### ✅ Example
```java
Course course = Course.builder()
    .title("Spring Boot")
    .credit(6)
    .build();

CourseMaterial material = CourseMaterial.builder()
    .url("springboot.com")
    .course(course)
    .build();

courseMaterialRepository.save(material); // Automatically saves both CourseMaterial and Course
```

---

### 💡 Best Practices
- Use `CascadeType.ALL` only when the lifecycle of the child entity is **fully dependent** on the parent.
- For shared entities (like `Course` used in multiple places), avoid cascading `REMOVE` to prevent accidental deletions.

---

## **31. CascadeType Explained**

### ✅ Overview
Cascade defines how operations on the **parent entity** affect the **associated child entity**. You can fine-tune behavior by choosing specific cascade types instead of using `ALL`.

---

### 🔍 Individual Cascade Types

| **Cascade Type** | **Description** | **When to Use** |
|------------------|------------------|------------------|
| `PERSIST`        | Saves the child when the parent is saved. | When child entities are new and should be saved with the parent. |
| `MERGE`          | Updates the child when the parent is merged. | Useful in detached entity scenarios (e.g., DTO → Entity conversion). |
| `REMOVE`         | Deletes the child when the parent is deleted. | Only if the child shouldn't exist independently. |
| `REFRESH`        | Reloads the child from the database when the parent is refreshed. | When you want both entities to reflect the latest DB state. |
| `DETACH`         | Detaches the child when the parent is detached from the persistence context. | Rarely used directly; useful in advanced session management. |

---

### 🧠 Practical Examples

#### 1. `CascadeType.PERSIST`
```java
@OneToOne(cascade = CascadeType.PERSIST)
private Profile profile;
```
> Saving `User` will also save `Profile`, but deleting `User` won’t delete `Profile`.

---

#### 2. `CascadeType.MERGE`
```java
@OneToOne(cascade = CascadeType.MERGE)
private Address address;
```
> When updating a detached `Customer`, changes in `Address` will be merged too.

---

#### 3. `CascadeType.REMOVE`
```java
@OneToOne(cascade = CascadeType.REMOVE)
private Avatar avatar;
```
> Deleting `User` will also delete the associated `Avatar`.

---

#### 4. `CascadeType.REFRESH`
```java
@OneToOne(cascade = CascadeType.REFRESH)
private Settings settings;
```
> Refreshing `AppConfig` will also reload `Settings` from DB.

---

#### 5. `CascadeType.DETACH`
```java
@OneToOne(cascade = CascadeType.DETACH)
private Session session;
```
> Detaching `User` from persistence context will also detach `Session`.

---

### ⚠️ Best Practice Tips
- Prefer **explicit cascade types** over `ALL` unless you're sure the child entity is tightly coupled.
- Avoid `REMOVE` if the child is reused elsewhere — it can lead to unintended deletions.
- Use `MERGE` and `PERSIST` in DTO-to-Entity conversion flows.

---

## **32. Fetch Types in JPA**

### 🔍 What is Fetching?
Fetching defines **how and when** related entities are loaded from the database — either **eagerly** (immediately) or **lazily** (on demand).

---

### ⚙️ FetchType Enum

```java
public enum FetchType {
    LAZY,
    EAGER
}
```

---

### 🧠 Types Explained

| **Fetch Type** | **Behavior** | **Use Case** |
|----------------|--------------|--------------|
| `LAZY`         | Loads the related entity **only when accessed**. | Best for large or optional relationships. |
| `EAGER`        | Loads the related entity **immediately** with the parent. | Use when the child is always needed. |

---

### 📌 Default Fetch Types by Relationship

| **Annotation**     | **Default Fetch Type** |
|--------------------|------------------------|
| `@OneToOne`        | `EAGER`                |
| `@ManyToOne`       | `EAGER`                |
| `@OneToMany`       | `LAZY`                 |
| `@ManyToMany`      | `LAZY`                 |

---

### ✅ Example: Lazy vs Eager

#### Lazy
```java
@OneToMany(fetch = FetchType.LAZY)
private List<Order> orders;
```
> Orders are fetched **only when accessed**.

#### Eager
```java
@ManyToOne(fetch = FetchType.EAGER)
private Customer customer;
```
> Customer is fetched **immediately** with the parent entity.

---

### ⚠️ Best Practices
- Prefer `LAZY` for collections to avoid performance bottlenecks.
- Use `EAGER` only when you're sure the related entity is **always needed**.
- Be cautious with `EAGER` in large object graphs — it can lead to **N+1 query problems** or **heavy joins**.

---

## **33. @ToString in Lombok**

### 🔧 Purpose
Generates a `toString()` method automatically for the class, including selected fields. Helps with logging, debugging, and printing object state.

---

### ✅ Basic Usage
```java
@ToString
public class CourseMaterial {
    private String url;
    private Course course;
}
```
> Generates: `CourseMaterial(url=springboot.com, course=Course(...))`

---

### 🚫 Excluding Fields
```java
@ToString(exclude = "course")
public class CourseMaterial {
    private String url;
    private Course course;
}
```
> Output: `CourseMaterial(url=springboot.com)`  
> Prevents **infinite recursion** in bidirectional relationships like `Course ↔ CourseMaterial`.

---

### 🧠 Why Exclude?
- Avoids **stack overflow** in circular references.
- Keeps `toString()` concise and readable.
- Prevents sensitive or verbose fields from being printed.

---

### 🧩 Other Options

| **Attribute**      | **Description** |
|--------------------|------------------|
| `exclude`          | Excludes specific fields from `toString()` |
| `callSuper = true` | Includes superclass fields in `toString()` |
| `onlyExplicitlyIncluded = true` | Includes only fields marked with `@ToString.Include` |

---

### ✨ Advanced Example
```java
@ToString(onlyExplicitlyIncluded = true)
public class Student {

    @ToString.Include
    private String name;

    @ToString.Include(name = "roll")
    private int rollNumber;

    @ToString.Exclude
    private List<Course> courses;
}
```
> Output: `Student(name=John, roll=101)`

---

### ⚠️ Best Practices
- Always exclude fields in **bidirectional relationships**.
- Use `onlyExplicitlyIncluded` for full control.
- Avoid printing large collections or sensitive data.

---

## **36. @OneToMany with @JoinColumn**

### ✅ Purpose
- Models a **one-to-many** relationship where **this entity is the owning side**.
- `@JoinColumn` specifies the foreign key column in the **child table** that points back to this entity.
- Avoids creating a join table (default for `@OneToMany` without `@JoinColumn`).

---

### 📌 Syntax
```java
@OneToMany(
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
)
@JoinColumn(
    name = "teacher_id",              // FK column in child table
    referencedColumnName = "teacherId" // PK column in parent table
)
private List<Course> courses;
```

---

### 🧠 Parameter Breakdown

| Annotation / Param | Meaning |
|--------------------|---------|
| `@OneToMany`       | Declares a one-to-many relationship. |
| `cascade = CascadeType.ALL` | All entity operations (persist, merge, remove, refresh, detach) are cascaded to children. |
| `fetch = FetchType.LAZY` | Child entities are loaded **on demand** (recommended for large collections). |
| `@JoinColumn`      | Specifies the FK column in the child table. |
| `name`             | Name of the FK column in the child table. |
| `referencedColumnName` | Column in the parent table that the FK references. |

---

### ⚠️ Best Practices
- **Prefer `LAZY`** for collections to avoid N+1 query issues.
- Use `CascadeType.ALL` only if child lifecycle is fully dependent on the parent.
- Keep `mappedBy` **absent** here — since this is the owning side, `@JoinColumn` is enough.
- Ensure the FK column (`teacher_id`) exists in the child table schema.

---

### ✅ Example: Teacher → Courses
**Teacher.java**
```java
@OneToMany(
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
)
@JoinColumn(name = "teacher_id", referencedColumnName = "teacherId")
private List<Course> courses;
```

**Course.java**
```java
@Entity
public class Course {
    @Id
    private Long courseId;
    private String title;
}
```

**Generated Schema (simplified)**
```
COURSE
---------
course_id   PK
title
teacher_id  FK → TEACHER.teacherId
```

---

## **37. @Transactional**

### ✅ Purpose
- Marks a method or class to be executed **within a database transaction**.
- Ensures **atomicity** — all operations inside either **complete successfully** or **roll back** on failure.

---

### 📌 Syntax
```java
@Transactional
public void updateStudentData() {
    // DB operations here
}
```

---

### 🧠 Key Points
- **Scope**:
    - At **method level** → applies to that method only.
    - At **class level** → applies to all public methods in the class.
- **Rollback Behavior**:
    - By default, rolls back on **unchecked exceptions** (`RuntimeException`, `Error`).
    - Can be configured to roll back on checked exceptions too:
      ```java
      @Transactional(rollbackFor = Exception.class)
      ```
- **Propagation**:
    - Defines how transactions behave when a method is called inside another transactional method.
    - Common values:
      Here’s a clean and properly formatted table for Spring's `@Transactional` **Propagation Types** and their behavior:

---

### 🧾 Spring Transaction Propagation Types

| Propagation Type | Behavior                                                                                |
| ---------------- | --------------------------------------------------------------------------------------- |
| `REQUIRED`       | Joins the existing transaction if one exists; otherwise, creates a new one. *(Default)* |
| `REQUIRES_NEW`   | Suspends any existing transaction and starts a completely new one.                      |
| `MANDATORY`      | Must be called within an existing transaction; throws an exception if none exists.      |
| `SUPPORTS`       | Joins an existing transaction if one exists; otherwise, runs non-transactionally.       |
| `NOT_SUPPORTED`  | Suspends any existing transaction and runs non-transactionally.                         |
| `NEVER`          | Must be called **outside** of a transaction; throws an exception if one exists.         |
| `NESTED`         | Executes within a nested transaction if a current transaction exists.                   |

---

- **Read-Only Transactions**:
    ```java
    @Transactional(readOnly = true)
    ```
    - Optimizes performance for read operations (hints to the persistence provider).

---

### ✅ Example: Using `@Transactional` in a Test Method
```java
@Test
@Transactional
public void printTeachers() {
    List<Teacher> teachers = teacherRepository.findAll();
    System.out.println("Teachers: " + teachers);
}
```

### 1️⃣ `@Test`
- Marks this method as a **JUnit test case**.
- The test runner (JUnit) will execute it automatically.

### 2️⃣ `@Transactional` in a Test Context
- In **Spring Boot tests**, `@Transactional` means:
    - The test method runs inside a **transaction**.
    - **By default**, Spring **rolls back** the transaction after the test finishes — so any DB changes made during the test are **not persisted**.
    - This keeps your test database clean and repeatable.

---

## **Why It’s Useful in Tests**
- **Isolation**: Each test starts with a clean state.
- **No manual cleanup**: You don’t have to delete inserted test data.
- **Consistency**: Ensures all repository calls in the method share the same persistence context.

---

## **Important Notes**
- If you **only read data** (like in your example), `@Transactional` isn’t strictly required — but it can still be useful if:
    - You want to lazily load related entities without hitting `LazyInitializationException`.
    - You want to ensure the same persistence context is used for the whole method.
- If you **modify data** in a test and want to keep it, you’d need to disable rollback:
  ```java
  @Test
  @Transactional
  @Rollback(false)
  public void saveTeacher() { ... }
  ```

---

## **Best Practice Tip for Your Case**
Since `teacherRepository.findAll()` might return entities with **lazy-loaded relationships** (e.g., `@OneToMany`), having `@Transactional` ensures:
- The Hibernate session stays open while printing.
- Lazy fields can be accessed without exceptions.

---
