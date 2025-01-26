@echo off

REM Set WildFly home directory
set WILDFLY_HOME=C:\wildfly-26.1.3.Final

REM Kill any running java processes (optional, uncomment if needed)
REM taskkill /F /IM java.exe

REM Build project
call mvn clean package

REM Copy WAR file
copy /Y target\bukutamu.war "%WILDFLY_HOME%\standalone\deployments\"

REM Start WildFly in standalone mode
cd /d "%WILDFLY_HOME%\bin"
echo Starting WildFly server...
call standalone.bat

REM Note: Don't add any commands after standalone.bat as it will block

echo Deployment complete. Access application at http://localhost:8080/bukutamu
pause 