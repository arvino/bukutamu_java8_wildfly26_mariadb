# Aplikasi Buku Tamu

Aplikasi buku tamu berbasis Java EE menggunakan JSF, EJB, dan REST API. Aplikasi ini memungkinkan pengunjung untuk mendaftar sebagai member dan menulis pesan buku tamu disertai dengan foto.

## Overview

Aplikasi ini memiliki 2 jenis user:
1. Admin
   - Melihat statistik (total member, total buku tamu, buku tamu hari ini)
   - Mengelola data member
   - Moderasi buku tamu
2. Member
   - Submit buku tamu dengan foto (max 1x sehari)
   - Melihat riwayat buku tamu
   - Update profil

## Teknologi yang Digunakan
- Java EE 8
- JSF 2.3 (JavaServer Faces)
- EJB 3.2 (Enterprise JavaBeans)
- JPA 2.2 (Java Persistence API)
- JAX-RS 2.1 (Java API for RESTful Web Services)
- WildFly 26.1.3 (Application Server)
- MySQL 8.0 (Database)
- Bootstrap 5.3 (Frontend Framework)
- JWT (JSON Web Token untuk autentikasi API)

## Setup Environment

### 1. Visual Studio Code
- Install Extension:
  - Language Support for Java
  - Debugger for Java
  - Maven for Java
  - Project Manager for Java
  - WildFly Server Tools

- Konfigurasi settings.json:
```json
{
    "java.configuration.updateBuildConfiguration": "automatic",
    "java.compile.nullAnalysis.mode": "automatic",
    "java.debug.settings.hotCodeReplace": "auto",
    "java.jdt.ls.java.home": "PATH_TO_JDK_8"
}
```

### 2. WildFly Setup
a. Download dan extract WildFly 26.1.3
b. Tambahkan user management:
```bash
cd $WILDFLY_HOME/bin
./add-user.sh
```

c. Setup MySQL JDBC Driver:
- Download mysql-connector-java-8.0.x.jar
- Buat struktur folder: modules/system/layers/base/com/mysql/main/
- Copy driver ke folder tersebut
- Buat module.xml:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.5" name="com.mysql">
    <resources>
        <resource-root path="mysql-connector-java-8.0.x.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

d. Konfigurasi datasource di standalone.xml:
```xml
<datasource jndi-name="java:/BukuTamuDS" pool-name="BukuTamuDS" enabled="true">
    <connection-url>jdbc:mysql://localhost:3306/bukutamu?useSSL=false&amp;serverTimezone=UTC</connection-url>
    <driver>mysql</driver>
    <security>
        <user-name>root</user-name>
        <password>root</password>
    </security>
</datasource>
```

## Flow Aplikasi

1. Autentikasi
   - User register sebagai member baru
   - Login menggunakan email dan password
   - JWT token digenerate saat login untuk akses API

2. Member Flow
   - Submit buku tamu dengan foto (validasi max 1x sehari)
   - Lihat riwayat buku tamu pribadi
   - Update profil dan password

3. Admin Flow
   - Dashboard dengan statistik
   - Manajemen member (lihat, hapus)
   - Moderasi buku tamu (lihat, hapus)

4. REST API Flow
   - Autentikasi menggunakan JWT
   - CRUD buku tamu
   - Get profile dan update profile

## Build dan Deploy

```bash
# Build project
mvn clean package

# Deploy ke WildFly
cp target/bukutamu.war $WILDFLY_HOME/standalone/deployments/

# Start WildFly
cd $WILDFLY_HOME/bin
./standalone.sh
```

## Struktur Project
```
src/
├── main/
│   ├── java/
│   │   └── com/example/
│   │       ├── entity/      # JPA entities
│   │       ├── ejb/         # Business logic
│   │       ├── web/         # JSF managed beans
│   │       ├── rest/        # REST endpoints
│   │       └── util/        # Utility classes
│   ├── resources/
│   │   └── META-INF/
│   │       └── persistence.xml
│   └── webapp/
│       ├── WEB-INF/
│       ├── resources/
│       ├── admin/           # Admin pages
│       ├── member/          # Member pages
│       └── error/           # Error pages
```

## Fitur Utama
1. Multi User Role (Admin/Member)
2. Upload Foto dengan Validasi
3. REST API dengan JWT Auth
4. Responsive Design dengan Bootstrap
5. Error Handling & Validasi
6. File Upload Management
7. Statistik Dashboard

## Developer
- Developer Name : Arvino Zulka
- Email: arvinozulka@gmail.com 
- Website: https://www.arvino.my.id/
- GitHub: https://github.com/arvino