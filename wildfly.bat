@echo off
set WILDFLY_HOME=C:\wildfly-26.1.3.Final

if "%1"=="start" (
    echo Starting WildFly...
    cd /d "%WILDFLY_HOME%\bin"
    start standalone.bat
    echo WildFly is starting. Wait a moment then access:
    echo Admin Console: http://localhost:9990
    echo Application: http://localhost:8080/bukutamu
) else if "%1"=="stop" (
    echo Stopping WildFly...
    cd /d "%WILDFLY_HOME%\bin"
    call jboss-cli.bat --connect command=:shutdown
) else (
    echo Usage: wildfly.bat [start^|stop]
) 