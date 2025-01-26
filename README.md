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

## Deployment di Windows 11

### 1. Persiapan Environment
- Install JDK 8 (misal di C:\Program Files\Java\jdk1.8.0_xxx)
- Install Maven (tambahkan ke PATH)
- Download & Extract WildFly 26.1.3.Final (misal di C:\wildfly-26.1.3.Final)
- Install MySQL 8.0

### 2. Konfigurasi WildFly
a. Buat user management (buka Command Prompt sebagai Administrator):
```batch
cd C:\wildfly-26.1.3.Final\bin
add-user.bat
```
- Pilih: a) Management User
- Username: admin
- Password: admin123
- Groups: leave blank
- Yes untuk pertanyaan terakhir

b. Setup MySQL Driver:
1. Download mysql-connector-java-8.0.x.jar
2. Buat folder struktur:
```batch
C:\wildfly-26.1.3.Final\modules\system\layers\base\com\mysql\main\
```
3. Copy mysql-connector-java-8.0.x.jar ke folder tersebut
4. Buat file module.xml di folder yang sama:
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

### 3. Setup Database
a. Buka MySQL Command Line atau MySQL Workbench:
```sql
CREATE DATABASE bukutamu;
```

b. Import schema dan data awal:
```batch
mysql -u root -p bukutamu < src\main\resources\db\init.sql
```

### 4. Deploy Aplikasi

#### Metode 1: Menggunakan Script
1. Edit file deploy.bat, sesuaikan path WildFly:
```batch
set WILDFLY_HOME=C:\wildfly-26.1.3.Final
```

2. Jalankan script deploy:
```batch
deploy.bat
```

#### Metode 2: Manual Deploy
1. Build project:
```batch
mvn clean package
```

2. Copy file WAR:
```batch
copy /Y target\bukutamu.war "C:\wildfly-26.1.3.Final\standalone\deployments\"
```

3. Start WildFly:
```batch
cd C:\wildfly-26.1.3.Final\bin
standalone.bat
```

### 5. Akses Aplikasi
- Web Application: http://localhost:8080/bukutamu
- Admin Console: http://localhost:9990

### 6. Login Admin Default
- Email: admin@bukutamu.com
- Password: admin123

### 7. Troubleshooting
1. Port Conflicts
- Pastikan port 8080 dan 9990 tidak digunakan
- Cek dengan command: `netstat -ano | findstr "8080"`
- Jika terpakai, stop service yang menggunakan atau ubah port di standalone.xml

2. Permission Issues
- Jalankan Command Prompt sebagai Administrator
- Pastikan folder uploads di webapp memiliki write permission

3. Database Connection
- Verifikasi kredensial database di standalone.xml
- Test koneksi database menggunakan tools seperti MySQL Workbench
- Cek log error di C:\wildfly-26.1.3.Final\standalone\log\server.log

4. Memory Issues
- Edit standalone.bat, tambahkan memory settings:
```batch
set "JAVA_OPTS=-Xms512m -Xmx1024m"
```

## Struktur File Penting

### 1. Konfigurasi
```
src/main/webapp/WEB-INF/
├── web.xml                 # Konfigurasi servlet & JSF
├── jboss-web.xml          # Konfigurasi context root
├── faces-config.xml       # Konfigurasi JSF
└── template.xhtml         # Template utama aplikasi
```

### 2. Komponen UI
```
src/main/webapp/
├── WEB-INF/components/    # Reusable components
│   ├── messages.xhtml     # Global messages
│   └── loading.xhtml      # Loading spinner
├── error/                 # Error pages
│   ├── 404.xhtml
│   └── 500.xhtml
├── index.xhtml           # Landing page
├── login.xhtml          # Login form
└── register.xhtml       # Registration form
```

### 3. Script Deployment
```
├── deploy.bat           # Script deploy Windows
├── deploy.sh           # Script deploy Linux/Mac
└── wildfly.bat         # Script start/stop WildFly
```

## Troubleshooting Detail

### 1. Error 404 - Not Found
a. Periksa Context Root
- Pastikan file jboss-web.xml ada dan berisi:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jboss-web>
    <context-root>/bukutamu</context-root>
</jboss-web>
```

b. Periksa Deployment
```batch
dir "C:\wildfly-26.1.3.Final\standalone\deployments"
```
Harus ada file:
- bukutamu.war
- bukutamu.war.deployed

c. Periksa Log Server
```batch
type "C:\wildfly-26.1.3.Final\standalone\log\server.log"
```

### 2. Error Database Connection
a. Verifikasi Datasource di standalone.xml:
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

b. Test Koneksi MySQL:
```batch
mysql -u root -p -e "SELECT 1"
```

### 3. Error JSF Components
a. Periksa Template
- Pastikan semua file komponen ada:
  - /WEB-INF/template.xhtml
  - /WEB-INF/components/messages.xhtml
  - /WEB-INF/components/loading.xhtml

b. Periksa Dependencies di pom.xml
```xml
<dependency>
    <groupId>javax.faces</groupId>
    <artifactId>javax.faces-api</artifactId>
    <version>2.3</version>
</dependency>
```

### 4. Error Deployment
a. Clean Deployment
```batch
del /F /Q "C:\wildfly-26.1.3.Final\standalone\deployments\bukutamu.*"
```

b. Restart WildFly Clean
```batch
wildfly.bat stop
taskkill /F /IM java.exe
wildfly.bat start
```

### 5. Permission Issues
a. Folder Uploads
```batch
mkdir "C:\wildfly-26.1.3.Final\standalone\deployments\bukutamu.war\uploads"
icacls "C:\wildfly-26.1.3.Final\standalone\deployments\bukutamu.war\uploads" /grant "Everyone":(OI)(CI)F
```

b. Log Folder
```batch
icacls "C:\wildfly-26.1.3.Final\standalone\log" /grant "Everyone":(OI)(CI)F
```

## Quick Commands

### 1. Build & Deploy
```batch
mvn clean package
copy /Y target\bukutamu.war "C:\wildfly-26.1.3.Final\standalone\deployments\"
```

### 2. Server Control
```batch
wildfly.bat start    # Start server
wildfly.bat stop     # Stop server
```

### 3. Log Monitoring
```batch
powershell Get-Content -Path "C:\wildfly-26.1.3.Final\standalone\log\server.log" -Wait
```