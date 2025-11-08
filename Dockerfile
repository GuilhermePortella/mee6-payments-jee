FROM payara/micro:6.2025.10-jdk17
COPY target/mee6-payments-jee-0.1.0-SNAPSHOT.war $DEPLOY_DIR
