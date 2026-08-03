# Role Access Hierarchy System - Documentation

## Overview
This document describes the Role Access Hierarchy system implemented for the Project Monitoring System (PMS) backend. It provides fine-grained access control for users based on their assigned roles, ministries, and projects.

---

## Access Hierarchy Specification

### Role Types and Access Matrix

| Role Type | Ministry Access | Project Access | Scope | Permissions |
|-----------|-----------------|----------------|-------|-------------|
| **SUPER_ADMIN** | FULL | FULL | All Ministries & Projects | Full Create, Read, Update, Delete |
| **FULL_MINISTRY_ACCESS** | MINISTRY | MINISTRY | Assigned Ministries + All their Projects | Full Ministry Access |
| **PROJECT_DIRECTOR** | PROJECT | PROJECT | Assigned Projects Only | Project-level Access |
| **MONITORING_OFFICER** | VIEW_ONLY | CREATE_EDIT | Assigned Ministries (View) + Assigned Projects (Edit) | View & Limited Data Entry |

---

## Access Levels

### Definition
- **FULL**: Complete access (Create, Read, Update, Delete)
- **MINISTRY**: Ministry-level access (manage ministry and related projects)
- **PROJECT**: Project-level access (manage specific projects)
- **CREATE_EDIT**: Create and edit permissions only (no delete)
- **DATA_ENTRY**: Limited data entry permissions
- **VIEW_ONLY**: Read-only access
- **NONE**: No access

### Access Hierarchy (from highest to lowest privilege)
```
FULL > PROJECT > MINISTRY > CREATE_EDIT > DATA_ENTRY > VIEW_ONLY > NONE
```

---

## Database Entities

### 1. Role Entity Enhancement
Added `roleType` field to the existing `Role` entity:
```java
@Enumerated(EnumType.STRING)
@Column(nullable = true)
private RoleType roleType;  // SUPER_ADMIN, FULL_MINISTRY_ACCESS, etc.

@ElementCollection(fetch = FetchType.EAGER)
@CollectionTable(name = "role_permissions")
private Set<Permission> permissions;  // Fine-grained permissions
```

### 2. RoleAccessHierarchy Entity
Defines default access rules for each role type:
```
- roleType: RoleType enum (SUPER_ADMIN, FULL_MINISTRY_ACCESS, etc.)
- ministryAccessLevel: AccessLevel (default level for ministry access)
- projectAccessLevel: AccessLevel (default level for project access)
- canAccessAllministries: Boolean (super admin flag)
- canAccessAllProjects: Boolean (super admin flag)
```

### 3. UserMinistryAccess Entity
Maps users to ministries with specific access levels:
```
- userId: Foreign key to User
- ministryId: Foreign key to Ministry
- accessLevel: AccessLevel (FULL, MINISTRY, VIEW_ONLY, etc.)
```

### 4. UserProjectAccess Entity
Maps users to projects with specific access levels:
```
- userId: Foreign key to User
- projectId: Foreign key to Project
- accessLevel: AccessLevel (FULL, PROJECT, CREATE_EDIT, etc.)
```

---

## API Endpoints

### Role Management
```
POST   /api/roles                          - Create role with permissions
GET    /api/roles                          - Get all roles
GET    /api/roles/{id}                     - Get role by ID
PUT    /api/roles/{id}                     - Update role name and permissions
DELETE /api/roles/{id}                     - Delete role
```

### Role Access Hierarchy Management
```
POST   /api/role-access-hierarchy          - Create hierarchy
GET    /api/role-access-hierarchy          - Get all hierarchies
GET    /api/role-access-hierarchy/role-type/{roleType}  - Get hierarchy by role type
PUT    /api/role-access-hierarchy/{id}     - Update hierarchy
DELETE /api/role-access-hierarchy/{id}     - Delete hierarchy
```

### User Ministry Access Control
```
POST   /api/access-control/users/{userId}/ministries/{ministryId}?accessLevel=FULL
       - Grant ministry access to user

GET    /api/access-control/users/{userId}/ministries/{ministryId}/can-access
       - Check if user can access ministry

GET    /api/access-control/users/{userId}/ministries/{ministryId}/access-level
       - Get user's ministry access level

GET    /api/access-control/users/{userId}/ministries
       - Get all ministries accessible to user

DELETE /api/access-control/users/{userId}/ministries/{ministryId}
       - Revoke ministry access
```

### User Project Access Control
```
POST   /api/access-control/users/{userId}/projects/{projectId}?accessLevel=CREATE_EDIT
       - Grant project access to user

GET    /api/access-control/users/{userId}/projects/{projectId}/can-access
       - Check if user can access project

GET    /api/access-control/users/{userId}/projects/{projectId}/access-level
       - Get user's project access level

GET    /api/access-control/users/{userId}/projects
       - Get all projects accessible to user

DELETE /api/access-control/users/{userId}/projects/{projectId}
       - Revoke project access
```

### Permission Checking
```
GET    /api/access/users/{userId}/has/{permission}
       - Check if user has specific permission (e.g., PROJECT_CREATE)
```

---

## Usage Examples

### 1. Create a Role with Permissions
```bash
curl -X POST http://localhost:8080/api/roles \
  -H "Content-Type: application/json" \
  -d '{
    "roleName": "Project Manager",
    "roleType": "PROJECT_DIRECTOR",
    "permissions": ["PROJECT_CREATE", "PROJECT_UPDATE", "PROJECT_READ"]
  }'
```

### 2. Grant Ministry Access to User
```bash
curl -X POST http://localhost:8080/api/access-control/users/1/ministries/5?accessLevel=MINISTRY \
  -H "Content-Type: application/json"
```

### 3. Grant Project Access to User
```bash
curl -X POST http://localhost:8080/api/access-control/users/1/projects/10?accessLevel=CREATE_EDIT \
  -H "Content-Type: application/json"
```

### 4. Check User's Ministry Access
```bash
curl http://localhost:8080/api/access-control/users/1/ministries/5/access-level
# Response: "CREATE_EDIT"
```

### 5. Check If User Can Access Project
```bash
curl http://localhost:8080/api/access-control/users/1/projects/10/can-access
# Response: true or false
```

### 6. Create Role Access Hierarchy
```bash
curl -X POST http://localhost:8080/api/role-access-hierarchy \
  -H "Content-Type: application/json" \
  -d '{
    "roleType": "MONITORING_OFFICER",
    "ministryAccessLevel": "VIEW_ONLY",
    "projectAccessLevel": "CREATE_EDIT",
    "canAccessAllministries": false,
    "canAccessAllProjects": false,
    "description": "Monitoring Officer with limited access"
  }'
```

---

## Default Roles & Hierarchies

### Super Admin
- **Description**: Full access to entire system
- **Ministry Access**: FULL (All ministries)
- **Project Access**: FULL (All projects)
- **Can Access All**: Yes
- **Scope**: System-wide

### Admin (Full Ministry Access)
- **Description**: Full access to assigned ministries and their projects
- **Ministry Access**: MINISTRY (Assigned ministries only)
- **Project Access**: MINISTRY (All projects within assigned ministries)
- **Can Access All**: No
- **Scope**: Assigned ministries

### Project Director
- **Description**: Access to assigned projects only
- **Ministry Access**: PROJECT (Assigned ministry)
- **Project Access**: PROJECT (Assigned projects only)
- **Can Access All**: No
- **Scope**: Assigned projects only

### Monitoring Officer (Entry User)
- **Description**: Limited access with view-only ministry access and create/edit project access
- **Ministry Access**: VIEW_ONLY (Read-only on assigned ministries)
- **Project Access**: CREATE_EDIT (Create and edit assigned projects)
- **Can Access All**: No
- **Scope**: Assigned ministries (view) & assigned projects (edit)

---

## Access Control Service

### Core Methods

#### Check Access Availability
```java
boolean canAccessMinistry(Long userId, Long ministryId);
boolean canAccessProject(Long userId, Long projectId);
```

#### Get Access Levels
```java
AccessLevel getUserMinistryAccessLevel(Long userId, Long ministryId);
AccessLevel getUserProjectAccessLevel(Long userId, Long projectId);
```

#### Check Specific Access Level
```java
boolean hasMinistryAccessLevel(Long userId, Long ministryId, AccessLevel requiredLevel);
boolean hasProjectAccessLevel(Long userId, Long projectId, AccessLevel requiredLevel);
```

#### Grant/Revoke Access
```java
void grantMinistryAccess(Long userId, Long ministryId, AccessLevel accessLevel);
void grantProjectAccess(Long userId, Long projectId, AccessLevel accessLevel);
void revokeMinistryAccess(Long userId, Long ministryId);
void revokeProjectAccess(Long userId, Long projectId);
```

---

## Implementation Guidelines

### For Service Layer
To enforce access control in your service methods:

```java
@Service
public class ProjectServiceImpl {
    
    private final AccessControlService accessControlService;
    
    public ProjectDto getProject(Long userId, Long projectId) {
        // Check if user can access the project
        if (!accessControlService.canAccessProject(userId, projectId)) {
            throw new AccessDeniedException("User does not have access to this project");
        }
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return ProjectMapper.mapToProjectDto(project);
    }
    
    public ProjectDto updateProject(Long userId, Long projectId, ProjectDto dto) {
        // Check if user has edit access
        AccessLevel level = accessControlService.getUserProjectAccessLevel(userId, projectId);
        if (!level.equals(AccessLevel.FULL) && !level.equals(AccessLevel.CREATE_EDIT)) {
            throw new AccessDeniedException("User does not have edit access to this project");
        }
        
        // ... proceed with update
    }
}
```

### For Controller Layer
To add access checks in controllers:

```java
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    private final AccessControlService accessControlService;
    
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        
        if (!accessControlService.canAccessProject(userId, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // ... fetch and return project
    }
}
```

---

## Database Migrations (Flyway/Liquibase)

The following tables will be created automatically by Hibernate with `spring.jpa.hibernate.ddl-auto=create-drop` or `update`:

```sql
-- Role Enhancement
ALTER TABLE roles ADD COLUMN role_type VARCHAR(50);

-- New Tables
CREATE TABLE role_access_hierarchy (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  role_type VARCHAR(50) UNIQUE NOT NULL,
  ministry_access_level VARCHAR(50) NOT NULL,
  project_access_level VARCHAR(50) NOT NULL,
  can_access_all_ministries BOOLEAN NOT NULL DEFAULT FALSE,
  can_access_all_projects BOOLEAN NOT NULL DEFAULT FALSE,
  description TEXT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE user_ministry_access (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  ministry_id BIGINT NOT NULL,
  access_level VARCHAR(50) NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (ministry_id) REFERENCES ministries(id)
);

CREATE TABLE user_project_access (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  project_id BIGINT NOT NULL,
  access_level VARCHAR(50) NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (project_id) REFERENCES projects(id)
);
```

---

## Testing

### Test Super Admin Access
```bash
# Create a Super Admin user (assigned to role with SUPER_ADMIN type)
# Check that they can access any ministry or project
curl http://localhost:8080/api/access-control/users/1/ministries/5/can-access
# Should return: true

curl http://localhost:8080/api/access-control/users/1/projects/10/can-access
# Should return: true
```

### Test Limited Access
```bash
# Create a Monitoring Officer user
# Assign to ministry 1 with VIEW_ONLY access
POST /api/access-control/users/2/ministries/1?accessLevel=VIEW_ONLY

# Assign to project 5 with CREATE_EDIT access
POST /api/access-control/users/2/projects/5?accessLevel=CREATE_EDIT

# Verify access levels
GET /api/access-control/users/2/ministries/1/access-level
# Should return: "VIEW_ONLY"

GET /api/access-control/users/2/projects/5/access-level
# Should return: "CREATE_EDIT"

# Verify access denial to other ministry
GET /api/access-control/users/2/ministries/2/can-access
# Should return: false
```

---

## Future Enhancements

1. **Directorate-level Access**: Add UserDirectorateAccess entity for directorate-level access control
2. **Time-based Access**: Add start/end dates for temporary access grants
3. **Access Audit Trail**: Log all access grants, revokes, and permission checks
4. **Bulk Access Assignment**: Endpoints to grant access to multiple users at once
5. **Access Reports**: Generate reports on user permissions and access patterns
6. **Integration with Spring Security**: Integrate with @PreAuthorize and custom SecurityContext
7. **Custom Permissions**: Extend Permission enum with domain-specific permissions

---

## Troubleshooting

### User has no access despite assignment
- Check that UserMinistryAccess or UserProjectAccess record exists
- Verify the user's role has the appropriate roleType
- Confirm the accessLevel is not AccessLevel.NONE

### Super Admin role not working
- Ensure the user's role has roleType = SUPER_ADMIN
- Check that RoleAccessHierarchy for SUPER_ADMIN exists with canAccessAllministries=true

### Permission checks failing
- Verify Permission enum values match the strings being passed
- Check that Role has permissions set correctly
- Confirm Permission import in relevant service classes

