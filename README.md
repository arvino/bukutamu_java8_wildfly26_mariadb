# Aplikasi Buku Tamu

Aplikasi buku tamu sederhana menggunakan Java EE 8, WildFly 26, dan MySQL/MariaDB.

## Dibuat Oleh
Arvino Zulka
- Email: arvinozulka@gmail.com 
- Website: https://www.arvino.my.id/
- GitHub: https://github.com/arvino

## Teknologi yang Digunakan
- Java 8 (OpenJDK 8)
- WildFly 26
- MySQL 8/MariaDB
- Maven 3.9.9
- JSF (JavaServer Faces)
- JPA (Java Persistence API)
- EJB (Enterprise JavaBeans)
- Bootstrap 5
- jQuery 3.7.0

## Fitur
- Create, Read, Delete data tamu
- Form input data tamu:
  - Nama (wajib diisi)
  - Nomor HP (wajib diisi) 
  - Email (wajib diisi)
  - Pesan
- Tampilan responsif dengan Bootstrap 5
- Validasi form
- Pencatatan waktu kunjungan otomatis
- Tabel daftar tamu dengan fitur hapus

## Persyaratan Sistem
- JDK 8 (OpenJDK 8)
- WildFly 26
- MySQL 8/MariaDB
- Maven 3.9.9
- Web Browser Modern (Chrome, Firefox, Edge)

## Cara Instalasi

### 1. Clone Repository
https://github.com/arvino/bukutamu_java8_wildfly26_mariadb.git

### 2. Setup Database

```sql
# Buat database
CREATE DATABASE bukutamu_java8;

# Buat user dan berikan akses
CREATE USER 'arvino'@'localhost' IDENTIFIED BY '123456789';
GRANT ALL PRIVILEGES ON bukutamu_java8.* TO 'arvino'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Konfigurasi WildFly

#### a. Setup Driver MySQL
- Buat folder `wildfly/modules/system/layers/base/com/mysql/main/`
- Copy `mysql-connector-java-8.0.27.jar` ke folder tersebut
- Buat file `module.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="com.mysql">
    <resources>
        <resource-root path="mysql-connector-java-8.0.27.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

#### b. Konfigurasi Datasource
Edit `wildfly/standalone/configuration/standalone.xml`, tambahkan di bagian datasources:

```xml
<datasource jndi-name="java:/BukuTamuDS" pool-name="BukuTamuDS" enabled="true" use-java-context="true">
    <connection-url>jdbc:mysql://localhost:3306/bukutamu_java8?useSSL=false&amp;serverTimezone=UTC</connection-url>
    <driver>mysql</driver>
    <security>
        <user-name>arvino</user-name>
        <password>arvino1345</password>
    </security>
    <validation>
        <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
        <background-validation>true</background-validation>
        <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
    </validation>
</datasource>
```

### 4. Build dan Deploy
```bash
# Build project
mvn clean package

# Deploy ke WildFly
cp target/bukutamu.war $WILDFLY_HOME/standalone/deployments/
```

Atau deploy melalui WildFly Management Console (http://localhost:9990)

## Struktur Proyek
```
bukutamu_java8_wildfly26_mariadb/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           ├── entity/
│       │           │   └── Tamu.java
│       │           ├── ejb/
│       │           │   └── TamuEJB.java
│       │           └── web/
│       │               └── TamuBean.java
│       ├── resources/
│       │   └── META-INF/
│       │       └── persistence.xml
│       └── webapp/
│           ├── index.xhtml
│           └── WEB-INF/
│               └── web.xml
├── pom.xml
└── README.md
```

## Penggunaan
1. Start WildFly server
2. Akses aplikasi di `http://localhost:8080/bukutamu`
3. Isi form untuk menambah data tamu:
   - Nama (wajib)
   - Nomor HP (wajib)
   - Email (wajib) 
   - Pesan (opsional)
4. Klik tombol "Simpan"
5. Data akan muncul di tabel di bawah form
6. Untuk menghapus data, klik tombol "Hapus" pada baris yang diinginkan

## Troubleshooting
- Jika terjadi error koneksi database:
  - Pastikan MySQL/MariaDB running
  - Cek kredensial database di standalone.xml
  - Restart WildFly
- Jika aplikasi tidak muncul:
  - Cek log WildFly di folder standalone/log
  - Pastikan file WAR ter-deploy dengan benar
  - Cek URL aplikasi sudah benar

## Lisensi
MIT License

Copyright (c) 2025 Arvino Zulka

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

