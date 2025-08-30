# Creating a JPA Entity in Spring Boot

### **Spring Boot – JPA Entity Quick Notes**
- **`@Entity`** → Marks class as a JPA entity (maps to DB table).
- **`@Table(name="...")`** *(optional)* → Custom table name.
- **`@Id`** → Primary key field.
- **`@GeneratedValue(strategy = GenerationType.IDENTITY)`** → Auto-increment ID.
- **Fields** → Each maps to a DB column.
- **No-arg constructor** → Required by JPA (`@NoArgsConstructor`).
- **Lombok**:
    - `@Data` → Getters, setters, `toString()`, `equals()`, `hashCode()`.
    - `@AllArgsConstructor` → Constructor with all fields.
    - `@NoArgsConstructor` → Constructor with no args.
    - `@Builder` → Builder pattern for clean object creation.
- **Best Practice** → Use meaningful field names, avoid exposing DB IDs in APIs directly.

---
💡 **Pro Tips:**
- Use `@Column` for custom column names or constraints.
- Keep entities simple; avoid business logic in them.
- Use DTOs for API data transfer, not entities directly.