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



