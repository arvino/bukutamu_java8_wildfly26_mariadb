#!/bin/bash

# Set WildFly home directory
WILDFLY_HOME=/opt/wildfly-26.1.3.Final

# Build project
mvn clean package

# Copy WAR file
cp target/bukutamu.war "$WILDFLY_HOME/standalone/deployments/"

# Start WildFly if not running
if ! pgrep -x "standalone.sh" > /dev/null
then
    cd "$WILDFLY_HOME/bin"
    ./standalone.sh &
fi

echo "Deployment complete. Access application at http://localhost:8080/bukutamu" 